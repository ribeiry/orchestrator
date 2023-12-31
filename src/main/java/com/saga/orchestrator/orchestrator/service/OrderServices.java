package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.model.OrderDto;
import com.saga.orchestrator.orchestrator.model.ProdutoDTO;
import com.saga.orchestrator.orchestrator.model.StockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import com.saga.orchestrator.orchestrator.mediator.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@PropertySource("classpath:application.properties")
public class OrderServices {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final  String SERVICE = "ORDER";
    private final  String SUCESS_MSG = "SUCCESS";

    @Autowired
    public Environment environment;


    private  final String apiUrl = "http://localhost:8081/orders";
    private final  String FAIL_MSG = "FAIL";

    private Communicator mediator = new Communicator();

    public OrderServices() {
    }


    public void getAllOrders() throws HttpClientErrorException {
        OrderDto orderRequest = new OrderDto();
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método getAllOrders() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderRequest, headers);
        try {
            ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, request, List.class);
            List<OrderDto> order = new ArrayList<>();
            order = response.getBody();
            logger.info("O retorno de pedidos é " + order);
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime);
            for (int i = 0; i < order.size(); i++) {
                Object pedido = order.get(i);
                if (pedido instanceof LinkedHashMap<?, ?>) {
                    LinkedHashMap<?, ?> linkedHashMap = (LinkedHashMap<?, ?>) pedido;
                    ArrayList<?> produtos = (ArrayList<?>) linkedHashMap.get("produtos");
                    if (produtos != null) {
                        for (Object produto : produtos) {
                            System.out.println(produto);
                            logger.info(produto.toString());
                        }
                    }
                }
            }
        }
        catch (HttpClientErrorException e){
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage() + "  Caiuu aquiii");
        }
    }

    public void getAOrders() throws HttpClientErrorException {
        OrderDto orderRequest = new OrderDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/cf1048be-a914-494b-984d-1253c6efad1e", apiUrl);
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método getAOrders() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderRequest, headers);
        try {
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET, request, OrderDto.class);
            OrderDto order = (OrderDto) response.getBody();
            logger.info("O retorno de pedidos é {}" ,order);

            logger.info(String.valueOf(order));
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime );

        }
        catch (HttpClientErrorException e){
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage() + "  Caiuu aquiii");
        }

    }

    public  OrderDto CreateOrder(OrderDto orderDto) throws HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderDto> entity = new HttpEntity<OrderDto>(orderDto,headers);

        logger.info("Chamando o método postCreateOrder()");

        try {

            OrderDto response =  restTemplate.exchange(apiUrl, HttpMethod.POST, entity,OrderDto.class).getBody();
            logger.info(String.valueOf(response));
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime);
            return  response;
        }
        catch (HttpClientErrorException e){

            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage() + "Caiuu aquii");
            return null;
        }

    }

    public void CancelOrder(String id) throws  HttpClientErrorException{
        OrderDto orderDto = new OrderDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/%s", apiUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        logger.info("Chamando o método putUpdateOrder() e cancelando o pedido id: ", id);

        HttpHeaders headers = new HttpHeaders();

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);

        HttpEntity<OrderDto> requestEntity = new HttpEntity<OrderDto>(orderDto,headers);
        try {
            ResponseEntity<OrderDto> response = restTemplate.exchange(url , HttpMethod.PUT, requestEntity,  OrderDto.class);
            orderDto = response.getBody();
            HttpStatusCode resp = response.getStatusCode();
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime );
            logger.info("Retorno " +  String.valueOf(orderDto));
        }
        catch (final HttpClientErrorException e) {

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                logger.info(e.getMessage() + "   caiu aquiiii");
                mediator.getNext(FAIL_MSG,SERVICE,dateTime );
            }
            else{
                logger.info(e.getMessage());

            }
        }

    }

}

package com.saga.orchestrator.service;


import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.OrderDto;
import com.saga.orchestrator.model.Order;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServices {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final  String SERVICE = "ORDER";
    private static final  String SUCESS_MSG = "SUCCESS";

    private String serverUrl;

    private static final  String FAIL_MSG = "FAIL";

    private static final Communicator mediator = new Communicator();

    public OrderServices() {
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String appConfigPath = rootPath + "application-server.properties";
            Properties server = new Properties();
            server.load(new FileInputStream(appConfigPath));
            serverUrl = server.getProperty("url.server.order-service");
        }
        catch (IOException ex){
            logger.info(ex.getMessage());
        }
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
            ResponseEntity<List> response = restTemplate.exchange(serverUrl, HttpMethod.GET, request, List.class);
            List<OrderDto> order = new ArrayList<>();
            order = response.getBody();
            logger.info("O retorno de pedidos é {}", order);
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime);
            for (Object pedido : order) {
                if (pedido instanceof LinkedHashMap<?, ?>) {
                    LinkedHashMap<?, ?> linkedHashMap = (LinkedHashMap<?, ?>) pedido;
                    ArrayList<?> produtos = (ArrayList<?>) linkedHashMap.get("produtos");
                    if (produtos != null) {
                        for (Object produto : produtos) {
                            logger.info("Objeto Produto é : {}",produto);
                        }
                    }
                }
            }
        }
        catch (HttpClientErrorException e){
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage());
        }
    }

    public void getAOrders(String id) throws HttpClientErrorException {
        OrderDto orderRequest = new OrderDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/id", serverUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método getAOrders() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderRequest, headers);
        try {
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET, request, OrderDto.class);
            OrderDto order = (OrderDto) response.getBody();
            logger.info("O retorno de pedidos é {}" ,order);

            logger.info("{}", order);
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime );

        }
        catch (HttpClientErrorException e){
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage());
        }

    }

    public  String createOrder(Order order) throws HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> entity = new HttpEntity<Order>(order,headers);

        logger.info("Chamando o método postCreateOrder()");

        try {

            String response =  restTemplate.exchange(serverUrl, HttpMethod.POST, entity,String.class).getBody();
            assert response != null;
            response = response.replaceAll("\"", "");
            logger.info("ID do pedido criado retornado {}", response);

            mediator.getNext(SUCESS_MSG,SERVICE,dateTime);
            return  response;
        }
        catch (Exception e){
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.error(e.getMessage());
            return null;
        }

    }

    public void cancelOrder(String id) throws  HttpClientErrorException{
        OrderDto orderDto = new OrderDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/%s", serverUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        logger.info("Chamando o método putUpdateOrder() e cancelando o pedido id: {}", id);

        HttpHeaders headers = new HttpHeaders();

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);

        HttpEntity<OrderDto> requestEntity = new HttpEntity<OrderDto>(orderDto,headers);
        try {
            ResponseEntity<OrderDto> response = restTemplate.exchange(url , HttpMethod.PUT, requestEntity,  OrderDto.class);
            orderDto = response.getBody();
            HttpStatusCode resp = response.getStatusCode();
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime );
            logger.info("Retorno {} {}",orderDto,resp);
        }
        catch (final HttpClientErrorException e) {

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                logger.error(e.getMessage());
                mediator.getNext(FAIL_MSG,SERVICE,dateTime );
            }
            else{
                logger.error(e.getMessage());

            }
        }

    }

}

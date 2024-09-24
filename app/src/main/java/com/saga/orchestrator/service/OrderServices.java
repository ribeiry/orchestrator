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
import static com.saga.orchestrator.constant.Constant.*;

@Service
public class OrderServices {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String serverUrl;
    private static final Communicator mediator = new Communicator();

    public OrderServices() {
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String appConfigPath = rootPath + "application-server.teste";
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderRequest, headers);
        try {
            ResponseEntity<List> response = restTemplate.exchange(serverUrl, HttpMethod.GET, request, List.class);
            List order;
            order = response.getBody();
            mediator.saveMicroserviceResult(SUCCESS_MSG,SERVICE_ORDER,dateTime);
            mediator.getNext(SUCCESS_MSG,SERVICE_ORDER,dateTime);
            for (int i = 0; i < Objects.requireNonNull(order).size(); i++) {
                Object pedido = order.get(i);
                if (pedido instanceof LinkedHashMap<?, ?> linkedHashMap) {
                    ArrayList<?> produtos = (ArrayList<?>) linkedHashMap.get("produtos");
                }
            }
        }
        catch (HttpClientErrorException e){
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE_ORDER, dateTime);
            mediator.getNext(FAIL_MSG, SERVICE_ORDER, dateTime);
            logger.info(e.getMessage());
        }
    }

    public void getAOrders(String id) throws HttpClientErrorException {
        OrderDto orderRequest = new OrderDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/id", serverUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderRequest, headers);
        try {
            ResponseEntity<OrderDto> response = restTemplate.exchange(url, HttpMethod.GET, request, OrderDto.class);
            OrderDto order = response.getBody();
            logger.info(String.valueOf(order));
            mediator.getNext(SUCCESS_MSG,SERVICE_ORDER,dateTime );
            mediator.saveMicroserviceResult(SUCCESS_MSG,SERVICE_ORDER,dateTime );

        }
        catch (HttpClientErrorException e){
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE_ORDER, dateTime);
            mediator.getNext(FAIL_MSG, SERVICE_ORDER, dateTime);
            logger.info(e.getMessage());
        }

    }

    public  String CreateOrder(Order order, UUID idprocess) throws HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> entity = new HttpEntity<Order>(order,headers);

        try {
            String response =  restTemplate.exchange(serverUrl, HttpMethod.POST, entity,String.class).getBody();
            assert response != null;
            response = response.replaceAll("\"", "");
            mediator.saveMicroserviceResult(SUCCESS_MSG,SERVICE_ORDER,dateTime);
            mediator.getNext(SUCCESS_MSG,SERVICE_ORDER,dateTime);
            return  response;
        }
        catch (Exception e){
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE_ORDER, dateTime);
            mediator.saveOrechestratorResult(idprocess, 503,
                    MICROSERVICE + SERVICE_ORDER + INTERNALSERVERERROR, e.getCause());
            mediator.getNext(FAIL_MSG, SERVICE_ORDER, dateTime);
            logger.error(e.getMessage());
            return null;
        }

    }

    public void CancelOrder(String id) throws  HttpClientErrorException{
        OrderDto orderDto = new OrderDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/%s", serverUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);

        HttpEntity<OrderDto> requestEntity = new HttpEntity<OrderDto>(orderDto,headers);
        try {
            ResponseEntity<OrderDto> response = restTemplate.exchange(url , HttpMethod.PUT, requestEntity,  OrderDto.class);
            orderDto = response.getBody();
            mediator.saveMicroserviceResult(SUCCESS_MSG,SERVICE_ORDER,dateTime );
            mediator.getNext(SUCCESS_MSG,SERVICE_ORDER,dateTime );
            logger.info("Retorno %s",orderDto);
        }
        catch (final HttpClientErrorException e) {

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                logger.error(e.getMessage());
                mediator.saveMicroserviceResult(FAIL_MSG,SERVICE_ORDER,dateTime );
                mediator.getNext(FAIL_MSG,SERVICE_ORDER,dateTime );
            }
            else{
                logger.error(e.getMessage());

            }
        }

    }

}

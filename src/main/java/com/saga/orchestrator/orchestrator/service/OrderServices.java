package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.model.OrderDto;
import com.saga.orchestrator.orchestrator.model.StockDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import com.saga.orchestrator.orchestrator.mediator.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderServices {


    private final String apiUrl = "http://localhost:8081";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final  String SERVICE = "ORDER";
    private final  String SUCESS_MSG = "SUCESS";

    private final  String FAIL_MSG = "FAIL";

    private Communicator mediator = new Communicator();


    public void getAllOrders() throws HttpClientErrorException {
        OrderDto orderRequest = new OrderDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/orders/", apiUrl);
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método getAllOrders() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderRequest, headers);
        try {
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);
            List<OrderDto> order = new ArrayList<>();
            order = response.getBody();
            logger.info("O retorno de pedidos é " + order);
            for (int i = 0; i < order.size(); i++) {
                logger.info(String.valueOf(order.get(i)));
                mediator.getNext(SUCESS_MSG,SERVICE,dateTime );
                //       for (int y = 0; y < order.get(i).getProdutos().size() ; y++) {

                //         logger.info(String.valueOf(order.get(i).getProdutos()));

                //   }
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
        String url = String.format("%s/orders/cf1048be-a914-494b-984d-1253c6efad1e", apiUrl);
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

}

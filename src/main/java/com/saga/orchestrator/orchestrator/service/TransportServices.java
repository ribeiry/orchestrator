package com.saga.orchestrator.orchestrator.service;


import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.OrderDto;
import com.saga.orchestrator.orchestrator.model.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransportServices {

    private String apiUrl = "http://localhost:8000/transport";
    private final String FAIL_MSG = "FAIL";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Communicator mediator = new Communicator();

    private final String SERVICE = "TRANSPORT";
    private final String SUCESS_MSG = "SUCESS";


    //método de envio
    public void sendToTransport(Issue issue) {

        apiUrl = apiUrl + "/send";
        Transport transportSendRequest = Transport.issueToTransport(issue);
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método send tranport() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transport> request = new HttpEntity<>(transportSendRequest, headers);
        try {
            //precisa testar com o método de pé
            String responseSendTransport = restTemplate.postForObject(apiUrl, request, String.class);
            List<Transport> transport = new ArrayList<>();
            logger.info("O id do transport é  " + responseSendTransport);
            mediator.getNext(SUCESS_MSG, SERVICE, dateTime);

        } catch (HttpClientErrorException e) {
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage() + "  Caiuu aquiii");
        }

    }


    //Método de cancelamento
    public void cancelTransport(String idTransport) {

        apiUrl = apiUrl + "/cancel/" + idTransport;
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método send tranport() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(idTransport, headers);
        try {
            String response = restTemplate.postForObject(apiUrl, request, String.class);
            logger.info("Pedido canecelado " + response);
            mediator.getNext(SUCESS_MSG, SERVICE, dateTime);

        } catch (HttpClientErrorException e) {
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage() + "  Caiuu aquiii");
        }

    }


}


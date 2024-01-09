package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentServices {


    private String apiUrl = "http://localhost:8000/payment";
    private final String FAIL_MSG = "FAIL";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Communicator mediator = new Communicator();

    private final String SERVICE = "PAYMENT";
    private final String SUCESS_MSG = "SUCESS";


    //método de pagamento
    public void payOrder(Issue issue) {

        apiUrl = apiUrl + "/cancel";
        Payment PaymenttSendRequest = Payment.issueToPayment(issue);
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método send tranport() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Payment> request = new HttpEntity<>(PaymenttSendRequest, headers);
        try {
            //precisa testar com o método de pé
            String responseSendPayment = restTemplate.postForObject(apiUrl, request, String.class);
            List<Payment> payments = new ArrayList<>();
            logger.info("O id do pagamento é  " + responseSendPayment);
            mediator.getNext(SUCESS_MSG, SERVICE, dateTime);

        } catch (HttpClientErrorException e) {
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage() + "  Caiuu aquiii");
        }

    }


    public void cancelTransport(String idPayment){

        apiUrl = apiUrl + "/cancel/" + idPayment;
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método send tranport() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(idPayment, headers);
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


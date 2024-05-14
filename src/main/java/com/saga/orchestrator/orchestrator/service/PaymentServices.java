package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Mediator;
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


    private String apiUrl = "http://localhost:8082/payments";
    private final String FAIL_MSG = "FAIL";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Mediator mediator = new Mediator();

    private final String SERVICE = "PAYMENTS";
    private final String SUCESS_MSG = "SUCCESS";


    //método de pagamento
    public void payOrder(Issue issue) {

        apiUrl = apiUrl + "/pay";
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
            issue.getPayment().setPaymentId(responseSendPayment);
            logger.info("O id do pagamento é  " + responseSendPayment);
            mediator.saveMicroserviceResult(SUCESS_MSG, SERVICE, dateTime);

        }
        catch (HttpClientErrorException e) {
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE, dateTime);
            mediator.saveOrechestratorResult(issue.getOrder().getCodPedido(), e.getStatusCode().value(), "Microservice : " + SERVICE + "\n" + "Erro : Internal Server Error", e.getCause());
            logger.error(e.getMessage());
        }
        catch (Exception e ){
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE, dateTime);
            mediator.saveOrechestratorResult(issue.getOrder().getCodPedido(), 503, "Microservice : " + SERVICE + "\n" + "Erro : Internal Server Error", e.getCause());
            logger.error(e.getMessage());
        }


    }


    public void cancelPayment(String idPayment){

        apiUrl = apiUrl + "/cancel/" + idPayment;
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método send tranport() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(idPayment, headers);
        try {
            String response = restTemplate.postForObject(apiUrl, request, String.class);
            logger.info("Pedido cancelado " + response);
            mediator.saveMicroserviceResult(SUCESS_MSG, SERVICE, dateTime);

        } catch (HttpClientErrorException e) {
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE, dateTime);
            logger.info(e.getMessage() + "  Caiuu aquiii");
        }
    }
        
        
}


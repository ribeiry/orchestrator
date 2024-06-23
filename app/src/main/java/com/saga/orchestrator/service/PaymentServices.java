package com.saga.orchestrator.service;

import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.saga.orchestrator.constant.Constant.*;

public class PaymentServices {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Communicator mediator = new Communicator();
    private String serverUrl;

    public PaymentServices() {
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String appConfigPath = rootPath + "application-server.teste";
            Properties server = new Properties();
            server.load(new FileInputStream(appConfigPath));
            serverUrl = server.getProperty("url.server.payments-service");
        }
        catch (IOException ex){
            logger.info(ex.getMessage());
        }
    }



    //método de pagamento
    public void payOrder(Issue issue) {

        String apiUrl = serverUrl + "/pay";
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
            mediator.saveMicroserviceResult(SUCESS_MSG, SERVICE_PAYMENTS, dateTime);
            mediator.getNext(SUCESS_MSG, SERVICE_PAYMENTS, dateTime);

        }

        catch (HttpClientErrorException e) {
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE_PAYMENTS, dateTime);
            mediator.saveOrechestratorResult(issue.getIdprocess(), e.getStatusCode().value(),
                    "Microservice : " + SERVICE_PAYMENTS + "\n" + "Erro : Internal Server Error", e.getCause());
            mediator.getNext(FAIL_MSG, SERVICE_PAYMENTS, dateTime);
            logger.error(e.getMessage());
        }
        catch (Exception e ){
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE_PAYMENTS, dateTime);
            mediator.saveOrechestratorResult(issue.getIdprocess(), 503,
                    "Microservice : " + SERVICE_PAYMENTS + "\n" + "Erro : Internal Server Error", e.getCause());
            mediator.getNext(FAIL_MSG, SERVICE_PAYMENTS, dateTime);
            logger.error(e.getMessage());
        }

    }


    public void cancelPayment(String idPayment){

        String apiUrl = serverUrl + "/cancel/" + idPayment;
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método send tranport() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(idPayment, headers);
        try {
            String response = restTemplate.postForObject(apiUrl, request, String.class);
            logger.info("Pedido cancelado " + response);
            mediator.getNext(SUCESS_MSG, SERVICE_PAYMENTS, dateTime);
            mediator.saveMicroserviceResult(SUCESS_MSG, SERVICE_PAYMENTS, dateTime);

        } catch (HttpClientErrorException e) {
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE_PAYMENTS, dateTime);
            mediator.getNext(FAIL_MSG, SERVICE_PAYMENTS, dateTime);
            logger.info(e.getMessage());
        }
    }

}


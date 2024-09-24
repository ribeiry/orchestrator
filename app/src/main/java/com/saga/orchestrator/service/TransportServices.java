package com.saga.orchestrator.service;


import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.TransportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

import static com.saga.orchestrator.constant.Constant.*;

@Service
public class TransportServices {

    private String serverUrl;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Communicator mediator = new Communicator();
    private static final String SERVICE = "TRANSPORT";



    public TransportServices() {
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String appConfigPath = rootPath + "application-server.teste";
            Properties server = new Properties();
            server.load(new FileInputStream(appConfigPath));
            serverUrl = server.getProperty("url.server.transport-service");
            logger.info(serverUrl);
        }
        catch (IOException ex){
            logger.info(ex.getMessage());
        }
    }

    public String calculateTransport(Issue issue) {

        String apiUrl = serverUrl + "/calculator/" + issue.getTransport().getCep();
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        try {
            //precisa testar com o método de pé
            String resultCalculate = restTemplate.getForObject(apiUrl, String.class);
            mediator.getNext(SUCCESS_MSG, SERVICE, dateTime);
            mediator.saveMicroserviceResult(SUCCESS_MSG, SERVICE, dateTime);
            return resultCalculate;

        }
        catch (Exception e){
            mediator.getNext(FAIL_MSG, SERVICE, dateTime);
            mediator.saveMicroserviceResult(FAIL_MSG,SERVICE,dateTime );
            logger.error(e.getMessage());
        }

        return null;
    }

    public void sendToTransport(Issue issue) {

        String apiUrl = serverUrl + "/send";
        TransportDto transportSendRequest = TransportDto.issueToTransport(issue);
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransportDto> request = new HttpEntity<>(transportSendRequest, headers);
        try {
            //precisa testar com o método de pé
            String responseSendTransport = restTemplate.postForObject(apiUrl, request, String.class);
            mediator.getNext(SUCCESS_MSG, SERVICE, dateTime);
            mediator.saveMicroserviceResult(SUCCESS_MSG, SERVICE, dateTime);
            mediator.saveOrechestratorResult(issue.getIdprocess(), 200,
                    SUCCESSTRANSPORT, null);

        }
        catch (Exception e){
            mediator.saveMicroserviceResult(FAIL_MSG,SERVICE,dateTime );
            mediator.saveOrechestratorResult(issue.getIdprocess(), 503,
                    MICROSERVICE + SERVICE + INTERNALSERVERERROR, e.getCause());
            logger.error(e.getMessage());
        }

    }


    //Método de cancelamento
    public void cancelTransport(String idTransport) {

        String apiUrl = serverUrl + "/cancel/" + idTransport;
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(idTransport, headers);
        try {
            String response = restTemplate.postForObject(apiUrl, request, String.class);
            mediator.getNext(SUCCESS_MSG, SERVICE, dateTime);
            mediator.saveMicroserviceResult(SUCCESS_MSG, SERVICE, dateTime);
        }
        catch (Exception e){
            mediator.saveMicroserviceResult(FAIL_MSG,SERVICE,dateTime );
            logger.error(e.getMessage());
        }
    }

}


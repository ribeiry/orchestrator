package com.saga.orchestrator.orchestrator.service;


import com.saga.orchestrator.orchestrator.mediator.Mediator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Transport;
import com.saga.orchestrator.orchestrator.model.TransportDto;
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

    private String apiUrl = "http://localhost:8001/transport";
    private final String FAIL_MSG = "FAIL";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Mediator mediator = new Mediator();

    private final String SERVICE = "TRANSPORT";
    private final String SUCESS_MSG = "SUCCESS";


    public String calculateTransport(Issue issue) {

        apiUrl = apiUrl + "/calculator/" + issue.getTransport().getCep();
//        Transport transportSendRequest = Transport.issueToTransport(issue);
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método calcula transport");
        try {
            //precisa testar com o método de pé
            String resultCalculate = restTemplate.getForObject(apiUrl, String.class);
            logger.info("O valor do transport é  {} ", resultCalculate);
            mediator.saveMicroserviceResult(SUCESS_MSG, SERVICE, dateTime);
            return resultCalculate;

        }
        catch (HttpClientErrorException e) {
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE, dateTime);
            logger.error(e.getMessage());
        }
        catch (Exception e){
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE, dateTime);
            logger.error(e.getMessage()  + "  Caiuu aquiii");
        }

        return null;
    }

    public void sendToTransport(Issue issue) {

        apiUrl = apiUrl + "/send";
        TransportDto transportSendRequest = TransportDto.issueToTransport(issue);
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Chamando o método send tranport() e efetuando a leitura de pedidos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransportDto> request = new HttpEntity<>(transportSendRequest, headers);
        try {
            //precisa testar com o método de pé
            String responseSendTransport = restTemplate.postForObject(apiUrl, request, String.class);
            List<Transport> transport = new ArrayList<>();
            logger.info("O id do transport é  {} ", responseSendTransport);
            mediator.saveMicroserviceResult(SUCESS_MSG, SERVICE, dateTime);

        } catch (HttpClientErrorException e) {
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE, dateTime);
            mediator.saveOrechestratorResult(issue.getOrder().getCodPedido(), e.getStatusCode().value(), SERVICE + "Indisponível", e.getCause());
            logger.error(e.getMessage() + "  Caiuu aquiii");
        }catch (Exception e){
            mediator.saveMicroserviceResult(FAIL_MSG,SERVICE,dateTime );
            mediator.saveOrechestratorResult(issue.getOrder().getCodPedido(), 503, SERVICE + "Exceção não tratada", e.getCause());
            logger.error(e.getMessage());
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
            logger.info("Pedido canecelado {} ", response);
            mediator.saveMicroserviceResult(SUCESS_MSG, SERVICE, dateTime);


        } catch (HttpClientErrorException e) {
            mediator.saveMicroserviceResult(FAIL_MSG, SERVICE, dateTime);
            logger.error(e.getMessage() + "  Caiuu aquiii");
        } catch (Exception e){
            mediator.saveMicroserviceResult(FAIL_MSG,SERVICE,dateTime );
            logger.error(e.getMessage());
        }

    }


}


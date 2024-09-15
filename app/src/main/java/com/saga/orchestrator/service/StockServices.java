package com.saga.orchestrator.service;


import com.saga.orchestrator.model.StockDto;
import com.saga.orchestrator.mediator.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static com.saga.orchestrator.constant.Constant.*;


@Service
public class StockServices {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Communicator mediator = new Communicator();
    private  String serverUrl;

    public void getAllStock() throws HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        String url = String.format("%s/stock", serverUrl);
        logger.info("Chamando o método getAllStock() e efetuando a leitura do estoque");
        try{
            StockDto[] stock = restTemplate.getForObject(url, StockDto[].class);
            logger.info("O retorno do estoque é %s", stock.length);
            for (int i = 0; i < stock.length; i++) {
                logger.info(String.valueOf(stock[i]));
            }
            mediator.getNext(SUCESS_MSG,SERVICE_STOCK,dateTime );
            mediator.saveMicroserviceResult(SUCESS_MSG,SERVICE_STOCK,dateTime );
        }
        catch (final  HttpClientErrorException e){

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                mediator.getNext(FAIL_MSG,SERVICE_STOCK,dateTime );
                mediator.saveMicroserviceResult(FAIL_MSG,SERVICE_STOCK,dateTime );
            }
            else{
                logger.info(e.getMessage());

            }
        }
    }

    public  void getAProduct(UUID idprocess, String id) throws HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        String url = String.format("%s/%s", serverUrl,id);

        try{
            logger.info("Chamando o método getAProduct() e efetuando a leitura de um produto no estoque");
            StockDto product = restTemplate.getForObject(url, StockDto.class);
            logger.info(String.valueOf(product));
            mediator.getNext(SUCESS_MSG,SERVICE_STOCK,dateTime);
            mediator.saveMicroserviceResult(SUCESS_MSG,SERVICE_STOCK,dateTime);
        }
        catch (final  HttpClientErrorException e){

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                mediator.getNext(FAIL_MSG,SERVICE_STOCK,dateTime);
                mediator.saveMicroserviceResult(FAIL_MSG,SERVICE_STOCK,dateTime);
            }
            mediator.saveOrechestratorResult(idprocess, e.getStatusCode().value(),
                    SERVICE_STOCK + "==== Nāo há quantidade suficiente de produtos ===", e.getCause());
        }
    }


    public StockServices() {
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String appConfigPath = rootPath + "application-server.teste";
            Properties server = new Properties();
            server.load(new FileInputStream(appConfigPath));
            serverUrl = server.getProperty("url.server.stock-service");
            logger.info(serverUrl);
        }
        catch (IOException ex){
            logger.info(ex.getMessage());
        }
    }

    public  void subAProduct(UUID idprocess, String id, int qtde) throws  HttpClientErrorException{
        StockDto stock = new StockDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/%s/sub",serverUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        logger.info("Chamando o método putSubAProduct() e efentuando a subtracao de um produto no estoque");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-qtde", String.valueOf(qtde));

        logger.info("Valor da quantidade retirada {} ", qtde);

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);

        logger.info("O Id do produto é: {}", id);
        HttpEntity<StockDto> requestEntity = new HttpEntity<StockDto>(stock,headers);
        try {
            ResponseEntity<StockDto> response = restTemplate.exchange(url , HttpMethod.PUT, requestEntity,  StockDto.class);
            stock = response.getBody();
            HttpStatusCode resp = response.getStatusCode();
            stock.setId(id);
            mediator.getNext(SUCESS_MSG,SERVICE_STOCK,dateTime );
            mediator.saveMicroserviceResult(SUCESS_MSG,SERVICE_STOCK,dateTime );
            logger.info("Retorno  {}", stock);
        }
        catch (final HttpClientErrorException e) {

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                logger.info("==== Nāo há quantidade suficiente de produtos ===");
                logger.error(e.getMessage() );
                mediator.getNext(FAIL_MSG,SERVICE_STOCK,dateTime );
                mediator.saveMicroserviceResult(FAIL_MSG,SERVICE_STOCK,dateTime );
                mediator.saveOrechestratorResult(idprocess, e.getStatusCode().value(),
                        SERVICE_STOCK + "==== Nāo há quantidade suficiente de produtos ===", e.getCause());
            }
            else{
                logger.error(e.getMessage());
                mediator.saveOrechestratorResult(idprocess, 503,
                        "Microservice : " + SERVICE_STOCK + "\n" + "Erro : Internal Server Error", e.getCause());
            }
        }
        catch (Exception e){
            mediator.getNext(FAIL_MSG,SERVICE_STOCK,dateTime );
            mediator.saveMicroserviceResult(FAIL_MSG,SERVICE_STOCK,dateTime );
            mediator.saveOrechestratorResult(idprocess, 503,
                    "Microservice : " + SERVICE_STOCK + "\n" + "Erro : Internal Server Error", e.getCause());
            logger.error(e.getMessage());
        }
    }

    public  void addAProduct(String id, int qtde) throws HttpClientErrorException{
        StockDto stock = new StockDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s%s/add",serverUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        logger.info("Chamando o método putAddAProduct() e efentuando a subtracao de um produto no estoque");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-qtde", String.valueOf(qtde));

        logger.info("Valor da quantidade retirada {} ", qtde);

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);

        logger.info("O Id do produto é: {} ", id);


        HttpEntity<StockDto> requestEntity = new HttpEntity<StockDto>(stock,headers);
        try{
            ResponseEntity<StockDto> response = restTemplate.exchange(url , HttpMethod.PUT, requestEntity,  StockDto.class);
            stock = response.getBody();
            stock.setId(id.split("/stock/")[1]);
            mediator.getNext("SUCESS",SERVICE_STOCK,dateTime );
            mediator.saveMicroserviceResult("SUCESS",SERVICE_STOCK,dateTime );
            logger.info(String.valueOf(stock));
        }
        catch (final HttpClientErrorException e ){
            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                logger.info("Nāo tem o produto informado");
                logger.error(e.getMessage());
                mediator.getNext("FAIL",SERVICE_STOCK,dateTime );
            }
            else{
                logger.error(e.getMessage());
            }
            mediator.saveMicroserviceResult("FAIL",SERVICE_STOCK,dateTime );
        }

    }

}

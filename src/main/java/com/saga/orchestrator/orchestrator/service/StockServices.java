package com.saga.orchestrator.orchestrator.service;


import com.saga.orchestrator.orchestrator.model.StockDto;
import com.saga.orchestrator.orchestrator.mediator.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class StockServices {

    private final String apiUrl = "http://localhost:8000/stock/";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final  String SERVICE = "STOCK";

    private final  String SUCESS_MSG = "SUCESS";

    private final  String FAIL_MSG = "FAIL";

    private Communicator mediator = new Communicator();


    public void getAllStock() throws HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        String url = String.format("%s/stock", apiUrl);
        logger.info("Chamando o método getAllStock() e efetuando a leitura do estoque");
        try{
            StockDto[] stock = restTemplate.getForObject(url, StockDto[].class);
            logger.info("O retorno do estoque é %s", stock.length);
            for (int i = 0; i < stock.length; i++) {
                logger.info(String.valueOf(stock[i]));
            }
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime );
        }
        catch (final  HttpClientErrorException e){

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                logger.info(e.getMessage() + "   caiu aquiiii");
                mediator.getNext(FAIL_MSG,SERVICE,dateTime );
            }
            else{
                logger.info(e.getMessage());

            }
        }
    }

    public  void getAProduct(String id) throws HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        LocalDateTime dateTime = LocalDateTime.now();
        String url = String.format(apiUrl,id);

        try{
            logger.info("Chamando o método getAProduct() e efetuando a leitura de um produto no estoque");
            StockDto product = restTemplate.getForObject(url, StockDto.class,id );
            logger.info(String.valueOf(product));
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime);
        }
        catch (final  HttpClientErrorException e){

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                logger.info(e.getMessage() + "    caiu aquiiii");
                mediator.getNext(FAIL_MSG,SERVICE,dateTime);
            }
        }
    }

    public  void SubAProduct(String id, int qtde) throws  HttpClientErrorException{
        StockDto stock = new StockDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s%s/sub",apiUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        logger.info("Chamando o método putSubAProduct() e efentuando a subtracao de um produto no estoque");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-qtde", String.valueOf(qtde));

        logger.info("Valor da quantidade retirada "+ String.valueOf(qtde));

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);

        logger.info("O Id do produto é: "+ id.split("/stock/")[1]);
        HttpEntity<StockDto> requestEntity = new HttpEntity<StockDto>(stock,headers);
        try {
            ResponseEntity<StockDto> response = restTemplate.exchange(url , HttpMethod.PUT, requestEntity,  StockDto.class);
            stock = response.getBody();
            HttpStatusCode resp = response.getStatusCode();
            stock.setId(id.split("/stock/")[1]);
            mediator.getNext(SUCESS_MSG,SERVICE,dateTime );
            logger.info("Retorno " + String.valueOf(stock));
        }
        catch (final HttpClientErrorException e) {

            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                //TODO QUANDO NÃO HOUVER QTDE SUFICIENTE
               logger.info(e.getMessage() + "   caiu aquiiii");
               mediator.getNext(FAIL_MSG,SERVICE,dateTime );
            }
            else{
                logger.info(e.getMessage());

            }
        }
    }

    public  void AddAProduct(String id, int qtde) throws HttpClientErrorException{
        StockDto stock = new StockDto();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s%s/add",apiUrl,id);
        LocalDateTime dateTime = LocalDateTime.now();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        logger.info("Chamando o método putAddAProduct() e efentuando a subtracao de um produto no estoque");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-qtde", String.valueOf(qtde));

        logger.info("Valor da quantidade retirada "+ String.valueOf(qtde));

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);

        logger.info("O Id do produto é: "+ id.split("/stock/")[1]);


        HttpEntity<StockDto> requestEntity = new HttpEntity<StockDto>(stock,headers);
        try{
            ResponseEntity<StockDto> response = restTemplate.exchange(url , HttpMethod.PUT, requestEntity,  StockDto.class);
            stock = response.getBody();
            stock.setId(id.split("/stock/")[1]);
            mediator.getNext("SUCESS",SERVICE,dateTime );
            logger.info(String.valueOf(stock));
        }
        catch (final HttpClientErrorException e ){
            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                //TODO QUANDO NÃO HOUVER QTDE SUFICIENTE
                logger.info(e.getMessage() + "   caiu aquiiii");
                mediator.getNext("FAIL",SERVICE,dateTime );
            }
            else{
                logger.info(e.getMessage());

            }

        }

    }

}

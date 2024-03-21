package com.saga.orchestrator.controller;

import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.service.Orchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrchestratorController {

    @Autowired
    Orchestrator orchestrator;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/issue")
    public ResponseEntity<String> issueOrder(@RequestBody Issue issue) {

        logger.info("Starting...");
        String cod_Pedido = orchestrator.callFunctions(issue);

        if(cod_Pedido.isBlank() || cod_Pedido.isEmpty()){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Houve um problema ao processar a requisicao");
        }
        return ResponseEntity.status(HttpStatus.OK).body(cod_Pedido);

    }


}

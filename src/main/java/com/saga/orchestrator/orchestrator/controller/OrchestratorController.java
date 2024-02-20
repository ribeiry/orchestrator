package com.saga.orchestrator.orchestrator.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.service.Orchestrator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
public class OrchestratorController {

    @Autowired
    Orchestrator orchestrator;

    @PostMapping("/issue")
    public ResponseEntity<String> issueOrder(@RequestBody Issue issue){

        System.out.println("Starting...");
        String cod_pedido = orchestrator.callFunctions(issue);

        if(cod_pedido.isBlank() || cod_pedido.isEmpty()){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Houve um problema ao processar a requisicao");
        }
        return ResponseEntity.status(HttpStatus.OK).body(cod_pedido);

    }


}

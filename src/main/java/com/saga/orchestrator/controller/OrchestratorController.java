package com.saga.orchestrator.controller;

import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.model.OrchestratorResultDTO;
import com.saga.orchestrator.service.Orchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class OrchestratorController {

    @Autowired
    Orchestrator orchestrator;

    @PostMapping("/issue")
    public ResponseEntity<String> issueOrder(@RequestBody Issue issue) {

//        System.out.println("Starting...");
//        String cod_pedido = orchestrator.callFunctions(issue);
//
//        if(cod_pedido.isBlank() || cod_pedido.isEmpty()){
//            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Houve um problema ao processar a requisicao");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(cod_pedido);



        issue.setIdprocess(UUID.randomUUID());
        OrchestratorResultDTO orchestratorResultDTO = orchestrator.callFunctions(issue);

        if(orchestratorResultDTO != null) {
            if (orchestratorResultDTO.getHttpstatuscod().equals("200")) {
                return  ResponseEntity.status(HttpStatus.OK).body(orchestratorResultDTO.getCodPedido());
            } else {
                return ResponseEntity.status(Integer.valueOf(orchestratorResultDTO.getHttpstatuscod())).body(orchestratorResultDTO.getHttpmessage());
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");


    }





}

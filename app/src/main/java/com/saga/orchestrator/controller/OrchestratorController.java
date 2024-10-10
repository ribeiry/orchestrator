package com.saga.orchestrator.controller;

import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.model.OrchestratorResultDTO;
import com.saga.orchestrator.service.Orchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.saga.orchestrator.constant.Constant.INTERNAL_SERVER_ERROR;
import static com.saga.orchestrator.constant.Constant.POST_URL;

@RestController
public class OrchestratorController {

    @Autowired
    Orchestrator orchestrator;
    @PostMapping(POST_URL)
    public ResponseEntity<String> issueOrder(@RequestBody Issue issue) {

        issue.setIdprocess(UUID.randomUUID());
        OrchestratorResultDTO orchestratorResultDTO = orchestrator.callFunctions(issue);
        HttpStatus status = HttpStatus.valueOf(orchestratorResultDTO.getHttpstatuscod());

        if (status.is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body(orchestratorResultDTO.getCodPedido());
        } else {
                return ResponseEntity.status(Integer.valueOf(orchestratorResultDTO.getHttpstatuscod())).
                    body(orchestratorResultDTO.getHttpcause());
        }
    }
}

package com.saga.orchestrator.service;

import com.saga.orchestrator.configuration.SetUp;
import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.model.OrchestratorResultDTO;
import com.saga.orchestrator.state.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Orchestrator {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Communicator mediator = new Communicator();
    @Autowired
    public SetUp setUp = new SetUp();

    public OrchestratorResultDTO callFunctions(Issue issue) {

        System.out.println(setUp.ORDER_SERVICE_URL);
        String cod_pedido = null;
        OrderState orderState = new OrderState();
        int i = 0;
        while (orderState.getState() != null){
           logger.info("Getting % state", i );
           logger.info(orderState.printStatus());
           orderState.nextState(issue);
        }
        return mediator.getOrechestratorResult(issue.getIdprocess().toString());
    }
}



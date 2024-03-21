package com.saga.orchestrator.service;

import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.state.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class Orchestrator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String callFunctions(Issue issue) {

       String cod_pedido = "";

       OrderState orderState = new OrderState();
       int i = 0;
       while (orderState.getState() != null){
           logger.info("Getting {} state", i );
           logger.info(orderState.printStatus());
           orderState.nextState(issue);
       }
       if(issue.getOrder().getCodPedido() != null){
           if(!issue.getOrder().getCodPedido().isEmpty() ||
                !issue.getOrder().getCodPedido().isBlank()){
            cod_pedido = issue.getOrder().getCodPedido();
           }
       }
       return cod_pedido;
    }
}



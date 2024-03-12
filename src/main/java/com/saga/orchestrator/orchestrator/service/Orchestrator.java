package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.state.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class Orchestrator {

    //Nessa classe que iremos controlar quais chamadas e qual é o status do semaforo para saber se andaremos


    //TODO: ->>> Tratar a exception de retorno (500)
    //TODO: ->>> Colocar as propriedades da Services em properties





    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Communicator mediator = new Communicator();

    public String callFunctions(Issue issue) {


        //TODO: Inserir um controle While

       String cod_pedido = "";

       OrderState orderState = new OrderState();
       int i = 0;
       while (orderState.getState() != null){
           logger.info("Getting % state", i );
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



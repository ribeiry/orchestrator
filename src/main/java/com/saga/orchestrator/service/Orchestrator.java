package com.saga.orchestrator.service;

import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.model.OrchestratorResultDTO;
import com.saga.orchestrator.state.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class Orchestrator {

    //TODO: Nessa classe que iremos controlar quais chamadas e qual é o status do semaforo para saber se andaremos
    //TODO: para a proxima requisicao.
    //TODO: ->>> 1º Gera Pedido  & Retira do Estoque -->> 2º Calcula Frete --> 3º Pagamento (calcula soma produtos + frete) --> 5º Envia produto


    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Communicator mediator = new Communicator();

    public OrchestratorResultDTO callFunctions(Issue issue) {


        //TODO: Inserir um controle While

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



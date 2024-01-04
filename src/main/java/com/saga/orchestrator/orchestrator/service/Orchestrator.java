package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.state.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class Orchestrator {

    //TODO: Nessa classe que iremos controlar quais chamadas e qual é o status do semaforo para saber se andaremos
    //TODO: para a proxima requisicao.
    //TODO: ->>> 1º Gera Pedido  -->> 2º Retira do Estoque -->> 3º Calcula Frete --> 4º Pagamento (calcula soma produtos + frete) --> 5º Envia produto

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Communicator mediator = new Communicator();

    public void callFunctions(Issue issue) {
       OrderState orderState = new OrderState();

       //Cria pedido
       orderState.nextState(issue);
       orderState.printStatus();

       //Envia para pagamento
        orderState.nextState(issue);
        orderState.printStatus();

        //Envia para Transport
       if("SUCCESS".equals(mediator.getStatus("PAYMENT").getMessage())){
           orderState.nextState(issue);
           orderState.printStatus();
       }
    }
}



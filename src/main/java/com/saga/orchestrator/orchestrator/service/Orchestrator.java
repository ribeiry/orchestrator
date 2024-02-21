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
    //TODO: ->>> 1º Gera Pedido  & Retira do Estoque -->> 2º Calcula Frete --> 3º Pagamento (calcula soma produtos + frete) --> 5º Envia produto

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Communicator mediator = new Communicator();

    public String callFunctions(Issue issue) {

       String cod_pedido = null;

       OrderState orderState = new OrderState();


       //Cria pedido
       orderState.nextState(issue);
       logger.info("Getting first state...");
       logger.info(orderState.printStatus());

       //Separa o Estoque
       orderState.nextState(issue);
        logger.info("Getting second state...");
        logger.info(orderState.printStatus());


       //Envia para pagamento
        orderState.nextState(issue);
        logger.info("Getting third state...");
        logger.info(orderState.printStatus());

        //Envia para Transport
        if(orderState.isValidaPrev()) {
            if ("SUCCESS".equals(mediator.getStatus("PAYMENTS").getMessage())) {
                orderState.nextState(issue);
                logger.info("Getting fourth state...");
                logger.info(orderState.printStatus());

                cod_pedido = issue.getOrder().getCodPedido();
            }
        }
        else {
            cod_pedido = "";
        }
       return cod_pedido;
    }
}



package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Mediator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.OrchestratorResultDTO;
import com.saga.orchestrator.orchestrator.state.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class Orchestrator {

    //Nessa classe que iremos controlar quais chamadas e qual é o status do semaforo para saber se andaremos


    //TODO: ->>> Tratar a exception de retorno (500)
    //TODO: ->>> Colocar as propriedades da Services em properties





    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Mediator mediator = new Mediator();

    public OrchestratorResultDTO callFunctions(Issue issue) {


        //TODO: Inserir um controle While

//       String cod_pedido = "";

       State state = new State();
       int i = 0;
       while (state.getOrderState() != null){
           logger.info("Getting % state", i );
           logger.info(state.printStatusState());
           state.nextState(issue);
       }

//        OrchestratorResultDTO orchestratorResultDTO = new OrchestratorResultDTO();

       return mediator.getOrechestratorResult(issue.getOrder().getCodPedido());
//       if(issue.getOrder().getCodPedido() != null){
//           if(!issue.getOrder().getCodPedido().isEmpty() ||
//                !issue.getOrder().getCodPedido().isBlank()){
//            cod_pedido = issue.getOrder().getCodPedido();
//           }
//       }
//       return cod_pedido;
    }
}



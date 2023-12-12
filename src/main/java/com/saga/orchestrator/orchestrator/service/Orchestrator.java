package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Communicator;

public class Orchestrator {

    //TODO: Nessa classe que iremos controlar quais chamadas e qual é o status do semaforo para saber se andaremos
    //TODO: para a proxima requisicao.
    //TODO:
    // 1 - CRIA O PEDIDO
    // 2 - VERIFICA Produto no ESTOQUE
    // 3 - RETIRA DO ESTOQUE
    // 4 - CALCULO O PRECO DO ESTOQUE
    // 5 - CRIO PAGAMENTO
    // 6 - EXIBO PRAZO DE ENTREGA


    private Communicator mediator = new Communicator();
    public boolean callFunctions(){
        boolean next = true;




        return next;
    }

}

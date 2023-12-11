package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Communicator;

public class Orchestrator {

    //TODO: Nessa classe que iremos controlar quais chamadas e qual é o status do semaforo para saber se andaremos
    //TODO: para a proxima requisicao.
    //TODO: 1 - CRIA O PEDIDO 2 - VERIFICA Produto no ESTOQUE 3 - RETIRA DO ESTOQUE  4 - CRIO PAGAMENTO  5 - EXIBO PRAZO DE ENTREGA


    private Communicator mediator = new Communicator();
    public boolean callFunctions(){
        boolean next = true;

       // StockServices stockServices = new StockServices();

        //stockServices.getAProduct("oo1");
        //if(mediator.getStatus() == SUCESS AND mediator.getServico() == StockServices){
        //    stockServices.SubAProduct("oo1",2);
       // }
        //else if(mediator.getStatus() == FAIL && mediator.getServico()== StockServices){
          //  OrderServices orderServices = new OrderServices();
          //  orderServices.CancelOrder("XXX");

        //}


        return next;
    }

}

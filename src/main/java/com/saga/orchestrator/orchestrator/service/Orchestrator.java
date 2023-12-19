package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.OrderDto;
import org.springframework.stereotype.Service;

@Service
public class Orchestrator {

    //TODO: Nessa classe que iremos controlar quais chamadas e qual é o status do semaforo para saber se andaremos
    //TODO: para a proxima requisicao.
    //TODO: ->>> 1º Consulta Estoque -->>> 2º Gera Pedido  -->> 3º Retira do Estoque -->> 4º Calcula Frete --> 5º Pagamento (calcula soma produtos + frete) --> 6º Envia produto


    private Communicator mediator = new Communicator();

    public void callFunctions() {

        StockServices stockServices = new StockServices();
        String idProduto = "";
        Integer qtde = 0;


        stockServices.getAProduct(idProduto);
        if ("SUCCESS".equals(mediator.getStatus("STOCK").getMessage())) {
            OrderServices orderServices = new OrderServices();
            OrderDto order = new OrderDto();
            orderServices.CreateOrder(order);

            if ("SUCCESS".equals(mediator.getStatus("ORDER").getMessage())) {

                stockServices.SubAProduct(idProduto, qtde);

                if ("SUCCESS".equals(mediator.getStatus("STOCK").getMessage())) {
                    //TODO CALCULA FRETE
                    TransportServices transportServices = new TransportServices();

                    if ("SUCCESS".equals(mediator.getStatus("PAYMENTS").getMessage())) {

                        //TODO EFETUA PAGAMENTO

                        if ("SUCCESS".equals(mediator.getStatus("TRANSPORT").getMessage())) {
                            Issue issue = new Issue();
                            transportServices.sendToTransport(issue);
                        } else {
                            //ESTORNA PAGAMENTO
                            stockServices.AddAProduct(idProduto, qtde);
                            orderServices.CancelOrder(order.getCodPedido().toString());
                        }

                    } else {
                        stockServices.AddAProduct(idProduto, qtde);
                        orderServices.CancelOrder(order.getCodPedido().toString());
                    }
                }
                else {
                    orderServices.CancelOrder(order.getCodPedido().toString());
                }
            }
        }
    }
}



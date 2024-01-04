package com.saga.orchestrator.orchestrator.service;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Order;
import com.saga.orchestrator.orchestrator.model.Product;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class Orchestrator {

    //TODO: Nessa classe que iremos controlar quais chamadas e qual é o status do semaforo para saber se andaremos
    //TODO: para a proxima requisicao.
    //TODO: ->>> 1º Consulta Estoque -->>> 1º Gera Pedido  -->> 2º Retira do Estoque -->> 4º Calcula Frete --> 5º Pagamento (calcula soma produtos + frete) --> 6º Envia produto



    private Communicator mediator = new Communicator();

    public void callFunctions(Issue issue) {

        StockServices stockServices = new StockServices();
        List<Product> products = issue.getOrder().getProdutos();

        for (Product product : products) {

            //TRATAR EXCEÇÃO E ADD QTD NA REQUISIÇÃO
            stockServices.getAProduct(product.getIdProduto());
            //Integer qtde = 0;

        }

        //stockServices.getAProduct(idProduto);
        if ("SUCCESS".equals(mediator.getStatus("STOCK").getMessage())) {
            OrderServices orderServices = new OrderServices();
            Order order = issue.getOrder();
            orderServices.CreateOrder(order);

            if ("SUCCESS".equals(mediator.getStatus("ORDER").getMessage())) {

                //Lista de produto
                for (Product product : products) {
                    //TRATAR EXCEÇÃO E ADD QTD NA REQUISIÇÃO
                    stockServices.SubAProduct(product.getIdProduto(), product.getQuantidade());
                }



                if ("SUCCESS".equals(mediator.getStatus("STOCK").getMessage())) {

                    //TODO : Chamar método de calculo de frete
                    TransportServices transportServices = new TransportServices();
                    Double valueTransport = Double.parseDouble(transportServices.calculateTransport(issue));


                    if ("SUCCESS".equals(mediator.getStatus("PAYMENTS").getMessage())) {

                        //TODO Calucula valor de pagamento
                        //Feito
                        Double valueOfPayment = null;
                        for (Product product : products) {
                            valueOfPayment = valueOfPayment + product.getPreco();
                        }
                        valueOfPayment = valueOfPayment+valueTransport;

                        //TODO EFETUA PAGAMENTO

                        if ("SUCCESS".equals(mediator.getStatus("TRANSPORT").getMessage())) {
//                            Issue issue = new Issue();
//                            transportServices.sendToTransport(issue);
                        } else {

                            //ESTORNA PAGAMENTO
                            for (Product product : products) {
                                stockServices.AddAProduct(product.getIdProduto(), product.getQuantidade());
                            }


                            orderServices.CancelOrder(order.getCodPedido().toString());
                        }

                    } else {
                        //ESTORNA PAGAMENTO
                        for (Product product : products) {
                            stockServices.AddAProduct(product.getIdProduto(), product.getQuantidade());
                        }
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



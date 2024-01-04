package com.saga.orchestrator.orchestrator.state;


import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Product;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.PaymentServices;
import com.saga.orchestrator.orchestrator.service.StockServices;
import com.saga.orchestrator.orchestrator.service.TransportServices;

import java.util.List;

public class ApprovePaymentStateI implements IOrderState {
    private Communicator mediator = new Communicator();

    @Override
    public void next(OrderState orderState, Issue issue) {

        TransportServices transportServices = new TransportServices();

        orderState.nextState(issue);
        PaymentServices paymentServices = new PaymentServices();
        paymentServices.payOrder(issue);
        if ("SUCCESS".equals(mediator.getStatus("PAYMENT"))) {
            orderState.setState(new TransportStateI());
        }
        else {
            prev(null,issue);
        }
    }


    private void prev(OrderState orderState, Issue issue) {
        StockServices stockServices = new StockServices();
        OrderServices orderServices = new OrderServices();
        orderState.setState(new ProductStateI());
        orderServices.CancelOrder(issue.getOrder().getCodPedido());

        List<Product> products = issue.getOrder().getProdutos();
        for (Product product : products) {
            stockServices.AddAProduct(product.getIdProduto(), product.getQuantidade());
        }
    }

    @Override
    public void printStatus() {
        System.out.println("O pedido está com pagamento aprovado");
    }
}

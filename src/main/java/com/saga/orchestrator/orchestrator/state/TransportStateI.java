package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Product;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.PaymentServices;
import com.saga.orchestrator.orchestrator.service.StockServices;
import com.saga.orchestrator.orchestrator.service.TransportServices;

import java.util.List;

public class TransportStateI implements IOrderState {
    private Communicator mediator = new Communicator();
    @Override
    public void next(OrderState orderState, Issue issue) {
        TransportServices transportServices = new TransportServices();
        transportServices.sendToTransport(issue);
        System.out.println("O pedido já saiu para entrega");

        if (!"SUCCESS".equals(mediator.getStatus("TRANSPORT").getMessage())) {
            prev(null, issue);
        }
    }

    private void prev(OrderState orderState, Issue issue) {
        PaymentServices paymentServices = new PaymentServices();
        StockServices stockServices = new StockServices();
        List<Product> products = issue.getOrder().getProdutos();
        OrderServices orderServices = new OrderServices();

        orderState.setState(new ApprovePaymentStateI());
        orderServices.CancelOrder(issue.getOrder().getCodPedido());
        paymentServices.cancelTransport("IDPAGAMENTO");
        for (Product product : products) {
            stockServices.addAProduct(product.getIdProduto(), product.getQuantidade());
        }
    }

    @Override
    public String printStatus() {
        return  "O pedido já saiu para entrega";

    }
}

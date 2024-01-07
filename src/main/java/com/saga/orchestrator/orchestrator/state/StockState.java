package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Product;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.StockServices;

import java.util.List;

public class StockState implements  IOrderState{

    private final Communicator mediator = new Communicator();
    @Override
    public void next(OrderState orderState, Issue issue) {
        StockServices stockServices = new StockServices();
        List<Product> products = issue.getOrder().getProdutos();
        for (Product product : products){
            stockServices.SubAProduct(product.getIdProduto(), product.getQuantidade());
        }
        if ("SUCCESS".equals(mediator.getStatus("ORDER").getMessage())) {
            orderState.setState(new ApprovePaymentStateI());
        }
        else {
            prev(null,issue);
        }
    }

    private void prev(OrderState orderState, Issue issue) {
        OrderServices orderServices = new OrderServices();
        orderServices.CancelOrder(issue.getOrder().getCodPedido());
        orderState.setState(new CreateOrderStateI());
    }

    @Override
    public String printStatus() {
        return "O pedido tem produtos no estoque";

    }
}

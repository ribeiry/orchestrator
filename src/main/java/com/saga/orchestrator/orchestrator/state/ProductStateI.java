package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.StockServices;


public class ProductStateI implements IOrderState {

    private Communicator mediator = new Communicator();
    private  StockServices stockServices = new StockServices();

    @Override
    public void next(OrderState orderState, Issue issue) {
        OrderServices orderServices = new OrderServices();
        orderServices.CreateOrder(issue.getOrder());
        if ("SUCCESS".equals(mediator.getStatus("ORDER").getMessage())) {
            orderState.setState(new ApprovePaymentStateI());
        }
        else {
            prev(null,null);
        }
    }

    private void prev(OrderState orderState, Issue issue) {
        System.out.println("Nāo tenho o produto o produto no estoque");
    }

    @Override
    public String printStatus() {
        System.out.println("O pedido tem produtos no estoque");
        return null;
    }
}

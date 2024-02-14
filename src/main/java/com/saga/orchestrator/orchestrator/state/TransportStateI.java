package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Product;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.PaymentServices;
import com.saga.orchestrator.orchestrator.service.StockServices;
import com.saga.orchestrator.orchestrator.service.TransportServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TransportStateI implements IOrderState {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void next(OrderState orderState, Issue issue, boolean validaPrev) {
        if(validaPrev) {
            TransportServices transportServices = new TransportServices();
            transportServices.sendToTransport(issue);
           logger.info("O pedido já saiu para entrega");

        }
    }

    public void prevState(OrderState orderState, Issue issue, boolean validaPrev) {
        TransportServices transportServices = new TransportServices();
        transportServices.cancelTransport(issue.getTransport().getTransport_id());
        orderState.setState(new ApprovePaymentStateI());
    }

    @Override
    public String printStatus() {
        return  "O pedido já saiu para entrega";

    }
}

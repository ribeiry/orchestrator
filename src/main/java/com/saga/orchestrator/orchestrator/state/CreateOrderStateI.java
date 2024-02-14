package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateOrderStateI implements IOrderState {

    private final Communicator mediator = new Communicator();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void next(OrderState orderState, Issue issue, boolean validaPrev) {
        if(validaPrev) {
            OrderServices orderServices = new OrderServices();
            String codPedido = orderServices.CreateOrder(issue.getOrder());
            issue.getOrder().setCodPedido(codPedido);
            if ("SUCCESS".equals(mediator.getStatus("ORDER").getMessage())) {
                orderState.setState(new StockState());
            }
        }
    }

    public void prevState(OrderState orderState, Issue issue,boolean validaPrev) {
        try {
            OrderServices orderServices = new OrderServices();
            orderServices.CancelOrder(issue.getOrder().getCodPedido());
            orderState.setState(new CreateOrderStateI());
            validaPrev = false;
        }
        catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public String printStatus() {
        return "O pedido tem produtos no estoque";

    }
}
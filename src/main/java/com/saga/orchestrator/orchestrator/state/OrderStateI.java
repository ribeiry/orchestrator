package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Mediator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderStateI implements IOrderState {

    private final Mediator mediator = new Mediator();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void nextOrderState(State state, Issue issue) {
        if(state.isValidaPrev()) {
            OrderServices orderServices = new OrderServices();
            String codPedido = orderServices.CreateOrder(issue.getOrder());
            issue.getOrder().setCodPedido(codPedido);
            if ("SUCCESS".equals(mediator.getMicroserviceResult("ORDER").getMessage())) {
                state.setOrderState(new StockState());
            }
            else{
                this.prevOrderState(state, issue);

            }
        }
        else {
            this.prevOrderState(state,issue);
        }
    }

    public void prevOrderState(State state, Issue issue) {
        state.setValidaPrev(false);
        try {
            OrderServices orderServices = new OrderServices();
            state.setOrderState(null);
            orderServices.CancelOrder(issue.getOrder().getCodPedido());
//            issue.getOrder().setCodPedido(null);
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public String printStatusOrderState() {
        return "O pedido tem produtos no estoque";

    }
}

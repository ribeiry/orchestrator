package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Mediator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.service.TransportServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransportStateI implements IOrderState {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Mediator mediator = new Mediator();
    @Override
    public void nextOrderState(State state, Issue issue) {
        if(state.isValidaPrev()) {
            TransportServices transportServices = new TransportServices();
            transportServices.sendToTransport(issue);
            logger.info("O pedido já saiu para entrega");
            if ("FAIL".equals(mediator.getMicroserviceResult("ORDER").getMessage())) {
                this.prevOrderState(state, issue);
            }
            else {
                state.setOrderState(null);
            }
        }
        else{
            this.prevOrderState(state,issue);
        }
    }

    public void prevOrderState(State state, Issue issue) {
        try {
            state.setValidaPrev(false);
            TransportServices transportServices = new TransportServices();
            transportServices.cancelTransport(issue.getTransport().getTransport_id());
            state.setOrderState(new ApprovePaymentStateI());
        }
        catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public String printStatusOrderState() {
        return  "O pedido já saiu para entrega";

    }
}

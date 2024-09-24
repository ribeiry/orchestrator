package com.saga.orchestrator.state;

import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.service.StockServices;
import com.saga.orchestrator.service.TransportServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.saga.orchestrator.constant.Constant.*;

@Component
public class TransportStateI implements IOrderState {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Communicator mediator = new Communicator();
    @Override
    public void next(OrderState orderState, Issue issue) {
        if(orderState.isValidaPrev()) {
            TransportServices transportServices = new TransportServices();
            transportServices.sendToTransport(issue);
            logger.info(ORDERSUCCESSDELVIERY);
            if (FAIL_MSG.equals(mediator.getStatus(SERVICE_ORDER).getMessage())) {
                this.prevState(orderState, issue);
            }
            else {
                orderState.setState(null);
            }
        }
        else{
            this.prevState(orderState,issue);
        }
    }

    public void prevState(OrderState orderState, Issue issue) {
        try {
            orderState.setValidaPrev(false);
            TransportServices transportServices = new TransportServices();
            transportServices.cancelTransport(issue.getTransport().getTransport_id());
            orderState.setState(new ApprovePaymentStateI());
        }
        catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public String printStatus() {
        return  "O pedido já saiu para entrega";

    }
}

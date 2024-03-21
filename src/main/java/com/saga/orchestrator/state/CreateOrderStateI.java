package com.saga.orchestrator.state;

import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.service.OrderServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class CreateOrderStateI implements IOrderState {

    private final Communicator mediator = new Communicator();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    OrderServices orderServices ;

    @Override
    public void next(OrderState orderState, Issue issue) {
        if(orderState.isValidaPrev()) {

             orderServices = new  OrderServices();
            String codPedido = orderServices.createOrder(issue.getOrder());
            issue.getOrder().setCodPedido(codPedido);
            if ("SUCCESS".equals(mediator.getStatus("ORDER").getMessage())) {
                orderState.setState(new StockState());
            }
            else{
                this.prevState(orderState, issue);

            }
        }
        else {
            this.prevState(orderState,issue);
        }
    }

    public void prevState(OrderState orderState, Issue issue) {
        orderState.setValidaPrev(false);
        try {
            OrderServices orderServices = new OrderServices();
            orderState.setState(null);
            orderServices.cancelOrder(issue.getOrder().getCodPedido());
            issue.getOrder().setCodPedido("");

        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public String printStatus() {
        return "O pedido tem produtos no estoque";

    }
}

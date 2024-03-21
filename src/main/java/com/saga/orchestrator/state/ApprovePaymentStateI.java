package com.saga.orchestrator.state;


import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.model.Product;
import com.saga.orchestrator.service.PaymentServices;
import com.saga.orchestrator.service.TransportServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class ApprovePaymentStateI implements IOrderState {
    private final Communicator mediator = new Communicator();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void next(OrderState orderState, Issue issue) {

        if(orderState.isValidaPrev()) {
            TransportServices transportServices = new TransportServices();
            try {
                Double transportValue = Double.parseDouble(transportServices.calculateTransport(issue));
                Double somaProduct = (double) 0;
                List<Product> produtos = issue.getOrder().getProdutos();
                for (Product produto : produtos) {
                    somaProduct += produto.getPreco().doubleValue();

                }

                Double totalValue = transportValue + somaProduct;
                issue.getPayment().setPaymentValue(totalValue);


                PaymentServices paymentServices = new PaymentServices();
                paymentServices.payOrder(issue);
                if ("SUCCESS".equals(mediator.getStatus("PAYMENTS").getMessage())) {
                    orderState.setState(new TransportStateI());
                }
                else {
                    this.prevState(orderState,issue);
                }
            }
            catch (Exception e ){
                logger.error(e.getMessage());
                mediator.getNext("FAIL", "PAYMENTS", LocalDateTime.now());
                orderState.setValidaPrev(false);
            }

        }else{
            this.prevState(orderState,issue);
        }
    }


    public void prevState(OrderState orderState, Issue issue) {
        orderState.setValidaPrev(false);
        orderState.setState(new StockState());
        try {
            PaymentServices paymentServices = new PaymentServices();
            paymentServices.cancelPayment(issue.getPayment().getPaymentId());
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public String printStatus() {
        return "O pedido está com pagamento aprovado";
    }
}

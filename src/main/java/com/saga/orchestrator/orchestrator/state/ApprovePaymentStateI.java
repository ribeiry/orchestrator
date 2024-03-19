package com.saga.orchestrator.orchestrator.state;


import com.saga.orchestrator.orchestrator.mediator.Mediator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Product;
import com.saga.orchestrator.orchestrator.service.PaymentServices;
import com.saga.orchestrator.orchestrator.service.TransportServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class ApprovePaymentStateI implements IOrderState {
    private Mediator mediator = new Mediator();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void nextOrderState(State state, Issue issue) {

        if(state.isValidaPrev()) {
            TransportServices transportServices = new TransportServices();
            //TODO Tratar a excecao
            try {
                Double transportValue = Double.parseDouble(transportServices.calculateTransport(issue));
                Double somaProduct = (double) 0;
                //Adiciona ao valor do pedido
                List<Product> produtos = issue.getOrder().getProdutos();
                for (Product produto : produtos) {
                    somaProduct += produto.getPreco().doubleValue();

                }

                Double totalValue = transportValue + somaProduct;
                issue.getPayment().setPaymentValue(totalValue);

                //Efetua pgamento do pedido
                PaymentServices paymentServices = new PaymentServices();
                paymentServices.payOrder(issue);
                if ("SUCCESS".equals(mediator.getMicroserviceResult("PAYMENTS").getMessage())) {
                    state.setOrderState(new TransportStateI());
                }
                else {
                    this.prevOrderState(state,issue);
                }
            }
            catch (Exception e ){
                logger.error(e.getMessage());
                mediator.saveMicroserviceResult("FAIL", "PAYMENTS", LocalDateTime.now());
                state.setValidaPrev(false);
            }

        }else{
            this.prevOrderState(state,issue);
        }
    }


    public void prevOrderState(State state, Issue issue) {
        state.setValidaPrev(false);
        state.setOrderState(new StockState());
        try {
            PaymentServices paymentServices = new PaymentServices();
            paymentServices.cancelPayment(issue.getPayment().getPaymentId());
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public String printStatusOrderState() {
        System.out.println("O pedido está com pagamento aprovado");
        return null;
    }
}

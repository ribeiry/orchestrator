package com.saga.orchestrator.orchestrator.state;


import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.OrderDto;
import com.saga.orchestrator.orchestrator.model.Product;
import com.saga.orchestrator.orchestrator.model.ProdutoDTO;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.PaymentServices;
import com.saga.orchestrator.orchestrator.service.StockServices;
import com.saga.orchestrator.orchestrator.service.TransportServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ApprovePaymentStateI implements IOrderState {
    private Communicator mediator = new Communicator();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void next(OrderState orderState, Issue issue, boolean validaPrev) {

        if(validaPrev) {
            TransportServices transportServices = new TransportServices();
            //TODO Tratar a excecao
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
            if ("SUCCESS".equals(mediator.getStatus("PAYMENTS").getMessage())) {
                orderState.setState(new TransportStateI());
            }
        }
    }


    public void prevState(OrderState orderState, Issue issue, boolean validaPrev) {
        try {
            PaymentServices paymentServices = new PaymentServices();
            orderState.setState(new StockState());
            paymentServices.cancelPayment(issue.getPayment().getPaymentId());
        }
        catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public String printStatus() {
        System.out.println("O pedido está com pagamento aprovado");
        return null;
    }
}

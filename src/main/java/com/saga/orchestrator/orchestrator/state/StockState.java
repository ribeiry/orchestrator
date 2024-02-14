package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Communicator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Product;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.StockServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StockState implements  IOrderState{

    private final Communicator mediator = new Communicator();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void next(OrderState orderState, Issue issue, boolean validaPrev) {
        if(validaPrev) {
            StockServices stockServices = new StockServices();
            List<Product> products = issue.getOrder().getProdutos();
            for (Product product : products) {
                stockServices.subAProduct(product.getIdProduto(), product.getQuantidade());
                if (!"SUCCESS".equals(mediator.getStatus("STOCK").getMessage())) {
                    break;
                }
            }
            if ("SUCCESS".equals(mediator.getStatus("STOCK").getMessage())) {
                orderState.setState(new ApprovePaymentStateI());
            }
        }
    }

    public void prevState(OrderState orderState, Issue issue, boolean validaPrev) {
        try {
            StockServices stockServices = new StockServices();
            orderState.setState(new CreateOrderStateI());
            List<Product> products = issue.getOrder().getProdutos();
            for (Product product : products) {
                stockServices.addAProduct(product.getIdProduto(), product.getQuantidade());
            }
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

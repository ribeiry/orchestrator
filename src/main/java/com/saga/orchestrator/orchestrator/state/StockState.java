package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.mediator.Mediator;
import com.saga.orchestrator.orchestrator.model.Issue;
import com.saga.orchestrator.orchestrator.model.Product;
import com.saga.orchestrator.orchestrator.service.StockServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StockState implements  IOrderState{

    private final Mediator mediator = new Mediator();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void nextOrderState(State state, Issue issue) {
        if(state.isValidaPrev()) {
            StockServices stockServices = new StockServices();
            List<Product> products = issue.getOrder().getProdutos();
            for (Product product : products) {
                stockServices.subAProduct(issue.getOrder().getCodPedido(), product.getIdProduto(), product.getQuantidade());
                if (!"SUCCESS".equals(mediator.getMicroserviceResult("STOCK").getMessage())) {
                    break;
                }
            }
            if ("SUCCESS".equals(mediator.getMicroserviceResult("STOCK").getMessage())) {
                state.setOrderState(new ApprovePaymentStateI());
            }
            else{
                this.prevOrderState(state,issue);
            }
        }
        else{
            this.prevOrderState(state,issue);
        }
    }

    public void prevOrderState(State state, Issue issue) {
        state.setValidaPrev(false);
        state.setOrderState(new OrderStateI());
        try {
            StockServices stockServices = new StockServices();
            List<Product> products = issue.getOrder().getProdutos();
            for (Product product : products) {
                stockServices.addAProduct(product.getIdProduto(), product.getQuantidade());
            }
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

package com.saga.orchestrator.state;

import com.saga.orchestrator.mediator.Communicator;
import com.saga.orchestrator.model.Issue;
import com.saga.orchestrator.model.Product;
import com.saga.orchestrator.service.StockServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.saga.orchestrator.constant.Constant.SERVICE_STOCK;
import static com.saga.orchestrator.constant.Constant.SUCCESS_MSG;

@Component
public class StockState implements  IOrderState{

    private final Communicator mediator = new Communicator();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private  StockServices stockServices;

    @Override
    public void next(OrderState orderState, Issue issue) {
        if(orderState.isValidaPrev()) {
            stockServices = new StockServices();
            List<Product> products = issue.getOrder().getProdutos();
            for (Product product : products) {
                stockServices.subAProduct(issue.getIdprocess(), product.getIdProduto(), product.getQuantidade());
                if (!SUCCESS_MSG.equals(mediator.getStatus(SERVICE_STOCK).getMessage())) {
                    break;
                }
            }
            if (SUCCESS_MSG.equals(mediator.getStatus(SERVICE_STOCK).getMessage())) {
                orderState.setState(new ApprovePaymentStateI());
            }
            else{
                this.prevState(orderState,issue);
            }
        }
        else{
            this.prevState(orderState,issue);
        }
    }

    public void prevState(OrderState orderState, Issue issue) {
        orderState.setValidaPrev(false);
        orderState.setState(new CreateOrderStateI());
        try {
            stockServices = new StockServices();
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
    public String printStatus() {
        return "O pedido tem produtos no estoque";

    }
}

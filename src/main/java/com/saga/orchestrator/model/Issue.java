package com.saga.orchestrator.model;

import java.util.UUID;

public class Issue {

    //Esta seria o objeto (body) da requisição ao orquestrador, conforme dessenho deveria haver os seguintes objetos :


    //Dados Cliente
    //Daddos Product
    // e Dados transporte conforme abaixo

    UUID idprocess;

    Order order;

    Transport transport;

    Payment payment;

    public UUID getIdprocess() {
        return idprocess;
    }

    public void setIdprocess(UUID idprocess) {
        this.idprocess = idprocess;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

}

package com.saga.orchestrator.orchestrator.model;

public class Issue {

    //Esta seria o objeto (body) da requisição ao orquestrador, conforme dessenho deveria haver os seguintes objetos :


    //Dados Cliente
    //Daddos Product
    // e Dados transporte conforme abaixo

    Order order;

    Transport transport;

    Payment payment;

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

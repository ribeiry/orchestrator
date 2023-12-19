package com.saga.orchestrator.orchestrator.model;

public class Issue {

    //Esta seria o objeto (body) da requisição ao orquestrador, conforme dessenho deveria haver os seguintes objetos :


    //Dados Cliente
    //Daddos Produto
    //Dados Pagamento
    // e Dados transporte conforme abaixo




    Transport transport;

    Payment payment;


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

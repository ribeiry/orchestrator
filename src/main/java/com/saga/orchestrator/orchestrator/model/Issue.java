package com.saga.orchestrator.orchestrator.model;

public class Issue {

    //Esta seria o objeto (body) da requisição ao orquestrador, conforme dessenho deveria haver os seguintes objetos :


    //Dados Cliente
    //Daddos Produto
    //Dados Pagamento
    // e Dados transporte conforme abaixo

    Transport transport;

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}

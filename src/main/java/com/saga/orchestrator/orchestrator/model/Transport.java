package com.saga.orchestrator.orchestrator.model;

public class Transport {

    public  String transport_id;
    public String cep;
    public String rua;
    public int numero;
    public String bairro;
    public String cidade;
    public String uf;

    public String getTransport_id() {
        return transport_id;
    }

    public void setTransport_id(String transport_id) {
        this.transport_id = transport_id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }


    public static Transport issueToTransport(Issue issue) {
        Transport transport = new Transport();

        transport.setCep(issue.getTransport().getCep());
        transport.setRua(issue.getTransport().getRua());
        transport.setNumero(issue.getTransport().getNumero());
        transport.setBairro(issue.getTransport().getBairro());
        transport.setCidade(issue.getTransport().getCidade());
        transport.setUf(issue.getTransport().getUf());
        return transport;
    }

}

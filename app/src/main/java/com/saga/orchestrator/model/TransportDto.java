package com.saga.orchestrator.model;

import java.util.ArrayList;
import java.util.List;

public class TransportDto {


    private String orderid;
    private String status;
    private Double transport_value;
    private List<String> list_id_products;


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTransport_value() {
        return transport_value;
    }

    public void setTransport_value(Double transport_value) {
        this.transport_value = transport_value;
    }

    public List<String> getList_id_products() {
        return list_id_products;
    }

    public void setList_id_products(List<String> list_id_products) {
        this.list_id_products = list_id_products;
    }



    public static TransportDto issueToTransport(Issue issue) {
        TransportDto transportDto = new TransportDto();

        transportDto.setOrderid(issue.getOrder().getCodPedido());
        transportDto.setTransport_value(issue.getPayment().getPaymentValue());
        transportDto.setStatus("enviando");

        List<String> lista_cod_produto = new ArrayList<>();
        for (Product produto : issue.getOrder().getProdutos()) {
            lista_cod_produto.add(produto.getIdProduto());
        }
        transportDto.setList_id_products(lista_cod_produto);
        return transportDto;
    }
}

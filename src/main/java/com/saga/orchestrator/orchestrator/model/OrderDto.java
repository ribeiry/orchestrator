package com.saga.orchestrator.orchestrator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

public class OrderDto {


    private UUID codPedido;
    private  UUID codCliente;

    private List<ProdutoDTO> produtos;
    private float totalPreco;
    private  String statusPedido;

    public UUID getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(UUID codPedido) {
        this.codPedido = codPedido;
    }

    public UUID getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(UUID codCliente) {
        this.codCliente = codCliente;
    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public float getTotalPreco() {
        return totalPreco;
    }

    public void setTotalPreco(float totalPreco) {
        this.totalPreco = totalPreco;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }


}

package com.saga.orchestrator.orchestrator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProdutoDTO {

    private  String descProduto;
    private float preco;
    private int quantidade;
    public String getDescProduto() {
        return descProduto;
    }

    public void setDescProduto(String descProduto) {
        this.descProduto = descProduto;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }


}

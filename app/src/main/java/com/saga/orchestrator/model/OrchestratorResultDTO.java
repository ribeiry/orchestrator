package com.saga.orchestrator.model;

public class OrchestratorResultDTO {

    private String codPedido;
    private Integer httpstatuscod;
    private String httpmessage;

    private String httpcause;

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public Integer getHttpstatuscod() {
        return httpstatuscod;
    }

    public void setHttpstatuscod(Integer httpstatuscod) {
        this.httpstatuscod = httpstatuscod;
    }

    public String getHttpmessage() {
        return httpmessage;
    }

    public void setHttpmessage(String httpmessage) {
        this.httpmessage = httpmessage;
    }

    public String getHttpcause() {
        return httpcause;
    }

    public void setHttpcause(String httpcause) {
        this.httpcause = httpcause;
    }
}

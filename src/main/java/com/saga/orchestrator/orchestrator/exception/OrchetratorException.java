package com.saga.orchestrator.orchestrator.exception;

public class OrchetratorException extends  Exception{

    private int httpstatuscode;

    private String httpstatusmessage;

    public OrchetratorException(int httpstatuscode, String httpstatusmessage){
        super(httpstatusmessage);
        this.httpstatuscode = httpstatuscode;
    }

    public int getHttpstatuscode() {
        return httpstatuscode;
    }

    public void setHttpstatuscode(int httpstatuscode) {
        this.httpstatuscode = httpstatuscode;
    }

    public String getHttpstatusmessage() {
        return httpstatusmessage;
    }

    public void setHttpstatusmessage(String httpstatusmessage) {
        this.httpstatusmessage = httpstatusmessage;
    }
}

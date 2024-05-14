package com.saga.orchestrator.orchestrator.exception;

public class OrchetratorException extends  Exception{

    private int httpstatuscode;

    private String httpstatusmessage;

    Throwable e;

    public OrchetratorException(int httpstatuscode, String httpstatusmessage, Throwable e){
        super(httpstatusmessage);
        this.httpstatuscode = httpstatuscode;
        this.e = e;
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

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }
}

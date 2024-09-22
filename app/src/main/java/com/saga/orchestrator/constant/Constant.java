package com.saga.orchestrator.constant;

public class Constant {

    public static final String POST_URL = "/issue";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server ERROR";
    public static final String SERVICE_PAYMENTS = "PAYMENTS";
    public static final String FAIL_MSG = "FAIL";
    public static final String SUCESS_MSG = "SUCESS";
    public static final String SERVICE_STOCK = "STOCK";
    public static final String SERVICE_ORDER = "ORDER";

    //COMUNICATOR
    public static final String SERIVCECOMUNICATOR        = "service";
    public static final String MESSAGECOMUNICATOR        = "message";
    public static final String DATETIMECOMUNICATOR       = "DateTime";
    public static final String SUCESSCOMUNICATOR         = "SUCCESS";
    public static final String CODIGOPEDIDOCOMUNICATOR   = "CodigoPedido";
    public static final String HTTPCODECOMUNICATOR       = "HttpStatusCode";
    public static final String HTTPMESSAGECOMUNICATOR    = "HttpStatusMessage";
    public static final String CAUSEMESSAGECOMUNICATOR   = "Cause";
    public static final String ERRORMESSAGECOMUNICATOR   = "ERROR RETORNO VIA CIRCUITBREAKER";
    public static final String SERVICEMESSAGECOMUNICATOR = "CIRCUIT BREAKER";
    public static final String HTTPCAUSECOMUNICATOR      = "Error unconnected REDIS";
    public static final String CODPEDIDOERRORCOMUNICATOR = "XXX";


    //LOGS
    public static final String CONNECTEDSERVER     = "Conectando com o server";
    public static final String SUCESSREDIS         = "Houve um sucesso a conectar com REDIS database";
    public static final String ERRORREDIS          = "Erro ao montar o Objeto do redis";
    public static final String NEXTCOMUNICATOR     = "Iniciando a classe de proximo serivco";
    public static final String RETURNINGERROR      = "Returning communicatorReturningError";
    public static final String ERRORCONNECTREDIS   = "Error ao Connect with Redis";

}

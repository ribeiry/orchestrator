package com.saga.orchestrator.mediator;

import com.saga.orchestrator.model.CommunicatorDTO;

import java.time.LocalDateTime;


public interface ICommunicator {

    boolean getNext(String message, String service, LocalDateTime data);

    CommunicatorDTO getMicroserviceResult(String service);

    void saveOrechestratorResult(String codigoPedido, int httpstatuscode, String httpstatusmessage, Throwable cause);

    boolean saveMicroserviceResult(String message, String service, LocalDateTime data);
}

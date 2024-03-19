package com.saga.orchestrator.orchestrator.mediator;

import com.saga.orchestrator.orchestrator.model.CommunicatorDTO;

import java.time.LocalDateTime;


public interface IMediator {

    boolean saveMicroserviceResult(String message, String service, LocalDateTime data);

    CommunicatorDTO getMicroserviceResult(String service);

    void saveOrechestratorResult(String codigoPedido, int httpstatuscode, String httpstatusmessage, Throwable cause);
}

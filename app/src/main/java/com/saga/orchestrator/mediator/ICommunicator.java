package com.saga.orchestrator.mediator;

import com.saga.orchestrator.model.CommunicatorDTO;

import java.time.LocalDateTime;
import java.util.UUID;


public interface ICommunicator {

    boolean getNext(String message, String service, LocalDateTime data);

    CommunicatorDTO getMicroserviceResult(String service);

    void saveOrechestratorResult(UUID idprocess, int httpstatuscode, String httpstatusmessage, Throwable cause);

    boolean saveMicroserviceResult(String message, String service, LocalDateTime data);
}

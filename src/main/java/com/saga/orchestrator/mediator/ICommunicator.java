package com.saga.orchestrator.mediator;

import java.time.LocalDateTime;


public interface ICommunicator {

    boolean getNext(String message, String service, LocalDateTime data);
}

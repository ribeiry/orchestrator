package com.saga.orchestrator.orchestrator.mediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class Communicator implements  ICommunicator{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public boolean getNext(String message, String service, LocalDateTime data)
    {
        logger.info("Iniciando a classe de proximo serivco");
        if("SUCCESS".equalsIgnoreCase(message)) {
            logger.info("Servico: " + service + " ---- Mensagem: " + message + " Data e Hora: " + String.valueOf(data));
            return true;
        }
        else {
            logger.info("Servico: " + service + " ---- Mensagem: " + message + " Data e Hora: " + String.valueOf(data));
            return false;
        }
    }
}

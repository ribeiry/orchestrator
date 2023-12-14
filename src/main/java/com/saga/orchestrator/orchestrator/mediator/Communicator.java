package com.saga.orchestrator.orchestrator.mediator;

import com.saga.orchestrator.orchestrator.database.RedisConfig;
import com.saga.orchestrator.orchestrator.model.CommunicatorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Communicator implements  ICommunicator{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    RedisConfig redisConnect = new RedisConfig();

    @Override
    public boolean getNext(String message, String service, LocalDateTime data)
    {

        String hashKey = String.format("Communicator%s", service);
        CommunicatorDTO communicator = new CommunicatorDTO();

        Jedis redis = redisConnect.ConfigurationRedis("localhost", 6379);
        logger.info("Iniciando a classe de proximo serivco");
        if("SUCCESS".equalsIgnoreCase(message)) {
            Map<String, String> hash = new HashMap<>();
            logger.info("Servico: " + service + " ---- Mensagem: " + message + " Data e Hora: " + String.valueOf(data));
            hash.put("service", service);
            hash.put("message", message);
            hash.put("DateTime", String.valueOf(data));

            redis.hset(hashKey, hash);
            logger.info(redis.hgetAll(hashKey).toString());
            return true;
        }
        else {
            Map<String, String> hash = new HashMap<>();
            logger.info("Servico: " + service + " ---- Mensagem: " + message + " Data e Hora: " + String.valueOf(data));
            logger.info("Servico: " + service + " ---- Mensagem: " + message + " Data e Hora: " + String.valueOf(data));
            hash.put("service", service);
            hash.put("message", message);
            hash.put("DateTime", String.valueOf(data));

            redis.hset(hashKey, hash);
            logger.info(redis.hgetAll(hashKey).toString());
            return false;
        }
    }

    public CommunicatorDTO getStatus(String service){
        CommunicatorDTO communicatorDTO = new CommunicatorDTO();
        Jedis redis = redisConnect.ConfigurationRedis("localhost", 6379);
        String hashKey = String.format("Communicator%s", service);

        Map<String, String> result = redis.hgetAll(hashKey);

        communicatorDTO.setService(result.get("service"));
        communicatorDTO.setMessage(result.get("message"));
        communicatorDTO.setDateTime(LocalDateTime.parse(result.get("DateTime")));


        return  communicatorDTO;

    }
}

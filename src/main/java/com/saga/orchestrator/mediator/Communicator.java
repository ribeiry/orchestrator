package com.saga.orchestrator.mediator;

import com.saga.orchestrator.database.RedisConfig;
import com.saga.orchestrator.model.CommunicatorDTO;
import com.saga.orchestrator.model.OrchestratorResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Communicator implements  ICommunicator{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    RedisConfig redisConnect = new RedisConfig();

    @Override
    public boolean getNext(String message, String service, LocalDateTime data)
    {

        String hashKey = String.format("Communicator%s", service);

        Jedis redis = redisConnect.configurationRedis("localhost", 6379);
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
            try {
                redis.hset(hashKey, hash);
                logger.info(redis.hgetAll(hashKey).toString());
            }
            catch (JedisException e){
                logger.error(e.getMessage());
            }
            catch (Exception e){
                logger.error(e.getMessage());

            }
            return false;
        }
    }

    public CommunicatorDTO getStatus(String service){
        CommunicatorDTO communicatorDTO = new CommunicatorDTO();
        Jedis redis = redisConnect.configurationRedis("localhost", 6379);
//        String hashKey = String.format("Communicator%s", service);
        String hashKey = String.format("Mediator%s", service);
        Map<String, String> result = redis.hgetAll(hashKey);

        communicatorDTO.setService(result.get("service"));
        communicatorDTO.setMessage(result.get("message"));
        communicatorDTO.setDateTime(LocalDateTime.parse(result.get("DateTime")));


        return  communicatorDTO;

    }


    public OrchestratorResultDTO getOrechestratorResult(String codigoPedido){
        OrchestratorResultDTO orchestratorResultDTO = new OrchestratorResultDTO();
        Jedis redis = redisConnect.configurationRedis("localhost", 6379);
        String hashKey = String.format("Mediator%s", codigoPedido);

        Map<String, String> result = redis.hgetAll(hashKey);

        orchestratorResultDTO.setCodPedido(result.get("CodigoPedido"));
        orchestratorResultDTO.setHttpstatuscod(result.get("HttpStatusCode"));
        orchestratorResultDTO.setHttpmessage(result.get("HttpStatusMessage"));
        if(result.get("Cause") != null){
            orchestratorResultDTO.setHttpcause(result.get("Cause"));
        }

        return  orchestratorResultDTO;

    }

    @Override
    public CommunicatorDTO getMicroserviceResult(String service){
        CommunicatorDTO communicatorDTO = new CommunicatorDTO();
        Jedis redis = redisConnect.configurationRedis("localhost", 6379);
        String hashKey = String.format("Mediator%s", service);

        Map<String, String> result = redis.hgetAll(hashKey);

        communicatorDTO.setService(result.get("service"));
        communicatorDTO.setMessage(result.get("message"));
        communicatorDTO.setDateTime(LocalDateTime.parse(result.get("DateTime")));


        return  communicatorDTO;

    }

    @Override
    public void saveOrechestratorResult(String codigoPedido, int httpstatuscode, String httpstatusmessage, Throwable cause) {

        String hashKey = String.format("Mediator%s", codigoPedido);

        Jedis redis = redisConnect.configurationRedis("localhost", 6379);
        logger.info("Iniciando a classe de proximo serivco");

        Map<String, String> hash = new HashMap<>();
        logger.info("CodigoPedido: " + codigoPedido + " ---- HttpStatusCode: " + httpstatuscode + " ---- HttpStatusMessage: " + httpstatusmessage + " Cause: " + cause);
        hash.put("CodigoPedido", codigoPedido);
        hash.put("HttpStatusCode", String.valueOf(httpstatuscode));
        hash.put("HttpStatusMessage", httpstatusmessage);
        if (cause != null ) hash.put("Cause", cause.getMessage());
        try {
            redis.hset(hashKey, hash);
            logger.info(redis.hgetAll(hashKey).toString());
        }
        catch (JedisException e){
            logger.error(e.getMessage());
        }
        catch (Exception e){
            logger.error(e.getMessage());

        };
    }

    @Override
    public boolean saveMicroserviceResult(String message, String service, LocalDateTime data)
    {

        String hashKey = String.format("Mediator%s", service);

        Jedis redis = redisConnect.configurationRedis("localhost", 6379);
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
            try {
                redis.hset(hashKey, hash);
                logger.info(redis.hgetAll(hashKey).toString());
            }
            catch (JedisException e){
                logger.error(e.getMessage());
            }
            catch (Exception e){
                logger.error(e.getMessage());

            }
            return false;
        }
    }
}

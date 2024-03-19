package com.saga.orchestrator.orchestrator.mediator;

import com.saga.orchestrator.orchestrator.database.RedisConfig;
import com.saga.orchestrator.orchestrator.model.CommunicatorDTO;
import com.saga.orchestrator.orchestrator.model.OrchestratorResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Mediator implements IMediator {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    RedisConfig redisConnect = new RedisConfig();

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


    public OrchestratorResultDTO getOrechestratorResult(String codigoPedido){
        OrchestratorResultDTO orchestratorResultDTO = new OrchestratorResultDTO();
        Jedis redis = redisConnect.configurationRedis("localhost", 6379);
        String hashKey = String.format("Mediator%s", codigoPedido);

        Map<String, String> result = redis.hgetAll(hashKey);

        orchestratorResultDTO.setCodPedido(result.get(codigoPedido));
        //orchestratorResultDTO.setMessage(result.get("message"));
        //orchestratorResultDTO.setDateTime(LocalDateTime.parse(result.get("DateTime")));


        return  orchestratorResultDTO;

    }



    @Override
    public void saveOrechestratorResult(String codigoPedido, int httpstatuscode, String httpstatusmessage, Throwable cause) {

        String hashKey = String.format("Mediator%s", httpstatusmessage);

        Jedis redis = redisConnect.configurationRedis("localhost", 6379);
        logger.info("Iniciando a classe de proximo serivco");

        Map<String, String> hash = new HashMap<>();
        logger.info("CodigoPedido: " + codigoPedido + " ---- HttpStatusCode: " + httpstatuscode + " ---- HttpStatusMessage: " + httpstatusmessage + " Cause: " + cause);
        hash.put("CodigoPedido", codigoPedido);
        hash.put("HttpStatusCode", String.valueOf(httpstatuscode));
        hash.put("HttpStatusMessage", httpstatusmessage);
        hash.put("Cause", cause.getMessage());
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
}

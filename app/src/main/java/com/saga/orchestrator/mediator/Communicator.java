package com.saga.orchestrator.mediator;

import com.saga.orchestrator.configuration.GetParameter;
import com.saga.orchestrator.database.RedisConfig;
import com.saga.orchestrator.model.CommunicatorDTO;
import com.saga.orchestrator.model.OrchestratorResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Component
public class Communicator implements  ICommunicator{

    static GetParameter parameter = new GetParameter();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    RedisConfig redisConnect = new RedisConfig();
    private static String SERVER_REDIS =  "192.168.105.4";
    private static Integer PORT_REDIS   =  6379 ;


    @Override
    public boolean getNext(String message, String service, LocalDateTime data) {

        String hashKey = String.format("Communicator%s", service);
       // SERVER_REDIS =  parameter.getParamValue(parameter.connect(),"urlRedis");
        //PORT_REDIS  =  Integer.valueOf(parameter.getParamValue(parameter.connect(),"portRedis"));

        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
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

   //     SERVER_REDIS =  parameter.getParamValue(parameter.connect(),"urlRedis");
   //     PORT_REDIS  =  Integer.valueOf(parameter.getParamValue(parameter.connect(),"portRedis"));
        CommunicatorDTO communicatorDTO = new CommunicatorDTO();
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
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
   //     SERVER_REDIS =  parameter.getParamValue(parameter.connect(),"urlRedis");
   //     PORT_REDIS  =  Integer.valueOf(parameter.getParamValue(parameter.connect(),"portRedis"));
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
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
  //      SERVER_REDIS =  parameter.getParamValue(parameter.connect(),"urlRedis");
  //      PORT_REDIS  =  Integer.valueOf(parameter.getParamValue(parameter.connect(),"portRedis"));
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
        String hashKey = String.format("Mediator%s", service);

        Map<String, String> result = redis.hgetAll(hashKey);

        communicatorDTO.setService(result.get("service"));
        communicatorDTO.setMessage(result.get("message"));
        communicatorDTO.setDateTime(LocalDateTime.parse(result.get("DateTime")));


        return  communicatorDTO;

    }

    @Override
    public void saveOrechestratorResult(UUID idprocess, int httpstatuscode, String httpstatusmessage, Throwable cause) {

        String hashKey = String.format("Mediator%s", idprocess);
    //    SERVER_REDIS =  parameter.getParamValue(parameter.connect(),"urlRedis");
    //    PORT_REDIS  =  Integer.valueOf(parameter.getParamValue(parameter.connect(),"portRedis"));
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
        logger.info("Iniciando a classe de proximo serivco");

        Map<String, String> hash = new HashMap<>();
        logger.info("CodigoPedido: " + idprocess + " ---- HttpStatusCode: " + httpstatuscode + " ---- HttpStatusMessage: " + httpstatusmessage + " Cause: " + cause);
        hash.put("CodigoPedido", String.valueOf(idprocess));
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

        }
    }

    @Override
    public boolean saveMicroserviceResult(String message, String service, LocalDateTime data) {

        String hashKey = String.format("Mediator%s", service);
    //    SERVER_REDIS =  parameter.getParamValue(parameter.connect(),"urlRedis");
    //    PORT_REDIS  =  Integer.valueOf(parameter.getParamValue(parameter.connect(),"portRedis"));
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
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

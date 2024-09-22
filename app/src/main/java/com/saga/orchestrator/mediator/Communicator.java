package com.saga.orchestrator.mediator;

import com.saga.orchestrator.configuration.CircuitBreakerConfiguration;
import com.saga.orchestrator.configuration.GetParameter;
import com.saga.orchestrator.configuration.RedisConfig;
import com.saga.orchestrator.model.CommunicatorDTO;
import com.saga.orchestrator.model.OrchestratorResultDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.time.LocalDateTime;
import java.util.*;

import static com.saga.orchestrator.constant.Constant.*;

@Component
public class Communicator implements  ICommunicator{

    static GetParameter parameter = new GetParameter();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    RedisConfig redisConnect = new RedisConfig();
    private static String SERVER_REDIS =  "192.168.105.4";
    private static Integer PORT_REDIS   =  6379 ;


    @Override
    @CircuitBreaker(name = "orchestratorCircuit", fallbackMethod = "communicatorReturningError")
    public boolean getNext(String message, String service, LocalDateTime data) {

        String hashKey = String.format("Communicator%s", service);
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        try(Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS)){
            Map<String, String> hash = new HashMap<>();
            logger.info(NEXTCOMUNICATOR);
            hash.put(SERIVCECOMUNICATOR, service);
            hash.put(MESSAGECOMUNICATOR, message);
            hash.put(DATETIMECOMUNICATOR, String.valueOf(data));

            redis.hset(hashKey, hash);
            logger.info(redis.hgetAll(hashKey).toString());
            return SUCESSCOMUNICATOR.equalsIgnoreCase(message);

        }
        catch (JedisException e){
            logger.error(e.getMessage());
        }
        catch (Exception e){
            logger.error(e.getMessage());

        }
        return false;
    }

    @CircuitBreaker(name = "orchestratorCircuit", fallbackMethod = "communicatorReturningStatusError")
    public CommunicatorDTO getStatus(String service){

        CommunicatorDTO communicatorDTO = new CommunicatorDTO();
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
        String hashKey = String.format("Mediator%s", service);
        Map<String, String> result = redis.hgetAll(hashKey);

        communicatorDTO.setService(result.get(SERIVCECOMUNICATOR));
        communicatorDTO.setMessage(result.get(MESSAGECOMUNICATOR));
        communicatorDTO.setDateTime(LocalDateTime.parse(result.get(DATETIMECOMUNICATOR)));

        return  communicatorDTO;

    }

    @CircuitBreaker(name= "orchestratorCircuit",fallbackMethod = "communicatorReturningOrchestratorError")
    public OrchestratorResultDTO getOrechestratorResult(String codigoPedido){
        OrchestratorResultDTO orchestratorResultDTO = new OrchestratorResultDTO();
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
        String hashKey = String.format("Mediator%s", codigoPedido);

        Map<String, String> result = redis.hgetAll(hashKey);

        orchestratorResultDTO.setCodPedido(result.get(CODIGOPEDIDOCOMUNICATOR));
        orchestratorResultDTO.setHttpstatuscod(result.get(HTTPCODECOMUNICATOR));
        orchestratorResultDTO.setHttpmessage(result.get(HTTPMESSAGECOMUNICATOR));
        if(result.get(CAUSEMESSAGECOMUNICATOR) != null){
            orchestratorResultDTO.setHttpcause(result.get(CAUSEMESSAGECOMUNICATOR));
        }

        return  orchestratorResultDTO;

    }

    @Override
    @CircuitBreaker(name = "orchestratorCircuit", fallbackMethod = "communicatorReturningStatusError")
    public CommunicatorDTO getMicroserviceResult(String service){
        CommunicatorDTO communicatorDTO = new CommunicatorDTO();
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS);
        String hashKey = String.format("Mediator%s", service);

        Map<String, String> result = redis.hgetAll(hashKey);

        communicatorDTO.setService(result.get(SERIVCECOMUNICATOR));
        communicatorDTO.setMessage(result.get(MESSAGECOMUNICATOR));
        communicatorDTO.setDateTime(LocalDateTime.parse(result.get(DATETIMECOMUNICATOR)));


        return  communicatorDTO;

    }

    @Override
    @CircuitBreaker(name = "orchestratorCircuit", fallbackMethod = "communicatorReturningSaveError")
    public void saveOrechestratorResult(UUID idprocess, int httpstatuscode, String httpstatusmessage, Throwable cause) {

        String hashKey = String.format("Mediator%s", idprocess);
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        try(Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS)){
            Map<String, String> hash = new HashMap<>();
            logger.info("CodigoPedido: {0} ---- HttpStatusCode: {1} ---- HttpStatusMessage: {2} Cause: {3}" ,
                                    idprocess,httpstatuscode,httpstatusmessage,cause);
            hash.put(CODIGOPEDIDOCOMUNICATOR, String.valueOf(idprocess));
            hash.put(HTTPCODECOMUNICATOR, String.valueOf(httpstatuscode));
            hash.put(HTTPMESSAGECOMUNICATOR, httpstatusmessage);
            if (cause != null )
                hash.put(CAUSEMESSAGECOMUNICATOR, cause.getMessage());

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
    @CircuitBreaker(name = "orchestratorCircuit", fallbackMethod = "communicatorReturningSaveError")
    public boolean saveMicroserviceResult(String message, String service, LocalDateTime data) {

        String hashKey = String.format("Communicator%s", service);
        // SERVER_REDIS =  parameter.getParamValue(parameter.connect(),"urlRedis");
        //PORT_REDIS  =  Integer.valueOf(parameter.getParamValue(parameter.connect(),"portRedis"));
        logger.info("Server REDIS {} PORT REDIS {}", SERVER_REDIS, PORT_REDIS);
        try(Jedis redis = redisConnect.configurationRedis(SERVER_REDIS, PORT_REDIS)){
            Map<String, String> hash = new HashMap<>();
            logger.info(NEXTCOMUNICATOR);
            hash.put(SERIVCECOMUNICATOR, service);
            hash.put(MESSAGECOMUNICATOR, message);
            hash.put(DATETIMECOMUNICATOR, String.valueOf(data));

            redis.hset(hashKey, hash);
            logger.info(redis.hgetAll(hashKey).toString());
            return SUCESSCOMUNICATOR.equalsIgnoreCase(message);

        }
        catch (JedisException e){
            logger.error(e.getMessage());
        }
        catch (Exception e){
            logger.error(e.getMessage());

        }
        return false;
    }

    public boolean communicatorReturningError(Throwable throwable){
        logger.error(RETURNINGERROR);
        logger.error(ERRORCONNECTREDIS);

        return false;
    }

    public void communicatorReturningSaveError(Throwable throwable){
        logger.error(RETURNINGERROR);
        logger.error(ERRORCONNECTREDIS);
    }

    public CommunicatorDTO communicatorReturningStatusError(Throwable throwable){
        CommunicatorDTO communicatorDTO = new CommunicatorDTO();

        logger.error(RETURNINGERROR);
        logger.error(ERRORCONNECTREDIS);

        communicatorDTO.setMessage(ERRORMESSAGECOMUNICATOR);
        communicatorDTO.setService(SERVICEMESSAGECOMUNICATOR);
        communicatorDTO.setDateTime(LocalDateTime.now());
        return communicatorDTO;
    }

    public OrchestratorResultDTO communicatorReturningOrchestratorError(Throwable throwable){
        OrchestratorResultDTO orchestratorResultDTO = new OrchestratorResultDTO();

        orchestratorResultDTO.setCodPedido(CODPEDIDOERRORCOMUNICATOR);
        orchestratorResultDTO.setHttpcause(HTTPCAUSECOMUNICATOR);
        orchestratorResultDTO.setHttpstatuscod("500");

        return  orchestratorResultDTO;
    }


}

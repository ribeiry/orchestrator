package com.saga.orchestrator.orchestrator.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;


@EnableCaching
@Configuration
public class RedisConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Jedis ConfigurationRedis(String server, int port){
        JedisPool pool = new JedisPool(server, port);
        try(Jedis jedis = pool.getResource()){
            logger.info("Houve um sucesso a conectar com REDIS database");
            return jedis;
        }
        catch (JedisException e ){
            logger.info("Erro ao montar o Objeto do redis : " + e.getMessage());
            return  null;
        }

    }
}

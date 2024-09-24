package com.saga.orchestrator.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import static com.saga.orchestrator.constant.Constant.ERRORREDIS;
import static com.saga.orchestrator.constant.Constant.SUCESSREDIS;


@EnableCaching
@Configuration
public class RedisConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Jedis configurationRedis(String server, int port){
        JedisPool pool = new JedisPool(server, port);
        try(Jedis jedis = pool.getResource()){
            logger.info(SUCESSREDIS);
            return jedis;
        }
        catch (JedisException e ){
            logger.info(ERRORREDIS, e.getMessage());
            return  null;
        }

    }
}


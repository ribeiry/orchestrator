package com.saga.orchestrator.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreaker customCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry){
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)//limite da taxa de falha em porcentagem
                .permittedNumberOfCallsInHalfOpenState(3) //NúmeroPermitidoDeChamadasEmEstadoMeioAberto
                .slidingWindowSize(10) //Configura o tamanho da janela deslizante que é usada para registrar o resultado das chamadas quando o CircuitBreaker é fechado.
                .waitDurationInOpenState(Duration.ofMillis(6000)) // O tempo que o disjuntor deve esperar antes de passar de aberto para semiaberto.
                .permittedNumberOfCallsInHalfOpenState(1) //NúmeroPermitidoDeChamadasEmEstadoMeioAberto
                .minimumNumberOfCalls(5) // Configures the minimum number of calls which are required (per sliding window period) before the CircuitBreaker can calculate the error rate or slow call rate.
                //.recordExceptions(SQLException.class)
                .build();

        return circuitBreakerRegistry.circuitBreaker("orchestratorCircuit",circuitBreakerConfig);
    }

    @Bean
    public CircuitBreakerRegistry CircuitBreakerRegistry(){
        return CircuitBreakerRegistry.ofDefaults();
    }
}

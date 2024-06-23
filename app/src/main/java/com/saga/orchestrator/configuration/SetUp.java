package com.saga.orchestrator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SetUp {

    @Value("${url.server.order-services}")
    public String ORDER_SERVICE_URL;


    public SetUp() {
    }

}

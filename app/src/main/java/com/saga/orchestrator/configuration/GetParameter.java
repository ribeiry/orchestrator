package com.saga.orchestrator.configuration;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.SsmException;

import java.net.URI;
import java.net.URISyntaxException;

public class GetParameter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SsmClient connect()  {
        logger.info("Setando a region US_EAST_1");
        try {
            Region region = Region.SA_EAST_1;
            URI urlprameter = new URI("https://localhost.localstack.cloud:4566");
            SsmClient ssmClient = SsmClient.builder()
                    .region(region)
                    .endpointOverride(urlprameter)
                    .build();
            return ssmClient;
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getParamValue(@NotNull SsmClient ssmClient, String paramName){
        String valorParameter = "";
        try{
            GetParameterRequest parameterRequest = GetParameterRequest.builder()
                    .name(paramName)
                    .build();
            GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
            logger.info("O valor do parametro é: {}", parameterResponse.parameter().value());
            valorParameter= parameterResponse.parameter().value();
            ssmClient.close();
        }
        catch (SsmException ex){
            logger.error(ex.getMessage().toString());
        }
        finally {
            return  valorParameter;
        }

    }
}

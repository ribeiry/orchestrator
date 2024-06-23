package com.saga.orchestrator;

import com.saga.orchestrator.configuration.SetUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
public class OrchestratorApplication {

	public static void main(String[] args)  {
		SpringApplication.run(OrchestratorApplication.class, args);


	}

}

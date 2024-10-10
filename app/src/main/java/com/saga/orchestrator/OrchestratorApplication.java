package com.saga.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class OrchestratorApplication {

	//TODO: Testar circuit Breaker
	//TODO: Fazer testes de CARGA

	public static void main(String[] args)  {
		SpringApplication.run(OrchestratorApplication.class, args);


	}

}

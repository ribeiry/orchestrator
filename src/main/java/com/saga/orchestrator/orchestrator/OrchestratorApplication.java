package com.saga.orchestrator.orchestrator;

import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.StockServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.ConnectException;

@SpringBootApplication
public class OrchestratorApplication {

	public static void main(String[] args)  {

		SpringApplication.run(OrchestratorApplication.class, args);

		OrderServices orderServices = new OrderServices();

		orderServices.getAllOrders();

	}

}

package com.saga.orchestrator.orchestrator;

import com.saga.orchestrator.orchestrator.model.OrderDto;
import com.saga.orchestrator.orchestrator.model.ProdutoDTO;
import com.saga.orchestrator.orchestrator.service.OrderServices;
import com.saga.orchestrator.orchestrator.service.StockServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.UUID;

@SpringBootApplication
public class OrchestratorApplication {

	public static void main(String[] args)  {

		String idProduto = "066de609-b04a-4b30-b46c-32537c7f1f6e";
		int qtde = 25;
		OrderDto orderDto = new OrderDto();
		ProdutoDTO produto = new ProdutoDTO();
		ArrayList<ProdutoDTO> produtos = new ArrayList<>();

		SpringApplication.run(OrchestratorApplication.class, args);

		OrderServices orderServices = new OrderServices();

		orderDto.setCodPedido(UUID.fromString("f54efab5-1766-40cd-a069-eb05478d9f91"));
		orderDto.setStatusPedido("Faturado");

		produto.setDescProduto("TV Samsung 65 polegadas");
		produto.setPreco(3500.0F);
		produto.setQuantidade(2);

		produtos.add(produto);

		produto.setDescProduto("XBOX Series ONE");
		produto.setPreco(5000.0F);
		produto.setQuantidade(3);

		produtos.add(produto);
		orderDto.setProdutos(produtos);

		orderServices.putUpdateOrder("c50e65b6-d617-4b1b-b7cc-8eb32b33fdc8");

	}

}

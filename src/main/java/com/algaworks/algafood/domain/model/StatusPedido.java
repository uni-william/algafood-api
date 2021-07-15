package com.algaworks.algafood.domain.model;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public enum StatusPedido {

	CRIADO("Criado"), CONFIRMADO("Confirmado", CRIADO), ENTREGUE("Entregue", CONFIRMADO),
	CANCELADO("Cancelado", CRIADO);

	@Getter
	private String descricao;
	private List<StatusPedido> statusAnteriores;

	private StatusPedido(String descricao, StatusPedido... statusAnteriores) {
		this.descricao = descricao;
		this.statusAnteriores = Arrays.asList(statusAnteriores);
	}

	public boolean naoPodeAlterarPara(StatusPedido novoStatus) {
		return !novoStatus.statusAnteriores.contains(this);
	}
}

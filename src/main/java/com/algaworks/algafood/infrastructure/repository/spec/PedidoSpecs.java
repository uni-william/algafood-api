package com.algaworks.algafood.infrastructure.repository.spec;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;

public class PedidoSpecs {

	public static Specification<Pedido> usandoFiltro(PedidoFilter filter) {
		return (root, query, builder) -> {
			var predicates = new ArrayList<Predicate>();
			if (Pedido.class.equals(query.getResultType())) {
				root.fetch("restaurante").fetch("cozinha");
				root.fetch("cliente");
				root.fetch("enderecoEntrega").fetch("cidade").fetch("estado");
			}

			if (filter.getClienteId() != null)
				predicates.add(builder.equal(root.get("cliente"), filter.getClienteId()));

			if (filter.getRestauranteId() != null)
				predicates.add(builder.equal(root.get("restaurante"), filter.getRestauranteId()));

			if (filter.getDataCriacaoInicio() != null)
				predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filter.getDataCriacaoInicio()));

			if (filter.getDataCriacaoFim() != null)
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filter.getDataCriacaoFim()));

			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}

}

package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}

	@GetMapping("/{id}")
	public Restaurante findById(@PathVariable Long id) {
		return cadastroRestaurante.buscarPorId(id);

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(@RequestBody Restaurante restaurante) {
		try {
			return cadastroRestaurante.salvar(restaurante);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping("/{id}")
	public Restaurante atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
		Restaurante restauranteSalva = cadastroRestaurante.buscarPorId(id);
		BeanUtils.copyProperties(restaurante, restauranteSalva, "id", "formasPagamento", "endereco", "dataCadastro",
				"produtos");
		try {
			return cadastroRestaurante.salvar(restauranteSalva);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		cadastroRestaurante.excluir(id);
	}

	@PatchMapping("/{id}")
	public Restaurante atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
		Restaurante restauranteSalvo = cadastroRestaurante.buscarPorId(id);
		merge(campos, restauranteSalvo);
		return atualizar(id, restauranteSalvo);
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}
}

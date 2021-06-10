package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		try {
			Cozinha cozinha = cadastroCozinha.buscarPorId(cozinhaId);
			restaurante.setCozinha(cozinha);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe cadsatro de cozinha com o código %d", cozinhaId));
		}
		
		return restauranteRepository.save(restaurante);
	}

	public Restaurante buscarPorId(Long id) {
		Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);
		if (!restauranteOptional.isPresent())
			throw new EmptyResultDataAccessException(1);
		return restauranteOptional.get();

	}

	public void excluir(Long id) {
		try {
			Restaurante restaurante = buscarPorId(id);
			restauranteRepository.delete(restaurante);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de restaurante com o código %d", id));
		}

	}

}

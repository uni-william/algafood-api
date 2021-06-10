package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

	@Autowired
	private EstadoRepository estadoRepository;

	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}

	public Estado buscarPorId(Long id) {
		Optional<Estado> estadoOptional = estadoRepository.findById(id);
		if (!estadoOptional.isPresent())
			throw new EmptyResultDataAccessException(1);
		return estadoOptional.get();

	}

	public void excluir(Long id) {
		try {
			Estado estado = buscarPorId(id);
			estadoRepository.delete(estado);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de estado com o código %d", id));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Estado de códugo %d não pode ser removido pois está em uso", id));
		}

	}

}

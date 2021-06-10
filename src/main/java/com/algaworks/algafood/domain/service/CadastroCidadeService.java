package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;

	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		try {
			Estado estado = cadastroEstado.buscarPorId(estadoId);
			cidade.setEstado(estado);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe cadsatro de estado com o código %d", estadoId));
		}
		
		return cidadeRepository.save(cidade);
	}

	public Cidade buscarPorId(Long id) {
		Optional<Cidade> cidadeOptional = cidadeRepository.findById(id);
		if (!cidadeOptional.isPresent())
			throw new EmptyResultDataAccessException(1);
		return cidadeOptional.get();

	}

	public void excluir(Long id) {
		try {
			Cidade cidade = buscarPorId(id);
			cidadeRepository.delete(cidade);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de cidade com o código %d", id));
		}

	}

}

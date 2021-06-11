package com.algaworks.algafood.domain.service;

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
		Estado estado = cadastroEstado.buscarPorId(estadoId);
		cidade.setEstado(estado);
		return cidadeRepository.save(cidade);
	}

	public Cidade buscarPorId(Long id) {
		Cidade cidade = cidadeRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de cidade com o código %d", id)));
		return cidade;

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

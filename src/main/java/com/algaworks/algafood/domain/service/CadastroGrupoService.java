package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class CadastroGrupoService {

	private static final String MSG_GRUPO_EM_USO = "Grupo de código %d não pode ser removida pois está em uso";
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	
	@Autowired
	private CadastroPermissaoService cadastroPermissao;	

	@Transactional
	public Grupo salvar(Grupo grupo) {
		return grupoRepository.save(grupo);
	}

	public Grupo buscarOuFalhar(Long id) {
		Grupo grupo = grupoRepository.findById(id)
		.orElseThrow(() -> new GrupoNaoEncontradoException(id));
		return grupo;
	}

	@Transactional
	public void excluir(Long id) {
		try {
			grupoRepository.deleteById(id);	
			grupoRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new GrupoNaoEncontradoException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, id));
		}

	}
	

	@Transactional
	public void desassociarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarOuFalhar(grupoId);
		Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);
		
		grupo.removerPermissao(permissao);
	}
	
	@Transactional
	public void associarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarOuFalhar(grupoId);
		Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);
		
		grupo.adicionarPermissao(permissao);
	}	

}

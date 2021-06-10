package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	@GetMapping
	public ResponseEntity<List<Cidade>> listar() {
		return ResponseEntity.ok(cidadeRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cidade> findById(@PathVariable Long id) {
		try {
			Cidade cidade = cadastroCidade.buscarPorId(id);
			return ResponseEntity.ok(cidade);
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
		try {
			Cidade cidadeSalvo = cadastroCidade.salvar(cidade);
			return ResponseEntity.status(HttpStatus.CREATED).body(cidadeSalvo);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {
		try {
			Cidade cidadeSalva = cadastroCidade.buscarPorId(id);
			BeanUtils.copyProperties(cidade, cidadeSalva, "id");
			cadastroCidade.salvar(cidadeSalva);
			return ResponseEntity.ok(cidadeSalva);
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.notFound().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			cadastroCidade.excluir(id);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
}

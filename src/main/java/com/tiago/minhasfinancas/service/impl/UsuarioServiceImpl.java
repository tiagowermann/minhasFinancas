package com.tiago.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiago.minhasfinancas.exceptions.ErroAutenticacao;
import com.tiago.minhasfinancas.exceptions.RegraNegocioException;
import com.tiago.minhasfinancas.model.entity.Usuario;
import com.tiago.minhasfinancas.model.repository.UsuarioRepository;
import com.tiago.minhasfinancas.service.UsuarioService;


@Service
public class UsuarioServiceImpl implements UsuarioService {

	
	private UsuarioRepository repository;
		
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	
	
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		if (!usuario.isPresent()) {											//VERIFICA SE O USUÁRIO EXISTE NO BANCO.												
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		if (!usuario.get().getSenha().equals(senha)) {						//VERIFICA SE A SENHA QUE ESTÁ NO BANCO É MESMA QUE FOI PASSADA COMO PARÂMENTRO.
			throw new ErroAutenticacao("Senha inválida.");
		}
		return usuario.get();
	}

	
	
	
	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		ValidarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	
	
	
	@Override
	public void ValidarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
		
	}

}

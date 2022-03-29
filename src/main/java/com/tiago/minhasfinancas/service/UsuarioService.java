package com.tiago.minhasfinancas.service;

import java.util.Optional;

import com.tiago.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void ValidarEmail(String email);
	
	Optional<Usuario> obterPorId(Long id);
	
}

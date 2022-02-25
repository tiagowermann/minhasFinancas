package com.tiago.minhasfinancas.service;

import com.tiago.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	
	Usuario autenticer(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void ValidarEmail(String email);
	
}

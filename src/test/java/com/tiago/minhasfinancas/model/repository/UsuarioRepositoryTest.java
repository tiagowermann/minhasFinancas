package com.tiago.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tiago.minhasfinancas.model.entity.Usuario;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager; 					//entityManager É A CLASSE RESPONSÁVEL POR FAZER O SALVAMETO, DELETAR E ETC DO BANCO DE DADOS.
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		
		//ação/ecxecução
		boolean result =  repository.existsByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
		
	}
	
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		//cenário
//		repository.deleteAll();
		
		
		//ação/ecxecução
		boolean result =  repository.existsByEmail("usuario@email.com");
		
		
		//verificação
		Assertions.assertThat(result).isFalse();
		
	}
	
	
	
	@Test
	public void devePesistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario =  criarUsuario();	
		//ação/ecxecução
		Usuario UsuarioSalvo = repository.save(usuario);
		
		
		//verificação
		Assertions.assertThat(UsuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		
		//verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		
		Assertions.assertThat( result.isPresent() ).isTrue();
		
	}
	
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
				
		//verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		
		Assertions.assertThat( result.isPresent() ).isFalse();
		
	}
	
	
	public static Usuario criarUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
	
	

}

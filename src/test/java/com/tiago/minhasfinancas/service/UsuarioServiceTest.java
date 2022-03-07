package com.tiago.minhasfinancas.service;


import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Opaquetoken;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tiago.minhasfinancas.exceptions.ErroAutenticacao;
import com.tiago.minhasfinancas.exceptions.RegraNegocioException;
import com.tiago.minhasfinancas.model.entity.Usuario;
import com.tiago.minhasfinancas.model.repository.UsuarioRepository;
import com.tiago.minhasfinancas.service.impl.UsuarioServiceImpl;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	public static String EMAIL = "fulano@email.com"; 
	public static String NOME = "Fulano das Flores";
	public static String SENHA = "1234";
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	
	
	@Test
	public void deveSalvarUmUsuario() {
		Assertions.assertDoesNotThrow(() -> {
			//Cenário
			Mockito.doNothing().when(service).ValidarEmail(Mockito.anyString());
			Usuario usuario = Usuario.builder()
					.id(1l)
					.nome(NOME)
					.email(EMAIL)
					.senha(SENHA)
					.build();
			
			Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
			
			//Ação
			Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
			
			//Verificação
			Assertions.assertNotNull(usuarioSalvo);
			Assertions.assertEquals(usuarioSalvo.getId(), 1l);
			Assertions.assertEquals(usuarioSalvo.getNome(), NOME);
			Assertions.assertEquals(usuarioSalvo.getEmail(), EMAIL);
			Assertions.assertEquals(usuarioSalvo.getSenha(), SENHA);
		});
		
	}
	
	
	
	@Test
	public void nãoDevesalvarUmUsuarioComEmailJaCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class,() -> {
			//Cenário
			Usuario usuario = Usuario.builder()
					.email(EMAIL)
					.build();
			Mockito.doThrow(RegraNegocioException.class).when(service).ValidarEmail(EMAIL);
			
			//Ação
			service.salvarUsuario(usuario);
			
			//Verificação
			Mockito.verify(repository, Mockito.never()).save(usuario);
			
		});
	}
	
	
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		Assertions.assertDoesNotThrow(() -> {
			// cenario
			String email = "email@email.com";
			String senha = "senha";
			
			Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
			Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
			
			//ação
			Usuario result = service.autenticar(email, senha);
			
			
			//verificação
			Assertions.assertNotNull(result);
		
		});
	}
	
	
		
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {								//verificar a aula 48
		Assertions.assertThrows(ErroAutenticacao.class,() -> {
			//cenário
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			//ação
			service.autenticar(EMAIL, EMAIL);
			
		});
	}
	
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {																//verificar a aula 48
		Assertions.assertThrows(ErroAutenticacao.class,() -> {
			// cenario
			Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).build();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
			
			//ação
			service.autenticar(EMAIL, "123");
		});
	}
	
	
	@Test
	public void deveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			
			// cenario
//			UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class);
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
 
			// acao
			service.ValidarEmail(EMAIL);
		});
	}
 
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			//cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
 
			//acao
			service.ValidarEmail(EMAIL);
		});
	}

	
}

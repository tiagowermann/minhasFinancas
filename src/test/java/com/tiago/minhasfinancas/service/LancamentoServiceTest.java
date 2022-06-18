package com.tiago.minhasfinancas.service;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.isNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tiago.minhasfinancas.exceptions.RegraNegocioException;
import com.tiago.minhasfinancas.model.entity.Lancamento;
import com.tiago.minhasfinancas.model.entity.Usuario;
import com.tiago.minhasfinancas.model.repository.LancamentoRepository;
import com.tiago.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.tiago.minhasfinancas.model.unums.StatusLancamento;
import com.tiago.minhasfinancas.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@SpyBean 											//Será da classe que estamos testando, pois precisamos que ele chame os métodos reais.
	LancamentoServiceImpl service;
	
	@MockBean 											//Serve para simular as chamadas que temos no repository.
	LancamentoRepository repository;
	

	@Test
	public void deveSalvarUmLancamento() {
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar); 						//Deste modo não lançará erro quando o service chamar o método de salvar dentro da implementação.
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execução
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//verificação
		Assertions.assertThat( lancamento.getId() ).isEqualTo( lancamentoSalvo.getId() );
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
		
	}
	
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		//execução
		Assertions.catchThrowableOfType( () -> service.salvar(lancamentoASalvar), RegraNegocioException.class );
		
		//verificação
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		
	}
	
	
	@Test
	public void deveAtualizarUmLancamento() {
		//cenário
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(service).validar(lancamentoSalvo); 						//Deste modo não lançará erro quando o service chamar o método de salvar dentro da implementação.
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//execução
		service.salvar(lancamentoSalvo);
		
		//verificação
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
		
	}
	
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		//execução
		Assertions.catchThrowableOfType( () -> service.atualizar(lancamento), NullPointerException.class );
		
		//verificação
		Mockito.verify(repository, Mockito.never()).save(lancamento);
		
	}
	
	
	@Test
	public void deveDeletarUmLancamento() {
		
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//execucao
		service.deletar(lancamento);
		
		//verificação
		Mockito.verify(repository).delete(lancamento);
		
	}
	
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
				
		//execucao
		Assertions.catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class );
				
		//verificação
		Mockito.verify( repository, Mockito.never() ).delete(lancamento);
		
	}
	
	
	@Test
	public void deveFiltrarLancamentos() {
		
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = java.util.Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//execução
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificação
		Assertions
			.assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(lancamento);
		
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento () {
		
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		//execução
		service.atualizarStatus(lancamento, novoStatus);
		
		//verificação
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
		
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		
		//cenario
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//execução
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificação
		Assertions.assertThat(resultado.isPresent()).isTrue();
		
	}
	
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		
		//cenario
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//execução
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificação
		Assertions.assertThat(resultado.isPresent()).isFalse();
		
	}
	
	
	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		
		Lancamento lancamento = new Lancamento();
		
		Throwable erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição Válida.");
		
		lancamento.setDescricao("");
		
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição Válida.");
		
		lancamento.setDescricao("Salario");
		
		//--------------------------------------------------------------------------------------------------------------------
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês Válido.");
		
		lancamento.setMes(0);
		
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês Válido.");
		
		lancamento.setMes(13);
		
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês Válido.");
		
		lancamento.setMes(1);
		
		//--------------------------------------------------------------------------------------------------------------------
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano Válido.");
		
		lancamento.setAno(202);
		
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano Válido.");
		
		lancamento.setAno(2022);
		
		//--------------------------------------------------------------------------------------------------------------------
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário Válido.");
				
		lancamento.setUsuario(new Usuario() );
		
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário Válido.");
		
		lancamento.getUsuario().setId(1l);
		
		//--------------------------------------------------------------------------------------------------------------------
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor Válido.");
				
		lancamento.setValor(BigDecimal.ZERO);
		
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor Válido.");
		
		lancamento.setValor(BigDecimal.valueOf(1));
				
		//--------------------------------------------------------------------------------------------------------------------
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo de Lancamento Válido.");
						
				
		
	}
	
	
}

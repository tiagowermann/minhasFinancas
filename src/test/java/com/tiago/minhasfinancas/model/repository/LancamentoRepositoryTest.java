package com.tiago.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tiago.minhasfinancas.model.entity.Lancamento;
import com.tiago.minhasfinancas.model.unums.StatusLancamento;
import com.tiago.minhasfinancas.model.unums.TipoLancamento;


@ExtendWith(SpringExtension.class)							//Usado no lugar do @RunWith (SpringRunner.class)
@DataJpaTest 												//Teste de Integração.
@AutoConfigureTestDatabase(replace = Replace.NONE) 			//Não sobrescreva as minhas configurações do ambiente de teste.
@ActiveProfiles("test") 									//Ativa o perfil de teste
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager; 						//entityManager É A CLASSE RESPONSÁVEL POR FAZER O SALVAMETO, DELETAR E ETC DO BANCO DE DADOS.
	
	
	@Test
	public void deveSalvarUmLancamento() {
		
		Lancamento lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		
		assertThat(lancamento.getId()).isNotNull();
		
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento LancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(LancamentoInexistente).isNull();
		
	}

		
	@Test
	public void deveAtualizarUmLancamento() {
		
		Lancamento lancamento = criarEPersistirUmLancamento();
		
//		Primeiro deve ser setado as alterações no lançamento para termos certeza que está atualizando.
		lancamento.setAno(2021);
		lancamento.setDescricao("Teste Atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2021);
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizar");
		assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
		
	}
	
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
		
	}
	

	//-------------------------------------------------------------------------------------------------------------------------------------------
//	Métodos Criados
	public static Lancamento criarLancamento() {
		return Lancamento.builder()
				.ano(2022)
				.mes(6)
				.descricao("lancamento qualquer")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA)
				.status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now())
				.build();
	}
	
	
	private Lancamento criarEPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		return lancamento;
	}
	
	
	
	
}

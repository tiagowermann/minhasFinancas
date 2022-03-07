package com.tiago.minhasfinancas.service;

import java.util.List;

import com.tiago.minhasfinancas.model.entity.Lancamento;
import com.tiago.minhasfinancas.model.unums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento salvar (Lancamento lancamento);
	
	Lancamento atualizar (Lancamento lancamento); 	//Deve receber com o ID.
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar (Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status );
	
	void validar (Lancamento lancamento);

}

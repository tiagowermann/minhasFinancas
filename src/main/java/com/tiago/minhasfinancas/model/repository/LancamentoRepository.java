package com.tiago.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiago.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}

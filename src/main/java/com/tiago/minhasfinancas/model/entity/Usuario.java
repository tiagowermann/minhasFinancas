package com.tiago.minhasfinancas.model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "usuario", schema = "financas") 		//Irá buscar as informações da tabela usuário na schema financas dentro da base de dados.
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id 							//Está dizendo que o ID é a chave primária
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 		//É o usado no postgres para gerar sequencial.
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	private String senha;

	
		
}

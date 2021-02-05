package com.example.texo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.texo.domain.Filme;

public interface FilmeRepository  extends JpaRepository<Filme, Long>, JpaSpecificationExecutor<Filme>{

	@Query("SELECT F FROM Filme F WHERE WINNER= 'YES' ORDER BY YEAR")
	List<Filme> buscarGanhadores();
	
	@Query("SELECT distinct producers FROM Filme")
	List<String> buscarProdutoresDistintos();
	
}

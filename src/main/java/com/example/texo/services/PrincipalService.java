package com.example.texo.services;

import com.example.texo.DTO.MinMaxDTO;

public interface PrincipalService {

	public void carregarTabela() throws Exception;
	public MinMaxDTO buscarMaiorIntervalo(MinMaxDTO minMaxDTO) throws Exception;
	public MinMaxDTO buscarMenorIntervalo(MinMaxDTO minMaxDTO) throws Exception;
}

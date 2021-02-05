package com.example.texo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.texo.DTO.MinMaxDTO;
import com.example.texo.DTO.RetornoDTO;
import com.example.texo.services.PrincipalService;

@RestController
@RequestMapping("/principal")
@CrossOrigin(origins = "*")
public class PrincipalController {
	
	@Autowired
	PrincipalService principalService;
	
  @RequestMapping(value = "buscarMaiorIntervalo", method = RequestMethod.GET)
  public ResponseEntity<RetornoDTO> buscarMaiorIntervalo() throws Exception {
	  RetornoDTO retornoDTO = new RetornoDTO();
	  MinMaxDTO minMaxRet = new MinMaxDTO();
	  try {
		  minMaxRet = principalService.buscarMaiorIntervalo(minMaxRet);
		  minMaxRet = principalService.buscarMenorIntervalo(minMaxRet);
		  
		  retornoDTO.setStatus(true);
		  retornoDTO.setMinMaxDTO(minMaxRet);		
	} catch (Exception e) {
		retornoDTO.setStatus(false);
		retornoDTO.setErro(e.getMessage());		
	}
	  return new ResponseEntity<RetornoDTO>(retornoDTO, HttpStatus.OK);	
  }
  
  @RequestMapping(value = "buscarMenorIntervalo", method = RequestMethod.GET)
  public ResponseEntity<String> buscarMenorIntervalo() throws Exception {
	
	  principalService.carregarTabela();
    return new ResponseEntity<String>("Deu certo", HttpStatus.OK);

    //  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}

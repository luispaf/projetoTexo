package com.example.texo.services.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.texo.DTO.MinMaxDTO;
import com.example.texo.DTO.RetornorPrincipalDTO;
import com.example.texo.domain.Filme;
import com.example.texo.repository.FilmeRepository;
import com.example.texo.services.PrincipalService;

@Service
public class PrincipalServiceImpl implements PrincipalService{
	
	@Autowired
	FilmeRepository filmeRepository;

	@PostConstruct
	public void carregarTabela() throws Exception {
		  //csv file containing data
		  String arquivoCSV = "src/main/resources/templates/movielist.csv";
		    BufferedReader br = null;
		    String linha = "";
		    String csvDivisor = ";";
		    String[] caracteresEspeciais = {"\""};
		    
		    try {		    	
		    	List<Filme> listaFilmes = new ArrayList<Filme>();
		    	
		        br = new BufferedReader(new FileReader(arquivoCSV));
		        Long cont = 1L;
		        while ((linha = br.readLine()) != null) {
		        	if (cont > 1) {
		        		Filme filme = new Filme();       		
		        		
		        		try {
		        			String[] listaColunas = linha.split(csvDivisor);	
		        			// Retira os caracter especiais
		        			for (String caracter : caracteresEspeciais) {
		        				filme.setProducers(listaColunas[3].replaceAll(caracter, ""));
		        			}
		        			
		        			List<String> listaNomes = listaNomes(filme.getProducers());
		        			
		        			if (listaNomes.size() > 1) {
		        				for (String nome : listaNomes) {
		        					try {
		        						filme = new Filme(); 
		        						filme.setYear(converterAno(listaColunas[0]));
		        						filme.setTitle(listaColunas[1]);
		        						filme.setStudios(listaColunas[2]);	
		        						filme.setProducers(nome);
		        						filme.setWinner(listaColunas[4].toUpperCase());	
		        						listaFilmes.add(filme);	
									} catch (Exception e) {
										filme.setWinner("NO");
										listaFilmes.add(filme);	
									}
								}
		        			} else {
		        				try {
		        					filme = new Filme(); 
		        					filme.setYear(converterAno(listaColunas[0]));
		        					filme.setTitle(listaColunas[1]);
		        					filme.setStudios(listaColunas[2]);	
		        					filme.setProducers(listaColunas[3]);
		        					filme.setWinner(listaColunas[4].toUpperCase());	
		        					listaFilmes.add(filme);	
								} catch (Exception e) {
									filme.setWinner("NO");
									listaFilmes.add(filme);	
								}
		        			}						
						} catch (Exception e) {
							throw new Exception("Valores da linha " + cont + " invalido: " + e.getMessage());
						}	        		
		        	} 
		        	cont ++;
		        }
		        
		        //limpa a tabela
		        filmeRepository.deleteAll();
		        
		        // Salva a planilha
		        if (!listaFilmes.isEmpty()) {
		        	filmeRepository.saveAll(listaFilmes);
		        }
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        if (br != null) {
		            try {
		                br.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }	
		
	}
	
	public List<String> listaNomes(String nomes) {
		String[] listaNomesSeparador1 = nomes.split("and");
		List<String> nomesret = new ArrayList<String>();
		for (String nome1 : listaNomesSeparador1) {
			String[] listaNomesSeparador2 = nome1.split(",");
			for (String nome2 : listaNomesSeparador2) {
				nomesret.add(nome2.trim());
			}
		}
		
		return nomesret;
	}
	
	public Long converterAno(String ano) throws Exception {
		try {
			return Long.parseLong(ano);
		} catch (Exception e) {
			throw new Exception("Ano com formato incorreto favor verificar!");
		}
	}

	public MinMaxDTO buscarMaiorIntervalo(MinMaxDTO minMaxRet) throws Exception {
		try {
			List<String> listaProdutoresDistintos = filmeRepository.buscarProdutoresDistintos();
			List<Filme> listaFilmes = filmeRepository.buscarGanhadores();
			
			if ((listaProdutoresDistintos != null && !listaProdutoresDistintos.isEmpty()) && (listaFilmes != null && !listaFilmes.isEmpty())) {
				
				
				for (String nomeProdutor : listaProdutoresDistintos) {
					Filme primeiroFilme  = null;
					Filme ultimoFilme = null;
					for (Filme filme : listaFilmes) {
						if (nomeProdutor.equals(filme.getProducers()) && primeiroFilme == null) {
							primeiroFilme = filme;
						} else {
							if (nomeProdutor.equals(filme.getProducers())) {
								ultimoFilme = filme;								
							}
						}
					}
					
					RetornorPrincipalDTO retDTO =  new RetornorPrincipalDTO();
					if (primeiroFilme != null && ultimoFilme != null) {
						
						Long intervalo = ultimoFilme.getYear() - primeiroFilme.getYear();
						
						retDTO.setProducer(primeiroFilme.getProducers());
						retDTO.setInterval(intervalo);
						retDTO.setPreviousWin(primeiroFilme.getYear());
						retDTO.setFollowingWin(ultimoFilme.getYear());
					} else {
						if (primeiroFilme != null) {							
							retDTO.setProducer(primeiroFilme.getProducers());
							retDTO.setInterval(0L);
							retDTO.setPreviousWin(primeiroFilme.getYear());
							retDTO.setFollowingWin(0L);							
						}
					}
					
					if (retDTO.getProducer() != null) {
						minMaxRet.getMax().add(retDTO);					
					}
				}
				
			} else {
				throw new Exception("Produtor não encontrado!");
			}
			
			return minMaxRet;			
		} catch (Exception e) {
			throw new Exception("Falha ao buscar o mair intervalo: " + e.getMessage());
		}
	}
	
	public MinMaxDTO buscarMenorIntervalo(MinMaxDTO minMaxRet) throws Exception {
		try {
			List<String> listaProdutoresDistintos = filmeRepository.buscarProdutoresDistintos();
			List<Filme> listaFilmes = filmeRepository.buscarGanhadores();
			
			if ((listaProdutoresDistintos != null && !listaProdutoresDistintos.isEmpty()) && (listaFilmes != null && !listaFilmes.isEmpty())) {
				
				
				for (String nomeProdutor : listaProdutoresDistintos) {
					Filme primeiroFilme  = null;
					Filme proximoFilme = null;
					for (Filme filme : listaFilmes) {
						if (nomeProdutor.equals(filme.getProducers()) && primeiroFilme == null) {
							primeiroFilme = filme;
						} else {
							if (nomeProdutor.equals(filme.getProducers())) {
								proximoFilme = filme;
								break;
							}
						}
					}
					
					RetornorPrincipalDTO retDTO =  new RetornorPrincipalDTO();
					if (primeiroFilme != null && proximoFilme != null) {
						
						Long intervalo = proximoFilme.getYear() - primeiroFilme.getYear();
						
						retDTO.setProducer(primeiroFilme.getProducers());
						retDTO.setInterval(intervalo);
						retDTO.setPreviousWin(primeiroFilme.getYear());
						retDTO.setFollowingWin(proximoFilme.getYear());
					} else {
						if (primeiroFilme != null) {							
							retDTO.setProducer(primeiroFilme.getProducers());
							retDTO.setInterval(0L);
							retDTO.setPreviousWin(primeiroFilme.getYear());
							retDTO.setFollowingWin(0L);							
						}
					}
					
					if (retDTO.getProducer() != null) {
						minMaxRet.getMin().add(retDTO);					
					}
				}
				
			} else {
				throw new Exception("Produtor não encontrado!");
			}
			
			return minMaxRet;			
		} catch (Exception e) {
			throw new Exception("Falha ao buscar o menor intervalo: " + e.getMessage());
		}
	}
	
}

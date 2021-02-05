package com.example.texo.DTO;

public class RetornoDTO {

	private Boolean status;
	private MinMaxDTO minMaxDTO;
	private String erro;
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public MinMaxDTO getMinMaxDTO() {
		return minMaxDTO;
	}
	public void setMinMaxDTO(MinMaxDTO minMaxDTO) {
		this.minMaxDTO = minMaxDTO;
	}
	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}	
	
	
	
}

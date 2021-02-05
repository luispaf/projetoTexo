package com.example.texo.DTO;

import java.util.ArrayList;
import java.util.List;

public class MinMaxDTO {

	private List<RetornorPrincipalDTO> min;
	
	private List<RetornorPrincipalDTO> max;

	public List<RetornorPrincipalDTO> getMin() {
		if (min == null) {
			min = new ArrayList<RetornorPrincipalDTO>();
		}
		return min;
	}

	public void setMin(List<RetornorPrincipalDTO> min) {
		this.min = min;
	}

	public List<RetornorPrincipalDTO> getMax() {
		if (max == null) {
			max = new ArrayList<RetornorPrincipalDTO>();
		}
		return max;
	}

	public void setMax(List<RetornorPrincipalDTO> max) {
		this.max = max;
	}

	

}

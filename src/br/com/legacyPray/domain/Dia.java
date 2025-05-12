package br.com.legacyPray.domain;

import java.util.ArrayList;
import java.util.List;

public class Dia {
	private Integer dia;
	private Boolean testemunho;
	private Boolean live;
	private List<String> colaboradores;
	
	public Dia() {
	}
	
	public Dia(int diaDoMes) {
		this.dia = diaDoMes;
		this.colaboradores = new ArrayList<String>();
		this.testemunho = false;
		this.live = false;
	}
	
	public Integer getDia() {
		return dia;
	}
	
	public List<String> getColaboradores(){
		return this.colaboradores;
	}
	
	public void addColaborador(String nome) {
		this.colaboradores.add(nome);
	}
	public void setTestemunho() {
		this.testemunho = true;
	}
	public Boolean getTestemunho() {
		return this.testemunho;
	}
	public void setLive() {
		this.live = true;
	}
	public Boolean getlive() {
		return this.live;
	}
}

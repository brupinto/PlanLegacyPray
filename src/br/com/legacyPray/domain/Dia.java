package br.com.legacyPray.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dia {
	private Integer dia;
	private Integer diaSemana;
	private Boolean testemunho;
	private Boolean live;
	private List<Colaborador> colaboradores;
	
	public Dia() {
	}
	
	public Dia(int diaDoMes, int diaSemana) {
		this.dia = diaDoMes;
		this.diaSemana = diaSemana;
		this.colaboradores = new ArrayList<Colaborador>();
		this.testemunho = false;
		this.live = false;
	}
	
	public String getDiaSemana() {
		String rtn = null;
		switch (this.diaSemana) {
		case Calendar.SUNDAY:
			rtn = "Domingo";
			break;
		case Calendar.MONDAY:
			rtn = "Segunda-feira";
			break;
		case Calendar.TUESDAY:
			rtn = "Terça-feira";
			break;
		case Calendar.WEDNESDAY:
			rtn = "Quarta-feira";
			break;
		case Calendar.THURSDAY:
			rtn = "Quinta-feira";
			break;
		case Calendar.FRIDAY:
			rtn = "Sexta-feira";
			break;
		case Calendar.SATURDAY:
			rtn = "Sabado";
			break;
		}
		
		return rtn;
	}
	
	public Integer getDia() {
		return dia;
	}
	
	public List<Colaborador> getColaboradores(){
		return this.colaboradores;
	}
	
	public void addColaborador(Colaborador colab) {
		this.colaboradores.add(colab);
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

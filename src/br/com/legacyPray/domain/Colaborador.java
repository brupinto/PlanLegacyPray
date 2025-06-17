package br.com.legacyPray.domain;

public class Colaborador {
	private String nome;
	private Boolean isSun;
	private Boolean isMon;
	private Boolean isTue;
	private Boolean isWed;
	private Boolean isThu;
	private Boolean isFri;
	private Boolean isSat;
	
	public Colaborador() {
		this.isSun = false;
		this.isMon = false;
		this.isTue = false;
		this.isWed = false;
		this.isThu = false;
		this.isFri = false;
		this.isSat = false;
	}
	
	public Colaborador(String nome, Boolean sunday, Boolean monday, Boolean tuesday, Boolean wednesday,
			Boolean thursday, Boolean friday, Boolean saturday) {
		
		this.nome = nome;
		this.isSun = sunday;
		this.isMon = monday;
		this.isTue = tuesday;
		this.isWed = wednesday;
		this.isThu = thursday;
		this.isFri = friday;
		this.isSat = saturday;
	}

	public Colaborador(String[] preColab) {
		
		if (preColab.length == 8) {
			this.nome = preColab[0];
			this.isSun = preColab[1].equalsIgnoreCase("S");
			this.isMon = preColab[2].equalsIgnoreCase("S");
			this.isTue = preColab[3].equalsIgnoreCase("S");
			this.isWed = preColab[4].equalsIgnoreCase("S");
			this.isThu = preColab[5].equalsIgnoreCase("S");
			this.isFri = preColab[6].equalsIgnoreCase("S");
			this.isSat = preColab[7].equalsIgnoreCase("S");	
		}
	}

	public String getNome() {
		return nome;
	}
	public Boolean isSun() {
		return isSun;
	}
	public Boolean isMon() {
		return isMon;
	}
	public Boolean isTue() {
		return isTue;
	}
	public Boolean isWed() {
		return isWed;
	}
	public Boolean isThu() {
		return isThu;
	}	
	public Boolean isFri() {
		return isFri;
	}
	public Boolean isSat() {
		return isSat;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setIsSun(Boolean isSun) {
		this.isSun = isSun;
	}
	public void setIsMon(Boolean isMon) {
		this.isMon = isMon;
	}
	public void setIsTue(Boolean isTue) {
		this.isTue = isTue;
	}
	public void setIsWed(Boolean isWed) {
		this.isWed = isWed;
	}
	public void setIsThu(Boolean isThu) {
		this.isThu = isThu;
	}
	public void setIsFri(Boolean isFri) {
		this.isFri = isFri;
	}
	public void setIsSat(Boolean isSat) {
		this.isSat = isSat;
	}
}
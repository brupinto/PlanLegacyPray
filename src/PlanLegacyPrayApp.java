import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import br.com.legacyPray.domain.Colaborador;
import br.com.legacyPray.domain.Dia;

public class PlanLegacyPrayApp {
	List<Dia> planMes;
	List<Colaborador> colaboradores;
	List<Colaborador> colabsSunday;
	List<Colaborador> colabsMonday;
	List<Colaborador> colabsTuesday;
	List<Colaborador> colabsWednesday;
	List<Colaborador> colabsThursday;
	List<Colaborador> colabsFriday;
	List<Colaborador> colabsSaturday;
	File jarDir;
	int mes;
	int ano;
	
	public static void main(String[] args) throws URISyntaxException {
		if (args.length > 1) {
			File jarDir = new File(PlanLegacyPrayApp.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
			System.out.println("path "+jarDir.getPath());
			(new PlanLegacyPrayApp()).execute(args[0], args[1], jarDir);
		} else {
			System.out.println("Os parametros necessarios são: <E/O> <MES/ANO>");
			System.out.println("E - layout de Estacionamento");
			System.out.println("O - layout de Oração");
			System.out.println("<MES/ANO> para buscar o layout do mês");
		}
			
	}
	
	private void execute(String layout, String mesano, File jarDir) {
		
		switch (layout) {
		case "O":{
			layoutOracao(mesano);
			break;
		}	
		case "E":{
			layoutEstacionamento(mesano);
			break;
		}
		default:{
			System.out.println("Deve ser utilizado o layout 'O' Oração / 'E' Estacionamento");
			break;
		}}
	}
	
	private void layoutEstacionamento(String mesano) {
		this.mes = Integer.parseInt((String)mesano.subSequence(0, 2));
		this.ano = Integer.parseInt((String)mesano.subSequence(3,7));
		
		System.out.println("iniciando programação do Estacionamento Legacy para o "+mesano);
		this.montaCalendarioEstacionamento(mes, ano);
		this.loadColaboradores();
		System.out.println("Carregando base de colaboradores: "+ String.valueOf(colaboradores.size()));
		
		System.out.println("Processando o mes: "+mesano);
		this.searchColaboradorDiaEstacionamento();
		this.saveResultEstacionamento();
		
	}
	
	private void layoutOracao(String mesano) {
		this.mes = Integer.parseInt((String)mesano.subSequence(0, 2));
		this.ano = Integer.parseInt((String)mesano.subSequence(3,7));
		
		System.out.println("iniciando programação do Legacy Pray para o "+mesano);
		this.montaCalendario(mes, ano);
		this.loadColaboradores();
		System.out.println("Carregando base de colaboradores: "+ String.valueOf(colaboradores.size()));
		
		System.out.println("Processando o mes: "+mesano);
		this.searchColaboradorDia();
		this.saveResult();
	}
	
	private void montaCalendario(int mes, int ano) {
		this.planMes = new ArrayList<Dia>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(ano, (mes-1), 1);
						
		while(cal.get(Calendar.MONTH) == (mes-1)) {
			Dia dia = new Dia(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_WEEK));
			
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				dia.setTestemunho();
			}
			
			if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) || 
			   (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)) {
				dia.setLive();
			}

			this.planMes.add(dia);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	
	private void montaCalendarioEstacionamento(int mes, int ano) {
		this.planMes = new ArrayList<Dia>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(ano, (mes-1), 1);
						
		while(cal.get(Calendar.MONTH) == (mes-1)) {
			Dia dia = new Dia(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_WEEK));
			
			if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) ||
				(cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) ||
				(cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)){
				dia.setCulto();
				this.planMes.add(dia);
			}
			
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				if (dia.getDia() + 14 > 31) {
					dia.setCulto();
					this.planMes.add(dia);
				}
			}
			
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				dia.setCulto();
				dia.setDomingo();
				this.planMes.add(dia);
			}
			
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	
	private void loadColaboradores() {
		this.colaboradores = new ArrayList<Colaborador>();
		
		try {
			File arquivo = new File(jarDir,"colaboradores.txt");
			Path jarDir = Paths.get(arquivo.toURI()).getParent();
			Path filePath = jarDir.resolve("colaboradores.txt");
			
			List<String> linhas = Files.readAllLines(filePath);
			
			for (String linha : linhas) {
				String[] preColab = linha.split(";");
				Colaborador colab = new Colaborador(preColab);
				this.colaboradores.add(colab);
			}

		} catch(Exception ex) {
			System.out.println("Erro!");
			ex.printStackTrace();
		}
	}
	
	private void resetFullColabs() {
		resetColabSunday();
		resetColabMonday();
		resetColabTuesday();
		resetColabWednesday();
		resetColabThursday();
		resetColabFriday();
		resetColabSaturday();
	}
	
	private void resetColabs(int dayOfWeek) {
		System.out.println("Reset colabs "+dayOfWeek);
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			resetColabSunday();
			break;
		case Calendar.MONDAY:
			resetColabMonday();
			break;
		case Calendar.TUESDAY:
			resetColabTuesday();
			break;
		case Calendar.WEDNESDAY:
			resetColabWednesday();
			break;
		case Calendar.THURSDAY:
			resetColabThursday();
			break;
		case Calendar.FRIDAY:
			resetColabFriday();
			break;
		case Calendar.SATURDAY:
			resetColabSaturday();
			break;
		}
	}
	
	private void resetColabSunday() {
		colabsSunday = new ArrayList<Colaborador>();
		
		for (Colaborador colab : colaboradores) {
			if (colab.isSun())
				colabsSunday.add(colab);
		}
	}
	
	private void resetColabMonday() {
		colabsMonday = new ArrayList<Colaborador>();
		
		for (Colaborador colab : colaboradores) {
			if (colab.isMon())
				colabsMonday.add(colab);
		}
	}
	
	private void resetColabTuesday() {
		colabsTuesday = new ArrayList<Colaborador>();
		
		for (Colaborador colab : colaboradores) {
			if (colab.isTue())
				colabsTuesday.add(colab);
		}
	}
	
	private void resetColabWednesday() {
		colabsWednesday = new ArrayList<Colaborador>();
		
		for (Colaborador colab : colaboradores) {
			if (colab.isWed())
				colabsWednesday.add(colab);
		}
	}
	
	private void resetColabThursday() {
		colabsThursday = new ArrayList<Colaborador>();
		
		for (Colaborador colab : colaboradores) {
			if (colab.isThu())
				colabsThursday.add(colab);
		}
	}
	
	private void resetColabFriday() {
		colabsFriday = new ArrayList<Colaborador>();
		
		for (Colaborador colab : colaboradores) {
			if (colab.isFri())
				colabsFriday.add(colab);
		}
	}
	
	private void resetColabSaturday() {
		colabsSaturday = new ArrayList<Colaborador>();
		
		for (Colaborador colab : colaboradores) {
			if (colab.isSat())
				colabsSaturday.add(colab);
		}
	}
	
	private List<Colaborador> getListFromWeekDay(int dayOfWeek) {
		List<Colaborador> rtn = null;
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			rtn = colabsSunday;
			break;
		case Calendar.MONDAY:
			rtn = colabsMonday;
			break;
		case Calendar.TUESDAY:
			rtn = colabsTuesday;
			break;
		case Calendar.WEDNESDAY:
			rtn = colabsWednesday;
			break;
		case Calendar.THURSDAY:
			rtn = colabsThursday;
			break;
		case Calendar.FRIDAY:
			rtn = colabsFriday;
			break;
		case Calendar.SATURDAY:
			rtn = colabsSaturday;
			break;
		}
		
		return rtn;
	}
	
	private Colaborador getColaborador(int dayOfWeek) {
		Random random = new Random();
		int indice;
		Colaborador rtn;
		
		if (getListFromWeekDay(dayOfWeek).isEmpty()) {
			resetColabs(dayOfWeek);
		}
		
		indice = random.nextInt(getListFromWeekDay(dayOfWeek).size());
		rtn = getListFromWeekDay(dayOfWeek).get(indice);
		rmColab(rtn.getNome());
		
		return rtn;
	}
	
	private void rmColab(String colabName) {
		for (Colaborador col : colabsSunday) {
			if (col.getNome().equalsIgnoreCase(colabName)) {
				colabsSunday.remove(col);
				break;
			}
		}
		for (Colaborador col : colabsMonday) {
			if (col.getNome().equalsIgnoreCase(colabName)) {
				colabsMonday.remove(col);
				break;
			}
		}
		for (Colaborador col : colabsTuesday) {
			if (col.getNome().equalsIgnoreCase(colabName)) {
				colabsTuesday.remove(col);
				break;
			}
		}
		for (Colaborador col : colabsWednesday) {
			if (col.getNome().equalsIgnoreCase(colabName)) {
				colabsWednesday.remove(col);
				break;
			}
		}
		for (Colaborador col : colabsThursday) {
			if (col.getNome().equalsIgnoreCase(colabName)) {
				colabsThursday.remove(col);
				break;
			}
		}
		for (Colaborador col : colabsFriday) {
			if (col.getNome().equalsIgnoreCase(colabName)) {
				colabsFriday.remove(col);
				break;
			}
		}
		for (Colaborador col : colabsSaturday) {
			if (col.getNome().equalsIgnoreCase(colabName)) {
				colabsSaturday.remove(col);
				break;
			}
		}
	}
	
	private void searchColaboradorDiaEstacionamento() {
		
		this.resetFullColabs();
		
		for (Dia dia : planMes) {
			System.out.println("Selecionando colaboradores para o dia "+ String.valueOf(dia.getDia())+"/"+ String.valueOf(this.mes));
			Calendar cal = Calendar.getInstance();
			cal.set(ano,(mes-1), dia.getDia());
			
			if (dia.getDomingo()) {
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
			}
			else {
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));				
			}
		}
	}
	
	private void searchColaboradorDia() {
				
		this.resetFullColabs();
		
		for (Dia dia : planMes) {
			System.out.println("Selecionando colaboradores para o dia "+ String.valueOf(dia.getDia())+"/"+ String.valueOf(this.mes));
			Calendar cal = Calendar.getInstance();
			cal.set(ano,(mes-1), dia.getDia());
			
			if (dia.getTestemunho()) {
				dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
			}
			else {
				if (!dia.getlive()) {
					dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
					dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));
					dia.addColaborador(getColaborador(cal.get(Calendar.DAY_OF_WEEK)));		
				}
			}
		}
	}

	private void saveResult() {
		try {
			File file = new File(jarDir,"programacao_"+String.valueOf(mes)+"_"+String.valueOf(ano)+".txt");
			FileWriter output = new FileWriter(file);
			
			for (Dia dia : planMes) {
				String texto;
				
				if (dia.getlive()) {
					texto = "Dia "+ String.valueOf(dia.getDia())+"/"+String.valueOf(mes)+"-"+ dia.getDiaSemana()+"\r\n"
					      + "Dia de Live no Instagram\r\n";
				}
				else {
					if (dia.getTestemunho()) {
						texto = "Dia "+ String.valueOf(dia.getDia())+"/"+String.valueOf(mes)+"-"+ dia.getDiaSemana()+"\r\n"
							      + "Dia de Testemunho\r\n"
							      + "Condizir.: "+dia.getColaboradores().get(0).getNome()+"\r\n"
							      + "Testemunhante.:\r\n";
					}
					else {
						texto = "Dia "+ String.valueOf(dia.getDia())+"/"+String.valueOf(mes)+"-"+ dia.getDiaSemana()+"\r\n"
							      + "Dia de Oração\r\n"
							      + "Conduzir.: "+dia.getColaboradores().get(0).getNome()+"\r\n"
								  + "Orador.: "+dia.getColaboradores().get(1).getNome()+"\r\n"
						  		  + "orador primeiro topico.: "+dia.getColaboradores().get(2).getNome()+"\r\n";
					}
				}
					
				texto+="\r\n";
				output.write(texto);
			}
			
			output.close();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void saveResultEstacionamento() {
		try {
			File file = new File(jarDir,"programacao_"+String.valueOf(mes)+"_"+String.valueOf(ano)+".txt");
			FileWriter output = new FileWriter(file);
			
			for (Dia dia : planMes) {
				String texto = "";	
				
				if (dia.getDomingo()) {
					texto = "Dia "+ String.valueOf(dia.getDia())+"/"+String.valueOf(mes)+"-"+ dia.getDiaSemana()+"\r\n";
					texto+="10h \r\n";	
					texto+= dia.getColaboradores().get(0).getNome()+"\r\n"
						  + dia.getColaboradores().get(1).getNome()+"\r\n"
						  + dia.getColaboradores().get(2).getNome()+"\r\n"
						  + dia.getColaboradores().get(3).getNome()+"\r\n";
					texto+="\r\n";
					texto+="17h \r\n";
					texto+= dia.getColaboradores().get(4).getNome()+"\r\n"
						  + dia.getColaboradores().get(5).getNome()+"\r\n"
						  + dia.getColaboradores().get(6).getNome()+"\r\n"
						  + dia.getColaboradores().get(7).getNome()+"\r\n";
					texto+="\r\n";
					texto+="19h \r\n";
					texto+= dia.getColaboradores().get(8).getNome()+"\r\n"
						  + dia.getColaboradores().get(9).getNome()+"\r\n"
						  + dia.getColaboradores().get(10).getNome()+"\r\n"
						  + dia.getColaboradores().get(11).getNome()+"\r\n";
				}
				else {
					texto = "Dia "+ String.valueOf(dia.getDia())+"/"+String.valueOf(mes)+"-"+ dia.getDiaSemana()+"\r\n"
							+ dia.getColaboradores().get(0).getNome()+"\r\n"
							+ dia.getColaboradores().get(1).getNome()+"\r\n"
							+ dia.getColaboradores().get(2).getNome()+"\r\n"
							+ dia.getColaboradores().get(3).getNome()+"\r\n";	
				}
									
				texto+="\r\n";
				output.write(texto);
			}
			
			output.close();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

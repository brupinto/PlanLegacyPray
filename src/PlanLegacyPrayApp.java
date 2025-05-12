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
import java.util.Scanner;

import br.com.legacyPray.domain.Dia;

public class PlanLegacyPrayApp {
	List<Dia> planMes;
	List<String> colaboradores;
	List<String> colaboradoresSearch;
	File jarDir;
	int mes;
	int ano;
	
	public static void main(String[] args) throws URISyntaxException {
		if (args.length > 0) {
			File jarDir = new File(PlanLegacyPrayApp.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
			System.out.println("path "+jarDir.getPath());
			(new PlanLegacyPrayApp()).execute(args[0], jarDir);
		} else {
			System.out.println("É necessário colocar o MÊS/ANO na chamada do aplicativo");
		}
			
	}
	
	private void execute(String mesano, File jarDir) {
		this.jarDir = jarDir;
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
		cal.set(ano, mes, 1);
						
		while(cal.get(Calendar.MONTH) == mes) {
			Dia dia = new Dia(cal.get(Calendar.DAY_OF_MONTH));
			
			if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) || 
			   (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
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
	
	private void loadColaboradores() {
		this.colaboradores = new ArrayList<String>();
		
		try {
			File arquivo = new File(jarDir,"colaboradores.txt");
			Path jarDir = Paths.get(arquivo.toURI()).getParent();
			Path filePath = jarDir.resolve("colaboradores.txt");
			
			List<String> linhas = Files.readAllLines(filePath);
			
			for (String linha : linhas) {
				this.colaboradores.add(linha);
			}

		} catch(Exception ex) {
			System.out.println("Erro!");
			ex.printStackTrace();
		}
	}
	
	private void resetColaborador() {
		colaboradoresSearch = new ArrayList<String>();
		
		for (String nome : colaboradores) {
			colaboradoresSearch.add(new String(nome));
		}
	}
	
	private String getColaborador() {
		Random random = new Random();
		int indice;
		String rtn;
		
		if (colaboradoresSearch.isEmpty()) {
			this.resetColaborador();
		}
		
		indice = random.nextInt(colaboradoresSearch.size());
		rtn = new String(colaboradoresSearch.get(indice));
		colaboradoresSearch.remove(indice);
		
		return rtn;
	}
	
	private void searchColaboradorDia() {
				
		this.resetColaborador();
		
		for (Dia dia : planMes) {
			System.out.println("Selecionando colaboradores para o dia "+ String.valueOf(dia.getDia())+"/"+ String.valueOf(this.mes));
			if (dia.getTestemunho()) {
				dia.addColaborador(getColaborador());
			}
			else {
				if (!dia.getlive()) {
					dia.addColaborador(getColaborador());
					dia.addColaborador(getColaborador());
					dia.addColaborador(getColaborador());		
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
					texto = "Dia "+ String.valueOf(dia.getDia())+"\r\n"
					      + "Dia de Live no Instagram\r\n";
				}
				else {
					if (dia.getTestemunho()) {
						texto = "Dia "+ String.valueOf(dia.getDia())+"\r\n"
							      + "Dia de Testemunho\r\n"
							      + "Apresentação.: "+dia.getColaboradores().get(0)+"\r\n";
					}
					else {
						texto = "Dia "+ String.valueOf(dia.getDia())+"\r\n"
							      + "Dia de Oração\r\n"
							      + "Apresentação.: "+dia.getColaboradores().get(0)+"\r\n"
								  + "Orador.: "+dia.getColaboradores().get(1)+"\r\n"
						  		  + "Backup/orador primeiro topico.: "+dia.getColaboradores().get(2)+"\r\n";
						
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
}

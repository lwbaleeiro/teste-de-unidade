package br.com.caelum.leilao.servico;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;

public class Avaliador {
	
	private double maiorDeTodos = Double.NEGATIVE_INFINITY;
	private double menorDeTodos = Double.POSITIVE_INFINITY;
	private double media = 0;
	private List<Lance> maiores;

	public void avalia(Leilao leilao) {
		
        if(leilao.getLances().size() ==0) 
            throw new RuntimeException("Não é possível avaliar um leilão sem lances");
		
		double total = 0;
		
		for(Lance lance : leilao.getLances()) {
			if (lance.getValor() > maiorDeTodos) maiorDeTodos = lance.getValor();
			if (lance.getValor() < menorDeTodos) menorDeTodos = lance.getValor();
			total += lance.getValor();
		}
		
		pegaOsMaioresNo(leilao);
		
		if (total == 0) {
			media = 0;
			return;
		}
		
		media = total  / leilao.getLances() .size();
	}
	
	private void pegaOsMaioresNo(Leilao leilao) {
		maiores = new ArrayList<Lance>(leilao.getLances());
		maiores.sort((l1, l2) -> Double.compare(l2.getValor(), l1.getValor()));
		maiores = maiores.subList(0, maiores.size() > 3 ? 3 : maiores.size());
	}
	
	public List<Lance> getTresMaiores(){
		return this.maiores;
	}

	public double getMaiorLance() {
		return this.maiorDeTodos;
	}
	
	public double getMenorLance() {
		return this.menorDeTodos;
	}
	
	public double getMedia() {
		return this.media;
	}

}

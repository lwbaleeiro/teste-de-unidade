package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class AvaliadorTest {
	
	private Avaliador leiloeiro;
	
	@BeforeClass
	public static void testandoBeforeClass() {
	  System.out.println("before class");
	}

	@AfterClass
	public static void testandoAfterClass() {
	  System.out.println("after class");
	}
	
	@Before
	public void setUp() {
		this.leiloeiro = new Avaliador();
	}

	@Test
	public void deveEntenderLancesEmOrdemCrescente() {

		Leilao leilao = new CriadorDeLeilao()
				.para("Cadeira Gamer")
				.lance(new Usuario("João"), 250)
				.lance(new Usuario("Maria"), 334)
				.lance(new Usuario("Tiago"), 401.2)
				.constroi();
		
		leiloeiro.avalia(leilao);

		double maiorEsperado = 401.2;
		double menorEsperado = 250;

		assertEquals(maiorEsperado, leiloeiro.getMaiorLance(), 0.0001);
		assertEquals(menorEsperado, leiloeiro.getMenorLance(), 0.0001);

	}

	@Test
	public void deveEntenderLeilaoComApenasUmLance() {

		Leilao leilao = new CriadorDeLeilao()
				.para("Cadeira Gamer")
				.lance(new Usuario("João"), 200)
				.constroi();

		
		leiloeiro.avalia(leilao);

		assertEquals(200, leiloeiro.getMaiorLance(), 0.0001);
		assertEquals(200, leiloeiro.getMenorLance(), 0.0001);
	}

	@Test
	public void deveEntenderLeilaoComLancesEmOrdemRandomica() {
		Usuario joao = new Usuario("Joao");
		Usuario maria = new Usuario("Maria");
		
		Leilao leilao = new CriadorDeLeilao()
				.para("Playstation 3 Novo")
				.lance(joao, 200.0)
				.lance(maria, 450.0)
				.lance(joao, 120.0)
				.lance(maria, 700.0)
				.lance(joao, 630)
				.lance(maria, 230)
				.constroi();
		
		leiloeiro.avalia(leilao);

		assertEquals(700.0, leiloeiro.getMaiorLance(), 0.0001);
		assertEquals(120.0, leiloeiro.getMenorLance(), 0.0001);
	}

	@Test
	public void deveEntenderLeilaoComLancesEmOrdemDecrescente() {
		Usuario joao = new Usuario("Joao");
		Usuario maria = new Usuario("Maria");
		
		Leilao leilao = new CriadorDeLeilao()
				.para("Xbox series Novo")
				.lance(joao, 400.0)
				.lance(maria, 300.0)
				.lance(joao, 200.0)
				.lance(maria, 100.0)
				.constroi();
		
		leiloeiro.avalia(leilao);

		assertEquals(400.0, leiloeiro.getMaiorLance(), 0.0001);
		assertEquals(100.0, leiloeiro.getMenorLance(), 0.0001);
	}

	@Test
	public void deveCalcularMedia() {

		Usuario joao = new Usuario("João");
		Usuario maria = new Usuario("Maria");
		Usuario tiago = new Usuario("Tiago");

		Leilao leilao = new CriadorDeLeilao()
				.para("Mac Book")
				.lance(joao, 300)
				.lance(maria, 400)
				.lance(tiago, 500)
				.constroi();
		
		leiloeiro.avalia(leilao);

		assertEquals(400, leiloeiro.getMedia(), 0.0001);
	}

	@Test(expected=RuntimeException.class)
	public void testaMediaZeroLance() {

		Leilao leilao = new CriadorDeLeilao()
				.para("Iphone 11")
				.constroi();
		
		leiloeiro.avalia(leilao);

		assertEquals(0, leiloeiro.getMedia(), 0.0001);
	}

	@Test
	public void deveDevolverOsTresMaiores() {

		Usuario joao = new Usuario("Joao");
		Usuario maria = new Usuario("Maria");

		Leilao leilao = new CriadorDeLeilao()
				.para("Playstation 5 Novo")
				.lance(joao, 400.0)
				.lance(maria, 300.0)
				.lance(joao, 200.0)
				.lance(maria, 100.0)
				.constroi();
		
		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();

		assertEquals(3, maiores.size());
		assertEquals(400, maiores.get(0).getValor(), 0.0001);
		assertEquals(300, maiores.get(1).getValor(), 0.0001);
		assertEquals(200, maiores.get(2).getValor(), 0.0001);

	}

	@Test
	public void deveDevolverOsTresMaioresComApenasDoisLances() {

		Leilao leilao = new CriadorDeLeilao()
				.para("Playstation 5 Novo")
				.lance(new Usuario("Joao"), 400)
				.lance(new Usuario("Maria"), 300)
				.constroi();
		
		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();

		assertEquals(2, maiores.size());
		assertEquals(400, maiores.get(0).getValor(), 0.0001);
		assertEquals(300, maiores.get(1).getValor(), 0.0001);

	}

	@Test(expected=RuntimeException.class)
	public void testarOsMaioresLancesSemLance() {

		Leilao leilao = new CriadorDeLeilao()
				.para("xBox Series Novo")
				.constroi();
	
		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();

		assertEquals(0, maiores.size());

	}

	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {

		Usuario rogerio = new Usuario("Rogério");

		Leilao leilao = new CriadorDeLeilao()
				.para("Honda Civc")
				.lance(rogerio, 400)
				.lance(rogerio, 400)
				.constroi();

		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();

		assertEquals(1, maiores.size());
		assertEquals(400, maiores.get(0).getValor(), 0.0001);

	}

	@Test
	public void naoDeveAceitarMaisDoQue5LancesDeUmMesmoUsuario() {
		Usuario steveJobs = new Usuario("Steve Jobs");
		Usuario billGates = new Usuario("Bill Gates");
		
		Leilao leilao = new CriadorDeLeilao()
				.para("Macbook Pro 15")
				.lance(steveJobs, 2000)
				.lance(billGates, 3000)
				.lance(steveJobs, 3000)
				.lance(billGates, 3000)
				.lance(steveJobs, 4000)
				.lance(billGates, 3000)
				.lance(steveJobs, 5000)
				.lance(billGates, 3000)
				.lance(steveJobs, 6000)
				.lance(billGates, 999)
				.lance(steveJobs, 7000)
				.constroi();
		
		assertEquals(10, leilao.getLances().size());
		int ultimo = leilao.getLances().size() - 1;
		assertEquals(999, leilao.getLances().get(ultimo).getValor(), 0.00001);
	}

	@Test
	public void deveDobrarOUltimoLanceDado() {
		
		Usuario steveJobs = new Usuario("Steve Jobs");
		Usuario billGates = new Usuario("Bill Gates");
		
		Leilao leilao = new CriadorDeLeilao()
				.para("Macbook Pro 15")
				.lance(steveJobs, 2000)
				.lance(billGates, 3000)
				.lance(steveJobs, 3300)
				.dobra(billGates)
				.constroi();
		
		assertEquals(4, leilao.getLances().size());
		assertEquals(6000, leilao.getLances().get(3).getValor(), 0.0001);
	}
	
	@Test
	public void naoDeveDobrarCasoNaoHajaUltimoLance() {
		
		Usuario steveJobs = new Usuario("Steve Jobs");
		
		Leilao leilao = new CriadorDeLeilao()
				.para("Surface Pro")
				.dobra(steveJobs)
				.constroi();
		
		assertEquals(0, leilao.getLances().size());
	}
	
	@Test(expected=RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {		
			Leilao leilao = new CriadorDeLeilao()
					.para("Playstation 3 Novo")
				    .constroi();
			
			leiloeiro.avalia(leilao);
	}
	
	@Test(expected = RuntimeException.class)
	public void naoDeveAceitarLanceComValorNegativo() {
		new Lance(new Usuario("Sandra"), -1200);
	}
	
	@Test(expected = RuntimeException.class)
	public void naoDeveAceitarLanceIgualAZero() {
		new Lance(new Usuario("Pedro"), 0);
	}
}

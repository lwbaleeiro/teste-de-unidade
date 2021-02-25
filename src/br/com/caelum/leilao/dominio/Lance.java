package br.com.caelum.leilao.dominio;

public class Lance {

	private Usuario usuario;
	private double valor;
	
	public Lance(Usuario usuario, double valor) {
		
		if (valor <= 0) throw new IllegalArgumentException("O lance deve ser maior que zero!");
		
		this.usuario = usuario;
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public double getValor() {
		return valor;
	}
	
	
	
}

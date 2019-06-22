package it.polito.tdp.ufo.model;

public class Avvistamenti {
	
	private int anno;
	private int numero;
	public Avvistamenti(int anno, int numero) {
		super();
		this.anno = anno;
		this.numero = numero;
	}
	public int getAnno() {
		return anno;
	}
	public void setAnno(int anno) {
		this.anno = anno;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	@Override
	public String toString() {
		return String.format("Avvistamenti [anno=%s, numero=%s]", anno, numero);
	}
	
	

}

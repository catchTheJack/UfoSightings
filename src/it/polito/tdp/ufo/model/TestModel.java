package it.polito.tdp.ufo.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Model m = new Model();
		Avvistamenti ax = new Avvistamenti(2013,21);
		m.creaGrafo(ax);

	}

}

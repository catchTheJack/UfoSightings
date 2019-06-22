package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private ArrayList<Sighting> avvistamenti;
	private ArrayList<Avvistamenti> popola;
	private SightingsDAO sdao ;
	private Graph<String,DefaultEdge> grafo ;
////DA UTILIZZARE NELLA RICORSIONE
	private ArrayList<String> ricorsione_ottima ;//inizializzo nel metoro pubblico della ricorsione
	private ArrayList<String> parziale; //soluzione parziale restituita dall'algoritmo ricorsivo da confrontare con la size ottima
	///serve una mappa per stati disponibili da prendere ( piu veloce da anlizzare ) in modo da non
	///ripetire lo stesso vertice.
	
	public List<String> ricorsione(String stato) {
		this.parziale = new ArrayList<String>();
		this.ricorsione_ottima = new ArrayList<String>();
		this.parziale.add(stato);
		private_ricorsivo(parziale);
		return ricorsione_ottima;
	}
	
	private void private_ricorsivo(ArrayList<String> paziale){
		///prendo il vertice di partenza
		///guardo i suoi successori
		//prendo ciascuno dei successori e lo rimuovo dalla mappa se posso selezionarlo
		//lo scarto se lo stato è gia presente nella mia mappa di selezionati
		if(parziale.size()>ricorsione_ottima.size()){
			ricorsione_ottima = new ArrayList<String>(parziale);
		}
		
		for(String successiva : statiSuccessori(parziale.get(parziale.size()-1))) {
			
			if(!parziale.contains(successiva)) {
				parziale.add(successiva);
				private_ricorsivo(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	

	
	public Model() {
		sdao = new SightingsDAO();
		this.popola = new ArrayList<Avvistamenti>(sdao.loadAnno());	
		avvistamenti = new ArrayList<Sighting>();
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
	}
	
	
	
	public List<Avvistamenti >loadBox(){
		return this.popola;
	}
	
	public List<String> loadStati(int anno){
		return sdao.getStateInteressati(anno);
	}
	
	public void creaGrafo(Avvistamenti ax) {
		
		int anno = ax.getAnno();
		avvistamenti = new ArrayList<Sighting>(sdao.getSightingsByYear(anno));
			
		///aggiungo i vertici
		
		Graphs.addAllVertices(grafo,sdao.getStateInteressati(anno));
		for(String sx : grafo.vertexSet()) {
			for(String sx2 : grafo.vertexSet()) {
				if(!sx.equals(sx2)) {
				if(thereIsConnection(sx,sx2,anno)) {
					grafo.addEdge(sx,sx2);
				 }
				}
			}
		}
		
		System.out.print(grafo);
		
		}
	
	public List<String> statiPrecedenti(String stato){
		List<String> precedenti = new ArrayList<String>();
		for(String sx:Graphs.predecessorListOf(grafo, stato)) {
			precedenti.add(sx);
		}
		return precedenti;
	}
	
	public List<String> statiSuccessori(String stato){
		List<String> successori = new ArrayList<String>();
		for(String sx:Graphs.successorListOf(grafo, stato)) {
			successori.add(sx);
		}
		return successori;
	}
	
	public List<String> statiConnessi(String stato){
		List<String> raggiungibili = new LinkedList<String>();
		DepthFirstIterator<String,DefaultEdge> dp = new DepthFirstIterator<String,DefaultEdge>(this.grafo);
		dp.next(); //scarto il primo vertice che è il vertice di partenza
		while(dp.hasNext()) {
			raggiungibili.add(dp.next());
		}
		
		return raggiungibili;
	}
	
	
	public boolean thereIsConnection(String sx, String sx2, int anno) {
		
		return sdao.getResponse(sx, sx2, anno);
	
	}
	
	public Graph getGrafo() {
		return this.grafo;
	}
	
}

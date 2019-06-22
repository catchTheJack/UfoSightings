/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.Avvistamenti;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {
	
	Model model = new Model();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Avvistamenti> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<String> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void handleAnalizza(ActionEvent event) {
    	txtResult.clear();
    	String stato = boxStato.getValue();
    	if(stato==null){
    		txtResult.clear();
    		txtResult.appendText("SELEZIONA UNO STATO");
    	}
    	else {
    	txtResult.appendText("PRECEDENTI:\n"+model.statiPrecedenti(stato));
    	txtResult.appendText("\n\nSUCCESSORI:\n"+model.statiSuccessori(stato));
    	txtResult.appendText("\n STATI RAGGIUNGIBILI: \n"+model.statiConnessi(stato));
    	}
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	int ax = boxAnno.getValue().getAnno();
    	boxStato.getItems().addAll(model.loadStati(ax));
    	double start = System.nanoTime()/1000000000;
    	model.creaGrafo(boxAnno.getValue());
    	double stop =System.nanoTime()/1000000000;
    	txtResult.clear();
    	txtResult.appendText("GRAFO CREATO!!!");
    	txtResult.appendText("\nVERTICI: "+model.getGrafo().vertexSet().size());
    	txtResult.appendText("\nARCHI: "+model.getGrafo().edgeSet().size());
    	txtResult.appendText("\n\n TEMPO DI CREZIONE:"+(stop-start)+"s");
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	String stato = boxStato.getValue();
    	txtResult.appendText("PERCORSO PIU' LUNGO \n");
    	for(String temp : model.ricorsione(stato)) {
    		txtResult.appendText(temp+" ");
    	}
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxAnno.getItems().addAll(model.loadBox());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }
}

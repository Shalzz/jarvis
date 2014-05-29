package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ApplicationController {

    @FXML                           // fx:id="Button1"
    private Button Button1;        // Value injected by FXMLLoader
    
    @FXML
    private Button Button2;
    
    @FXML
    private Button Button3;
    
    @FXML
    private Button Button4;
	
    @FXML
    void handleButtonAction(ActionEvent event) {
       System.out.println("You clicked button 1");
    }
}

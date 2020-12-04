package application.controller;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class MainPage extends Application {
	private double x,y; 
	
	//Sets the primary stage
	@Override
	public void start(Stage primaryStage) throws IOException {

			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Dashboard.fxml"));
		    primaryStage.setScene(new Scene(root));
		    primaryStage.initStyle(StageStyle.UNDECORATED);
		    root.setOnMousePressed(event -> {
		    	x = event.getSceneX();
		    	y = event.getSceneY(); 
		    });
		    
		    root.setOnMouseDragged(event -> {
		    	primaryStage.setX(event.getScreenX() - x);
		    	primaryStage.setY(event.getScreenY() - y);
		    });
		    primaryStage.show();
	}
	
	public static void main(String[] args) { 
		launch(args);
	}
}

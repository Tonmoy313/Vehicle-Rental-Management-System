/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author HP
 */
public class Vehicle_Rental_Mangment extends Application {

   
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/Home.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/Main.fxml"));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Vehicle Rent");
        stage.getIcons().add(new Image("/image/logo.png"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    
       
        launch(args);
    }
    
}

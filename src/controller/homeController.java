/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class homeController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private ImageView btnProfile;
    @FXML
    private Button btnCustomers;
    @FXML
    private Button btnRentCar;
    @FXML
    private Button btnReturnedCar;
    @FXML
    private Button btnSignout;
    @FXML
    private AnchorPane rootanchorpane;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void loadFXML(String fileName) {
        Parent parent;
        try {
            AnchorPane childanchorpane = FXMLLoader.load(getClass().getResource("/viewfxml/" + fileName + ".fxml"));
            rootanchorpane.getChildren().setAll(childanchorpane);

        } catch (IOException ex) {
            Logger.getLogger(homeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clear() {
        borderpane.setCenter(null);
    }

    @FXML
    private void btnActn(ActionEvent event) throws IOException {

        if (event.getSource() == btnCustomers) {
            loadFXML("customer");

        } else if (btnRentCar == event.getSource()) {
            loadFXML("RentCarPg");

        } else if (event.getSource() == btnReturnedCar) {
            loadFXML("ReturnedCarPg");

        } else if (event.getSource() == btnSignout) {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/Main.fxml"));

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Vehicle Rent");
            stage.getIcons().add(new Image("/image/logo.png"));
            stage.setScene(scene);
            stage.show();

            System.out.print("log out");
        }

    }

}

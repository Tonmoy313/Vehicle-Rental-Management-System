/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DbConnection.DBConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class SignInController implements Initializable {

    @FXML
    private Button signinbtn;
    @FXML
    private TextField usernamebox;
    @FXML
    private TextField passwordbox;
    @FXML
    private Text alertbox;

    ResultSet res;
    Connection con = null;
    PreparedStatement pst = null;
    DBConnect dbcon;

    String password, user_input;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        alertbox.setText("");
    }

    @FXML
    private void signinbtnaction(ActionEvent event) throws IOException, SQLException {
        dbcon = new DBConnect();
        con = dbcon.connectToDB();

        System.out.println("res" + res + "\nconnection:" + con);

        if (this.isValidated() && con != null) {
            try {
                String sql = "Select CONVERT(varchar,PasswordHash) from signin where loginname=?";

                pst = con.prepareStatement(sql);
                pst.setString(1, usernamebox.getText());
                res = pst.executeQuery();

                System.out.println("res: " + res + "\nconnection :- " + con);
                System.out.print("CHecking is ther any resultset:   ");

                while (res.next()) {
                    //when "Compare: user_input.compareTo(password)= -25, as a reuslt password from database need to be trimed
                    password = res.getString(1);
                    System.out.print("TRUE \npassword(database):" + password);
                }
                user_input = passwordbox.getText();
                System.out.println(".\t password(user) :" + user_input + ".\nmatching is:" + user_input.contentEquals(password));
                if (user_input.equals(password)||user_input.equals(password.trim())) {

                    JOptionPane.showMessageDialog(null, "user login successful");

                    Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/Home.fxml"));

                    Scene scene = new Scene(root);
                    Stage stage = (Stage) signinbtn.getScene().getWindow();
                    stage.close();
                    stage.setScene(scene);
                    stage.setTitle("Car registration");
                    stage.getIcons().add(new Image("/image/logo.png"));
                    stage.show();
                } else {
                    System.out.println("FALSE.");
                    alertbox.setText("username or password doesntmatch.");
                }
            

            dbcon.disconnectFromDB(pst, con);

        }catch (SQLException ex) {
                Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
}

private boolean isValidated() {
        alertbox.setVisible(true);
        if (usernamebox.getText().equals("")) {
            alertbox.setText("Username text field cannot be blank.");
            usernamebox.requestFocus();
        } else if (usernamebox.getText().length() < 5 || usernamebox.getText().length() > 25) {
            alertbox.setText("Username text field cannot be less than 5.");
            usernamebox.requestFocus();
        } else if (passwordbox.getText().equals("")) {
            alertbox.setText("Password text field cannot be blank.");
            passwordbox.requestFocus();
        } else if (passwordbox.getText().length() < 5) {
            alertbox.setText("Password text field cannot be less than 5.");
            passwordbox.requestFocus();
        } else {
            System.out.println("login conditions: true");

            return true;
        }
        System.out.println("login conditons: false");

        return false;//To change body of generated methods, choose Tools | Templates.
    }
    /*
    private void signupact(MouseEvent event) throws IOException {
        Stage stage = (Stage) signinbtn.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/RegisterView.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Registration");
        stage.getIcons().add(new Image("/image/logo.png"));
        stage.show();
    }*/

}

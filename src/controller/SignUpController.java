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
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class SignUpController implements Initializable {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Button registerButton;
    @FXML
    private Text alertbox;

    Connection connect;
    Window window;
    DBConnect dbconn;

    Statement stmt;
    PreparedStatement ps;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void register(ActionEvent event) throws SQLException, IOException {
        DBConnect dbc = new DBConnect();
        connect = dbc.connectToDB();
        System.out.println("The  connection is successful." + connect);

        if (this.isValidated()) {
            System.out.println("The registration is valid.");
            if (connect != null) {
                try {

                    String query = "insert into signin (FirstName,LastName,Email,LoginName,PasswordHash)values (?,?,?,?,CONVERT(binary,?))";

                    ps = connect.prepareStatement(query);
                    ps.setString(1, firstName.getText());
                    ps.setString(2, lastName.getText());
                    ps.setString(3, email.getText());
                    ps.setString(4, username.getText());
                    ps.setString(5, password.getText());
                    if (ps.executeUpdate() > 0) {
                        this.clearForm();
                        alertbox.setText(" ");
                        JOptionPane.showMessageDialog(null, "You have registered successfully.");

                    } else {
                        JOptionPane.showMessageDialog(null, "Something went wrong.");
                    }

                } catch (SQLException ex) {

                    JOptionPane.showMessageDialog(null, "Something went wrong to insert.");
                }
            }

        }

    }

    private boolean clearForm() {
        firstName.clear();
        lastName.clear();
        email.clear();
        username.clear();
        password.clear();
        confirmPassword.clear();
        return true;
    }

    private boolean isValidated() throws SQLException {

        window = registerButton.getScene().getWindow();
        if (firstName.getText().equals("")) {
            alertbox.setText("First name text field cannot be blank.");
            firstName.requestFocus();
        } else if (firstName.getText().length() < 2 || firstName.getText().length() > 25) {
            alertbox.setText("First name text field cannot be less than 2 and greator than 25 characters.");
            firstName.requestFocus();
        } else if (lastName.getText().equals("")) {
            alertbox.setText("Last name text field cannot be blank.");
            lastName.requestFocus();
        } else if (lastName.getText().length() < 2 || lastName.getText().length() > 25) {
            alertbox.setText("Last name text field cannot be less than 2 and greator than 25 characters.");
            lastName.requestFocus();
        } else if (email.getText().equals("")) {
            alertbox.setText("Email text field cannot be blank.");
            email.requestFocus();
        } else if (!email.getText().endsWith("@gmail.com")) {
            alertbox.setText("Please, Enter a valid email address.");
            email.requestFocus();
        } else if (email.getText().length() < 5 || email.getText().length() > 45) {
            alertbox.setText("Email field cannot be less than 5 and greator than 45 characters.");
            email.requestFocus();
        } else if (username.getText().equals("")) {
            alertbox.setText("Username field cannot be blank.");
            username.requestFocus();
        } else if (username.getText().length() < 5 || username.getText().length() > 25) {
            alertbox.setText("Username text field cannot be less than 5 and greator than 25 characters.");
            username.requestFocus();
        } else if (password.getText().equals("")) {
            alertbox.setText("Password field cannot be blank.");
            password.requestFocus();
        } else if (password.getText().length() < 5 || password.getText().length() > 25) {
            alertbox.setText("Password cannot be less than 5 and greator than 25 characters.");
            password.requestFocus();
        } else if (confirmPassword.getText().equals("")) {
            alertbox.setText("Confirm password field cannot be blank.");
            confirmPassword.requestFocus();
        } else if (confirmPassword.getText().length() < 5 || password.getText().length() > 25) {
            alertbox.setText("Confirm password text cannot be less than 5 and greator than 25 characters.");
            confirmPassword.requestFocus();
        } else if (!password.getText().equals(confirmPassword.getText())) {
            alertbox.setText("Password and confirm password text fields does not match.");
            password.requestFocus();
        } else if (isAlreadyRegistered()) {
            alertbox.setText("The username is already taken by someone else.");
            username.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    private boolean isAlreadyRegistered() throws SQLException {
        //PreparedStatement pst;
        ResultSet rs;
        boolean usernameExist = false;

        if (connect != null) {
            String query = "select * from signin WHERE LoginName= '" + username.getText() + "'";
            System.out.println("checking is there any duplciate username");
            //PreparedStatement pst=connect.prepareStatement(query);
            //pst.setString(1, username.getText());

            try {
                //rs= pst.executeQuery();
                java.sql.Statement stmt = connect.createStatement();
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    usernameExist = true;
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        return usernameExist;
    }
    /*
    private void showLoginStage(MouseEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/startpg.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Car Rent");
        stage.getIcons().add(new Image("/image/logo.png"));
        stage.show();

    }*/

}

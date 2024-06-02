/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DbConnection.DBConnect;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class AddVehicleController implements Initializable {

    @FXML
    private AnchorPane customerPgAnchrPane;
    @FXML
    private ChoiceBox<String> categoryBox;
    @FXML
    private TextField engineBox;
    @FXML
    private TextField modelBox;
    @FXML
    private ImageView vehicleImg;
    @FXML
    private Button backBtn;
    @FXML
    private Button saveBtn;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    byte[] vehicleImage;
    //for class
    DBConnect dbcon;
    @FXML
    private TextField priceBox;
    @FXML
    private Text alerttxt;
    @FXML
    private TextField regBox;
    @FXML
    private Button browseBtn;

    ObservableList CategoryList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadData();
    }

    @FXML
    private void onClkBtn(ActionEvent event) throws IOException, SQLException {
        if (event.getSource() == backBtn) {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/Home.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.close();

            stage.setScene(scene);

            stage.setTitle("Vehicle Rental Mangment System");
            stage.getIcons().add(new Image("/image/logo.png"));
            stage.show();
        } else if (event.getSource() == saveBtn && conditionMeetUp()) {

            addCar();
            //((Node) event.getSource()).getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/Home.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.close();

            stage.setScene(scene);

            stage.setTitle("Vehicle Rental Mangment System");
            stage.getIcons().add(new Image("/image/logo.png"));
            stage.show();
        } else if (event.getSource() == browseBtn) {

            FileChooser imagechoser = new FileChooser();
            imagechoser.setInitialDirectory(new File("C:\\Users\\HP\\Documents\\NetBeansProjects\\Vehicle_Rental_Mangment\\src\\image\\vehicle"));

            File selectedImage = imagechoser.showOpenDialog(null);
            String selectedImagefilePath = selectedImage.getAbsolutePath();

            if (selectedImage != null) {

                Image image = new Image(new FileInputStream(selectedImage));
                vehicleImg.setImage(image);
            } else {
                System.out.println("The file is not valid..!!");
            }

            vehicleImage = ImgaeToByteArray(selectedImagefilePath);
        }
    }

    private void addCar() throws SQLException {
        dbcon = new DBConnect();
        connection = dbcon.connectToDB();

        query = "Insert into Vehicle values(?,?,?,?,?,?,?,?)";

        preparedStatement = connection.prepareStatement(query);

        String category = categoryBox.getValue();
        preparedStatement.setString(1, category);

        if (category.equalsIgnoreCase("Micro-Bus")) {
            preparedStatement.setInt(2, 12);
        } else if (category.equalsIgnoreCase("CAR")) {
            preparedStatement.setInt(2, 5);
        } else if (category.equalsIgnoreCase("BIKE")) {
            preparedStatement.setInt(2, 2);
        }
        preparedStatement.setString(3, modelBox.getText());
        preparedStatement.setString(4, engineBox.getText());
        preparedStatement.setString(5, regBox.getText());

        preparedStatement.setString(6, priceBox.getText());

        preparedStatement.setString(7, "Y");

        preparedStatement.setBytes(8, vehicleImage);

        preparedStatement.execute();

        JOptionPane.showMessageDialog(null, "New Vehicle has been added.");

    }

    private boolean conditionMeetUp() throws SQLException {

        if (categoryBox.getValue().equals(" ")) {
            alerttxt.setText("Please Select a category.");
            categoryBox.requestFocus();
        } else if (engineBox.getText().equals(" ")) {
            alerttxt.setText("Engine Number cannot be empty.");
            engineBox.requestFocus();
        } else if (priceBox.getText().equals(" ")) {
            alerttxt.setText("Please set a price for the vehicle at daily based.");
            priceBox.requestFocus();
        } else if (modelBox.getText().equals(" ")) {
            alerttxt.setText("Please provide Vheicle model.");
            modelBox.requestFocus();
        } else if (regBox.getText().equals(" ")) {
            alerttxt.setText("Please provide the registration number.");
            regBox.requestFocus();
        } else if (isAlreadyRegistered()) {
            alerttxt.setText("The vehicle of this engine number is already been register");
            JOptionPane.showMessageDialog(null, "There is already a registerd vehicle with this engine number.");
            engineBox.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    private boolean isAlreadyRegistered() throws SQLException {
        ResultSet rs;
        dbcon = new DBConnect();
        connection = dbcon.connectToDB();

        String query = "select Vehicle_EngNo from Vehicle WHERE Vehicle_EngNo= '" + engineBox.getText() + "'";
        System.out.println("checking is there any duplciate customer");

        java.sql.Statement stmt = connection.createStatement();
        rs = stmt.executeQuery(query);

        while (rs.next()) {
            return true;
        }

        rs.close();
        dbcon.disconnectFromDB(stmt, connection);
        return false;
    }

    private byte[] ImgaeToByteArray(String selectedImagefilePath) throws FileNotFoundException, IOException {
        File file = new File(selectedImagefilePath);
        FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            //Writes to this byte array output stream
            bos.write(buf, 0, readNum);
            System.out.println("read " + readNum + " bytes,");
        }

        return bos.toByteArray();
    }

    private void loadData() {
        CategoryList.removeAll(CategoryList);
        String a = "MICRO-BUS";
        String b = "CAR";
        String c = "BIKE";
        CategoryList.addAll(a, b, c);
        categoryBox.getItems().addAll(CategoryList);
    }

}

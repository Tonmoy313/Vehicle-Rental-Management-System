/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

//import com.sun.org.apache.xml.internal.security.c14n.implementations.NameSpaceSymbTable;
import DbConnection.DBConnect;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import clss.CarList;
import clss.rentCar;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
//import com.sun.org.apache.xml.internal.security.c14n.implementations.NameSpaceSymbTable;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class RentCarPgController implements Initializable {

    /**
     * Initializes the controller class.
     */
    // private TableView myTable;
    @FXML
    private Button savebtn;
    @FXML
    private TextField advanceTf;
    @FXML
    private Text customerNameTitle;

    @FXML
    private DatePicker returnDtf;
    @FXML
    private DatePicker rentDtf;
    @FXML
    private Button addVehicleBtn;
    @FXML
    private ImageView vehiclPic;
    @FXML
    private Text totalPriceBox;
    @FXML
    private Button calculateBtn;
    @FXML
    private Text alertbox;
    @FXML
    private TextField searchBox;

    @FXML
    private TableView<CarList> carTable;
    @FXML
    private TableColumn<CarList, String> categoryCol;
    @FXML
    private TableColumn<CarList, Integer> seatCol;
    @FXML
    private TableColumn<CarList, String> carregCol;
    @FXML
    private TableColumn<CarList, String> enginCol;
    @FXML
    private TableColumn<CarList, String> modelCol;
    @FXML
    private TableColumn<CarList, String> availabilityCol;
    @FXML
    private TableColumn<CarList, BigDecimal> priceCol;

    //observalble list to store data
    private ObservableList<CarList> obs_CarList = FXCollections.observableArrayList();

    //for connection
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    //for class
    DBConnect dbcon;
    CarList car = null;
    rentCar rentC = null;

    //for customer datapass
    String passedCustomerName;
    int passedCustomerID = 0;

    //selected car from table
    String selectedVehicleEng, selectedVehicleavailable, selectedVehicleRegs, selectedVehicleModel;
    BigDecimal selectedVehiclePrice = null;
    BigDecimal totalPrice = null;
    int selectedVehicleId = 0;
    int days;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carLoadData();
        if (passedCustomerID == 0) {
            customerNameTitle.setText("");
        }
        //restrictDatePicker(rentDtf.getValue(),LocalDate.MAX,returnDtf);
    }

    public void restrictDatePicker(LocalDate minDate, LocalDate maxDate, DatePicker dp) {
//        minDate = LocalDate.of(1989, 4, 16); //get joining since date 
//        maxDate = LocalDate.now();

        dp.setDayCellFactory(d
                -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
            }
        });

    }

    private void carLoadData() {

        categoryCol.setCellValueFactory(new PropertyValueFactory<>("CATEGORY"));
        seatCol.setCellValueFactory(new PropertyValueFactory<>("SEAT"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("MODEL"));
        enginCol.setCellValueFactory(new PropertyValueFactory<>("ENGINE_NO"));
        carregCol.setCellValueFactory(new PropertyValueFactory<>("VEHICLE_REG"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("AVAILABILITY"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("PRICE"));

        carrefreshTable();

        carTable.setItems(obs_CarList);
    }

    private void carrefreshTable() {

        try {
            obs_CarList.clear();
            dbcon = new DBConnect();
            connection = dbcon.connectToDB();
            query = "select Category,Wheel,Vehicle_Model,Vehicle_EngNo,Vehicle_RegNo, Vehicle_Price ,Availabilty from Vehicle ";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                obs_CarList.add(new CarList(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getBigDecimal(6),
                        resultSet.getString(7)));

                carTable.setItems(obs_CarList);

            }

        } catch (SQLException ex) {
            Logger.getLogger(RentCarPgController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void allClear() throws FileNotFoundException {
        rentDtf.setValue(null);
        returnDtf.setValue(null);
        advanceTf.setText(null);
        searchBox.setText(null);
        alertbox.setText(null);
        totalPriceBox.setText(null);
        File deafaultImageFile = new File("C:\\Users\\HP\\Documents\\NetBeansProjects\\Vehicle_Rental_Mangment\\src\\image\\vehicle\\caradd1.png");
        Image defaultImage = new Image(new FileInputStream(deafaultImageFile));
        vehiclPic.setImage(defaultImage);

    }

    private boolean condiitonMeet() {

        if (rentDtf.getValue() == null) {
            alertbox.setText("Rent date can cannot be blank.");
            rentDtf.requestFocus();
            return false;
        } else if (returnDtf.getValue() == null) {

            alertbox.setText("Returned date can cannot be blank.");
            returnDtf.requestFocus();
            return false;
        } else if (selectedVehiclePrice == null) {
            JOptionPane.showMessageDialog(null, "Please select a vehicle");
            return false;
        }
        return true;
    }

    private boolean condiitonMeetOfBooked() {

        if (rentDtf.getValue() == null) {
            alertbox.setText("Rent date can cannot be blank.");
            rentDtf.requestFocus();
            return false;
        } else if (returnDtf.getValue() == null) {

            alertbox.setText("Returned date can cannot be blank.");
            returnDtf.requestFocus();
            return false;
        } else if (passedCustomerName == null && passedCustomerID == 0) {
            alertbox.setText("Registered the customer \nSelect the customer then click NEXT, \nu will show the name on the above");
            JOptionPane.showMessageDialog(null, "The customer has not been register please try again. ");
            customerNameTitle.requestFocus();
            return false;
        } else if (selectedVehicleavailable.equals("N")) {
            alertbox.setText("Srry, the selected vehicle is not available.");
            JOptionPane.showMessageDialog(null, "The vehicle is currently unavailable. Please try again. ");
            customerNameTitle.requestFocus();
            return false;
        } else if (selectedVehiclePrice == null) {
            JOptionPane.showMessageDialog(null, "Please select a vehicle");
            return false;
        }
        return true;
    }

    @FXML
    private void onClkBtn(ActionEvent event) throws IOException, SQLException {
        if (event.getSource() == calculateBtn && condiitonMeet()) {

            calculate();

        } else if (event.getSource() == savebtn && condiitonMeetOfBooked()) {

            calculate();

            dbcon = new DBConnect();
            connection = dbcon.connectToDB();
            query = "Insert into Payment(Netprice,AdvanceTk) values(?,?)\n"
                    + "insert into BookedCar(Booked_Date,Arrival_Date,CustomerId,VehicleId,PaymentId)\n"
                    + "values (?,?,?,?,IDENT_CURRENT( 'Payment'))\n"
                    + "UPDATE cr SET cr.Availabilty='N' FROM BookedCar bkdcr, Vehicle cr\n"
                    + "WHERE bkdcr.VehicleId =cr.VehicleId and BookedCarId=IDENT_CURRENT( 'BookedCar')";
            preparedStatement = connection.prepareStatement(query);

            if (days == 0) {
                preparedStatement.setBigDecimal(1, selectedVehiclePrice);
            } else {
                preparedStatement.setBigDecimal(1, totalPrice);

            }
            //String text = totalPriceBox.getText();
            //double doblevar=Double.parseDouble(text);
            //BigDecimal totalPrice=BigDecimal.valueOf(doblevar);
            //preparedStatement.setBigDecimal(1, );

            String text2 = advanceTf.getText();
            double doblevar2 = Double.parseDouble(text2);
            BigDecimal advanceTk = BigDecimal.valueOf(doblevar2);
            preparedStatement.setBigDecimal(2, advanceTk);

            LocalDate localDate = rentDtf.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            java.sql.Date sqlrentDate = new java.sql.Date(date.getTime());

            LocalDate localDate1 = returnDtf.getValue();
            Instant instant1 = Instant.from(localDate1.atStartOfDay(ZoneId.systemDefault()));
            Date date1 = Date.from(instant1);
            java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());
            // java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(rentDtf.getDayCellFactory());
            preparedStatement.setDate(3, sqlrentDate);
            preparedStatement.setDate(4, sqlDate1);
            preparedStatement.setInt(5, passedCustomerID);
            preparedStatement.setInt(6, selectedVehicleId);

            preparedStatement.execute();

            JOptionPane.showMessageDialog(null, "The car has been booked.");
            allClear();

        } else if (event.getSource() == addVehicleBtn) {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/addVehicle.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) addVehicleBtn.getScene().getWindow();
            stage.close();
            stage.setScene(scene);
            stage.setTitle("Car registration");
            stage.getIcons().add(new Image("/image/logo.png"));
            stage.show();

        }
    }

    private void getPicFrmDb(String carEng) throws SQLException, IOException {
        dbcon = new DBConnect();
        connection = dbcon.connectToDB();

        String Picquery = "select VehicleId,vehicle_Pic from Vehicle where Vehicle_EngNo=?";
        preparedStatement = connection.prepareStatement(Picquery);
        preparedStatement.setString(1, carEng);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            selectedVehicleId = resultSet.getInt(1);
            InputStream is = resultSet.getBinaryStream(2);
            BufferedImage bg = ImageIO.read(is);

            if (bg != null) {
                Image image = SwingFXUtils.toFXImage(bg, null);
                vehiclPic.setImage(image);
            }
        }
        resultSet.close();
        dbcon.disconnectFromDB(preparedStatement, connection);;
    }

    @FXML
    private void onRowClkCrTbl(MouseEvent event) throws SQLException, IOException {
        obs_CarList = carTable.getSelectionModel().getSelectedItems();

        selectedVehicleEng = obs_CarList.get(0).getENGINE_NO();
        selectedVehicleRegs = obs_CarList.get(0).getVEHICLE_REG();
        selectedVehiclePrice = obs_CarList.get(0).getPRICE();
        selectedVehicleModel = obs_CarList.get(0).getMODEL();
        selectedVehicleavailable = obs_CarList.get(0).getAVAILABILITY();
        if (selectedVehicleavailable.equals("N")) {
            alertbox.setText("Sorry, the selected vehicle is not available.");
        }
        totalPriceBox.setText(selectedVehiclePrice.toString() + "TK");
        System.out.println("Row is clicked and selected vhicle AVAILABLE:" + selectedVehicleavailable + " and selected vhicle Name:" + selectedVehicleModel);

        getPicFrmDb(selectedVehicleEng);

    }

    void getsutomerIdFromRtnPg(int customerId, String customerName) {
        System.out.println("CusrtomerId:" + customerId);
        System.out.println("customerName:" + customerName);
        passedCustomerID = customerId;
        passedCustomerName = customerName;
        customerNameTitle.setText(passedCustomerName.toUpperCase());

    }

    private void calculate() {
        Period diff = Period.between(rentDtf.getValue(), returnDtf.getValue());
        System.out.println("rent date: " + rentDtf.getValue() + "retrun date: " + returnDtf.getValue() + "The diff between: " + diff);
        days = diff.getDays() + diff.getMonths() * 30 + diff.getYears() * 365;
        totalPrice = BigDecimal.valueOf(days).multiply(selectedVehiclePrice);
        if (days == 0) {
            totalPriceBox.setText(selectedVehiclePrice.toString() + " TK");

        } else {
            totalPriceBox.setText(totalPrice.toString() + "  TK");

        }
    }

}

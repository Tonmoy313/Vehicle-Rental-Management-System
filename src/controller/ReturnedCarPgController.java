/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DbConnection.DBConnect;
import clss.ReturnedCar;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class ReturnedCarPgController implements Initializable {

    @FXML
    private ImageView searchbtn;

    @FXML
    private Button returncarbtn;
    @FXML
    private TextField searchBox;

    @FXML
    private TableView<ReturnedCar> returnedCarTbl;
    @FXML
    private TableColumn<ReturnedCar, String> csutomerNameCol;
    @FXML
    private TableColumn<ReturnedCar, String> carCol;
    @FXML
    private TableColumn<ReturnedCar, String> bookedDateCol;
    @FXML
    private TableColumn<ReturnedCar, String> retrnedDateCol;
    @FXML
    private TableColumn<ReturnedCar, String> arrivalDateCol;
    @FXML
    private TableColumn<ReturnedCar, String> lateCol;
    @FXML
    private AnchorPane returnedCarPane;
    Date var;
    @FXML
    private Text alreadyreturnedtxt;
    @FXML
    private Text todaystimetxt;
    @FXML
    private Text customerNameBox;
    @FXML
    private TextField customerPhoneBox;
    @FXML
    private TextField customerLicenceBox;
    @FXML
    private Text carRegBox;
    @FXML
    private TextField bookeddatebox;
    @FXML
    private TextField returneddatebox;
    @FXML
    private TextField arrivaldatebox;
    @FXML
    private TextField latebox;
    @FXML
    private Text upcomingtxt;
    @FXML
    private Button showallbtn;

    //for connection
    String query = null;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;
    LocalDate currentdate, prevdate;

    //for table
    private ObservableList<ReturnedCar> obsrvbllst_rtncr = FXCollections.observableArrayList();
    private final ObservableList<ReturnedCar> obsrvbl_rtncr = FXCollections.observableArrayList();

    //for clss
    DBConnect dbcon;
    //ReturnedCar rtncr = null;
    PaymentPgController passRtnId = null;

    String customerName, carRegNo, availabilty;
    Date bookedDate, arrivalDate, rtncarDate;
    int BookedcarId, late;
    @FXML
    private Button paymentBtn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        todaystimetxt.setText(java.time.LocalDate.now().toString());
        ReturnTblLoadData();
    }

    private void ReturnTblLoadData() {
        csutomerNameCol.setCellValueFactory(new PropertyValueFactory<>("csutomerName"));
        carCol.setCellValueFactory(new PropertyValueFactory<>("carRegNo"));
        bookedDateCol.setCellValueFactory(new PropertyValueFactory<>("bookedDate"));
        arrivalDateCol.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        retrnedDateCol.setCellValueFactory(new PropertyValueFactory<>("retrnedDate"));
        lateCol.setCellValueFactory(new PropertyValueFactory<>("late"));
        refreshTable();
        returnedCarTbl.setItems(obsrvbllst_rtncr);
    }

    private void refreshTable() {

        try {
            obsrvbllst_rtncr.clear();

            dbcon = new DBConnect();
            con = dbcon.connectToDB();
            //for upcoming event
            query = "select cstmr.Name,cr.Vehicle_RegNo,bkdcr.Booked_date,bkdcr.Arrival_date,bkdcr.Returned_Date,bkdcr.late \n"
                    + "from BookedCar bkdcr\n"
                    + "inner join Customer cstmr on bkdcr.CustomerId=cstmr.CustomerId\n"
                    + "inner join Vehicle cr on bkdcr.VehicleId=cr.VehicleId\n"
                    + "inner join Payment pay on bkdcr.PaymentId=pay.PaymentId \n"
                    + "where bkdcr.Arrival_Date>CONVERT(date,getdate()) and pay.PaidStatus='Y'\n"
                    + "order by bkdcr.Arrival_Date";
            pst = con.prepareStatement(query);
            res = pst.executeQuery();

            while (res.next()) {
                obsrvbllst_rtncr.add(new ReturnedCar(
                        res.getString(1),
                        res.getString(2),
                        res.getDate(3),
                        res.getDate(4),
                        res.getDate(5),
                        res.getInt(6)));

                returnedCarTbl.setItems(obsrvbllst_rtncr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReturnedCarPgController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshTableHistory() {
        try {
            obsrvbl_rtncr.clear();
            dbcon = new DBConnect();
            con = dbcon.connectToDB();
            query = "select cstmr.Name,cr.Vehicle_RegNo,rntcr.Booked_date,rntcr.Arrival_date,rntcr.returned_date,rntcr.late\n"
                    + "from BookedCar as rntcr\n"
                    + "inner join Customer as cstmr on rntcr.CustomerId=cstmr.CustomerId\n"
                    + "inner join Vehicle as cr on rntcr.VehicleId=cr.VehicleId\n"
                    + "order by rntcr.Arrival_Date   ";
            pst = con.prepareStatement(query);
            res = pst.executeQuery();
            while (res.next()) {
                obsrvbl_rtncr.add(new ReturnedCar(
                        res.getString(1),
                        res.getString(2),
                        res.getDate(3),
                        res.getDate(4),
                        res.getDate(5),
                        res.getInt(6)));

                returnedCarTbl.setItems(obsrvbl_rtncr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReturnedCarPgController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onBtnClkAct(ActionEvent event) throws SQLException, IOException {
        if (event.getSource() == showallbtn) {
            upcomingtxt.setText("HISTORY :");
            refreshTableHistory();
        } else if (event.getSource() == returncarbtn && availabilty.matches("Y") && rtncarDate != null) {
            JOptionPane.showMessageDialog(null, "This car is already returned.");
        } else if (event.getSource() == returncarbtn && availabilty.matches("N")) {

            dbcon = new DBConnect();
            con = dbcon.connectToDB();

            String sql = "UPDATE bkdcr\n"
                    + "SET bkdcr.Returned_Date =?,bkdcr.late=?\n"
                    + "FROM BookedCar bkdcr, Vehicle cr\n"
                    + "WHERE bkdcr.VehicleId=cr.VehicleId\n"
                    + "and BookedCarId=?\n"
                    + "\n"
                    + "UPDATE cr\n"
                    + "SET cr.Availabilty='Y'\n"
                    + "FROM BookedCar bkdcr, Vehicle cr\n"
                    + "WHERE bkdcr.VehicleId=cr.VehicleId\n"
                    + "and BookedCarId=? ";
            pst = con.prepareStatement(sql);
            java.sql.Date sqldate = new java.sql.Date(rtncarDate.getTime());

            System.out.println("The data is:\n return date:" + sqldate);
            System.out.println(" late:" + late);
            System.out.println("Return Car id:" + BookedcarId);

            pst.setDate(1, sqldate);
            pst.setInt(2, late);
            pst.setInt(3, BookedcarId);
            pst.setInt(4, BookedcarId);
            pst.execute();

            JOptionPane.showMessageDialog(null, "Data has been updated.");

            refreshTableHistory();

        } else if (event.getSource() == paymentBtn) {
            if (rtncarDate == null) {
                JOptionPane.showMessageDialog(null, "The car has not been RETURNED yet.");
            } else {
                DataTransfer.var = BookedcarId;
                ((Node) event.getSource()).getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/paymentPg.fxml"));
                Scene scene = new Scene(root);

                Stage stage = (Stage) paymentBtn.getScene().getWindow();
                stage.close();
                stage.setScene(scene);
                stage.setTitle("Car registration");
                stage.getIcons().add(new Image("/image/logo.png"));
                stage.show();
                //FXMLLoader loader = FXMLLoader.load(getClass().getResource("/viewfxml/paymentPg.fxml"));
                // AnchorPane var=FXMLLoader.load(getClass().getResource("/viewfxml/paymentPg.fxml"));
                // returnedCarPane.getChildren().setAll(loader);

                //Scene scene = new Scene(returnedCarPane);
                //passRtnId.
                //passRtnId = loader.getController();
                // passRtnId.getRtnIdFromRtnPg(BookedcarId);
                /*
                ((Node) event.getSource()).getScene().getWindow().hide();
                Stage primaryStage = new Stage();
                
                FXMLLoader loader = FXMLLoader.load(getClass().getResource("/viewfxml/paymentPg.fxml"));
                //AnchorPane childanchorpane = loader.load();
                Parent root = loader.load();
                //Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/paymentPg.fxml"));
                Scene src = new Scene(root);

                //PaymentPgController passRtnId = loader.getController();
               // passRtnId.getChildren().setAll(root);
                //passRtnId.getChildren().setAll(childanchorpane);
               passRtnId = loader.getController();
               passRtnId.getRtnIdFromRtnPg(BookedcarId);

                primaryStage.setTitle("car rental mangment system");
                primaryStage.setScene(src);
                primaryStage.getIcons().add(new Image("/image/logo.png"));
                primaryStage.show();*/
            }
            //Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/paymentPg.fxml"));
            /* FXMLLoader loader = FXMLLoader.load(getClass().getResource("/viewfxml/paymentPg.fxml"));
            Parent root = (Parent)loader.load();
            Scene scene = new Scene(root);

            PaymentPgController passRtnId = loader.getController();
            passRtnId.getRtnIdFromRtnPg(BookedcarId);
            
            Stage stage = (Stage) paymentBtn.getScene().getWindow();
            stage.close();
            //Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Car registration");
            stage.getIcons().add(new Image("/image/logo.png"));
            stage.show();
             */
            //JOptionPane.showMessageDialog(null, "Database is connected successfully");

            // Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/Home.fxml"));
            //        Scene scene = new Scene(root);
            //      stage.setScene(scene);
            //              stage.show();
        }

    }

    @FXML
    private void onSrchBtnClkAct(MouseEvent event) {
    }

    @FXML
    private void onRowClk(MouseEvent event) throws SQLException {

        obsrvbllst_rtncr = returnedCarTbl.getSelectionModel().getSelectedItems();

        customerName = obsrvbllst_rtncr.get(0).getCsutomerName();
        carRegNo = obsrvbllst_rtncr.get(0).getCarRegNo();
        bookedDate = obsrvbllst_rtncr.get(0).getBookedDate();
        arrivalDate = obsrvbllst_rtncr.get(0).getArrivalDate();

        customerNameBox.setText(customerName);
        carRegBox.setText(carRegNo);
        bookeddatebox.setText(bookedDate.toString());
        arrivaldatebox.setText(arrivalDate.toString());

        //for rtn car id;
        BookedcarId = BookedcarId();
        System.out.println("The selected Return car id is: " + BookedcarId);
        prevdate = new java.sql.Date(arrivalDate.getTime()).toLocalDate();
        rtncarDate = obsrvbllst_rtncr.get(0).getRetrnedDate();

        if (rtncarDate == null) {

            currentdate = java.time.LocalDate.now();
            returneddatebox.setText(currentdate.toString());
            rtncarDate = Date.from(currentdate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            System.out.println("Currentdate:" + currentdate);
            System.out.println("Previous date:" + prevdate);

            if (prevdate.isAfter(currentdate) || currentdate.isEqual(prevdate)) {
                late = 0;
                latebox.setText("0 Days");
            } else {
                Period diff = Period.between(prevdate, currentdate);
                late = diff.getDays() + diff.getMonths() * 30 + diff.getYears() * 365;
                latebox.setText(Integer.toString(late) + " Days");
                alreadyreturnedtxt.setText(" ");
            }

        } else {
            returneddatebox.setText(obsrvbllst_rtncr.get(0).getRetrnedDate().toString() + " Day");
            latebox.setText(Integer.toString(obsrvbllst_rtncr.get(0).getLate()));
            alreadyreturnedtxt.setText("***This car is already RETURNED***");
        }

        ///for phoneno,licence,availabilty via returncarid
        /* try {
            dbcon = new DBConnect();
            con = dbcon.connectToDB();
            String sql = "select cstmr.PhoneNo,cstmr.Cstmr_Licence,cr.Availabilty  \n"
                    + "from ReturnedCar as rtncr\n"
                    + "inner join Customer as cstmr on rtncr.CustomerId=cstmr.CustomerId\n"
                    + "inner join Cars as cr on rtncr.CarId=cr.CarId\n"
                    + "where rtncr.ReturnedCarId= ? ";
            pst = con.prepareStatement(sql);
            pst.setInt(1, BookedcarId);
            res = pst.executeQuery();

            while (res.next()) {
                customerPhoneBox.setText(res.getString(1));
                customerLicenceBox.setText(res.getString(2));
                availabilty = (res.getString(3));
                //  System.out.println("phoneno:" + res.getString(1));
                //System.out.println("cstmrlicence no:" + res.getString(2));
                //System.out.println("availability:" + res.getInt(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReturnedCarPgController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    int BookedcarId() throws SQLException {
        int id = 0;
        dbcon = new DBConnect();
        con = dbcon.connectToDB();
        String sql = "select bkdcr.BookedCarId,cstmr.PhoneNo,cstmr.Cstmr_Licence,cr.Availabilty\n"
                + "from BookedCar as bkdcr\n"
                + "inner join Customer as cstmr on bkdcr.CustomerId=cstmr.CustomerId\n"
                + "inner join Vehicle as cr on bkdcr.VehicleId=cr.VehicleId\n"
                + "where cstmr.Name=? and cr.Vehicle_RegNo=? and bkdcr.Booked_Date=? and bkdcr.Arrival_Date=? ";
        pst = con.prepareStatement(sql);
        pst.setString(1, customerName);
        pst.setString(2, carRegNo);
        pst.setDate(3, (java.sql.Date) bookedDate);
        pst.setDate(4, (java.sql.Date) arrivalDate);
        res = pst.executeQuery();

        while (res.next()) {
            id = res.getInt(1);
            customerPhoneBox.setText(res.getString(2));
            customerLicenceBox.setText(res.getString(3));
            availabilty = (res.getString(4));
            System.out.println("BookedcarId:" + id);
        }
        return id;
    }
}

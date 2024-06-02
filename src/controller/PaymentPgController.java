/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DbConnection.DBConnect;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class PaymentPgController implements Initializable {

    @FXML
    private Text totalBox;
    @FXML
    private Button cancelBtn;
    @FXML
    private ImageView imgIcon;
    @FXML
    private Button payBtn;
    @FXML
    private Text lateDayBox;
    @FXML
    private Text nameBox;
    @FXML
    private Text phoneNoBox;
    @FXML
    private Text carModelBox;
    @FXML
    private Text carRegBox;
    @FXML
    private Text bookBox;
    @FXML
    private Text arriveBox;
    @FXML
    private Text returnBox;
    @FXML
    private Text lateBox;
    @FXML
    private Text advanceBox;
    @FXML
    private Text successfulTxt;
    @FXML
    private Text dueBox;
    @FXML
    private Text carPriceBox;

    String query = null;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;
    DBConnect dbcon;
    ReturnedCarPgController rtncar;

    String name, phoneNo, carModel, carReg, paidStatus;
    Date bookedDate, arrivalDate, returnedDate;
    BigDecimal carPrice;
    BigDecimal totalpay, finepay, dueTk, advanceTk, netPrice;
    int late = 0;
    int idOfRentCar;
    BigDecimal needToPayment;
    String receivedTk;
    BigDecimal latecalculation;
    @FXML
    private Text hasntReturnTxt;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idOfRentCar = DataTransfer.var;
        try {
            loadAllData();
            /*try {
            loadAllData();
            } catch (SQLException ex) {
            Logger.getLogger(PaymentPgController.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        } catch (SQLException ex) {
            Logger.getLogger(PaymentPgController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void onClickBtn(ActionEvent event) throws IOException, SQLException {
        if (event.getSource() == payBtn) {
            System.out.println("Pay btn is clicked.");
            if (paidStatus.matches("Y")) {

                JOptionPane.showMessageDialog(null, "Payment is already DONE.");
            } else if (returnedDate == null) {
                JOptionPane.showMessageDialog(null, "The car has not been RETURNED.");
            } else if (paidStatus.matches("N")) {

                dbcon = new DBConnect();
                con = dbcon.connectToDB();
                query = "  update payment \n"
                        + "set payment.PaidStatus='Y',payment.Total=?,payment.fine=?\n"
                        + "from payment pay,BookedCar bkdcr\n"
                        + "where bkdcr.PaymentId=pay.PaymentId\n"
                        + "and bkdcr.BookedCarId=? ";
                pst = con.prepareStatement(query);

                pst.setBigDecimal(1, totalpay);
                pst.setBigDecimal(2, latecalculation);
                pst.setInt(3, idOfRentCar);
                pst.executeUpdate();

                imgIcon.setVisible(true);
                successfulTxt.setVisible(true);

                JOptionPane.showMessageDialog(null, "payment is UPDATED.");
            }
        } else if (event.getSource() == cancelBtn) {
            System.out.println("Cancel btn is clicked");
            Parent root = FXMLLoader.load(getClass().getResource("/viewfxml/Home.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.getIcons().add(new Image("/image/logo.png"));
            stage.show();
        }
    }

    private void loadAllData() throws SQLException {

        dbcon = new DBConnect();
        con = dbcon.connectToDB();
        query = "select cstmr.Name,cstmr.PhoneNo,cr.Vehicle_Model,cr.Vehicle_RegNo,cr.Vehicle_Price,\n"
                + "bkedcr.Booked_date,bkedcr.Arrival_date,\n"
                + "bkedcr.returned_date,bkedcr.late,\n"
                + "pay.Netprice,pay.AdvanceTk,pay.PaidStatus\n"
                + "from BookedCar as bkedcr\n"
                + "inner join Customer as cstmr on bkedcr.CustomerId=cstmr.CustomerId\n"
                + "inner join Vehicle as cr on bkedcr.VehicleId=cr.VehicleId\n"
                + "inner join Payment as pay on bkedcr.PaymentId=pay.PaymentId\n"
                + "where BookedCarId=?";
        pst = con.prepareStatement(query);
        pst.setInt(1, idOfRentCar);
        res = pst.executeQuery();
        while (res.next()) {

            name = res.getString(1);
            phoneNo = res.getString(2);

            carModel = res.getString(3);
            carReg = res.getString(4);
            carPrice = res.getBigDecimal(5);

            bookedDate = res.getDate(6);
            arrivalDate = res.getDate(7);

            returnedDate = res.getDate(8);
            late = res.getInt(9);

            netPrice = res.getBigDecimal(10);
            advanceTk = res.getBigDecimal(11);
            dueTk = netPrice.subtract(advanceTk);
            paidStatus = res.getString(12);

        }
        nameBox.setText(name);
        phoneNoBox.setText(phoneNo);

        carModelBox.setText(carModel);
        carRegBox.setText(carReg);
        carPriceBox.setText(carPrice.toString() + "TK");

        bookBox.setText(bookedDate.toString());
        arriveBox.setText(arrivalDate.toString());

        System.out.println(returnedDate);
        if (returnedDate == null) {
            returnBox.setText("-");
            lateDayBox.setText("-");
            lateBox.setText(" ");
            hasntReturnTxt.setVisible(true);
            payBtn.disarm();
        } else {
            returnBox.setText(returnedDate.toString());
            lateDayBox.setText(Integer.toString(late));
        }

        advanceBox.setText(advanceTk.toString() + "TK");
        dueBox.setText(dueTk.toString() + "TK");
        if (paidStatus.matches("Y")) {

            imgIcon.setVisible(true);
            successfulTxt.setVisible(true);
        }
        latecalculation = BigDecimal.valueOf(late).multiply(BigDecimal.valueOf(5000));
        needToPayment = latecalculation.add(dueTk);
        totalpay = needToPayment.add(advanceTk);
        lateBox.setText(latecalculation + "TK");
        totalBox.setText(needToPayment + "TK");

        dbcon.disconnectFromDB(pst, con);

    }

}

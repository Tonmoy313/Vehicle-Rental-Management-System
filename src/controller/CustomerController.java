/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DbConnection.DBConnect;
import clss.customerList;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class CustomerController implements Initializable {

    @FXML
    private TextField CustNameTf;
    @FXML
    private TextField CustPhnTf;
    @FXML
    private TextField nidtf;
    @FXML
    private TextField litf;
    @FXML
    private CheckBox Driverchk;
    @FXML
    private ImageView customerimg;
    @FXML
    private Button DltBtn;
    @FXML
    private Button SaveBtn;
    @FXML
    private TableView<customerList> customerTable;
    @FXML
    private TableColumn<customerList, String> custNamecol;
    @FXML
    private TableColumn<customerList, String> custPhncol;
    @FXML
    private TableColumn<customerList, String> custnidcol;
    @FXML
    private TableColumn<customerList, String> custlicol;
    @FXML
    private TableColumn<customerList, String> custdrivecol;

    @FXML
    private AnchorPane customerPgAnchrPane;
    @FXML
    private FontAwesomeIconView searchbtn;
    @FXML
    private Text alertbox;
    @FXML
    private TextField searchBox;
    @FXML
    private Button editBtn;
    @FXML
    private Button nextBtn;

    //for pic
    String filename;
    byte[] personImage = null;

    //for selectiong customer
    int selectedCustmrId = 0;
    String selctedCustomerName = null;

    //for customer tabl
    private final ObservableList<customerList> obs_custList = javafx.collections.FXCollections.observableArrayList();

    //for connection
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    //for class
    DBConnect dbcon;
    customerList customer;

    //for controller
    RentCarPgController RentCarPgController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        custListLoad();
        search();
        try {
            allClear();
            //refreshTable();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void custListLoad() {
        custNamecol.setCellValueFactory(new PropertyValueFactory<>("Customer_name"));
        custPhncol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        custnidcol.setCellValueFactory(new PropertyValueFactory<>("Nid"));
        custlicol.setCellValueFactory(new PropertyValueFactory<>("License"));
        custdrivecol.setCellValueFactory(new PropertyValueFactory<>("Driver_hire"));

        refreshTable();

        customerTable.setItems(obs_custList);
    }

    private void refreshTable() {
        try {
            obs_custList.clear();
            dbcon = new DBConnect();
            connection = dbcon.connectToDB();

            query = "select Name,PhoneNo,NID,Cstmr_Licence,DriverHire from Customer";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                obs_custList.add(new customerList(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)));

                customerTable.setItems(obs_custList);
            }
            resultSet.close();
            dbcon.disconnectFromDB(preparedStatement, connection);

        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isAlreadyRegistered() {
        ResultSet rs;
        dbcon = new DBConnect();
        connection = dbcon.connectToDB();

        String query = "select Name,PhoneNo,NID,Cstmr_Licence,DriverHire,customerPic  from Customer WHERE PhoneNo= '" + CustPhnTf.getText() + "'";
        System.out.println("checking is there any duplciate customer");
        try {
            java.sql.Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("In to the While Loop");

                CustNameTf.setText(rs.getString(1));
                CustPhnTf.setText(rs.getString(2));
                nidtf.setText(rs.getString(3));
                litf.setText(rs.getString(4));
                /*if(!rs.getString(5).equalsIgnoreCase())
                {
                } else {
                    Driverchk.isSelected();
                }*/

                return true;
            }

            rs.close();
            dbcon.disconnectFromDB(stmt, connection);;

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    private boolean conditionMeetUp() throws SQLException, FileNotFoundException {

        if (CustNameTf.getText().equals("")) {
            alertbox.setText("**Customer name can't be Empty**");
            JOptionPane.showMessageDialog(null, "Please, provide a valid Name & the name must be between 2 to 40 characters.");
            CustNameTf.requestFocus();
            return false;
        } else if (CustNameTf.getText().length() < 2 || CustNameTf.getText().length() > 40) {
            alertbox.setText("First name text field cannot be less than 2 and greator than 40 characters.");
            CustNameTf.requestFocus();
            return false;
        } else if (CustPhnTf.getText().equals("")) {
            alertbox.setText("**Customer phone number can't be Empty**");
            CustPhnTf.requestFocus();
            return false;
        } else if (!CustPhnTf.getText().startsWith("01") || CustPhnTf.getText().length() != 11 || !CustPhnTf.getText().matches(("-?([0-9]\\d*)"))) {
            alertbox.setText("Phone number must be starts with 01, \nThe lenght must be 11 \nNeeded to be digit.");
            JOptionPane.showMessageDialog(null, "Please, provide a valid Phone Number ");
            CustPhnTf.requestFocus();
            return false;
        } else if (nidtf.getText().equals("") && nidtf.getText().length() < 10) {
            alertbox.setText("**Customer NID can't be Empty**");
            nidtf.requestFocus();
            return false;
        } else if (isAlreadyRegistered()) {
            alertbox.setText("The username is already taken by someone else.");
            JOptionPane.showMessageDialog(null, "There is already a registerd customer with this phone number.");
            CustPhnTf.requestFocus();
            return false;
        } /*else if (defaultImage()) {
            alertbox.setText("**customer pic didn't provided.**");
            JOptionPane.showMessageDialog(null, "Please provide a passport size image.");
            customerimg.requestFocus();
            return false;
        }  */ else {
            return true;
        }

    }

    private boolean defaultImage() throws FileNotFoundException {

        File deafaultImageFile = new File("C:\\Users\\HP\\Documents\\NetBeansProjects\\Vehicle_Rental_Mangment\\src\\image\\customer\\defaultUser.png");
        Image defaultImage = new Image(new FileInputStream(deafaultImageFile));
        // Image checkImage =new Image(new FileInputStream(customerimg.getImage());

        return true;
    }

    private boolean toNext() {
        System.out.println("passing to rentcarpg selectedCustmrId=" + selectedCustmrId + " & customer name:" + selctedCustomerName);

        if (selctedCustomerName == null) {
            JOptionPane.showMessageDialog(null, "Please select a customer first");
            return false;
        } else if (selectedCustmrId == 0) {
            JOptionPane.showMessageDialog(null, "Please select a customer first");
            return false;
        }
        return true;
    }

    @FXML
    private void onClkBtn(ActionEvent event) throws SQLException, FileNotFoundException, IOException {

        if (event.getSource() == SaveBtn && conditionMeetUp()) {

            //obs_custList.clear();   
            dbcon = new DBConnect();
            connection = dbcon.connectToDB();

            query = "insert into Customer values(?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, CustNameTf.getText());
            preparedStatement.setString(2, CustPhnTf.getText());
            preparedStatement.setString(3, nidtf.getText());
            if (litf.getText() == null) {
                Driverchk.isSelected();
            }
            preparedStatement.setString(4, litf.getText());

            if (Driverchk.isSelected()) {
                System.out.println(Driverchk.isSelected());
                preparedStatement.setString(5, "Y");

            } else {
                preparedStatement.setString(5, "N");

            }
            if (personImage == null) {
                System.out.println("Default Image has been stored.");
  
                File deafaultImageFile = new File("C:\\Users\\HP\\Documents\\NetBeansProjects\\Vehicle_Rental_Mangment\\src\\image\\customer\\defaultUser.png");
                String selectedImagefilePath = deafaultImageFile.getAbsolutePath();
                personImage = ImgaeToByteArray(selectedImagefilePath);
                preparedStatement.setBytes(6, personImage);
                
            } else {
                preparedStatement.setBytes(6, personImage);

            }
            //preparedStatement.setBytes(6, personImage);
            preparedStatement.execute();

            JOptionPane.showMessageDialog(null, "Customer has been registered.");

            resultSet.close();
            dbcon.disconnectFromDB(preparedStatement, connection);

            allClear();
            refreshTable();

        } else if (event.getSource() == nextBtn && toNext()) {
            System.out.println("passing to rentcarpg selectedCustmrId=" + selectedCustmrId + " & customer name:" + selctedCustomerName);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewfxml/RentCarPg.fxml"));
            AnchorPane childanchorpane = loader.load();
            customerPgAnchrPane.getChildren().setAll(childanchorpane);

            RentCarPgController = loader.getController();
            RentCarPgController.getsutomerIdFromRtnPg(selectedCustmrId, selctedCustomerName);

        } else if (event.getSource() == DltBtn) {

            dbcon = new DBConnect();
            connection = dbcon.connectToDB();

            customerList cusdlt = (customerList) customerTable.getSelectionModel().getSelectedItem();
            String selectedCustomer = cusdlt.getPhone();
            String query = "delete from Customer where PhoneNo=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedCustomer);
            preparedStatement.execute();

            dbcon.disconnectFromDB(preparedStatement, connection);
            resultSet.close();

            allClear();
            refreshTable();

        } else if (event.getSource() == editBtn) {
            //CustNameTf.cancelEdit();

            dbcon = new DBConnect();
            connection = dbcon.connectToDB();

            System.out.println("the Customer id is:" + selectedCustmrId);
            String updateQuery = "update Customer set Name=?, PhoneNo=?,NID=?, Cstmr_Licence=?,DriverHire=?,customerPic=? where CustomerId=? ";
            preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, CustNameTf.getText());
            preparedStatement.setString(2, CustPhnTf.getText());
            preparedStatement.setString(3, nidtf.getText());
            if (litf.getText() == null) {
                Driverchk.isSelected();
            }
            preparedStatement.setString(4, litf.getText());

            if (Driverchk.isSelected()) {
                System.out.println(Driverchk.isSelected());
                preparedStatement.setString(5, "Y");

            } else {
                preparedStatement.setString(5, "N");

            }
            preparedStatement.setBytes(6, personImage);

            preparedStatement.setInt(7, selectedCustmrId);

            preparedStatement.execute();

            dbcon.disconnectFromDB(preparedStatement, connection);
            resultSet.close();

            allClear();
            refreshTable();
        }
    }

    @FXML
    private void onimageclk(MouseEvent event) throws FileNotFoundException, IOException {
        //fc.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("PDF Files","*.pdf"));

        FileChooser imagechoser = new FileChooser();
        imagechoser.setInitialDirectory(new File("C:\\Users\\HP\\Documents\\NetBeansProjects\\Vehicle_Rental_Mangment\\src\\image\\customer"));

        File selectedImage = imagechoser.showOpenDialog(null);
        String selectedImagefilePath = selectedImage.getAbsolutePath();

        if (selectedImage != null) {

            Image image = new Image(new FileInputStream(selectedImage));
            customerimg.setImage(image);
        } else {
            System.out.println("The file is not valid..!!");
        }

        personImage = ImgaeToByteArray(selectedImagefilePath);

    }

    private void getPicFrmDb(String selectName) throws SQLException, IOException {
        dbcon = new DBConnect();
        connection = dbcon.connectToDB();

        String Picquery = "select CustomerId,customerPic from Customer where Name=?";
        preparedStatement = connection.prepareStatement(Picquery);
        preparedStatement.setString(1, selectName);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            selectedCustmrId = resultSet.getInt(1);
            InputStream is = resultSet.getBinaryStream(2);
            BufferedImage bg = ImageIO.read(is);

            if (bg != null) {
                Image image = SwingFXUtils.toFXImage(bg, null);
                customerimg.setImage(image);
            }
        }
        resultSet.close();
        dbcon.disconnectFromDB(preparedStatement, connection);;

    }

    @FXML
    private void onRowClk(MouseEvent event) throws SQLException, IOException {

        customerList cusdlt = (customerList) customerTable.getSelectionModel().getSelectedItem();

        selctedCustomerName = cusdlt.getCustomer_name();

        CustNameTf.setText(selctedCustomerName);
        CustPhnTf.setText(cusdlt.getPhone());
        nidtf.setText(cusdlt.getNid());
        litf.setText(cusdlt.getLicense());

        if (cusdlt.getDriver_hire().matches("Y")) {
            Driverchk.setSelected(true);
        } else {
            Driverchk.setSelected(false);
        }

        getPicFrmDb(cusdlt.getCustomer_name());

    }

    void search() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<clss.customerList> filteredData = new FilteredList<>(obs_custList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getCustomer_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (customer.getNid().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (customer.getLicense().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(customer.getPhone()).contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false; // Does not match.
                }
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<clss.customerList> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(customerTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        customerTable.setItems(sortedData);

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

    private void allClear() throws FileNotFoundException {

        customer = null;
        CustNameTf.setText("");
        CustPhnTf.setText("");
        nidtf.setText("");
        litf.setText("");
        Driverchk.setSelected(false);
        searchBox.setText("");
        File deafaultImageFile = new File("C:\\Users\\HP\\Documents\\NetBeansProjects\\Vehicle_Rental_Mangment\\src\\image\\customer\\defaultUser.png");
        Image defaultImage = new Image(new FileInputStream(deafaultImageFile));
        customerimg.setImage(defaultImage);
    }

}

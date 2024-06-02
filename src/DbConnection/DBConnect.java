/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class DBConnect {
    private Statement stmt = null;
    private Connection connection;

    public Connection connectToDB() {
        System.out.println("DB connecting..............");

        try {
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String Url = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName= vehicle_rental_mangment_system";

            connection = DriverManager.getConnection(Url);
            System.out.println("Connect: Connection:" + connection);
            stmt = connection.createStatement();
            return connection;

        } catch (ClassNotFoundException ex) {
            System.out.println("class was not found");
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("url can't execute.");
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void disconnectFromDB(Statement stmt, Connection connection) {

        try {

            System.out.println("Disconnect: connection: " + connection);
            System.out.println("Disconnect: statement:" + stmt);
            // if (rs != null){rs.close();}
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Unable to disconnect");
        }
    }

}

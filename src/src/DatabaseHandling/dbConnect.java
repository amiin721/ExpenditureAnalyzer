package src.DatabaseHandling;

import java.sql.*;
import javax.swing.JOptionPane;
import dbDetails.dbLoginDetails;

public class dbConnect {

    public static Connection con;

    static {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            String url = dbLoginDetails.geturl();
            String username = dbLoginDetails.getusername();
            String password = dbLoginDetails.getpassword();
            
            con = DriverManager.getConnection(url,username,password);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public static void closecon() {
        try {
            con.close();
        } catch (Exception ex) {

        }
    }
}

package src.DatabaseHandling;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Zap
 */
public class dbOperations {
    
    public Statement st = null;

    //Method to perform update query on the database
    public void updatefunc(String s) {
        try ( PreparedStatement ps = dbConnect.con.prepareStatement(s);) {

            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Method to perform search query on the database
    public ResultSet runquery(String s) throws SQLException {
        
        Statement st=null;
        ResultSet rs=null;
        
        try {

            st = dbConnect.con.createStatement();
            rs = st.executeQuery(s);
            return rs;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } 
        return null;
    }

    //Method to perform insert query on the database
    public void insertfunc(String s) {
        
        try(Statement st = dbConnect.con.createStatement();) {
            
            st.executeUpdate(s);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Method to perform delete query on the database
    public void deletefunc(String s) {
        
        try(Statement st = dbConnect.con.createStatement();) {
            
            st.executeUpdate(s);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
}

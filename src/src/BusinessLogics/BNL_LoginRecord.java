/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import src.FileHandling.fileOperations;
import src.DatabaseHandling.dbOperations;
import java.sql.*;

/**
 *
 * @author Zap
 */
public class BNL_LoginRecord {
    
    public String finaluser;

    public BNL_LoginRecord(String fuser) {
        //Fetching the current logged in username into the variable
        finaluser = fuser;
    }

    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();

    //Declaration for java swing fields used in this component
    public javax.swing.JTable recordtable;

    //Setters for all the java swing fields used in this component
    public void setrecordtable(javax.swing.JTable recordtable) {
        this.recordtable = recordtable;
    }

    //Method to fetch details from database and show them in the table
    public void showrecord() throws SQLException {
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) recordtable.getModel();
        int rc = dtm.getRowCount();
        
        //Emptying the table
        while (rc-- != 0) {
            dtm.removeRow(0);
        }
        
        String s = "select * from loginrecord where uid='" + finaluser + "' order by record desc";
        
        
        try {
            //Inserting values fetched into the table
            ResultSet rs = dbo.runquery(s);
            while (rs.next()) {
                Object o[] = {rs.getString("device"),rs.getString("ip"),rs.getString("record")};
                dtm.addRow(o);
            }
            
        } catch (Exception e) {

        }
    }

}

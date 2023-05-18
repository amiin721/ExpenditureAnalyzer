/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import src.DatabaseHandling.dbOperations;
import src.FileHandling.fileOperations;
import src.DatabaseHandling.dbConnect;

/**
 *
 * @author Zap
 */
public class BNL_Category {
    
    public String finaluser;

    public BNL_Category(String fuser) {
        //Fetching the current logged in username into the variable
        finaluser = fuser;
    }
    
    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();
    
    //Declaration for java swing fields used in this component
    public javax.swing.JTextField catinput;
    public javax.swing.JTable cattable;
    
    //Setters for all the java swing fields used in this component
    public void setcatinput(javax.swing.JTextField catinput) {
        this.catinput = catinput;
    }

    public void setcattable(javax.swing.JTable cattable) {
        this.cattable = cattable;
    }
    
    //Method to return the fetched logged in username
    public String setfinaluser() {
        return finaluser;
    }
    
    //Method to check for the SQL Injection
    public void injectioncheck() {
        JOptionPane.showMessageDialog(null, "IT WONT WORK");
        
        try {
            
            //Resetting the login flag in the database
            String s2 = "update login set flag = 0 where uid='" + finaluser + "'";
            dbo.updatefunc(s2);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        System.exit(0);
    }
     
    //Method to show the available categories for the current user in the table
    public void showcategorycat() {
        try {
            
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) cattable.getModel();
            int rc = dtm.getRowCount();
            
            //Emptying the table whenever the method is called
            while (rc-- != 0) {
                dtm.removeRow(0);
            }
            
            String s = "select * from categorydetails where uid='" + finaluser + "'";
            ResultSet rs = dbo.runquery(s);
            int sno = 0;
            
            //Inserting the category values in the table
            while (rs.next()) {
                String categ = rs.getString("category");
                Object o[] = {++sno, categ};
                dtm.addRow(o);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    //Method to insert the new category into the database and refreshing the table
    public void catinsert() {
        try {
            String cat = catinput.getText();
            
            if (cat.contains("'")) {
                //Checking for SQL Injections
                injectioncheck();
            }
            
            String s = "insert into categorydetails values('" + cat + "','" + finaluser + "')";
            
            if (!cat.equals("")) {
                
                //When the entered category value is not null
                dbo.st = dbConnect.con.createStatement();
                dbo.st.executeUpdate(s);
                
                //Category added successfully, and updating the table contents
                JOptionPane.showMessageDialog(null, "Category Added Successfully");
                catinput.setText("");
                showcategorycat();
                
            } else {
                JOptionPane.showMessageDialog(null, "Cannot Leave input field blank");
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
            
            //SQL Exception for duplicate entries/primary key violation
            JOptionPane.showMessageDialog(null, "Category already exists");
            catinput.setText("");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Inputs");
        }
    }

    //Method to delete the category by selected row
    public void catdelete() {
        
        int ri = cattable.getSelectedRow();
        
        if (ri != -1) {
            
            //When there is a selected row
            String cat = (String) cattable.getValueAt(ri, 1);
            String s = "delete from categorydetails where category='" + cat + "' and uid='" + finaluser + "'";
            
            try {
                
                int res = JOptionPane.showConfirmDialog(null, "Do you want to delete this category?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    dbo.deletefunc(s);
                    JOptionPane.showMessageDialog(null, "Category Deleted Successfully");
                    showcategorycat();
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            //When there is no row selected
            JOptionPane.showMessageDialog(null, "Please select a row before deleting");
        }
    }
}

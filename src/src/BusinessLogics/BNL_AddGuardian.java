/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import src.FileHandling.fileOperations;
import src.DatabaseHandling.dbOperations;
import java.sql.*;
import javax.swing.JOptionPane;
import src.DatabaseHandling.dbConnect;

/**
 *
 * @author Zap
 */
public class BNL_AddGuardian {
    
    public String finaluser;

    public BNL_AddGuardian(String fuser) {
        //Fetching the current logged in username into the variable
        finaluser = fuser;
    }

    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();

    //Declaration for java swing fields used in this component
    public javax.swing.JTextField guser;
    public javax.swing.JButton gbut;
    public javax.swing.JFrame jf;

    //Setters for all the java swing fields used in this component
    public void setguser(javax.swing.JTextField guser) {
        this.guser = guser;
    }

    public void setgbut(javax.swing.JButton gbut) {
        this.gbut = gbut;
    }

    public void setjf(javax.swing.JFrame jf) {
        this.jf = jf;
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

    //Method to set the entered username as their new guardian
    public void setguardian() {

        String usr = guser.getText();

        if (usr.contains("'")) {
            //Checking for SQL Injection
            injectioncheck();
        }

        if (usr.equals(finaluser)) {

            //When the user tries to set itself as a guardian
            JOptionPane.showMessageDialog(null, "You cannot set yourself as your guardian");
            guser.setText("");

        } else if (usr.equals("")) {

            JOptionPane.showMessageDialog(null, "Input Field is blank");

        } else {

            //flag is to check if username exists
            int flag = 0;

            try {

                String s3 = "select * from login";
                String s2 = "insert into guardian values('" + finaluser + "','" + usr + "')";

                ResultSet rs = dbo.runquery(s3);

                while (rs.next()) {

                    if (rs.getString("uid").equals(usr)) {
                        //If the enterd username is a valid username, it will set the flag
                        flag = 1;
                    }
                }

                if (flag == 1) {

                    //When the flag is set, the entered username is set as the logged in user's guardian
                    Statement st = dbConnect.con.createStatement();
                    st.executeUpdate(s2);
                    
                    JOptionPane.showMessageDialog(null, "Successfully set '" + usr + "' as your guardian.");
                    guser.setText("");

                } else {

                    //Flag is not set meaning, the entered username does not exist
                    JOptionPane.showMessageDialog(null, "This username does not exist");
                    guser.setText("");

                }

            } catch (java.sql.SQLIntegrityConstraintViolationException ex) {

                //SQL Exception for duplicate entries/primary key violation
                JOptionPane.showMessageDialog(null, "This username is already present as your guardian");
                guser.setText("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
    }
}

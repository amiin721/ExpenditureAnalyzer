/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import src.DatabaseHandling.dbOperations;
import src.FileHandling.fileOperations;

/**
 *
 * @author Zap
 */
public class BNL_DeleteAcc {
    
    public String finaluser;

    public BNL_DeleteAcc(String fuser) {
        //Fetching the current logged in username into the variable
        finaluser = fuser;
    }

    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();

    //Declaration for java swing fields used in this component
    public javax.swing.JTextField deleteuser;
    public javax.swing.JPasswordField deletepass;
    public javax.swing.JLabel notifdelete;
    public javax.swing.JButton deletebut;

    //Setters for all the java swing fields used in this component
    public void setdeleteuser(javax.swing.JTextField deleteuser) {
        this.deleteuser = deleteuser;
    }

    public void setdeletepass(javax.swing.JPasswordField deletepass) {
        this.deletepass = deletepass;
    }

    public void setnotifdelete(javax.swing.JLabel notifdelete) {
        this.notifdelete = notifdelete;
    }

    public void setdeletebut(javax.swing.JButton deletebut) {
        this.deletebut = deletebut;
    }

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

    //Method to Check if the user has entered correct details in order to delete account
    public void checkdelete() {
        
        String user1 = deleteuser.getText();
        String oldpass = deletepass.getText();
        
        if (user1.contains("'") || oldpass.contains("'")) {
            //Checking for SQL Injection
            injectioncheck();
        }
        
        try {
            if (!user1.equals("") && !oldpass.equals("")) {
                try {
                    
                    String s = "select * from login where uid='" + user1 + "' and pass='" + oldpass + "'";
                    ResultSet rs = dbo.runquery(s);
                    
                    if (rs.next()) {
                        
                        //When the entered details are correct, the delete button is enabled
                        deletebut.setEnabled(true);
                        deletepass.setEditable(false);
                        notifdelete.setText("");
                        
                    } else {
                        
                        //When the entered details are incorrect, the user is notified
                        notifdelete.setText("Invalid Credentials!");
                        deletepass.setText("");
                        
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                //When the input fields are empty
                JOptionPane.showMessageDialog(null, "Cannot leave fields empty!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Inputs");
        }
    }

    //Method to delete a user's account, after performing user account checking
    public void performdelete() {
        
        String user1 = deleteuser.getText();
        String oldpass = deletepass.getText();
        
        try {
            
            if (user1.contains("'") || oldpass.contains("'")) {
                //Checking for SQL Injection
                injectioncheck();
            }
            
            int res = JOptionPane.showConfirmDialog(null, "Do you want to delete this account?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                
                String s1 = "delete from expenditure where uid='" + user1 + "'";
                String s2 = "delete from login where uid='" + user1 + "'";
                String s3 = "delete from categorydetails where uid='" + user1 + "'";
                String s4 = "delete from loginrecord where uid='" + user1 + "'";
                String s5 = "delete from guardian where uid='" + user1 + "'";
                String s6 = "delete from guardian where gid='" + user1 + "'";
                
                try {
                    //Removing records from all tables with current username
                    dbo.deletefunc(s1);
                    dbo.deletefunc(s2);
                    dbo.deletefunc(s3);
                    dbo.deletefunc(s4);
                    dbo.deletefunc(s5);
                    dbo.deletefunc(s6);
                                        
                    //Deleting the saved login info in the file
                    fop.writezero();
                    
                    JOptionPane.showMessageDialog(null, "Account deleted!");
                    System.exit(0);
                } catch (Exception e) {
                    
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Inputs");
        }
    }
}

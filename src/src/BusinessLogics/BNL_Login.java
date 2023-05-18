/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import src.DatabaseHandling.dbOperations;
import src.GUI.GUI_Home;
import src.FileHandling.fileOperations;
import src.UserPass.userpass;

/**
 *
 * @author Zap
 */
public class BNL_Login {
    
    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();
    
    public String finaluser;

    //Declaration for java swing fields used in this component
    public javax.swing.JTextField userlogin;
    public javax.swing.JPasswordField passlogin;
    public javax.swing.JButton forgetbut;
    public javax.swing.JCheckBox remembercheck;
    public javax.swing.JFrame explogin;
    
    //Setters for all the java swing fields used in this component
    public void setuserlogin(javax.swing.JTextField userlogin) {
        this.userlogin = userlogin;
    }

    public void setpasslogin(javax.swing.JPasswordField passlogin) {
        this.passlogin = passlogin;
    }

    public void setforgetbut(javax.swing.JButton forgetbut) {
        this.forgetbut = forgetbut;
    }

    public void setremembercheck(javax.swing.JCheckBox remembercheck) {
        this.remembercheck = remembercheck;
    }

    public void setexplogin(javax.swing.JFrame explogin) {
        this.explogin = explogin;
    }
    
    public String setfinaluser() {
        return finaluser;
    }
    
    //Method to check for the SQL Injection
    public void injectioncheck() {
        JOptionPane.showMessageDialog(null, "IT WONT WORK");        
        System.exit(0);
    }
    
    //Method to set the input fields as the saved login details
    public void setremlogin() throws IOException {
                
        try(FileInputStream fis = new FileInputStream("login.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);) {
            
            int count = ois.readInt();
            userpass up = (userpass) ois.readObject();
            
            if (count == 1) {
                //Only works when there is a login credential saved in the file
                userlogin.setText(up.getUser());
                passlogin.setText(up.getPass());
                forgetbut.setEnabled(true);
                remembercheck.setVisible(false);
            }

        } catch (Exception e) {

        }
    }
    
    //Method to forget the login info, that is to erase the file contents
    public void forgetlogin() {
        fop.writezero();
        userlogin.setText("");
        passlogin.setText("");
    }

    //Method to save the login information in a file
    public void savelogin() {
        
        String user1 = userlogin.getText();
        String pass1 = passlogin.getText();

        int flag = 0;
        
        if (!user1.equals("") && !pass1.equals("")) {
            try {
                
                //when the input fields are not blank
                String s = "select * from login where uid='" + user1 + "' and pass='" + pass1 + "'";
                ResultSet rs = dbo.runquery(s);
                if (rs.next()) {
                    //Flag is set when the login details are correct
                    flag = 1;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            if (flag == 1) {
                //Only save in file when the login details are correct
                fop.writeone(user1, pass1);
            } else {
                //notify the user that the entered details are incorrect
                JOptionPane.showMessageDialog(null, "Incorrect credentials, cant save details");

            }
        }
    }

    //Main Method to perform the login operation
    public void mainlogin() {
        
        String user1 = userlogin.getText();
        String pass1 = passlogin.getText();
        
        if (user1.contains("'") || pass1.contains("'")) {
            //Checking for SQL Injection
            injectioncheck();
        }
        
        if (!user1.equals("") && !pass1.equals("")) {
            try {
                
                //when the input fields are not blank
                String s = "select * from login where uid='" + user1 + "' and pass='" + pass1 + "'";
                String s2 = "update login set flag = 1 where uid='" + user1 + "'";
                ResultSet rs = dbo.runquery(s);
                
                if (rs.next()) {
                    
                    if (rs.getInt("flag") == 0) {
                        //When the login flag is not set, that is when no device is currently logged in with this account
                        try {
                            
                            finaluser = user1;
                            
                            //setting the login flag in database meaning, someone is logged in with this username
                            dbo.updatefunc(s2);
                            
                            //If login successful, load up the home page and dispose the current frame
                            GUI_Home exp = new GUI_Home(finaluser);
                            exp.setVisible(true);
                            explogin.dispose();
                            
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e);
                        }

                    } else {
                        //When login flag is set and someone tries to login
                        JOptionPane.showMessageDialog(null, "User already logged in, try again later!");
                    }

                } else {
                    //When the login details are invalid
                    JOptionPane.showMessageDialog(null, "Incorrect Login Credentials");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            //When the input fields are blank
            JOptionPane.showMessageDialog(null, "Fill up all fields");
        }
        
        //Save login information while logging in, if the check box is selected
        if (remembercheck.isSelected()) {
            savelogin();
        }
    }
}

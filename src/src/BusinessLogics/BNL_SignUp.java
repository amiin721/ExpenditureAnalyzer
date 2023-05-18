/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import src.DatabaseHandling.dbConnect;
import src.DatabaseHandling.dbOperations;
import src.GUI.GUI_Login;
import src.FileHandling.fileOperations;

/**
 *
 * @author Zap
 */
public class BNL_SignUp {

    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();

    //Declaration for java swing fields used in this component
    public javax.swing.JTextField signupbudget;
    public javax.swing.JTextField usersignup;
    public javax.swing.JPasswordField passsignup;
    public javax.swing.JLabel signupuserincorrect;
    public javax.swing.JLabel signuppassincorrect;
    public javax.swing.JLabel signupcond1;
    public javax.swing.JLabel signupcond2;
    public javax.swing.JLabel signupcond3;
    public javax.swing.JLabel signupcond4;
    public javax.swing.JButton signupbut;
    public javax.swing.JFrame expsignup;

    //Setters for all the java swing fields used in this component
    public void setsignupbudget(javax.swing.JTextField signupbudget) {
        this.signupbudget = signupbudget;
    }

    public void setusersignup(javax.swing.JTextField usersignup) {
        this.usersignup = usersignup;
    }

    public void setpasssignup(javax.swing.JPasswordField passsignup) {
        this.passsignup = passsignup;
    }

    public void setsignupuserincorrect(javax.swing.JLabel signupuserincorrect) {
        this.signupuserincorrect = signupuserincorrect;
    }

    public void setsignuppassincorrect(javax.swing.JLabel signuppassincorrect) {
        this.signuppassincorrect = signuppassincorrect;
    }

    public void setsignupcond1(javax.swing.JLabel signupcond1) {
        this.signupcond1 = signupcond1;
    }

    public void setsignupcond2(javax.swing.JLabel signupcond2) {
        this.signupcond2 = signupcond2;
    }

    public void setsignupcond3(javax.swing.JLabel signupcond3) {
        this.signupcond3 = signupcond3;
    }

    public void setsignupcond4(javax.swing.JLabel signupcond4) {
        this.signupcond4 = signupcond4;
    }

    public void setsignupbut(javax.swing.JButton signupbut) {
        this.signupbut = signupbut;
    }

    public void setexpsignup(javax.swing.JFrame expsignup) {
        this.expsignup = expsignup;
    }

    //Method for pattern matching with regular expression
    public boolean patternmatch(String regex, String str) {
        
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        
        return b;
    }

    //Method to check for the SQL Injection
    public void injectioncheck() {
        JOptionPane.showMessageDialog(null, "IT WONT WORK");
        
        System.exit(0);
    }
    
    //Method to enable/disable sign up button as per username and password validation
    public void signupbutcheck(){
        
        if (signupuserincorrect.getText().equals("") && signuppassincorrect.getText().equals("")) {
            signupbut.setEnabled(true);
        } else {
            signupbut.setEnabled(false);
        }
        
    }

    //Method to set the conditions for username
    public void userconditionset() {
        
        signupcond1.setText("Conditions for username :");
        
        String s = "- Username should contain 6-20 characters and (.)(-)(_) are allowed as special "
                + "charcters.";
        String s2 = "- Username should not start with (.)or(-)or(_). ";
        String s3 = "- Charcaters (.)or(-)or(_) should not appear consecutively.";
        
        signupcond2.setText(s);
        signupcond4.setText(s2);
        signupcond3.setText(s3);
    }

    //Method to reset the conditions for username
    public void userconditionclear() {
        signupcond4.setText("");
        signupcond3.setText("");
        signupcond1.setText("");
        signupcond2.setText("");
    }

    //Method to set the conditions for password
    public void passconditionset() {
        
        signupcond1.setText("Conditions for password :");
        
        String str = "- Password should contain atleast 8 characters,atleast1 "
                + "digit.";
        String s2 = "- Password should atleast have one uppercase and lowercase alphabet.";
        String s3 = "- Password should contain atleast one special "
                + "character, and no white spaces.";
        
        signupcond2.setText(str);
        signupcond4.setText(s2);
        signupcond3.setText(s3);
    }

    //Method to reset the conditions for password
    public void passconditionclear() {
        signupcond4.setText("");
        signupcond3.setText("");
        signupcond1.setText("");
        signupcond2.setText("");
    }  

    //Method to perform password validation as per the conditions using regular expression
    public void passcheck(){
        
        String pass1 = passsignup.getText();
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        
        boolean b = patternmatch(regex, pass1);
        
        if (b == false) {
            signuppassincorrect.setText("Incorrect password format. Enter a correct format in order to sign up");
        }
        else{
            signuppassincorrect.setText("");
        }
    }

    //Method to perform username validation as per the conditions using regular expression
    public void usercheck() {
        
        String user1 = usersignup.getText();
        String usercheck
                = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){4,20}[a-zA-Z0-9]$";
        
        boolean b = patternmatch(usercheck, user1);
        
        if (b == false) {
            signupuserincorrect.setText("Incorrect username format. Enter a correct format in order to sign up");
        } else {
            signupuserincorrect.setText("");
        }
    }

    //Method to perform the main signup operation
    public void mainsignup() {
        
        String user1 = usersignup.getText();
        String pass1 = passsignup.getText();
        String var = signupbudget.getText();
        
        try {
            if (user1.contains("'") || pass1.contains("'") || var.contains("'")) {
                //Checking for SQL Injection
                injectioncheck();
            }
            
            int budget1 = 0;
            
            if (!var.equals("")) {
                budget1 = Integer.parseInt(var);
            }
            
            String s = "insert into login values ('" + user1 + "','" + pass1 + "'," + budget1 + ",0)";
            
            if (!user1.equals("") && !pass1.equals("") && !var.equals("")) {
                
                //when input fields are not blank
                signupbut.setEnabled(true);
                
                try {
                    
                    //Performing insertion of the values into database
                    dbo.st = dbConnect.con.createStatement();
                    dbo.st.executeUpdate(s);
                    
                    usersignup.setText("");
                    passsignup.setText("");
                    signupbudget.setText("");
                    
                    JOptionPane.showMessageDialog(null, "Account created successfully!");
                    
                    //Resetting the login info saved in the file
                    fop.writezero();
                    
                    //Login frame will be visible and sign up frame will be disposed
                    new GUI_Login().setVisible(true);
                    expsignup.dispose();

                } catch (SQLIntegrityConstraintViolationException e) {
                    
                    //When the entered username already exists, primary key violation
                    JOptionPane.showMessageDialog(null, "Username already exists, try different username!");
                    usersignup.setText("");
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            } else {
                //When inputs are empty
                JOptionPane.showMessageDialog(null, "You cannot leave any fields blank!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Inputs");
        }
    }
}

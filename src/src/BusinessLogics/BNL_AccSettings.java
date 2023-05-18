/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import src.DatabaseHandling.dbOperations;
import src.FileHandling.fileOperations;

/**
 *
 * @author Zap
 */
public class BNL_AccSettings {

    public String finaluser;

    public BNL_AccSettings(String fuser) {
        //Fetching the current logged in username into the variable
        finaluser = fuser;
    }

    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();

    //Declaration for java swing fields used in this component
    public javax.swing.JTextField userchange;
    public javax.swing.JPasswordField oldpasschange;
    public javax.swing.JPasswordField newpasschange;
    public javax.swing.JLabel changecondition;
    public javax.swing.JCheckBox showpasschange;
    public javax.swing.JButton changepassbut;
    public javax.swing.JTextField userchangebudget;
    public javax.swing.JTextField changebudgetval;

    //Setters for all the java swing fields used in this component
    public void setchangebudgetval(javax.swing.JTextField changebudgetval) {
        this.changebudgetval = changebudgetval;
    }

    public void setuserchangebudget(javax.swing.JTextField userchangebudget) {
        this.userchangebudget = userchangebudget;
    }

    public void setchangepassbut(javax.swing.JButton changepassbut) {
        this.changepassbut = changepassbut;
    }

    public void setshowpasschange(javax.swing.JCheckBox showpasschange) {
        this.showpasschange = showpasschange;
    }

    public void setchangecondition(javax.swing.JLabel changecondition) {
        this.changecondition = changecondition;
    }

    public void setnewpasschange(javax.swing.JPasswordField newpasschange) {
        this.newpasschange = newpasschange;
    }

    public void setoldpasschange(javax.swing.JPasswordField oldpasschange) {
        this.oldpasschange = oldpasschange;
    }

    public void setuserchange(javax.swing.JTextField userchange) {
        this.userchange = userchange;
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

    //Method for validating username and password with reular expression
    public boolean patternmatch(String regex, String str) {

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean b = m.matches();

        return b;
    }

    //Method to check the old credentials of the logged in user before changing password
    public void changecheck() {

        String user1 = userchange.getText();
        String oldpass = oldpasschange.getText();

        try {
            if (!user1.equals("") && !oldpass.equals("")) {

                try {

                    String s = "select * from login where uid='" + user1 + "' and pass='" + oldpass + "'";
                    ResultSet rs = dbo.runquery(s);

                    if (rs.next()) {

                        //When the details are true
                        changepassbut.setEnabled(true);
                        newpasschange.setEditable(true);
                        showpasschange.setEnabled(true);
                        oldpasschange.setEditable(false);
                        changecondition.setText("");

                        JOptionPane.showMessageDialog(null, "Enter the new password now");

                    } else {

                        //When the details are wrong
                        changecondition.setText("Invalid Credentials!");
                        oldpasschange.setText("");

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cannot leave fields empty!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Inputs");
        }
    }

    //Method to perform password change operation after checking the old credentials
    public void changepass() {

        String user1 = userchange.getText();
        String oldpass = oldpasschange.getText();
        String newpass = newpasschange.getText();

        if (user1.contains("'") || oldpass.contains("'") || newpass.contains("'")) {
            //Checking for SQL Injection
            injectioncheck();
        }

        try {

            if (!newpass.equals("")) {
                String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
                boolean b = patternmatch(regex, newpass);
                if (b == false) {

                    //When the new password does not match the pattern
                    newpasschange.setText("");
                    changecondition.setText("Incorrect password format");

                } else {
                    //When the new password is in valid format
                    try {

                        if (newpass.equals(oldpass)) {

                            //When the newpass and oldpass are same
                            JOptionPane.showMessageDialog(null, "The new password cant be same as the old Password!");
                            newpasschange.setText("");

                        } else {

                            String s2 = "update login set pass='" + newpass + "' where uid='" + user1 + "'";
                            dbo.updatefunc(s2);
                            JOptionPane.showMessageDialog(null, "Password Changed");

                            oldpasschange.setText("");
                            newpasschange.setText("");
                            changepassbut.setEnabled(false);
                            newpasschange.setEditable(false);
                            showpasschange.setEnabled(false);
                            changecondition.setText("");

                            int count = fop.readint();
                            if (count == 1) {
                                /*if the login details are saved, the new password will be changed in the
                                login details as well*/
                                fop.writeone(user1, newpass);
                            }
                        }
                    } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null, e);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Field empty");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Inputs");
        }
    }

    //Method to change budget
    public void changebudget() {
        try {

            String user = userchangebudget.getText();
            String bud = changebudgetval.getText();
            int budg = Integer.parseInt(bud);

            if (!user.equals("") && !bud.equals("")) {
                try {

                    //when the input fields are not blank
                    String s2 = "update login set budget=" + budg + " where uid='" + user + "'";
                    dbo.updatefunc(s2);

                    JOptionPane.showMessageDialog(null, "Budget Changed");
                    changebudgetval.setText("");

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Connot Leave field empty");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Inputs");
        }
    }

}

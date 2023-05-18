/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import java.sql.ResultSet;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import src.DatabaseHandling.dbOperations;
import src.FileHandling.fileOperations;

/**
 *
 * @author Zap
 */
public class BNL_ExpOverview {
    
    public String finaluser;

    public BNL_ExpOverview(String fuser) {
        //Fetching the current logged in username into the variable
        finaluser = fuser;
    }

    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();

    //Declaration for java swing fields used in this component
    public com.toedter.calendar.JDateChooser ovfromdate;
    public com.toedter.calendar.JDateChooser ovtodate;
    public javax.swing.JTable ovtable;
    public javax.swing.JLabel totalov;
    public javax.swing.JComboBox ovcombox;
    public javax.swing.JComboBox ovcomboxuser;
    public javax.swing.JButton sbut;
    public javax.swing.JLabel title;
    public javax.swing.JButton printbut;
    public javax.swing.JButton checkbut;

    //Setters for all the java swing fields used in this component
    public void setovfromdate(com.toedter.calendar.JDateChooser ovfromdate) {
        this.ovfromdate = ovfromdate;
    }

    public void setovtodate(com.toedter.calendar.JDateChooser ovtodate) {
        this.ovtodate = ovtodate;
    }

    public void setovtable(javax.swing.JTable ovtable) {
        this.ovtable = ovtable;
    }

    public void settotalov(javax.swing.JLabel totalov) {
        this.totalov = totalov;
    }

    public void setovcombox(javax.swing.JComboBox ovcombox) {
        this.ovcombox = ovcombox;
    }

    public void setovcomboxuser(javax.swing.JComboBox ovcomboxuser) {
        this.ovcomboxuser = ovcomboxuser;
    }

    public void setsbut(javax.swing.JButton sbut) {
        this.sbut = sbut;
    }

    public void setcheckbut(javax.swing.JButton checkbut) {
        this.checkbut = checkbut;
    }

    public void settitle(javax.swing.JLabel title) {
        this.title = title;
    }

    public void setprintbut(javax.swing.JButton printbut) {
        this.printbut = printbut;
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

    //Method to empty the table
    public void emptytab() {

        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) ovtable.getModel();

        int rc = dtm.getRowCount();

        while (rc-- != 0) {
            dtm.removeRow(0);
        }
    }

    public void checkreset() {

        String temp = (String) ovcomboxuser.getSelectedItem();

        if (!temp.equals("No Guardian Selected")) {
            
            emptytab();
            
            title.setText("Expenses Overview for " + temp);
            showcategoryov();
            
        } else {
            
            emptytab();
            
            title.setText("Expenses Overview for " + finaluser);
            showcategoryov();
            showguardianov();
            
        }
    }

    //Method to show the guardians in the combo box
    public void showguardianov() {

        //Removing all items first
        ovcomboxuser.removeAllItems();
        ovcomboxuser.addItem("No Guardian Selected");

        //getting all the guardians for the current user from the databse and adding in the combo box
        String s = "select * from guardian where gid='" + finaluser + "'";

        try {

            ResultSet rs = dbo.runquery(s);
            while (rs.next()) {
                //Inserting all the guardians
                ovcomboxuser.addItem(rs.getString("uid"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Method to show the categories for the user in the combo box
    public void showcategoryov() {

        if (title.getText().equals("Expenses Overview for " + finaluser)) {
            //When the logged in user's categories have to be displayed
            try {

                String s = "select * from categorydetails where uid='" + finaluser + "'";
                ovcombox.removeAllItems();
                ovcombox.addItem("");

                ResultSet rs = dbo.runquery(s);

                while (rs.next()) {
                    ovcombox.addItem(rs.getString("category"));
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            //When the selected username's categories have to be displayed
            String t = (String) ovcomboxuser.getSelectedItem();

            try {

                String s = "select * from categorydetails where uid='" + t + "'";
                ovcombox.removeAllItems();
                ovcombox.addItem("");
                ResultSet rs = dbo.runquery(s);

                while (rs.next()) {
                    ovcombox.addItem(rs.getString("category"));
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Method to perform the search operation and show expenses in the table
    public void searchov() {

        String s;

        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) ovtable.getModel();

        int rc = dtm.getRowCount();
        while (rc-- != 0) {
            dtm.removeRow(0);
        }

        if (title.getText().equals("Expenses Overview for " + finaluser)) {
            //When the logged in user's expenses have to be displayed
            try {

                java.sql.Date fd1 = new java.sql.Date(ovfromdate.getDate().getTime());
                java.sql.Date td1 = new java.sql.Date(ovtodate.getDate().getTime());
                String cat = (String) ovcombox.getSelectedItem();

                if (cat.equals("")) {
                    //When no category is selected
                    try {

                        s = "select * from expenditure where expdate>='" + fd1 + "' and expdate<='" + td1 + "' and uid='" + finaluser + "' order by expdate asc";
                        ResultSet rs = dbo.runquery(s);

                        int total1 = 0;
                        int no = 1;

                        while (rs.next()) {
                            total1 += rs.getInt("amount");
                            Object o[] = {no++, rs.getDate("expdate"), rs.getString("category"), rs.getInt("amount")};
                            dtm.addRow(o);
                        }

                        totalov.setText(total1 + "");

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                } else {
                    //When category is selected
                    try {

                        s = "select * from expenditure where expdate>='" + fd1 + "' and expdate<='" + td1 + "' and category='" + cat + "' and uid='" + finaluser + "' order by expdate asc";
                        ResultSet rs = dbo.runquery(s);

                        int total1 = 0;
                        int no = 1;

                        while (rs.next()) {
                            total1 += rs.getInt("amount");
                            Object o[] = {no++, rs.getDate("expdate"), rs.getString("category"), rs.getInt("amount")};
                            dtm.addRow(o);
                        }

                        totalov.setText(total1 + "");

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid Inputs");
            }
        } else {
            //When the selected username's expenses have to be displayed
            String t = (String) ovcomboxuser.getSelectedItem();

            try {

                java.sql.Date fd1 = new java.sql.Date(ovfromdate.getDate().getTime());
                java.sql.Date td1 = new java.sql.Date(ovtodate.getDate().getTime());
                String cat = (String) ovcombox.getSelectedItem();

                if (cat.equals("")) {
                    //When no category is selected
                    try {

                        s = "select * from expenditure where expdate>='" + fd1 + "' and expdate<='" + td1 + "' and uid='" + t + "' order by expdate asc";
                        ResultSet rs = dbo.runquery(s);

                        int total1 = 0;
                        int no = 1;

                        while (rs.next()) {
                            total1 += rs.getInt("amount");
                            Object o[] = {no++, rs.getDate("expdate"), rs.getString("category"), rs.getInt("amount")};
                            dtm.addRow(o);
                        }

                        totalov.setText(total1 + "");

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                } else {
                    //When category is selected
                    try {

                        s = "select * from expenditure where expdate>='" + fd1 + "' and expdate<='" + td1 + "' and category='" + cat + "' and uid='" + t + "' order by expdate asc";
                        ResultSet rs = dbo.runquery(s);

                        int total1 = 0;
                        int no = 1;

                        while (rs.next()) {
                            total1 += rs.getInt("amount");
                            Object o[] = {no++, rs.getDate("expdate"), rs.getString("category"), rs.getInt("amount")};
                            dtm.addRow(o);
                        }

                        totalov.setText(total1 + "");

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid Inputs");
            }
        }

    }

    //Method to print/save the table data
    public void printsave() {

        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) ovtable.getModel();

        int rc = dtm.getRowCount();
        if (rc == 0) {

            //When the table is empty, cannot carry out the operation
            JOptionPane.showMessageDialog(null, "Cannot Print/Save Empty Table Data");

        } else {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dtFrom = sdf.format(ovfromdate.getDate());
            String dtTo = sdf.format(ovtodate.getDate());

            int totc = Integer.parseInt(totalov.getText());

            MessageFormat header;
            MessageFormat footer;

            if (ovcombox.getSelectedItem().equals("")) {
                //When no category is selected
                header = new MessageFormat("Expenses from " + dtFrom + " to " + dtTo);
            } else {
                //When category is selected
                header = new MessageFormat("Expenses from " + dtFrom + " to " + dtTo + " for " + ovcombox.getSelectedItem());
            }

            String u = (String) ovcomboxuser.getSelectedItem();

            if (title.getText().equals("Expenses Overview for " + finaluser)) {
                //When logged in user's data is printed/saved
                footer = new MessageFormat("User : " + finaluser + " | Total Cost : " + totc);
            } else {
                //When selected username's data is printed/saved
                footer = new MessageFormat("User : " + u + " | Total Cost : " + totc);
            }

            try {
                //performing the print operation
                ovtable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
    }
}

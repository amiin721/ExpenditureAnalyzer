/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import static java.lang.Thread.sleep;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import src.DatabaseHandling.dbOperations;
import src.FileHandling.fileOperations;
import java.net.*;
import src.DatabaseHandling.dbConnect;
import src.GUI.GUI_DeleteGuardian;

/**
 *
 * @author Zap
 */
public class BNL_Home {

    public String finaluser;

    public BNL_Home(String fuser) {
        //Fetching the current logged in username into the variable
        finaluser = fuser;
    }
    
    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();
    
    public int ret = 0;

    //Declaration for java swing fields used in this component
    public com.toedter.calendar.JDateChooser homedate;
    public javax.swing.JComboBox homecombox;
    public javax.swing.JTextField homeamount;
    public javax.swing.JLabel homepopup;
    public javax.swing.JTable hometable;
    public javax.swing.JLabel homebudgetfield;
    public javax.swing.JLabel hometotc;
    public javax.swing.JLabel timeee;

    //Setters for all the java swing fields used in this component
    public void sethomedate(com.toedter.calendar.JDateChooser homedate) {
        this.homedate = homedate;
    }

    public void sethomecombox(javax.swing.JComboBox homecombox) {
        this.homecombox = homecombox;
    }

    public void sethomeamount(javax.swing.JTextField homeamount) {
        this.homeamount = homeamount;
    }

    public void sethomepopup(javax.swing.JLabel homepopup) {
        this.homepopup = homepopup;
    }

    public void sethometable(javax.swing.JTable hometable) {
        this.hometable = hometable;
    }

    public void sethomebudgetfield(javax.swing.JLabel homebudgetfield) {
        this.homebudgetfield = homebudgetfield;
    }

    public void sethometotc(javax.swing.JLabel hometotc) {
        this.hometotc = hometotc;
    }

    public void settimeee(javax.swing.JLabel timeee) {
        this.timeee = timeee;
    }

    public String setfinaluser() {
        return finaluser;
    }

    //Method to get the budget value for the current logged in user
    public int gethomebudget() {
        try {

            String s = "select budget from login where uid='" + finaluser + "'";
            ResultSet rs = dbo.runquery(s);

            if (rs.next()) {
                //ret is set as the user's budget value
                ret = rs.getInt("budget");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
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

    //Method to show the categories available for the current user in combo box
    public void showcategoryhome() {
        try {

            //Removing all items first
            homecombox.removeAllItems();

            String s = "select * from categorydetails where uid='" + finaluser + "'";
            ResultSet rs = dbo.runquery(s);

            while (rs.next()) {
                //Inserting all the items again
                homecombox.addItem(rs.getString("category"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Method to display all the expenses for the current logged in user in the table
    public void showexpensehome() {
        try {

            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) hometable.getModel();

            int rc = dtm.getRowCount();

            //Emptying the table
            while (rc-- != 0) {
                dtm.removeRow(0);
            }

            //Two dates, 30 days apart to show expenses for last 30 days in the home page
            java.time.LocalDate ld = java.time.LocalDate.now();
            java.time.LocalDate ld2 = ld.minusDays(30);

            String s = "select * from expenditure where expdate<='" + ld + "' and expdate>='" + ld2 + "' and uid='" + finaluser + "' order by expdate desc";
            ResultSet rs = dbo.runquery(s);
            int tot = 0;

            //Fetching values and inserting into table
            while (rs.next()) {
                tot += rs.getInt("amount");
                Object o[] = {rs.getInt("sid"), rs.getDate("expdate"), rs.getString("category"), rs.getInt("amount")};
                dtm.addRow(o);
            }

            //Setting totalcost of the expenses displayed
            hometotc.setText(tot + "");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        //Setting the budget field value
        homebudgetfield.setText("Budget : " + gethomebudget());

        int totcost = Integer.parseInt(hometotc.getText());

        //Checking if total expenses are more than the user's budget, and showing appropriate notification
        if (totcost > ret) {
            homepopup.setText("YOUR EXPENSES ARE MORE THAN YOUR BUDGET!");
        } else {
            homepopup.setText("");
        }

    }

    //Method to insert an expenses into the database and update the table content
    public void homeinsert() {
        try {

            //Fetching the user input details
            java.util.Date dt = homedate.getDate();
            String s1 = homeamount.getText();
            String s2 = (String) homecombox.getSelectedItem();

            if (s1.contains("'") || s2.contains("'")) {
                //Checking for SQL Injection
                injectioncheck();
            }

            if (dt != null && !s1.equals("") && !s2.equals("")) {

                if (s1.equals("0")) {

                    //When the user tries to input an expense with amount 0
                    JOptionPane.showMessageDialog(null, "Amount should be greater than 0");
                    homeamount.setText("");

                } else {

                    //When the input fields are not blank, insertion is carried out
                    int amount = Integer.parseInt(s1);
                    java.sql.Date dat = new java.sql.Date(dt.getTime());

                    String s = "insert into expenditure(category,expdate,amount,uid) values('" + s2 + "','" + dat + "'," + amount + ",'" + finaluser + "')";
                    dbo.insertfunc(s);

                    JOptionPane.showMessageDialog(null, "Inserted Successfully");
                    homeamount.setText("");
                    showexpensehome();

                }

            } else {
                //When the input fields are blank
                JOptionPane.showMessageDialog(null, "You cannot leave any of the fields blank");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Inputs");
        }
    }

    //Method to delete the selected expense from database and update table contents
    public void homedelete() {

        int ri = hometable.getSelectedRow();

        if (ri != -1) {
            //When there is a row selected
            int id = (int) hometable.getValueAt(ri, 0);

            try {

                int res = JOptionPane.showConfirmDialog(null, "Do you want to delete this Expense?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {

                    String te = "delete from expenditure where sid=" + id + " and uid='" + finaluser + "'";
                    dbo.deletefunc(te);

                    JOptionPane.showMessageDialog(null, "Expense Deleted Successfully");
                    showexpensehome();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            //When no row is selected
            JOptionPane.showMessageDialog(null, "Please select a row before deleting");
        }
    }

    //Method to Logout from the application
    public void homelogout() {
        try {

            //resetting the login flag
            String s2 = "update login set flag = 0 where uid='" + finaluser + "'";
            dbo.updatefunc(s2);

            //closgin the connection on logout
            dbConnect.closecon();
            
            System.exit(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        
    }

    //Method to get current time from gregorian calendar
    public String gettime() {

        Calendar cal = new GregorianCalendar();

        String sec, min, hou;

        //calculating current seconds
        int second = cal.get(Calendar.SECOND);
        String sectemp = Integer.toString(second);
        if (sectemp.length() == 1) {
            sec = "0" + second;
        } else {
            sec = "" + second;
        }

        //calculating current minutes
        int minute = cal.get(Calendar.MINUTE);
        String mintemp = Integer.toString(minute);
        if (mintemp.length() == 1) {
            min = "0" + minute;
        } else {
            min = "" + minute;
        }

        //calculating current hours in 24hr format
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String hourtemp = Integer.toString(hour);
        if (hourtemp.length() == 1) {
            hou = "0" + hour;
        } else {
            hou = "" + hour;
        }

        //returning current time as a string
        return hou + ":" + min + ":" + sec;
    }

    //Method to show current time on the home page
    public void settime() {
        Thread settime = new Thread() {
            public void run() {
                try {
                    while (true) {

                        //getting the current time in the form of string
                        String time = gettime();

                        //setting the time field
                        timeee.setText("Time : " + time);

                        //calling the run method for this thread every 1000msec, that is every second
                        sleep(1000);

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        };
        //starting the thread 
        settime.start();
    }

    //Method to store the login record in the table for the current user
    public void insertloginrecord() throws UnknownHostException {

        Calendar cal = new GregorianCalendar();

        String d, m;

        //getting the current date
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String daytemp = Integer.toString(day);
        if (daytemp.length() == 1) {
            d = "0" + day;
        } else {
            d = "" + day;
        }

        //getting the current month
        int month = cal.get(Calendar.MONTH) + 1;
        String montemp = Integer.toString(month);
        if (montemp.length() == 1) {
            m = "0" + month;
        } else {
            m = "" + month;
        }

        //getting the current year
        int year = cal.get(Calendar.YEAR);

        //getting current time
        String time = gettime();

        //creating a string inclusive of current date and time
        String s = d + "-" + m + "-"
                + year + " at " + time;

        //Getting the IP Address, host name for the current user
        InetAddress localHost = InetAddress.getLocalHost();
        String ip = localHost.getHostAddress();
        String name = localHost.getHostName();

        //inserting the current date and time in the database
        String ins = "insert into loginrecord values('" + finaluser + "','"
                + s + "','" + name + "','" + ip + "')";

        dbo.insertfunc(ins);
    }

    //Method to delete the existing guardian for the current user
    public void delguardian() {

        String s = "select * from guardian where uid='" + finaluser + "'";

        try {

            ResultSet rs = dbo.runquery(s);

            if (rs.next()) {

                //When the user has guardians has added, the frame will open up
                new GUI_DeleteGuardian(finaluser).setVisible(true);

            } else {
                
                //When the guardian doesnt exist
                JOptionPane.showMessageDialog(null, "You dont have a guardian added that can be deleted");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}

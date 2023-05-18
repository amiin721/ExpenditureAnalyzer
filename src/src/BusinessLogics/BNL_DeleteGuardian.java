/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.BusinessLogics;

import java.sql.*;
import javax.swing.JOptionPane;
import src.DatabaseHandling.dbOperations;
import src.FileHandling.fileOperations;

public class BNL_DeleteGuardian {
    
    public String finaluser;

    public BNL_DeleteGuardian(String fuser) {
        //Fetching the current logged in username into the variable
        finaluser = fuser;
    }

    //Initializing the file operations and database operations objects
    fileOperations fop = new fileOperations();
    dbOperations dbo = new dbOperations();

    //Declaration for java swing fields used in this component
    public javax.swing.JComboBox delgcombox;
    public javax.swing.JFrame delgframe;

    //Setters for all the java swing fields used in this component    
    public void setdelgcombox(javax.swing.JComboBox delgcombox) {
        this.delgcombox = delgcombox;
    }

    public void setdelgframe(javax.swing.JFrame delgframe) {
        this.delgframe = delgframe;
    }

    //Method to return the fetched logged in username
    public String setfinaluser() {
        return finaluser;
    }

    //Method to show the guardians in the combo box
    public void showguardian() {

        //Removing all items first
        delgcombox.removeAllItems();

        //getting all the guardians for the current user from the databse and adding in the combo box
        String s = "select * from guardian where uid='" + finaluser + "'";

        try {

            ResultSet rs = dbo.runquery(s);
            while (rs.next()) {
                //Inserting all the guardians
                delgcombox.addItem(rs.getString("gid"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Two methods combined to perform the delete operation on selected guardian
    public void performdel(){
        
        //getting the selected username
        String u = (String) delgcombox.getSelectedItem();

        String s = "delete from guardian where uid='" + finaluser + "' and gid='" + u + "'";

        //performing deletion of guardian
        int res = JOptionPane.showConfirmDialog(null, "Do you want to delete '" + u + "' as your guardian?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            dbo.deletefunc(s);
            JOptionPane.showMessageDialog(null, "Guardian Deleted Successfully");

            //updating the new guardians list
            showguardian();
        }
        
    }

    public void delguardian() {
        
        if (delgcombox.getItemCount() == 1) {
            //If all guardians are deleted close the current frame
            performdel();
            
            JOptionPane.showMessageDialog(null, "You have deleted all the guardians");
            delgframe.dispose();
            
        }
        else{
            performdel();
        }
        
    }
}

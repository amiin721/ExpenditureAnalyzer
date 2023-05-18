/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.FileHandling;

import java.io.*;
import src.UserPass.userpass;

/**
 *
 * @author Zap
 */
public class fileOperations {

    File f;
    FileOutputStream fos;
    ObjectOutputStream oos;
    
    //Method to read the stored integer in the login file that is used to check whether the user has saved login info or not
    public int readint() {

        try ( FileInputStream fis = new FileInputStream("login.txt");  ObjectInputStream ois = new ObjectInputStream(fis);) {

            int count = ois.readInt();
            return count;

        } catch (Exception e) {

        }
        return 0;
    }

    //Method to write zero in the login file, meaning that login info is not saved
    public void writezero() {

        try ( FileOutputStream fos = new FileOutputStream("login.txt");  ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeInt(0);

        } catch (Exception e) {
            //                    JOptionPane.showMessageDialog(null, e);
        }
    }

    //Method to write one and the saved username and password in the login file, meaning that the login info is saved
    public void writeone(String s1, String s2) {

        try ( FileOutputStream fos = new FileOutputStream("login.txt");  ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            userpass up = new userpass(s1, s2);
            oos.writeInt(1);
            oos.writeObject(up);

        } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null, e);
        }
    }

}

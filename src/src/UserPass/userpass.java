/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.UserPass;
import java.io.*;
/**
 *
 * @author Zap
 */
public class userpass implements Serializable {
    public String user;
    public String pass;
    
    public userpass(String user, String pass){
        this.user=user;
        this.pass=pass;
    }
    
    public String getUser(){
        return user;
    }
    
    public String getPass(){
        return pass;
    }
    
}

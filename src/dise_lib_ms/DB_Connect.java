/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dise_lib_ms;

import intf.ErrorFrame;
import intf.LoginFrame;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 *
 * @author Hasintha Nayanajith
 */
public class DB_Connect {
    
   
    
    public int connect_one;
    
    public static Connection connect(){
    
        Connection conn = null;
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/esoft_library","root","");
            //JOptionPane.showMessageDialog(null,"Connection Successfull!");
            
        } catch (Exception e) {
            
            //JOptionPane.showMessageDialog(null,"Connection Error Occured! \nCouldn't connect with database.");
            UIManager UI=new UIManager();
            UI.put("OptionPane.background", new java.awt.Color(0, 7, 49));
            UI.put("Panel.background", new java.awt.Color(0, 7, 49));
            JTextArea label = new JTextArea("Connection Failed. Couldn't connect with database. Try Again.\n"+e);
            label.setBackground(new java.awt.Color(0, 7, 43));
            label.setForeground(new java.awt.Color(240,240,240));
            label.setFont(new Font("FreeUniversal", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(null,label," Esoft Metro Campus | Application Error",JOptionPane.ERROR_MESSAGE);
            
            
            
        }
        
        return conn;
    
    }
    
    

    
}

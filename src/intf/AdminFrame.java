
package intf;

import dise_lib_ms.DB_Connect;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import net.proteanit.sql.DbUtils;


public class AdminFrame extends javax.swing.JFrame {

    int xMouse;
    int yMouse;
    
    String UserID;
    String PWord;
    
    String time;
    
    //Connecting package with database
    Connection conn = null;
    PreparedStatement pet = null;
    ResultSet rst = null;
    
    public AdminFrame() {
        initComponents();
        
        setIcon();
        
        conn = DB_Connect.connect();
        
        this.getRootPane().setBorder(BorderFactory.createLineBorder(new java.awt.Color(0,12,19),1));
        
        //Table 1 Look & Feel
        jTable1.getTableHeader().setFont(new Font("Leelawadee",1,14));
        jTable1.getTableHeader().setOpaque(false);
        jTable1.getTableHeader().setBackground(new Color(0,0,22));
        jTable1.getTableHeader().setForeground(new Color(102,204,255));
        jTable1.setRowHeight(30);
        
        //Table 2 Look & Feel
        jTable2.getTableHeader().setFont(new Font("Leelawadee",1,14));
        jTable2.getTableHeader().setOpaque(false);
        jTable2.getTableHeader().setBackground(new Color(0,0,22));
        jTable2.getTableHeader().setForeground(new Color(102,204,255));
        jTable2.setRowHeight(30);
        
        //Table 3 Look & Feel
        jTable3.getTableHeader().setFont(new Font("Leelawadee",1,14));
        jTable3.getTableHeader().setOpaque(false);
        jTable3.getTableHeader().setBackground(new Color(0,0,22));
        jTable3.getTableHeader().setForeground(new Color(102,204,255));
        jTable3.setRowHeight(30);
        
        //Table 4 Look & Feel
        jTable4.getTableHeader().setFont(new Font("Leelawadee",1,14));
        jTable4.getTableHeader().setOpaque(false);
        jTable4.getTableHeader().setBackground(new Color(0,0,22));
        jTable4.getTableHeader().setForeground(new Color(102,204,255));
        jTable4.setRowHeight(30);
        
        BookTableLoad();
        AdminTableLoad();
        MemberTableLoad();
        IssuedBookTableLoad();
        HomeScreenTablesSummary();
        
    }
    
    public void WelcomeHome(String id,String pw){
    
        String uID = UserID =id;
        PWord = pw;
        
        HomeScreenUserDetails();
        
    }
    
    public void HomeScreenUserDetails(){
    
        try {
            
            String sql = "select * from admin_table where AdminID='"+UserID+"'";
            pet=conn.prepareStatement(sql);
            rst=pet.executeQuery();
            
            if(rst.next()){
            
            String one = rst.getString("FullName");
            W_nametxt.setText(one);
            W_addresstxt.setText(rst.getString("Address"));
            W_mobiletxt.setText(rst.getString("Contact"));
            W_emailtxt.setText(rst.getString("Email"));
            W_registertxt.setText(rst.getString("RegDate"));
            
            WelcomeTxt.setText("Welcome back, "+one);
            WelcomeIdTxt.setText("Admin Registration ID : "+UserID);
            
            addresstxt.setText(W_addresstxt.getText());
                        mobiletxt.setText(W_mobiletxt.getText());
                        emailtxt.setText(W_emailtxt.getText());
                
            }
            
         } catch (Exception e) {
            
            customMessageFrame("An unexpected query error occured! \nError code - 0003 \n"+e);
            
        }
        
        getLocalTime();
        BookTableLoad();
        AdminTableLoad();
        MemberTableLoad();
        IssuedBookTableLoad();
        
    }
    
    public void HomeScreenTablesSummary(){
    
        try {
            
            String sql1 = "select count(*) from book_table";
            pet = conn.prepareStatement(sql1);
            rst = pet.executeQuery();
            
            rst.next();
            int BookCount = rst.getInt(1);
            T_Books.setText(String.format("%03d", BookCount));
            
        } catch (Exception e) {
            
            String txt = "An error occured while counting books. Error 004"+e;
            customMessageFrame(txt);
            
        }
        
        try {
            
            String sql2 = "select count(*) from member_table";
            pet = conn.prepareStatement(sql2);
            rst = pet.executeQuery();
            
            rst.next();
            int MemberCount = rst.getInt(1);
            T_Members.setText(String.format("%03d", MemberCount));
            
        } catch (Exception e) {
            
            String txt = "An error occured while counting members. Error 005"+e;
            customMessageFrame(txt);
            
        }
        
        try {
            
            String sql3 = "select count(*) from admin_table";
            pet = conn.prepareStatement(sql3);
            rst = pet.executeQuery();
            
            rst.next();
            int AdminCount = rst.getInt(1);
            T_Admins.setText(String.format("%03d", AdminCount));
            
        } catch (Exception e) {
            
            String txt = "An error occured while counting admins. Error 006"+e;
            customMessageFrame(txt);
            
        }
        
        try {
            
            String sql4 = "select count(*) from issued_books";
            pet = conn.prepareStatement(sql4);
            rst = pet.executeQuery();
            
            rst.next();
            int IssuedCount = rst.getInt(1);
            T_IssuedBooks.setText(String.format("%03d", IssuedCount));
            
        } catch (Exception e) {
            
            String txt = "An error occured while counting issued books. Error 007"+e;
            customMessageFrame(txt);
            
        }
        
    }
    
    public void getLocalTime(){
    
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy 'at' HH:mm");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String one = now.format(dtf);
        
        String two = now.format(dtf2);
        time = two;
        localTime.setText(two);
        
    }
    
    public void HomeScreenBookSearch(){
    
        String one = Home_SrchTxt.getText();
        
        try {
            
            String sql = "SELECT * FROM book_table WHERE BookID LIKE '%"+one+"%' OR BookName LIKE '%"+one+"%' OR Author LIKE '%"+one+"%'";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            
            if(rst.next()){
            
                String name = rst.getString("BookName");
                String availability = rst.getString("Availability");
                String id = rst.getString("BookID");
                
                home_srch_bookName.setText(name+" ("+id+")");
                home_srch_bookAva.setText(availability);
                
            }else{
            
                home_srch_bookName.setText("No such book");
                home_srch_bookAva.setText("No such book");
                
            }
            
        } catch (Exception e) {
            
            String txt = "An error occured while searching books."+e;
            customMessageFrame(txt);
            
        }
        
        
    }
    
    public void BookTableLoad(){
        
        try{
            
            String sql = "SELECT * FROM book_table";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            jTable1.setModel(DbUtils.resultSetToTableModel(rst));
        
        }catch(Exception e){
            
            String txt = "Failed to load book table data to system.";
            customMessageFrame(txt);
        
        }
    
    }
    
    public void AdminTableLoad(){
        
        try{
            
            String sql = "SELECT * FROM admin_table";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            jTable3.setModel(DbUtils.resultSetToTableModel(rst));
        
        }catch(Exception e){
            
            String txt = "Failed to load admin table data to system.";
            customMessageFrame(txt);
        
        }
    
    }
    
    public void MemberTableLoad(){
        
        try{
            
            String sql = "SELECT * FROM member_table";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            jTable2.setModel(DbUtils.resultSetToTableModel(rst));
        
        }catch(Exception e){
            
            String txt = "Failed to load member table data to system.";
            customMessageFrame(txt);
        
        }
    
    }
    
    public void IssuedBookTableLoad(){
        
        try{
            
            String sql = "SELECT * FROM issued_books";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            jTable4.setModel(DbUtils.resultSetToTableModel(rst));
        
        }catch(Exception e){
            
            String txt = "Failed to load issued book data to system.";
            customMessageFrame(txt);
            
        }
    
    }
    
    public void BookInsert(){
    
        String one = new_bookID.getText();
        String two = new_bookName.getText();
        String three = new_bookAuthor.getText();
        String four = new_bookRack.getText();
        String five = new_bookRegister.getText();
        
        String six = "In Library Premises";
        
        if(one.equals("") || two.equals("") ||three.equals("") || four.equals("") || five.equals("") ){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Book ID, Name/Title, Author , Rack Number & Register Date can not be null values. Please fill all details with correct details. ");
            obj.setVisible(true);
            
        }else{
        
            try {
                
                String sql = "INSERT INTO book_table (BookID,BookName,Author,RackNumber,RegDate,Availability) VALUES ('"+one+"','"+two+"','"+three+"','"+four+"','"+five+"','"+six+"')";
                pet=conn.prepareStatement(sql);
                pet.execute();
            
                BookTableLoad();
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Succcess! New book has been registered.\n\nBOOK DETAILS\n\nBook ID : "+one+"\nBookName : "+two+"\nAuthor : "+three+"\nRack Number : "+four+"\nRegister Date : "+five);
                obj.setVisible(true);
                
                new_bookID.setText("");
                new_bookName.setText("");
                new_bookAuthor.setText("");
                new_bookRack.setText("");
                //new_bookRegister.setText("");
                
                new_bookRegister.setText(time);
                
            } catch (Exception e) {
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("An unexpected error occured in query while trying to save the new book. \n"+e);
                obj.setVisible(true);
                
            }
        
        }
        
    }
    
    public void BookTableDataSelect(){
    
        int row = jTable1.getSelectedRow();
        
        bookIDlbl.setText(jTable1.getValueAt(row,0).toString());
        bookNamelbl.setText(jTable1.getValueAt(row,1).toString());
        bookAuthorlbl.setText(jTable1.getValueAt(row,2).toString());
        bookRacklbl.setText(jTable1.getValueAt(row,3).toString());
        bookRegisterlbl.setText(jTable1.getValueAt(row,4).toString());
        bookAvailabilitylbl.setText(jTable1.getValueAt(row,5).toString());
        
    }
    
    public void BookSearch(){
    
        String one = bookSearchtxt.getText();
        
        try {
            
            String sql = "SELECT * FROM book_table WHERE BookID LIKE '%"+one+"%' OR BookName LIKE '%"+one+"%' OR Author LIKE '%"+one+"%'";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            jTable1.setModel(DbUtils.resultSetToTableModel(rst));
            
        } catch (Exception e) {
            
            ErrorFrame obj = new ErrorFrame();
            obj.Error("An unexpected error occured! \nError code - 0002 (AdminView Book Search Query) \n"+e);
            obj.setVisible(true);
            
        }
        
    }
    
    public void BookDataClear(){
    
        bookSearchtxt.setText("");
        bookIDlbl.setText("");
        bookNamelbl.setText("");
        bookAuthorlbl.setText("");
        bookRacklbl.setText("");
        bookAvailabilitylbl.setText("");
        bookRegisterlbl.setText("");
        
        BookTableLoad();
        
    }
    
    public void BookDataUpdate(){
    
        String one = bookIDlbl.getText();
        String two = bookNamelbl.getText();
        String three = bookAuthorlbl.getText();
        String four = bookRacklbl.getText();
        
        if(one.equals("")){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Please select a book to update it's details. Then, try again.");
            obj.setVisible(true);
            
        }else if(two.equals("") || three.equals("") || four.equals("") ){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Book Name, Auhtor or Rack Number can not be null values.");
            obj.setVisible(true);
        
        }else{
        
            try {
                
                String sql = "UPDATE book_table SET BookName='"+two+"' , Author='"+three+"' , RackNumber='"+four+"' WHERE BookID='"+one+"'";
                pet=conn.prepareStatement(sql);
                pet.execute();
                
                BookTableLoad();
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Success! Details of Book ( "+two+" ) has been updated!");
                obj.setVisible(true);
                
            } catch (Exception e) {
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("An unexpected error occured in Query. \n"+e);
                obj.setVisible(true);
                
            }
            
        }
        
    }
    
    public void BookDispose(){
    
        String id = bookIDlbl.getText();
        
        if(id.equals("")){
        
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Please select a book to dispose it. Then, try again.");
                obj.setVisible(true);
            
        }else{
        
            UIManager UI=new UIManager();
            UI.put("OptionPane.background", new java.awt.Color(0, 7, 49));
            UI.put("Panel.background", new java.awt.Color(0, 7, 49));
            JTextArea label = new JTextArea("Are you sure you want to delete this book?");
            label.setBackground(new java.awt.Color(0, 7, 43));
            label.setForeground(new java.awt.Color(240,240,240));
            label.setFont(new Font("FreeUniversal", Font.PLAIN, 14));
            int check = JOptionPane.showConfirmDialog(null,label," Esoft Metro Campus | Warning Message",JOptionPane.OK_CANCEL_OPTION);
            
            if(check==0){
            
                try {
                   
                    String sql = "DELETE FROM book_table WHERE BookID='"+id+"'";
                    pet=conn.prepareStatement(sql);
                    pet.execute();
            
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Success! The book has been disposed.");
                obj.setVisible(true);
            
                BookDataClear();
                
                    
                } catch (Exception e) {
                    
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("An unexpected error occured in Query. \n"+e);
                    obj.setVisible(true);
                    
                }
                
            }
            
        }
        
    }
    
    public void RegisterNewUser() throws SQLException{
    
        String one = uType.getText();
        String two = uID.getText();
        String three = uName.getText();
        String four = uAddress.getText();
        String five = uMobile.getText();
        String six = uEmail.getText();
        String seven = uRegisterDate.getText();
        
        String eight = uNewPassword.getText();
        String nine = uConfirmPassword.getText();
        
        if(two.equals("") || three.equals("") || four.equals("") || five.equals("") || six.equals("") || seven.equals("") || eight.equals("") || nine.equals("")){
        
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("Please fill all fields with correct information & Try again.");
                    obj.setVisible(true);
            
        }else{
        
            if(nine.equals(eight)){
            
                if(one.equals("Library Member            ")){
                
                    one = "Library Member";
                    
                    try {
                        
                        String sql = "INSERT INTO member_table (MemberID,FullName,Address,Contact,Email,RegDate) VALUES ('"+two+"','"+three+"','"+four+"','"+five+"','"+six+"','"+seven+"')";
                        pet=conn.prepareStatement(sql);
                        pet.execute();
                        
                        MemberTableLoad();
                        LoginDataInsert(two, nine, one);
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Success! New Library Member has been registered.\n\nMemeber ID : "+two+"\nName : "+three+"\nAddress : "+four+"\nMobile : "+five+"\nEmail : "+six+"\nRegister Date : "+seven+"\n\nLOGIN PASSWORD : "+nine);
                        obj.setVisible(true);
                        
                        uType.setText("Library Member            ");
                        uName.setText("");
                        uID.setText("");
                        uAddress.setText("");
                        uMobile.setText("");
                        uEmail.setText("");
                        uNewPassword.setText("");
                        uConfirmPassword.setText("");
                                
                        
                    }catch (Exception e) {
                        
                        try {
                            
                            String sql= "Select * from member_table where MemberID='"+two+"'";
                            pet=conn.prepareStatement(sql);
                            rst = pet.executeQuery();
                            
                            if(rst.next()){
                            
                                ErrorFrame obj = new ErrorFrame();
                                obj.Error("Please choose another UserID. This ID is already used by someone.");
                                obj.setVisible(true);
                                
                            }
                        
                        } catch (Exception w) {
                            
                            ErrorFrame obj = new ErrorFrame();
                            obj.Error("Something went wrong."+e);
                            obj.setVisible(true);
                            
                        }
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("An unexpected query error occured while trying to save new library member. \n"+e);
                        obj.setVisible(true);
                        
                    }
                    
                }else if(one.equals("Administrator                ")){
                
                    one = "Administrator";
                    try {
                        
                        String sql = "INSERT INTO admin_table (AdminID,FullName,Contact,RegDate,Email,Address) VALUES ('"+two+"','"+three+"','"+five+"','"+seven+"','"+six+"','"+four+"')";
                        pet=conn.prepareStatement(sql);
                        pet.execute();
                        
                        AdminTableLoad();
                        LoginDataInsert(two, nine, one);
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Success! New Administrator has been registered.\n\nAdmin ID : "+two+"\nName : "+three+"\nAddress : "+four+"\nMobile : "+five+"\nEmail : "+six+"\nRegister Date : "+seven+"\n\nLOGIN PASSWORD : "+nine);
                        obj.setVisible(true);
                        
                        uName.setText("");
                        uID.setText("");
                        uAddress.setText("");
                        uMobile.setText("");
                        uEmail.setText("");
                        
                    }catch (Exception e) {
                        
                        try {
                            
                            String sql= "Select * from admin_table where AdminID='"+two+"'";
                            pet=conn.prepareStatement(sql);
                            rst = pet.executeQuery();
                            
                            if(rst.next()){
                            
                                ErrorFrame obj = new ErrorFrame();
                                obj.Error("Please choose another UserID. This ID is already used by someone.");
                                obj.setVisible(true);
                                
                            }
                        
                        } catch (Exception w) {
                            
                            ErrorFrame obj = new ErrorFrame();
                            obj.Error("Something went wrong."+e);
                            obj.setVisible(true);
                            
                        }
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("An unexpected query error occured while trying to save new administrator. \n"+e);
                        obj.setVisible(true);
                        
                    }
                    
                    
                    
                }else{
                    
                            ErrorFrame obj = new ErrorFrame();
                            obj.Error("Sorry! System was unabled to identify the new user type.");
                            obj.setVisible(true);
                
                }
                
            }else{
            
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("New Password & Confirm Password must be same.");
                    obj.setVisible(true);
                
            }
            
        }
        
    }
    
    public void LoginDataInsert(String id,String pw,String type){
    
        String one = id;
        String two = pw;
        String three = type;
        
        try {
            
            String sql = "INSERT INTO login_table (UserID,PWord,UserType) VALUES ('"+one+"','"+two+"','"+three+"')";
            pet=conn.prepareStatement(sql);
            pet.execute();
            
            
            
        } catch (Exception e) {
            
            if(three.equals("Library Member")){
            
                try {
                    
                    String sql = "DELETE FROM member_table WHERE MemberID='"+one+"'";
                    pet=conn.prepareStatement(sql);
                    pet.execute();
                    
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("Unfortunately, new member has been deleted from the system because of a PRIMARY KEY DUPLICATION error. \nPlease choose another UserID for this new Library Member. ");
                    obj.setVisible(true);
                    
                } catch (Exception w) {
                    
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("An Unexpected Error occured while saving the login details of new member. \n"+w);
                    obj.setVisible(true);
                    
                }
                
            }else if(three.equals("Administrator")){
            
                try {
                    
                    String sql = "DELETE FROM admin_table WHERE AdminID='"+one+"'";
                    pet=conn.prepareStatement(sql);
                    pet.execute();
                    
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("Unfortunately, new admin has been deleted from the system because of a PRIMARY KEY DUPLICATION error. \nPlease choose another UserID for this new admin.");
                    obj.setVisible(true);
                    
                } catch (Exception c) {
                    
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("An Unexpected Error occured while saving the login details of new member. \n"+c);
                    obj.setVisible(true);
                    
                }
                
            }
            
                    
            
        }
        
    
    
    }
    
    public void MemberDataSelect(){
    
        int row = jTable2.getSelectedRow();
    
    String one = jTable2.getValueAt(row, 0).toString();
    String two = jTable2.getValueAt(row, 1).toString();
    String three = jTable2.getValueAt(row, 2).toString();
    String four = jTable2.getValueAt(row, 3).toString();
    String five = jTable2.getValueAt(row, 4).toString();
    String six = jTable2.getValueAt(row, 5).toString();
    
    mem_idlbl.setText(one);
    mem_namelbl.setText(two);
    mem_addresslbl.setText(three);
    mem_mobilelbl.setText(four);
    mem_emaillbl.setText(five);
    mem_regdatelbl.setText(six);
        
    }
    
    public void MemberDataUpdate(){
    
        String one = mem_idlbl.getText();
        String two = mem_namelbl.getText();
        String three = mem_addresslbl.getText();
        String four = mem_emaillbl.getText();
        String five = mem_mobilelbl.getText();
        
        if(one.equals("")){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Please select a member to update his/her details. Then, try again.");
            obj.setVisible(true);
            
        }else if(two.equals("") || three.equals("") || four.equals("") ){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Member Name, Address or Mobile can not be null values.");
            obj.setVisible(true);
        
        }else{
        
            try {
                
                String sql = "UPDATE member_table SET FullName='"+two+"' , Address='"+three+"' , Contact='"+five+"' , Email='"+four+"' WHERE MemberID='"+one+"'";
                pet=conn.prepareStatement(sql);
                pet.execute();
                
                MemberTableLoad();
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Success! Details of Member ( "+two+" ) has been updated!");
                obj.setVisible(true);
                
            } catch (Exception e) {
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("An unexpected error occured in Query. \n"+e);
                obj.setVisible(true);
                
            }
            
        }
        
    }
    
    public void MemberDispose(){
    
        String id = mem_idlbl.getText();
        
        if(id.equals("")){
        
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Please select a member to dispose. Then, try again.");
                obj.setVisible(true);
            
        }else{
        
            UIManager UI=new UIManager();
            UI.put("OptionPane.background", new java.awt.Color(0, 7, 49));
            UI.put("Panel.background", new java.awt.Color(0, 7, 49));
            JTextArea label = new JTextArea("Are you sure you want to delete this member?");
            label.setBackground(new java.awt.Color(0, 7, 43));
            label.setForeground(new java.awt.Color(240,240,240));
            label.setFont(new Font("FreeUniversal", Font.PLAIN, 14));
            int check = JOptionPane.showConfirmDialog(null,label," Esoft Metro Campus | Warning Message",JOptionPane.OK_CANCEL_OPTION);
            
            if(check==0){
            
                try {
                   
                    String sql = "DELETE FROM member_table WHERE MemberID='"+id+"'";
                    pet=conn.prepareStatement(sql);
                    pet.execute();
            
                    MemberTableLoad();
                    
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Success! The member has been disposed.");
                obj.setVisible(true);
            
                MemberDataClear();
                
                    
                } catch (Exception e) {
                    
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("An unexpected error occured in Query. \n"+e);
                    obj.setVisible(true);
                    
                }
                
            }
            
        }
        
    }
    
    public void MemberDataClear(){
    
                    mem_idlbl.setText("");
                    mem_namelbl.setText("");
                    mem_addresslbl.setText("");
                    mem_mobilelbl.setText("");
                    mem_emaillbl.setText("");
                    mem_regdatelbl.setText("");
        
    }
    
    public void MemberDataSearch(){
    
        String one = mem_search.getText();
        
        try {
            
            String sql = "SELECT * FROM member_table WHERE MemberID LIKE '%"+one+"%' OR FullName LIKE '%"+one+"%' OR Address LIKE '%"+one+"%'";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            jTable2.setModel(DbUtils.resultSetToTableModel(rst));
            
        } catch (Exception e) {
            
            ErrorFrame obj = new ErrorFrame();
            obj.Error("An unexpected error occured! \nError code - 0002 (AdminView Member Search Query) \n"+e);
            obj.setVisible(true);
            
        }
        
    }
    
    public void AdminDataSelect(){
    
        int row = jTable3.getSelectedRow();
    
    String one = jTable3.getValueAt(row, 0).toString();
    String two = jTable3.getValueAt(row, 1).toString();
    String three = jTable3.getValueAt(row, 2).toString();
    String four = jTable3.getValueAt(row, 3).toString();
    String five = jTable3.getValueAt(row, 4).toString();
    String six = jTable3.getValueAt(row, 5).toString();
    
    adminID.setText(one);
    adminName.setText(two);
    adminAddress.setText(six);
    adminMobile.setText(three);
    adminEmail.setText(five);
    adminRegister.setText(four);
        
    }
    
    public void AdminDataUpdate(){
    
        String one = adminID.getText();
        String three = adminAddress.getText();
        String four = adminMobile.getText();
        String five = adminEmail.getText();
        String two = adminName.getText();
        
        if(one.equals("")){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Please select an admin to update his/her details. Then, try again.");
            obj.setVisible(true);
            
        }else if(two.equals("") || three.equals("") || four.equals("") ){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Admin Name, Address or Mobile can not be null values.");
            obj.setVisible(true);
        
        }else{
        
            try {
                
                String sql = "UPDATE admin_table SET FullName='"+two+"' , Address='"+three+"' , Contact='"+four+"' , Email='"+five+"' WHERE AdminID='"+one+"'";
                pet=conn.prepareStatement(sql);
                pet.execute();
                
                AdminTableLoad();
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Success! Details of admin ( "+two+" ) has been updated!");
                obj.setVisible(true);
                
            } catch (Exception e) {
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("An unexpected error occured in Query. \n"+e);
                obj.setVisible(true);
                
            }
            
        }
        
    }
    
    public void AdminDispose(){
    
        String id = adminID.getText();
        
        if(id.equals("")){
        
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Please select an admin to dispose. Then, try again.");
                obj.setVisible(true);
            
        }else{
        
            UIManager UI=new UIManager();
            UI.put("OptionPane.background", new java.awt.Color(0, 7, 49));
            UI.put("Panel.background", new java.awt.Color(0, 7, 49));
            JTextArea label = new JTextArea("Are you sure you want to delete this administrator?");
            label.setBackground(new java.awt.Color(0, 7, 43));
            label.setForeground(new java.awt.Color(240,240,240));
            label.setFont(new Font("FreeUniversal", Font.PLAIN, 14));
            int check = JOptionPane.showConfirmDialog(null,label," Esoft Metro Campus | Warning Message",JOptionPane.OK_CANCEL_OPTION);
            
            if(check==0){
            
                try {
                   
                    String sql = "DELETE FROM admin_table WHERE AdminID='"+id+"'";
                    pet=conn.prepareStatement(sql);
                    pet.execute();
            
                    AdminTableLoad();
                    
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Success! The admin has been disposed.");
                obj.setVisible(true);
            
                AdminDataClear();
                
                    
                } catch (Exception e) {
                    
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("An unexpected error occured in Query. \n"+e);
                    obj.setVisible(true);
                    
                }
                
            }
            
        }
        
    }
    
    public void AdminDataClear(){
    
        adminID.setText("");
        adminName.setText("");
        adminAddress.setText("");
        adminMobile.setText("");
        adminEmail.setText("");
        adminRegister.setText("");
        adminSearch.setText("");
        
        AdminTableLoad();
        
        
    }
    
    public void AdminDataSearch(){
    
        String one = adminSearch.getText();
        
        try {
            
            String sql = "SELECT * FROM admin_table WHERE adminID LIKE '%"+one+"%' OR FullName LIKE '%"+one+"%' OR Address LIKE '%"+one+"%'";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            jTable3.setModel(DbUtils.resultSetToTableModel(rst));
            
        } catch (Exception e) {
            
            ErrorFrame obj = new ErrorFrame();
            obj.Error("An unexpected error occured! \nError code - 0002 (AdminView Admin Search Query) \n"+e);
            obj.setVisible(true);
            
        }
        
    }
    
    public void IssueTableDataSelect(){
    
        int row = jTable4.getSelectedRow();
    
    String one = jTable4.getValueAt(row, 0).toString();
    String two = jTable4.getValueAt(row, 1).toString();
    String three = jTable4.getValueAt(row, 2).toString();
    String four = jTable4.getValueAt(row, 3).toString();
    
    issueBookID.setText(one);
    issueMemberID.setText(two);
    issueReturn.setText(three);
    issueDate.setText(four);
            
            
    }
    
    public void IssuedBookDataInsert(){
    
        String one = issueBookID.getText();
        String two = issueMemberID.getText();
        String three = issueReturn.getText();
        String four = issueDate.getText();
        
        if(one.equals("") || two.equals("") || three.equals("")){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Please fill all fields with correct details and \nThen, try again.");
            obj.setVisible(true);
            
        }else{
            
            try {
                
                String sql1 = "select * from issued_books where BookID='"+one+"'";
                pet = conn.prepareStatement(sql1);
                rst = pet.executeQuery();
                
                if(rst.next()){
                
                    String rDate = rst.getString("DateOfReturn");
                    
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("Sorry! This book has already borrowed by someone. It will be returned to the library before "+rDate);
                    obj.setVisible(true);
                    
                }else{
                
                    try {
                        
                        String sql4 = "select * from issued_books where MemberID='"+two+"'";
                        pet = conn.prepareStatement(sql4);
                        rst = pet.executeQuery();
                        
                        if(rst.next()){
                        
                            ErrorFrame obj = new ErrorFrame();
                            obj.Error("Sorry! This member has already borrowed a book.");
                            obj.setVisible(true);
                        
                        }else{
                    
                    try {
                        
                        String sql2 = "select * from book_table where BookID='"+one+"'";
                        pet = conn.prepareStatement(sql2);
                        rst = pet.executeQuery();
                        
                        if(rst.next()){
                        
                            try {
                                
                                String sql3 = "select * from member_table where MemberID='"+two+"'";
                                pet = conn.prepareStatement(sql3);
                                rst = pet.executeQuery();
                                
                                if(rst.next()){
                                
                                    try {
                                        
                                        String sql5 = "INSERT INTO issued_books (BookID,MemberID,DateOfIssue,DateOfReturn) VALUES ('"+one+"','"+two+"','"+four+"','"+three+"')";
                                        pet=conn.prepareStatement(sql5);
                                        pet.execute();
            
                                        String BookAvailability = "Out Of Library";
                    
                                        BookAvailabilityUpdate(one,BookAvailability);
                                        
                                        ErrorFrame obj = new ErrorFrame();
                                        obj.Error("Success! The Book has been marked as issued.\n\nBook ID : "+one+"\nMember ID : "+two+"\nDate of Issue : "+four+"\nDate of Return : "+three);
                                        obj.setVisible(true);
                                        
                                        IssuedBookDataClear();
                                        
                                    } catch (Exception e) {
                                        
                                        ErrorFrame obj = new ErrorFrame();
                                        obj.Error("An unexpected error occured in query while trying to mark the book as issued."+e);
                                        obj.setVisible(true); 
                                        
                                    }
                                    
                                }else{
                                
                                    ErrorFrame obj = new ErrorFrame();
                                    obj.Error("Sorry! There is no such a member with this Member ID.");
                                    obj.setVisible(true);
                                    
                                }
                                
                            } catch (Exception e) {
                                
                                ErrorFrame obj = new ErrorFrame();
                                obj.Error("An unexpected error occured in query while chekcing the availability of member.");
                                obj.setVisible(true);
                                
                            }
                            
                        }else{
                        
                            ErrorFrame obj = new ErrorFrame();
                            obj.Error("Please enter a valid Book ID. There is no such a book in library with this Book ID.");
                            obj.setVisible(true);
                        
                        }
                        
                        
                    } catch (Exception e) {
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("An unexpected error occured in query while chekcing the availability of book. Error 0008");
                        obj.setVisible(true);
                        
                    }
                    
                    }
                        
                    } catch (Exception e) {
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("An unexpected error occured in query while verifying the Member ID. Error 0008");
                        obj.setVisible(true);
                        
                    }
                    
                    
                }
                
            } catch (Exception e) {
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("An unexpected error occured in query while chekcing the availability of book. Error 0007 \n"+e);
                obj.setVisible(true);
                
            }
            
        }
        

    }
    
    public void BookAvailabilityUpdate(String idd, String aStatus){
    
        String one = idd;
        String two = aStatus;
        
        try {
            
            String sql = "UPDATE book_table SET Availability='"+two+"' WHERE BookID='"+one+"'";
            pet=conn.prepareStatement(sql);
            pet.execute();
            
            BookTableLoad();
            
        } catch (Exception e) {
            
                ErrorFrame obj = new ErrorFrame();
                obj.Error("An unexpected error occured in query while updating the availability of book.");
                obj.setVisible(true);
            
        }
        
    }
    
    public void IssuedBookDataClear(){
    
        issueBookID.setText("");
        issueMemberID.setText("");
        //issueDate.setText("");
        issueReturn.setText("");
        issueSearch.setText("");
        issueDate.setText(time);
        
        IssuedBookTableLoad();
        
    }
    
    public void IssuedBookDataSearch(){
    
        String one = issueSearch.getText();
        
        try {
            
            String sql = "SELECT * FROM issued_books WHERE BookID LIKE '%"+one+"%' OR MemberID LIKE '%"+one+"%' OR DateOfIssue LIKE '%"+one+"%'";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            jTable3.setModel(DbUtils.resultSetToTableModel(rst));
            
        } catch (Exception e) {
            
            ErrorFrame obj = new ErrorFrame();
            obj.Error("An unexpected error occured! \nError code - 0002 (IssuedBookView Search Query) \n"+e);
            obj.setVisible(true);
            
        }
        
    }
    
    public void IssuedBookReturn(){
    
        String one = returnBook.getText();
        String two = returnDate.getText();
        
        if(one.equals("") || two.equals("")){
        
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Please insert the correct Book ID & Return Date.");
                obj.setVisible(true);
            
        }else{
        
            try {
                
                String sql = "select * from issued_books where BookID='"+one+"'";
                pet= conn.prepareStatement(sql);
                rst = pet.executeQuery();
                
                if(rst.next()){
                
                    try {
                        
                        String sql2 = "DELETE FROM issued_books WHERE BookID='"+one+"'";
                        pet=conn.prepareStatement(sql2);
                        pet.execute();
                        
                        BookAvailabilityUpdate(one,"In Library Premises");
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Success! This book has been marked as returned on "+two+" ( today ).");
                        obj.setVisible(true);
                        
                        returnBook.setText("");
                        returnDate.setText(time);
                        
                    } catch (Exception e) {
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("An unexpected error occured while processing the task. \n"+e);
                        obj.setVisible(true);
                        
                    }
                    
                }else{
                
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Invalid Book ID. There is no such a book saved as issued.");
                        obj.setVisible(true);
                
                }
                
            } catch (Exception e) {
                
                ErrorFrame obj = new ErrorFrame();
                obj.Error("An unexpected error occured while verifying the Book ID. \n"+e);
                obj.setVisible(true);
                
            }
            
        }
    
    }
    
    public void PasswordChange(){
    
        String one = currentPasswordtxt.getText();
        String two = newPasswordtxt.getText();
        String three = confirmPasswordtxt.getText();
        
        if(one.equals("") || two.equals("") || three.equals("")){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Current Password, New Password or Confirm Password Can not be null values.");
            obj.setVisible(true);
            
        }else{
        
            if(one.equals(PWord)){
            
                if(two.equals(three)){
                
                    try {
                        
                        String sql = "UPDATE login_table SET PWord='"+two+"' WHERE UserID='"+UserID+"'";
                        pet=conn.prepareStatement(sql);
                        pet.execute();
                        
                        PWord = two;
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Success! Your login password changed.");
                        obj.setVisible(true);
                        
                        currentPasswordtxt.setText("");
                        newPasswordtxt.setText("");
                        confirmPasswordtxt.setText("");
                        
                    } catch (Exception e) {
                        
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Sorry!\nAn unexpected error occured in SQL Query.\n"+e);
                        obj.setVisible(true);
                        
                    }
                    
                }else{
                
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("New Password & Confirm Password must be same.");
                    obj.setVisible(true);
            
                    
                }
                
            }else{
            
                ErrorFrame obj = new ErrorFrame();
                obj.Error("Current Password is invalid. \nPlease enter your correct current login password.");
                obj.setVisible(true);
            
            }
            
        }
        
    }
    
    public void UserDataUpdate(){
    
        String one = addresstxt.getText();
        String two = mobiletxt.getText();
        String three = emailtxt.getText();
        
        if(one.equals("") || two.equals("") || three.equals("")){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Address , Mobile Number & Email can not be null values.\nPlease keep existing details if you have no any new details to add.\n\nThen, Try again.");
            obj.setVisible(true);
            
            addresstxt.setText(W_addresstxt.getText());
                        mobiletxt.setText(W_mobiletxt.getText());
                        emailtxt.setText(W_emailtxt.getText());
            
        }else{
        
            try {
                
            String sql = "UPDATE admin_table SET Address='"+one+"',Contact='"+two+"',Email='"+three+"' WHERE AdminID='"+UserID+"'";
            pet=conn.prepareStatement(sql);
            pet.execute();
            
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Success! Your Address, Mobile & Email has been successfully updated.\nAddress : "+one+"\nMobile Number : "+two+"\nEmail Address : "+three);
                        obj.setVisible(true);
                        
                        HomeScreenUserDetails();
                        
                        addresstxt.setText(W_addresstxt.getText());
                        mobiletxt.setText(W_mobiletxt.getText());
                        emailtxt.setText(W_emailtxt.getText());
                        
                        addresstxt.setText(W_addresstxt.getText());
                        mobiletxt.setText(W_mobiletxt.getText());
                        emailtxt.setText(W_emailtxt.getText());
            
                
            } catch (Exception e) {
                
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Sorry!\nAn unexpected error occured in SQL Query.\n"+e);
                        obj.setVisible(true);
                
            }
            
        }
        
    }
    
    public void customMessageFrame(String one){
    
            UIManager UI=new UIManager();
            UI.put("OptionPane.background", new java.awt.Color(0, 7, 49));
            UI.put("Panel.background", new java.awt.Color(0, 7, 49));
            JTextArea label = new JTextArea(one);
            label.setBackground(new java.awt.Color(0, 7, 43));
            label.setForeground(new java.awt.Color(240,240,240));
            label.setFont(new Font("FreeUniversal", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(null,label," Esoft Metro Campus | Application Error",JOptionPane.ERROR_MESSAGE);
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        kButton1 = new com.k33ptoo.components.KButton();
        kButton2 = new com.k33ptoo.components.KButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel5 = new com.k33ptoo.components.KGradientPanel();
        T_Members = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        kGradientPanel6 = new com.k33ptoo.components.KGradientPanel();
        jLabel39 = new javax.swing.JLabel();
        T_IssuedBooks = new javax.swing.JLabel();
        kGradientPanel7 = new com.k33ptoo.components.KGradientPanel();
        T_Admins = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        kGradientPanel4 = new com.k33ptoo.components.KGradientPanel();
        jLabel35 = new javax.swing.JLabel();
        T_Books = new javax.swing.JLabel();
        kGradientPanel3 = new com.k33ptoo.components.KGradientPanel();
        WelcomeTxt = new javax.swing.JLabel();
        WelcomeIdTxt = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        localTime = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        W_nametxt = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        W_addresstxt = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        W_mobiletxt = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        W_emailtxt = new javax.swing.JLabel();
        kButton3 = new com.k33ptoo.components.KButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        W_registertxt = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        kGradientPanel8 = new com.k33ptoo.components.KGradientPanel();
        Home_SrchTxt = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        home_srch_bookName = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        home_srch_bookAva = new javax.swing.JLabel();
        kButton4 = new com.k33ptoo.components.KButton();
        jLabel13 = new javax.swing.JLabel();
        kGradientPanel9 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel14 = new com.k33ptoo.components.KGradientPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        kGradientPanel10 = new com.k33ptoo.components.KGradientPanel();
        bookSearchtxt = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bookIDlbl = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        kGradientPanel11 = new com.k33ptoo.components.KGradientPanel();
        bookRacklbl = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        kGradientPanel12 = new com.k33ptoo.components.KGradientPanel();
        bookNamelbl = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        kGradientPanel13 = new com.k33ptoo.components.KGradientPanel();
        bookAuthorlbl = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        bookRegisterlbl = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        bookAvailabilitylbl = new javax.swing.JLabel();
        kButton5 = new com.k33ptoo.components.KButton();
        kButton6 = new com.k33ptoo.components.KButton();
        jLabel126 = new javax.swing.JLabel();
        kButton22 = new com.k33ptoo.components.KButton();
        jLabel84 = new javax.swing.JLabel();
        kGradientPanel15 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel16 = new com.k33ptoo.components.KGradientPanel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        kGradientPanel17 = new com.k33ptoo.components.KGradientPanel();
        mem_search = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        mem_idlbl = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        kGradientPanel18 = new com.k33ptoo.components.KGradientPanel();
        mem_emaillbl = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        kGradientPanel19 = new com.k33ptoo.components.KGradientPanel();
        mem_namelbl = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        kGradientPanel20 = new com.k33ptoo.components.KGradientPanel();
        mem_addresslbl = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        mem_regdatelbl = new javax.swing.JLabel();
        kButton8 = new com.k33ptoo.components.KButton();
        kButton9 = new com.k33ptoo.components.KButton();
        jLabel79 = new javax.swing.JLabel();
        kGradientPanel21 = new com.k33ptoo.components.KGradientPanel();
        mem_mobilelbl = new javax.swing.JTextField();
        jLabel127 = new javax.swing.JLabel();
        kButton23 = new com.k33ptoo.components.KButton();
        jLabel85 = new javax.swing.JLabel();
        kGradientPanel22 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel23 = new com.k33ptoo.components.KGradientPanel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        kGradientPanel24 = new com.k33ptoo.components.KGradientPanel();
        adminSearch = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        adminID = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        kGradientPanel25 = new com.k33ptoo.components.KGradientPanel();
        adminEmail = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        kGradientPanel26 = new com.k33ptoo.components.KGradientPanel();
        adminName = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        kGradientPanel27 = new com.k33ptoo.components.KGradientPanel();
        adminAddress = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        adminRegister = new javax.swing.JLabel();
        kButton11 = new com.k33ptoo.components.KButton();
        kButton12 = new com.k33ptoo.components.KButton();
        jLabel94 = new javax.swing.JLabel();
        kGradientPanel28 = new com.k33ptoo.components.KGradientPanel();
        adminMobile = new javax.swing.JTextField();
        jLabel128 = new javax.swing.JLabel();
        kButton24 = new com.k33ptoo.components.KButton();
        jLabel95 = new javax.swing.JLabel();
        kGradientPanel29 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel30 = new com.k33ptoo.components.KGradientPanel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        kGradientPanel77 = new com.k33ptoo.components.KGradientPanel();
        jLabel166 = new javax.swing.JLabel();
        jLabel167 = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        jLabel169 = new javax.swing.JLabel();
        jLabel170 = new javax.swing.JLabel();
        jLabel171 = new javax.swing.JLabel();
        kGradientPanel33 = new com.k33ptoo.components.KGradientPanel();
        kButton13 = new com.k33ptoo.components.KButton();
        kButton14 = new com.k33ptoo.components.KButton();
        kGradientPanel32 = new com.k33ptoo.components.KGradientPanel();
        uType = new javax.swing.JLabel();
        kGradientPanel31 = new com.k33ptoo.components.KGradientPanel();
        uName = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        kGradientPanel34 = new com.k33ptoo.components.KGradientPanel();
        uAddress = new javax.swing.JTextField();
        jLabel105 = new javax.swing.JLabel();
        kGradientPanel35 = new com.k33ptoo.components.KGradientPanel();
        uMobile = new javax.swing.JTextField();
        jLabel106 = new javax.swing.JLabel();
        kGradientPanel36 = new com.k33ptoo.components.KGradientPanel();
        uEmail = new javax.swing.JTextField();
        jLabel107 = new javax.swing.JLabel();
        kGradientPanel37 = new com.k33ptoo.components.KGradientPanel();
        uRegisterDate = new javax.swing.JTextField();
        jLabel108 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        kGradientPanel51 = new com.k33ptoo.components.KGradientPanel();
        uID = new javax.swing.JTextField();
        kGradientPanel38 = new com.k33ptoo.components.KGradientPanel();
        jLabel99 = new javax.swing.JLabel();
        kGradientPanel39 = new com.k33ptoo.components.KGradientPanel();
        uNewPassword = new javax.swing.JTextField();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        kGradientPanel40 = new com.k33ptoo.components.KGradientPanel();
        uConfirmPassword = new javax.swing.JTextField();
        kButton15 = new com.k33ptoo.components.KButton();
        kButton16 = new com.k33ptoo.components.KButton();
        jLabel109 = new javax.swing.JLabel();
        kGradientPanel41 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel42 = new com.k33ptoo.components.KGradientPanel();
        jLabel98 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        kGradientPanel78 = new com.k33ptoo.components.KGradientPanel();
        jLabel172 = new javax.swing.JLabel();
        jLabel174 = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        kGradientPanel43 = new com.k33ptoo.components.KGradientPanel();
        kButton17 = new com.k33ptoo.components.KButton();
        kButton18 = new com.k33ptoo.components.KButton();
        kGradientPanel45 = new com.k33ptoo.components.KGradientPanel();
        new_bookName = new javax.swing.JTextField();
        jLabel113 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        kGradientPanel46 = new com.k33ptoo.components.KGradientPanel();
        new_bookAuthor = new javax.swing.JTextField();
        jLabel116 = new javax.swing.JLabel();
        kGradientPanel47 = new com.k33ptoo.components.KGradientPanel();
        new_bookRack = new javax.swing.JTextField();
        jLabel117 = new javax.swing.JLabel();
        kGradientPanel48 = new com.k33ptoo.components.KGradientPanel();
        new_bookRegister = new javax.swing.JTextField();
        jLabel120 = new javax.swing.JLabel();
        kGradientPanel50 = new com.k33ptoo.components.KGradientPanel();
        new_bookID = new javax.swing.JTextField();
        jLabel119 = new javax.swing.JLabel();
        kButton19 = new com.k33ptoo.components.KButton();
        kButton20 = new com.k33ptoo.components.KButton();
        jLabel123 = new javax.swing.JLabel();
        kGradientPanel44 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel49 = new com.k33ptoo.components.KGradientPanel();
        jLabel112 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        kGradientPanel52 = new com.k33ptoo.components.KGradientPanel();
        issueSearch = new javax.swing.JTextField();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel129 = new javax.swing.JLabel();
        kGradientPanel54 = new com.k33ptoo.components.KGradientPanel();
        issueBookID = new javax.swing.JTextField();
        jLabel130 = new javax.swing.JLabel();
        kGradientPanel55 = new com.k33ptoo.components.KGradientPanel();
        issueMemberID = new javax.swing.JTextField();
        kButton21 = new com.k33ptoo.components.KButton();
        jLabel131 = new javax.swing.JLabel();
        kGradientPanel56 = new com.k33ptoo.components.KGradientPanel();
        issueReturn = new javax.swing.JTextField();
        jLabel132 = new javax.swing.JLabel();
        kGradientPanel57 = new com.k33ptoo.components.KGradientPanel();
        issueDate = new javax.swing.JTextField();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        kButton35 = new com.k33ptoo.components.KButton();
        jLabel135 = new javax.swing.JLabel();
        kGradientPanel53 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel58 = new com.k33ptoo.components.KGradientPanel();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        kGradientPanel59 = new com.k33ptoo.components.KGradientPanel();
        kButton25 = new com.k33ptoo.components.KButton();
        kButton26 = new com.k33ptoo.components.KButton();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        kGradientPanel60 = new com.k33ptoo.components.KGradientPanel();
        returnBook = new javax.swing.JTextField();
        jLabel141 = new javax.swing.JLabel();
        kGradientPanel61 = new com.k33ptoo.components.KGradientPanel();
        returnDate = new javax.swing.JTextField();
        kButton7 = new com.k33ptoo.components.KButton();
        jLabel145 = new javax.swing.JLabel();
        kGradientPanel62 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel63 = new com.k33ptoo.components.KGradientPanel();
        jLabel142 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        kGradientPanel64 = new com.k33ptoo.components.KGradientPanel();
        kButton27 = new com.k33ptoo.components.KButton();
        kButton28 = new com.k33ptoo.components.KButton();
        jLabel144 = new javax.swing.JLabel();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        kGradientPanel65 = new com.k33ptoo.components.KGradientPanel();
        currentPasswordtxt = new javax.swing.JTextField();
        jLabel148 = new javax.swing.JLabel();
        kGradientPanel66 = new com.k33ptoo.components.KGradientPanel();
        confirmPasswordtxt = new javax.swing.JTextField();
        kButton10 = new com.k33ptoo.components.KButton();
        jLabel150 = new javax.swing.JLabel();
        kGradientPanel67 = new com.k33ptoo.components.KGradientPanel();
        newPasswordtxt = new javax.swing.JTextField();
        jLabel149 = new javax.swing.JLabel();
        kGradientPanel68 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel69 = new com.k33ptoo.components.KGradientPanel();
        jLabel151 = new javax.swing.JLabel();
        jLabel152 = new javax.swing.JLabel();
        kGradientPanel70 = new com.k33ptoo.components.KGradientPanel();
        kButton29 = new com.k33ptoo.components.KButton();
        kButton30 = new com.k33ptoo.components.KButton();
        jLabel153 = new javax.swing.JLabel();
        jLabel154 = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        kButton31 = new com.k33ptoo.components.KButton();
        jLabel158 = new javax.swing.JLabel();
        kGradientPanel71 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel72 = new com.k33ptoo.components.KGradientPanel();
        jLabel157 = new javax.swing.JLabel();
        jLabel159 = new javax.swing.JLabel();
        kGradientPanel73 = new com.k33ptoo.components.KGradientPanel();
        kButton32 = new com.k33ptoo.components.KButton();
        kButton33 = new com.k33ptoo.components.KButton();
        jLabel160 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        kGradientPanel74 = new com.k33ptoo.components.KGradientPanel();
        addresstxt = new javax.swing.JTextField();
        jLabel163 = new javax.swing.JLabel();
        kGradientPanel75 = new com.k33ptoo.components.KGradientPanel();
        emailtxt = new javax.swing.JTextField();
        kButton34 = new com.k33ptoo.components.KButton();
        jLabel164 = new javax.swing.JLabel();
        kGradientPanel76 = new com.k33ptoo.components.KGradientPanel();
        mobiletxt = new javax.swing.JTextField();
        jLabel165 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setLayout(null);

        kGradientPanel1.setkBorderRadius(0);
        kGradientPanel1.setkEndColor(new java.awt.Color(0, 6, 19));
        kGradientPanel1.setkStartColor(new java.awt.Color(0, 6, 19));
        kGradientPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                kGradientPanel1MouseDragged(evt);
            }
        });
        kGradientPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kGradientPanel1MousePressed(evt);
            }
        });
        kGradientPanel1.setLayout(null);

        kButton1.setText("X");
        kButton1.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        kButton1.setkBorderRadius(0);
        kButton1.setkEndColor(new java.awt.Color(0, 6, 19));
        kButton1.setkForeGround(new java.awt.Color(204, 204, 204));
        kButton1.setkHoverEndColor(new java.awt.Color(255, 51, 51));
        kButton1.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton1.setkHoverStartColor(new java.awt.Color(255, 51, 51));
        kButton1.setkStartColor(new java.awt.Color(0, 6, 19));
        kButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton1ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton1);
        kButton1.setBounds(1065, 10, 50, 40);

        kButton2.setText("-");
        kButton2.setFont(new java.awt.Font("Leelawadee", 0, 36)); // NOI18N
        kButton2.setkBorderRadius(0);
        kButton2.setkEndColor(new java.awt.Color(0, 6, 19));
        kButton2.setkForeGround(new java.awt.Color(204, 204, 204));
        kButton2.setkHoverEndColor(new java.awt.Color(230, 230, 230));
        kButton2.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton2.setkHoverStartColor(new java.awt.Color(230, 230, 230));
        kButton2.setkStartColor(new java.awt.Color(0, 6, 19));
        kButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton2ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton2);
        kButton2.setBounds(1015, 10, 50, 40);

        jLabel9.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 204, 204));
        jLabel9.setText("ESOFT LIBRARY MANAGEMENT SYSTEM ( ADMIN VIEW )");
        kGradientPanel1.add(jLabel9);
        jLabel9.setBounds(25, 20, 370, 20);

        jPanel1.add(kGradientPanel1);
        kGradientPanel1.setBounds(-10, -10, 1120, 50);

        jLabel4.setFont(new java.awt.Font("Leelawadee", 1, 30)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_menu_32.png"))); // NOI18N
        jLabel4.setText(" MENU");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 60, 140, 30);

        jLabel5.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 204, 255));
        jLabel5.setText("Manage Books");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited(evt);
            }
        });
        jPanel1.add(jLabel5);
        jLabel5.setBounds(20, 150, 160, 20);

        jLabel6.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_reply_arrow_16.png"))); // NOI18N
        jLabel6.setText(" Return Books");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel6MouseExited(evt);
            }
        });
        jPanel1.add(jLabel6);
        jLabel6.setBounds(20, 270, 160, 30);

        jLabel7.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_home_16.png"))); // NOI18N
        jLabel7.setText(" Overview");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
        });
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 110, 160, 20);

        jLabel8.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_add_book_16.png"))); // NOI18N
        jLabel8.setText(" Add New Books");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel8MouseExited(evt);
            }
        });
        jPanel1.add(jLabel8);
        jLabel8.setBounds(20, 210, 160, 30);

        jLabel10.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_forward_arrow_16.png"))); // NOI18N
        jLabel10.setText(" Issue Books");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
        });
        jPanel1.add(jLabel10);
        jLabel10.setBounds(20, 240, 160, 30);

        jLabel11.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 204, 255));
        jLabel11.setText("Manage People");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
        });
        jPanel1.add(jLabel11);
        jLabel11.setBounds(20, 320, 160, 20);

        jLabel12.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_add_user_male_16.png"))); // NOI18N
        jLabel12.setText(" Add New Member");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel12MouseExited(evt);
            }
        });
        jPanel1.add(jLabel12);
        jLabel12.setBounds(20, 380, 160, 30);

        jLabel14.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_add_administrator_16.png"))); // NOI18N
        jLabel14.setText(" Add New Admin");
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel14MouseExited(evt);
            }
        });
        jPanel1.add(jLabel14);
        jLabel14.setBounds(20, 440, 160, 30);

        jLabel15.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 204, 255));
        jLabel15.setText("Other");
        jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel15MouseExited(evt);
            }
        });
        jPanel1.add(jLabel15);
        jLabel15.setBounds(20, 490, 160, 20);

        jLabel16.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_security_lock_16.png"))); // NOI18N
        jLabel16.setText(" Privacy Settings");
        jLabel16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel16MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel16MouseExited(evt);
            }
        });
        jPanel1.add(jLabel16);
        jLabel16.setBounds(20, 520, 160, 30);

        jLabel17.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_logout_rounded_up_16.png"))); // NOI18N
        jLabel17.setText(" Logout");
        jLabel17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel17MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel17MouseExited(evt);
            }
        });
        jPanel1.add(jLabel17);
        jLabel17.setBounds(20, 650, 160, 30);

        jLabel18.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_book_16.png"))); // NOI18N
        jLabel18.setText(" Library Books");
        jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel18MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel18MouseExited(evt);
            }
        });
        jPanel1.add(jLabel18);
        jLabel18.setBounds(20, 180, 160, 30);

        jLabel19.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_user_groups_16.png"))); // NOI18N
        jLabel19.setText(" Library Members");
        jLabel19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel19MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel19MouseExited(evt);
            }
        });
        jPanel1.add(jLabel19);
        jLabel19.setBounds(20, 350, 160, 30);

        jLabel20.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_administrator_male_16.png"))); // NOI18N
        jLabel20.setText(" Library Admins");
        jLabel20.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel20MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel20MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel20MouseExited(evt);
            }
        });
        jPanel1.add(jLabel20);
        jLabel20.setBounds(20, 410, 160, 30);

        jLabel21.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_about_16.png"))); // NOI18N
        jLabel21.setText(" About Us");
        jLabel21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel21MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel21MouseExited(evt);
            }
        });
        jPanel1.add(jLabel21);
        jLabel21.setBounds(20, 550, 160, 30);

        jLabel50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Menu Panel.png"))); // NOI18N
        jPanel1.add(jLabel50);
        jLabel50.setBounds(-10, -10, 240, 730);

        kGradientPanel2.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel2.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel2.setLayout(null);

        kGradientPanel5.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel5.setkBorderRadius(20);
        kGradientPanel5.setkEndColor(new java.awt.Color(0, 204, 51,100));
        kGradientPanel5.setkStartColor(new java.awt.Color(0, 204, 51,100));
        kGradientPanel5.setLayout(null);

        T_Members.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        T_Members.setForeground(new java.awt.Color(255, 255, 255));
        T_Members.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        T_Members.setText("000");
        kGradientPanel5.add(T_Members);
        T_Members.setBounds(20, 30, 180, 40);

        jLabel38.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Total No. of Members");
        kGradientPanel5.add(jLabel38);
        jLabel38.setBounds(20, 10, 180, 20);

        kGradientPanel2.add(kGradientPanel5);
        kGradientPanel5.setBounds(630, 190, 220, 80);

        kGradientPanel6.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel6.setkBorderRadius(20);
        kGradientPanel6.setkEndColor(new java.awt.Color(255, 0, 102,100));
        kGradientPanel6.setkStartColor(new java.awt.Color(255, 0, 102,100));
        kGradientPanel6.setLayout(null);

        jLabel39.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Total No. of Issued Book");
        kGradientPanel6.add(jLabel39);
        jLabel39.setBounds(20, 10, 180, 20);

        T_IssuedBooks.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        T_IssuedBooks.setForeground(new java.awt.Color(255, 255, 255));
        T_IssuedBooks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        T_IssuedBooks.setText("000");
        kGradientPanel6.add(T_IssuedBooks);
        T_IssuedBooks.setBounds(20, 30, 180, 40);

        kGradientPanel2.add(kGradientPanel6);
        kGradientPanel6.setBounds(370, 290, 220, 80);

        kGradientPanel7.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel7.setkBorderRadius(20);
        kGradientPanel7.setkEndColor(new java.awt.Color(0,51, 255,50));
        kGradientPanel7.setkStartColor(new java.awt.Color(0,51, 255,50));
        kGradientPanel7.setLayout(null);

        T_Admins.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        T_Admins.setForeground(new java.awt.Color(255, 255, 255));
        T_Admins.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        T_Admins.setText("000");
        kGradientPanel7.add(T_Admins);
        T_Admins.setBounds(20, 30, 180, 40);

        jLabel42.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Total No. of Admins");
        kGradientPanel7.add(jLabel42);
        jLabel42.setBounds(20, 10, 180, 20);

        kGradientPanel2.add(kGradientPanel7);
        kGradientPanel7.setBounds(630, 290, 220, 80);

        kGradientPanel4.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel4.setkBorderRadius(20);
        kGradientPanel4.setkEndColor(new java.awt.Color(255, 102, 0,100));
        kGradientPanel4.setkStartColor(new java.awt.Color(255, 102, 0,100));
        kGradientPanel4.setLayout(null);

        jLabel35.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Total No. of Books");
        kGradientPanel4.add(jLabel35);
        jLabel35.setBounds(20, 10, 180, 20);

        T_Books.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        T_Books.setForeground(new java.awt.Color(255, 255, 255));
        T_Books.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        T_Books.setText("000");
        kGradientPanel4.add(T_Books);
        T_Books.setBounds(20, 30, 180, 40);

        kGradientPanel2.add(kGradientPanel4);
        kGradientPanel4.setBounds(370, 190, 220, 80);

        kGradientPanel3.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel3.setkBorderRadius(20);
        kGradientPanel3.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel3.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel3.setLayout(null);

        WelcomeTxt.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        WelcomeTxt.setForeground(new java.awt.Color(255, 255, 255));
        WelcomeTxt.setText("Welcome back, Hasintha Nayanajith");
        kGradientPanel3.add(WelcomeTxt);
        WelcomeTxt.setBounds(10, 5, 540, 40);

        WelcomeIdTxt.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        WelcomeIdTxt.setForeground(new java.awt.Color(255, 255, 255));
        WelcomeIdTxt.setText("Admin Registration ID - A0001");
        kGradientPanel3.add(WelcomeIdTxt);
        WelcomeIdTxt.setBounds(10, 40, 540, 20);

        jLabel31.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Today");
        kGradientPanel3.add(jLabel31);
        jLabel31.setBounds(680, 10, 70, 30);

        localTime.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        localTime.setForeground(new java.awt.Color(255, 255, 255));
        localTime.setText("2021-12-25");
        kGradientPanel3.add(localTime);
        localTime.setBounds(680, 30, 130, 30);

        kGradientPanel2.add(kGradientPanel3);
        kGradientPanel3.setBounds(20, 70, 830, 70);

        jLabel3.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Admin Details ( You )");
        kGradientPanel2.add(jLabel3);
        jLabel3.setBounds(30, 180, 200, 20);

        jLabel33.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Name");
        kGradientPanel2.add(jLabel33);
        jLabel33.setBounds(30, 210, 180, 20);

        W_nametxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_nametxt.setForeground(new java.awt.Color(102, 204, 255));
        W_nametxt.setText("Hasintha Nayanajith BW");
        kGradientPanel2.add(W_nametxt);
        W_nametxt.setBounds(30, 230, 240, 20);

        jLabel25.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Address");
        kGradientPanel2.add(jLabel25);
        jLabel25.setBounds(30, 260, 180, 20);

        W_addresstxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_addresstxt.setForeground(new java.awt.Color(102, 204, 255));
        W_addresstxt.setText("Hettipola, Kurunegala");
        kGradientPanel2.add(W_addresstxt);
        W_addresstxt.setBounds(30, 280, 240, 20);

        jLabel27.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Mobile Number");
        kGradientPanel2.add(jLabel27);
        jLabel27.setBounds(30, 310, 180, 20);

        W_mobiletxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_mobiletxt.setForeground(new java.awt.Color(102, 204, 255));
        W_mobiletxt.setText("077 55 47 47 3");
        kGradientPanel2.add(W_mobiletxt);
        W_mobiletxt.setBounds(30, 330, 240, 20);

        jLabel29.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Email");
        kGradientPanel2.add(jLabel29);
        jLabel29.setBounds(30, 360, 180, 20);

        W_emailtxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_emailtxt.setForeground(new java.awt.Color(102, 204, 255));
        W_emailtxt.setText("hasintha.payoneer@gmail.com");
        kGradientPanel2.add(W_emailtxt);
        W_emailtxt.setBounds(30, 380, 240, 20);

        kButton3.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton3.setText("Update Details");
        kButton3.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton3.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton3.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton3.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton3.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton3.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton3ActionPerformed(evt);
            }
        });
        kGradientPanel2.add(kButton3);
        kButton3.setBounds(30, 470, 190, 40);

        jLabel34.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel2.add(jLabel34);
        jLabel34.setBounds(300, 175, 10, 390);

        jLabel43.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Registered On");
        kGradientPanel2.add(jLabel43);
        jLabel43.setBounds(30, 410, 180, 20);

        W_registertxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_registertxt.setForeground(new java.awt.Color(102, 204, 255));
        W_registertxt.setText("2021-12-25");
        kGradientPanel2.add(W_registertxt);
        W_registertxt.setBounds(30, 430, 240, 20);

        jLabel45.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Search Books Availability");
        kGradientPanel2.add(jLabel45);
        jLabel45.setBounds(370, 410, 240, 20);

        jLabel46.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Type Book ID / Name / Author");
        kGradientPanel2.add(jLabel46);
        jLabel46.setBounds(370, 430, 180, 30);

        kGradientPanel8.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel8.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel8.setkFillBackground(false);
        kGradientPanel8.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel8.setLayout(null);

        Home_SrchTxt.setBackground(new java.awt.Color(0, 12, 19));
        Home_SrchTxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        Home_SrchTxt.setForeground(new java.awt.Color(255, 255, 255));
        Home_SrchTxt.setBorder(null);
        Home_SrchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Home_SrchTxtKeyReleased(evt);
            }
        });
        kGradientPanel8.add(Home_SrchTxt);
        Home_SrchTxt.setBounds(10, 10, 250, 20);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_search_16.png"))); // NOI18N
        kGradientPanel8.add(jLabel47);
        jLabel47.setBounds(270, 0, 16, 40);

        kGradientPanel2.add(kGradientPanel8);
        kGradientPanel8.setBounds(370, 460, 310, 40);

        jLabel48.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Book Name");
        kGradientPanel2.add(jLabel48);
        jLabel48.setBounds(370, 510, 110, 30);

        home_srch_bookName.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        home_srch_bookName.setForeground(new java.awt.Color(102, 204, 255));
        home_srch_bookName.setText(" -");
        kGradientPanel2.add(home_srch_bookName);
        home_srch_bookName.setBounds(370, 540, 180, 20);

        jLabel51.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Availability");
        kGradientPanel2.add(jLabel51);
        jLabel51.setBounds(560, 510, 120, 30);

        home_srch_bookAva.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        home_srch_bookAva.setForeground(new java.awt.Color(102, 204, 255));
        home_srch_bookAva.setText(" -");
        kGradientPanel2.add(home_srch_bookAva);
        home_srch_bookAva.setBounds(560, 540, 120, 20);

        kButton4.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton4.setText("More Details");
        kButton4.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        kButton4.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton4.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton4.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton4.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton4.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton4ActionPerformed(evt);
            }
        });
        kGradientPanel2.add(kButton4);
        kButton4.setBounds(370, 570, 120, 30);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel2.add(jLabel13);
        jLabel13.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel2);

        kGradientPanel9.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel9.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel9.setLayout(null);

        kGradientPanel14.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel14.setkBorderRadius(20);
        kGradientPanel14.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel14.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel14.setLayout(null);

        jLabel61.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Library Books");
        kGradientPanel14.add(jLabel61);
        jLabel61.setBounds(10, 5, 310, 40);

        jLabel62.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Manage books saved in the system.");
        kGradientPanel14.add(jLabel62);
        jLabel62.setBounds(10, 40, 310, 20);

        kGradientPanel9.add(kGradientPanel14);
        kGradientPanel14.setBounds(20, 70, 540, 70);

        jLabel74.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel9.add(jLabel74);
        jLabel74.setBounds(330, 170, 10, 500);

        kGradientPanel10.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel10.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel10.setkFillBackground(false);
        kGradientPanel10.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel10.setLayout(null);

        bookSearchtxt.setBackground(new java.awt.Color(0, 12, 19));
        bookSearchtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookSearchtxt.setForeground(new java.awt.Color(255, 255, 255));
        bookSearchtxt.setBorder(null);
        bookSearchtxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookSearchtxtMouseClicked(evt);
            }
        });
        bookSearchtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bookSearchtxtKeyReleased(evt);
            }
        });
        kGradientPanel10.add(bookSearchtxt);
        bookSearchtxt.setBounds(10, 10, 190, 20);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_search_16.png"))); // NOI18N
        kGradientPanel10.add(jLabel53);
        jLabel53.setBounds(210, 0, 20, 40);

        kGradientPanel9.add(kGradientPanel10);
        kGradientPanel10.setBounds(600, 100, 240, 40);

        jLabel54.setFont(new java.awt.Font("Leelawadee", 1, 16)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Selected Book Details");
        kGradientPanel9.add(jLabel54);
        jLabel54.setBounds(20, 170, 200, 20);

        jLabel55.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Rack Number");
        kGradientPanel9.add(jLabel55);
        jLabel55.setBounds(20, 367, 120, 20);

        jScrollPane1.setBackground(new java.awt.Color(0, 12, 19));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 100)));

        jTable1.setBackground(new java.awt.Color(0, 0, 22));
        jTable1.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jTable1.setForeground(new java.awt.Color(204, 204, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"hasintha", "nayanaijith", "asdxa", "adadc"},
                {null, null, null, null},
                {null, "axax", "asdxadx", null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(0, 0, 22));
        jTable1.setRowHeight(25);
        jTable1.setSelectionBackground(new java.awt.Color(0, 0, 100));
        jTable1.setSelectionForeground(new java.awt.Color(102, 204, 255));
        jTable1.setShowHorizontalLines(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        kGradientPanel9.add(jScrollPane1);
        jScrollPane1.setBounds(370, 180, 470, 480);

        bookIDlbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookIDlbl.setForeground(new java.awt.Color(102, 204, 255));
        kGradientPanel9.add(bookIDlbl);
        bookIDlbl.setBounds(20, 220, 250, 20);

        jLabel57.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Book ID");
        kGradientPanel9.add(jLabel57);
        jLabel57.setBounds(20, 200, 180, 20);

        kGradientPanel11.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel11.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel11.setkFillBackground(false);
        kGradientPanel11.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel11.setLayout(null);

        bookRacklbl.setBackground(new java.awt.Color(0, 12, 19));
        bookRacklbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookRacklbl.setForeground(new java.awt.Color(255, 255, 255));
        bookRacklbl.setBorder(null);
        kGradientPanel11.add(bookRacklbl);
        bookRacklbl.setBounds(10, 5, 80, 20);

        kGradientPanel9.add(kGradientPanel11);
        kGradientPanel11.setBounds(20, 390, 100, 30);

        jLabel58.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Search Book ID / Name / Author");
        kGradientPanel9.add(jLabel58);
        jLabel58.setBounds(600, 70, 240, 30);

        kGradientPanel12.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel12.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel12.setkFillBackground(false);
        kGradientPanel12.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel12.setLayout(null);

        bookNamelbl.setBackground(new java.awt.Color(0, 12, 19));
        bookNamelbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookNamelbl.setForeground(new java.awt.Color(255, 255, 255));
        bookNamelbl.setBorder(null);
        kGradientPanel12.add(bookNamelbl);
        bookNamelbl.setBounds(10, 5, 250, 20);

        kGradientPanel9.add(kGradientPanel12);
        kGradientPanel12.setBounds(20, 270, 270, 30);

        jLabel59.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Author");
        kGradientPanel9.add(jLabel59);
        jLabel59.setBounds(20, 307, 180, 20);

        kGradientPanel13.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel13.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel13.setkFillBackground(false);
        kGradientPanel13.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel13.setLayout(null);

        bookAuthorlbl.setBackground(new java.awt.Color(0, 12, 19));
        bookAuthorlbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookAuthorlbl.setForeground(new java.awt.Color(255, 255, 255));
        bookAuthorlbl.setBorder(null);
        kGradientPanel13.add(bookAuthorlbl);
        bookAuthorlbl.setBounds(10, 5, 250, 20);

        kGradientPanel9.add(kGradientPanel13);
        kGradientPanel13.setBounds(20, 330, 270, 30);

        jLabel60.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Register Date");
        kGradientPanel9.add(jLabel60);
        jLabel60.setBounds(170, 370, 80, 20);

        bookRegisterlbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookRegisterlbl.setForeground(new java.awt.Color(102, 204, 255));
        bookRegisterlbl.setText("-");
        kGradientPanel9.add(bookRegisterlbl);
        bookRegisterlbl.setBounds(170, 390, 120, 30);

        jLabel64.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("Availability");
        kGradientPanel9.add(jLabel64);
        jLabel64.setBounds(20, 440, 80, 20);

        bookAvailabilitylbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookAvailabilitylbl.setForeground(new java.awt.Color(102, 204, 255));
        bookAvailabilitylbl.setText("-");
        kGradientPanel9.add(bookAvailabilitylbl);
        bookAvailabilitylbl.setBounds(20, 460, 150, 30);

        kButton5.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton5.setText("Update");
        kButton5.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton5.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton5.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton5.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton5.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton5.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton5ActionPerformed(evt);
            }
        });
        kGradientPanel9.add(kButton5);
        kButton5.setBounds(20, 530, 130, 35);

        kButton6.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton6.setText("Dispose");
        kButton6.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton6.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton6.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton6.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton6.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton6.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton6ActionPerformed(evt);
            }
        });
        kGradientPanel9.add(kButton6);
        kButton6.setBounds(160, 530, 130, 35);

        jLabel126.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel126.setForeground(new java.awt.Color(255, 255, 255));
        jLabel126.setText("Book Name");
        kGradientPanel9.add(jLabel126);
        jLabel126.setBounds(20, 247, 180, 20);

        kButton22.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton22.setText("Clear");
        kButton22.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton22.setkEndColor(new java.awt.Color(240, 240, 240));
        kButton22.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton22.setkHoverEndColor(new java.awt.Color(250, 250, 250));
        kButton22.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton22.setkHoverStartColor(new java.awt.Color(250, 250, 250));
        kButton22.setkStartColor(new java.awt.Color(240, 240, 240));
        kButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton22ActionPerformed(evt);
            }
        });
        kGradientPanel9.add(kButton22);
        kButton22.setBounds(20, 580, 270, 35);

        jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel9.add(jLabel84);
        jLabel84.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel9);

        kGradientPanel15.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel15.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel15.setLayout(null);

        kGradientPanel16.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel16.setkBorderRadius(20);
        kGradientPanel16.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel16.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel16.setLayout(null);

        jLabel66.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setText("Library Members");
        kGradientPanel16.add(jLabel66);
        jLabel66.setBounds(10, 5, 310, 40);

        jLabel67.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Manage members saved in the system.");
        kGradientPanel16.add(jLabel67);
        jLabel67.setBounds(10, 40, 310, 20);

        kGradientPanel15.add(kGradientPanel16);
        kGradientPanel16.setBounds(20, 70, 540, 70);

        jLabel75.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel15.add(jLabel75);
        jLabel75.setBounds(330, 170, 10, 500);

        kGradientPanel17.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel17.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel17.setkFillBackground(false);
        kGradientPanel17.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel17.setLayout(null);

        mem_search.setBackground(new java.awt.Color(0, 12, 19));
        mem_search.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mem_search.setForeground(new java.awt.Color(255, 255, 255));
        mem_search.setBorder(null);
        mem_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mem_searchKeyReleased(evt);
            }
        });
        kGradientPanel17.add(mem_search);
        mem_search.setBounds(10, 10, 190, 20);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_search_16.png"))); // NOI18N
        kGradientPanel17.add(jLabel68);
        jLabel68.setBounds(210, 0, 20, 40);

        kGradientPanel15.add(kGradientPanel17);
        kGradientPanel17.setBounds(600, 100, 240, 40);

        jLabel69.setFont(new java.awt.Font("Leelawadee", 1, 16)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Selected Member Details");
        kGradientPanel15.add(jLabel69);
        jLabel69.setBounds(20, 170, 200, 20);

        jLabel70.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Email");
        kGradientPanel15.add(jLabel70);
        jLabel70.setBounds(20, 370, 120, 20);

        jScrollPane2.setBackground(new java.awt.Color(0, 12, 19));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 100)));

        jTable2.setBackground(new java.awt.Color(0, 0, 22));
        jTable2.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jTable2.setForeground(new java.awt.Color(204, 204, 204));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"hasintha", "nayanaijith", "asdxa", "adadc"},
                {null, null, null, null},
                {null, "axax", "asdxadx", null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable2.setGridColor(new java.awt.Color(0, 0, 22));
        jTable2.setRowHeight(25);
        jTable2.setSelectionBackground(new java.awt.Color(0, 0, 100));
        jTable2.setSelectionForeground(new java.awt.Color(102, 204, 255));
        jTable2.setShowHorizontalLines(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        kGradientPanel15.add(jScrollPane2);
        jScrollPane2.setBounds(370, 180, 470, 480);

        mem_idlbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mem_idlbl.setForeground(new java.awt.Color(102, 204, 255));
        mem_idlbl.setText("B0000");
        kGradientPanel15.add(mem_idlbl);
        mem_idlbl.setBounds(20, 220, 250, 20);

        jLabel72.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setText("Member ID");
        kGradientPanel15.add(jLabel72);
        jLabel72.setBounds(20, 200, 180, 20);

        kGradientPanel18.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel18.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel18.setkFillBackground(false);
        kGradientPanel18.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel18.setLayout(null);

        mem_emaillbl.setBackground(new java.awt.Color(0, 12, 19));
        mem_emaillbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mem_emaillbl.setForeground(new java.awt.Color(255, 255, 255));
        mem_emaillbl.setBorder(null);
        mem_emaillbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mem_emaillblActionPerformed(evt);
            }
        });
        kGradientPanel18.add(mem_emaillbl);
        mem_emaillbl.setBounds(10, 5, 250, 20);

        kGradientPanel15.add(kGradientPanel18);
        kGradientPanel18.setBounds(20, 390, 270, 30);

        jLabel73.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Name");
        kGradientPanel15.add(jLabel73);
        jLabel73.setBounds(20, 247, 180, 20);

        kGradientPanel19.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel19.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel19.setkFillBackground(false);
        kGradientPanel19.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel19.setLayout(null);

        mem_namelbl.setBackground(new java.awt.Color(0, 12, 19));
        mem_namelbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mem_namelbl.setForeground(new java.awt.Color(255, 255, 255));
        mem_namelbl.setBorder(null);
        kGradientPanel19.add(mem_namelbl);
        mem_namelbl.setBounds(10, 5, 250, 20);

        kGradientPanel15.add(kGradientPanel19);
        kGradientPanel19.setBounds(20, 270, 270, 30);

        jLabel76.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Address");
        kGradientPanel15.add(jLabel76);
        jLabel76.setBounds(20, 307, 180, 20);

        kGradientPanel20.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel20.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel20.setkFillBackground(false);
        kGradientPanel20.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel20.setLayout(null);

        mem_addresslbl.setBackground(new java.awt.Color(0, 12, 19));
        mem_addresslbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mem_addresslbl.setForeground(new java.awt.Color(255, 255, 255));
        mem_addresslbl.setBorder(null);
        kGradientPanel20.add(mem_addresslbl);
        mem_addresslbl.setBounds(10, 5, 250, 20);

        kGradientPanel15.add(kGradientPanel20);
        kGradientPanel20.setBounds(20, 330, 270, 30);

        jLabel77.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Register Date");
        kGradientPanel15.add(jLabel77);
        jLabel77.setBounds(170, 440, 80, 20);

        mem_regdatelbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mem_regdatelbl.setForeground(new java.awt.Color(102, 204, 255));
        kGradientPanel15.add(mem_regdatelbl);
        mem_regdatelbl.setBounds(170, 460, 120, 30);

        kButton8.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton8.setText("Update");
        kButton8.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton8.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton8.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton8.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton8.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton8.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton8ActionPerformed(evt);
            }
        });
        kGradientPanel15.add(kButton8);
        kButton8.setBounds(20, 530, 130, 35);

        kButton9.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton9.setText("Dispose");
        kButton9.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton9.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton9.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton9.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton9.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton9.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton9ActionPerformed(evt);
            }
        });
        kGradientPanel15.add(kButton9);
        kButton9.setBounds(160, 530, 130, 35);

        jLabel79.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Mobile Number");
        kGradientPanel15.add(jLabel79);
        jLabel79.setBounds(20, 435, 120, 20);

        kGradientPanel21.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel21.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel21.setkFillBackground(false);
        kGradientPanel21.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel21.setLayout(null);

        mem_mobilelbl.setBackground(new java.awt.Color(0, 12, 19));
        mem_mobilelbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mem_mobilelbl.setForeground(new java.awt.Color(255, 255, 255));
        mem_mobilelbl.setBorder(null);
        kGradientPanel21.add(mem_mobilelbl);
        mem_mobilelbl.setBounds(10, 5, 100, 20);

        kGradientPanel15.add(kGradientPanel21);
        kGradientPanel21.setBounds(20, 460, 120, 30);

        jLabel127.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel127.setForeground(new java.awt.Color(255, 255, 255));
        jLabel127.setText("Search Member ID / Name / Address");
        kGradientPanel15.add(jLabel127);
        jLabel127.setBounds(600, 70, 240, 30);

        kButton23.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton23.setText("Clear");
        kButton23.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton23.setkEndColor(new java.awt.Color(240, 240, 240));
        kButton23.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton23.setkHoverEndColor(new java.awt.Color(250, 250, 250));
        kButton23.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton23.setkHoverStartColor(new java.awt.Color(250, 250, 250));
        kButton23.setkStartColor(new java.awt.Color(240, 240, 240));
        kButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton23ActionPerformed(evt);
            }
        });
        kGradientPanel15.add(kButton23);
        kButton23.setBounds(20, 580, 270, 35);

        jLabel85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel15.add(jLabel85);
        jLabel85.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel15);

        kGradientPanel22.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel22.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel22.setLayout(null);

        kGradientPanel23.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel23.setkBorderRadius(20);
        kGradientPanel23.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel23.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel23.setLayout(null);

        jLabel80.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Library Administrators");
        kGradientPanel23.add(jLabel80);
        jLabel80.setBounds(10, 5, 310, 40);

        jLabel81.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Manage admins saved in the system.");
        kGradientPanel23.add(jLabel81);
        jLabel81.setBounds(10, 40, 310, 20);

        kGradientPanel22.add(kGradientPanel23);
        kGradientPanel23.setBounds(20, 70, 540, 70);

        jLabel82.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel22.add(jLabel82);
        jLabel82.setBounds(330, 170, 10, 500);

        kGradientPanel24.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel24.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel24.setkFillBackground(false);
        kGradientPanel24.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel24.setLayout(null);

        adminSearch.setBackground(new java.awt.Color(0, 12, 19));
        adminSearch.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        adminSearch.setForeground(new java.awt.Color(255, 255, 255));
        adminSearch.setBorder(null);
        adminSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adminSearchMouseClicked(evt);
            }
        });
        adminSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                adminSearchKeyReleased(evt);
            }
        });
        kGradientPanel24.add(adminSearch);
        adminSearch.setBounds(10, 10, 190, 20);

        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_search_16.png"))); // NOI18N
        kGradientPanel24.add(jLabel83);
        jLabel83.setBounds(210, 0, 20, 40);

        kGradientPanel22.add(kGradientPanel24);
        kGradientPanel24.setBounds(600, 100, 240, 40);

        jLabel86.setFont(new java.awt.Font("Leelawadee", 1, 16)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(255, 255, 255));
        jLabel86.setText("Selected Admin Details");
        kGradientPanel22.add(jLabel86);
        jLabel86.setBounds(20, 170, 200, 20);

        jLabel87.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(255, 255, 255));
        jLabel87.setText("Email");
        kGradientPanel22.add(jLabel87);
        jLabel87.setBounds(20, 367, 270, 20);

        jScrollPane3.setBackground(new java.awt.Color(0, 12, 19));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 100)));

        jTable3.setBackground(new java.awt.Color(0, 0, 22));
        jTable3.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jTable3.setForeground(new java.awt.Color(204, 204, 204));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"hasintha", "nayanaijith", "asdxa", "adadc"},
                {null, null, null, null},
                {null, "axax", "asdxadx", null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable3.setGridColor(new java.awt.Color(0, 0, 22));
        jTable3.setRowHeight(25);
        jTable3.setSelectionBackground(new java.awt.Color(0, 0, 100));
        jTable3.setSelectionForeground(new java.awt.Color(102, 204, 255));
        jTable3.setShowHorizontalLines(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jTable3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable3KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        kGradientPanel22.add(jScrollPane3);
        jScrollPane3.setBounds(370, 180, 470, 480);

        adminID.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        adminID.setForeground(new java.awt.Color(102, 204, 255));
        adminID.setText("B0000");
        kGradientPanel22.add(adminID);
        adminID.setBounds(20, 220, 250, 20);

        jLabel89.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(255, 255, 255));
        jLabel89.setText("Administrator ID");
        kGradientPanel22.add(jLabel89);
        jLabel89.setBounds(20, 200, 180, 20);

        kGradientPanel25.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel25.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel25.setkFillBackground(false);
        kGradientPanel25.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel25.setLayout(null);

        adminEmail.setBackground(new java.awt.Color(0, 12, 19));
        adminEmail.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        adminEmail.setForeground(new java.awt.Color(255, 255, 255));
        adminEmail.setBorder(null);
        kGradientPanel25.add(adminEmail);
        adminEmail.setBounds(10, 5, 100, 20);

        kGradientPanel22.add(kGradientPanel25);
        kGradientPanel25.setBounds(20, 390, 270, 30);

        jLabel90.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(255, 255, 255));
        jLabel90.setText("Name");
        kGradientPanel22.add(jLabel90);
        jLabel90.setBounds(20, 247, 180, 20);

        kGradientPanel26.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel26.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel26.setkFillBackground(false);
        kGradientPanel26.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel26.setLayout(null);

        adminName.setBackground(new java.awt.Color(0, 12, 19));
        adminName.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        adminName.setForeground(new java.awt.Color(255, 255, 255));
        adminName.setBorder(null);
        kGradientPanel26.add(adminName);
        adminName.setBounds(10, 5, 250, 20);

        kGradientPanel22.add(kGradientPanel26);
        kGradientPanel26.setBounds(20, 270, 270, 30);

        jLabel91.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 255, 255));
        jLabel91.setText("Address");
        kGradientPanel22.add(jLabel91);
        jLabel91.setBounds(20, 307, 180, 20);

        kGradientPanel27.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel27.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel27.setkFillBackground(false);
        kGradientPanel27.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel27.setLayout(null);

        adminAddress.setBackground(new java.awt.Color(0, 12, 19));
        adminAddress.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        adminAddress.setForeground(new java.awt.Color(255, 255, 255));
        adminAddress.setBorder(null);
        kGradientPanel27.add(adminAddress);
        adminAddress.setBounds(10, 5, 250, 20);

        kGradientPanel22.add(kGradientPanel27);
        kGradientPanel27.setBounds(20, 330, 270, 30);

        jLabel92.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(255, 255, 255));
        jLabel92.setText("Register Date");
        kGradientPanel22.add(jLabel92);
        jLabel92.setBounds(170, 430, 80, 20);

        adminRegister.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        adminRegister.setForeground(new java.awt.Color(102, 204, 255));
        kGradientPanel22.add(adminRegister);
        adminRegister.setBounds(170, 450, 120, 30);

        kButton11.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton11.setText("Update");
        kButton11.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton11.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton11.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton11.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton11.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton11.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton11ActionPerformed(evt);
            }
        });
        kGradientPanel22.add(kButton11);
        kButton11.setBounds(20, 530, 130, 35);

        kButton12.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton12.setText("Dispose");
        kButton12.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton12.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton12.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton12.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton12.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton12.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton12ActionPerformed(evt);
            }
        });
        kGradientPanel22.add(kButton12);
        kButton12.setBounds(160, 530, 130, 35);

        jLabel94.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(255, 255, 255));
        jLabel94.setText("Mobile Number");
        kGradientPanel22.add(jLabel94);
        jLabel94.setBounds(20, 430, 120, 20);

        kGradientPanel28.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel28.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel28.setkFillBackground(false);
        kGradientPanel28.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel28.setLayout(null);

        adminMobile.setBackground(new java.awt.Color(0, 12, 19));
        adminMobile.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        adminMobile.setForeground(new java.awt.Color(255, 255, 255));
        adminMobile.setBorder(null);
        kGradientPanel28.add(adminMobile);
        adminMobile.setBounds(10, 5, 100, 20);

        kGradientPanel22.add(kGradientPanel28);
        kGradientPanel28.setBounds(20, 450, 120, 30);

        jLabel128.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel128.setForeground(new java.awt.Color(255, 255, 255));
        jLabel128.setText("Search Admin ID / Name /Address");
        kGradientPanel22.add(jLabel128);
        jLabel128.setBounds(600, 70, 240, 30);

        kButton24.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton24.setText("Clear");
        kButton24.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton24.setkEndColor(new java.awt.Color(240, 240, 240));
        kButton24.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton24.setkHoverEndColor(new java.awt.Color(250, 250, 250));
        kButton24.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton24.setkHoverStartColor(new java.awt.Color(250, 250, 250));
        kButton24.setkStartColor(new java.awt.Color(240, 240, 240));
        kButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton24ActionPerformed(evt);
            }
        });
        kGradientPanel22.add(kButton24);
        kButton24.setBounds(20, 580, 270, 35);

        jLabel95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel22.add(jLabel95);
        jLabel95.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel22);

        kGradientPanel29.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel29.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel29.setLayout(null);

        kGradientPanel30.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel30.setkBorderRadius(20);
        kGradientPanel30.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel30.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel30.setLayout(null);

        jLabel96.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 255, 255));
        jLabel96.setText("Register New Members & Administrators");
        kGradientPanel30.add(jLabel96);
        jLabel96.setBounds(10, 5, 690, 40);

        jLabel97.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("Save the details of new members and admins here to efficient management.");
        kGradientPanel30.add(jLabel97);
        jLabel97.setBounds(10, 40, 410, 20);

        kGradientPanel29.add(kGradientPanel30);
        kGradientPanel30.setBounds(20, 70, 820, 70);

        jLabel100.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(153, 153, 153));
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel100.setText("Select User Type");
        kGradientPanel29.add(jLabel100);
        jLabel100.setBounds(80, 220, 130, 20);

        kGradientPanel77.setBackground(new java.awt.Color(255, 255, 255,0));
        kGradientPanel77.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel77.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel77.setLayout(null);

        jLabel166.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel166.setForeground(new java.awt.Color(102, 102, 102));
        jLabel166.setText("M0001 , M0002 , M0003 , .... , M0025 etc...");
        kGradientPanel77.add(jLabel166);
        jLabel166.setBounds(10, 50, 360, 20);

        jLabel167.setFont(new java.awt.Font("Leelawadee", 1, 16)); // NOI18N
        jLabel167.setForeground(new java.awt.Color(51, 51, 51));
        jLabel167.setText("Administrator ID");
        kGradientPanel77.add(jLabel167);
        jLabel167.setBounds(10, 80, 340, 20);

        jLabel168.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel168.setForeground(new java.awt.Color(102, 102, 102));
        jLabel168.setText("Member ID should start with Capital \"M\" letter & it should be like");
        kGradientPanel77.add(jLabel168);
        jLabel168.setBounds(10, 30, 360, 20);

        jLabel169.setFont(new java.awt.Font("Leelawadee", 1, 16)); // NOI18N
        jLabel169.setForeground(new java.awt.Color(51, 51, 51));
        jLabel169.setText("Member ID");
        kGradientPanel77.add(jLabel169);
        jLabel169.setBounds(10, 10, 340, 20);

        jLabel170.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel170.setForeground(new java.awt.Color(102, 102, 102));
        jLabel170.setText("A0001 , A0002 , A0003 , .... , A0025 etc...");
        kGradientPanel77.add(jLabel170);
        jLabel170.setBounds(10, 120, 360, 20);

        jLabel171.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel171.setForeground(new java.awt.Color(102, 102, 102));
        jLabel171.setText("Administrator ID should start with Capital \"A\" letter & it should be like");
        kGradientPanel77.add(jLabel171);
        jLabel171.setBounds(10, 100, 370, 20);

        kGradientPanel29.add(kGradientPanel77);
        kGradientPanel77.setBounds(80, 319, 390, 0);

        kGradientPanel33.setBackground(new java.awt.Color(255, 255, 255,0));
        kGradientPanel33.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel33.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel33.setLayout(null);

        kButton13.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton13.setText("Administrator");
        kButton13.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton13.setkBorderRadius(0);
        kButton13.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton13.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton13.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton13.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton13.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton13.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton13ActionPerformed(evt);
            }
        });
        kGradientPanel33.add(kButton13);
        kButton13.setBounds(0, 40, 200, 30);

        kButton14.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton14.setText("Library Member");
        kButton14.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton14.setkBorderRadius(0);
        kButton14.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton14.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton14.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton14.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton14.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton14.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton14ActionPerformed(evt);
            }
        });
        kGradientPanel33.add(kButton14);
        kButton14.setBounds(0, 10, 200, 30);

        kGradientPanel29.add(kGradientPanel33);
        kGradientPanel33.setBounds(80, 280, 200, 0);

        kGradientPanel32.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel32.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel32.setkFillBackground(false);
        kGradientPanel32.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel32.setLayout(null);

        uType.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uType.setForeground(new java.awt.Color(255, 255, 255));
        uType.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        uType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_expand_arrow_16.png"))); // NOI18N
        uType.setText("Select                              ");
        uType.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        uType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                uTypeMouseClicked(evt);
            }
        });
        kGradientPanel32.add(uType);
        uType.setBounds(10, 5, 180, 20);

        kGradientPanel29.add(kGradientPanel32);
        kGradientPanel32.setBounds(80, 250, 200, 30);

        kGradientPanel31.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel31.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel31.setkFillBackground(false);
        kGradientPanel31.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel31.setLayout(null);

        uName.setBackground(new java.awt.Color(0, 12, 19));
        uName.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uName.setForeground(new java.awt.Color(255, 255, 255));
        uName.setBorder(null);
        kGradientPanel31.add(uName);
        uName.setBounds(10, 5, 220, 20);

        kGradientPanel29.add(kGradientPanel31);
        kGradientPanel31.setBounds(320, 330, 240, 30);

        jLabel101.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel29.add(jLabel101);
        jLabel101.setBounds(40, 180, 10, 390);

        jLabel103.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(153, 153, 153));
        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel103.setText("02. Name");
        kGradientPanel29.add(jLabel103);
        jLabel103.setBounds(320, 300, 240, 20);

        jLabel104.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(153, 153, 153));
        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel104.setText("03. Address");
        kGradientPanel29.add(jLabel104);
        jLabel104.setBounds(600, 300, 240, 20);

        kGradientPanel34.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel34.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel34.setkFillBackground(false);
        kGradientPanel34.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel34.setLayout(null);

        uAddress.setBackground(new java.awt.Color(0, 12, 19));
        uAddress.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uAddress.setForeground(new java.awt.Color(255, 255, 255));
        uAddress.setBorder(null);
        kGradientPanel34.add(uAddress);
        uAddress.setBounds(10, 5, 220, 20);

        kGradientPanel29.add(kGradientPanel34);
        kGradientPanel34.setBounds(600, 330, 240, 30);

        jLabel105.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel105.setForeground(new java.awt.Color(153, 153, 153));
        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel105.setText("04. Mobile Number");
        kGradientPanel29.add(jLabel105);
        jLabel105.setBounds(80, 380, 200, 20);

        kGradientPanel35.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel35.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel35.setkFillBackground(false);
        kGradientPanel35.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel35.setLayout(null);

        uMobile.setBackground(new java.awt.Color(0, 12, 19));
        uMobile.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uMobile.setForeground(new java.awt.Color(255, 255, 255));
        uMobile.setBorder(null);
        kGradientPanel35.add(uMobile);
        uMobile.setBounds(10, 5, 170, 20);

        kGradientPanel29.add(kGradientPanel35);
        kGradientPanel35.setBounds(80, 410, 200, 30);

        jLabel106.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel106.setForeground(new java.awt.Color(153, 153, 153));
        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel106.setText("05. Email Address");
        kGradientPanel29.add(jLabel106);
        jLabel106.setBounds(320, 380, 240, 20);

        kGradientPanel36.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel36.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel36.setkFillBackground(false);
        kGradientPanel36.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel36.setLayout(null);

        uEmail.setBackground(new java.awt.Color(0, 12, 19));
        uEmail.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uEmail.setForeground(new java.awt.Color(255, 255, 255));
        uEmail.setBorder(null);
        kGradientPanel36.add(uEmail);
        uEmail.setBounds(10, 5, 220, 20);

        kGradientPanel29.add(kGradientPanel36);
        kGradientPanel36.setBounds(320, 410, 240, 30);

        jLabel107.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(153, 153, 153));
        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel107.setText("06. Registration Date");
        kGradientPanel29.add(jLabel107);
        jLabel107.setBounds(600, 380, 240, 20);

        kGradientPanel37.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel37.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel37.setkFillBackground(false);
        kGradientPanel37.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel37.setLayout(null);

        uRegisterDate.setBackground(new java.awt.Color(0, 12, 19));
        uRegisterDate.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uRegisterDate.setForeground(new java.awt.Color(255, 255, 255));
        uRegisterDate.setBorder(null);
        kGradientPanel37.add(uRegisterDate);
        uRegisterDate.setBounds(10, 5, 220, 20);

        kGradientPanel29.add(kGradientPanel37);
        kGradientPanel37.setBounds(600, 410, 240, 30);

        jLabel108.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(255, 255, 255));
        jLabel108.setText("New User Registration Form");
        kGradientPanel29.add(jLabel108);
        jLabel108.setBounds(80, 180, 390, 30);

        jLabel121.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(153, 153, 153));
        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel121.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_help_16.png"))); // NOI18N
        jLabel121.setText("01. User ID                                         ");
        jLabel121.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel121.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel121MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel121MouseExited(evt);
            }
        });
        kGradientPanel29.add(jLabel121);
        jLabel121.setBounds(80, 300, 200, 20);

        kGradientPanel51.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel51.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel51.setkFillBackground(false);
        kGradientPanel51.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel51.setLayout(null);

        uID.setBackground(new java.awt.Color(0, 12, 19));
        uID.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uID.setForeground(new java.awt.Color(255, 255, 255));
        uID.setBorder(null);
        kGradientPanel51.add(uID);
        uID.setBounds(10, 5, 180, 20);

        kGradientPanel29.add(kGradientPanel51);
        kGradientPanel51.setBounds(80, 330, 200, 30);

        kGradientPanel38.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel38.setkBorderRadius(20);
        kGradientPanel38.setkEndColor(new java.awt.Color(0,51, 255,50));
        kGradientPanel38.setkStartColor(new java.awt.Color(0,51, 255,50));
        kGradientPanel38.setLayout(null);

        jLabel99.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 255, 255));
        jLabel99.setText("LOGIN PASSWORD");
        kGradientPanel38.add(jLabel99);
        jLabel99.setBounds(18, 10, 162, 70);

        kGradientPanel39.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel39.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel39.setkFillBackground(false);
        kGradientPanel39.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel39.setLayout(null);

        uNewPassword.setBackground(new java.awt.Color(0, 12, 19));
        uNewPassword.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uNewPassword.setForeground(new java.awt.Color(255, 255, 255));
        uNewPassword.setBorder(null);
        kGradientPanel39.add(uNewPassword);
        uNewPassword.setBounds(10, 5, 170, 20);

        kGradientPanel38.add(kGradientPanel39);
        kGradientPanel39.setBounds(230, 40, 200, 30);

        jLabel110.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel110.setForeground(new java.awt.Color(153, 153, 153));
        jLabel110.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel110.setText("New Password");
        kGradientPanel38.add(jLabel110);
        jLabel110.setBounds(230, 10, 200, 30);

        jLabel111.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(153, 153, 153));
        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel111.setText("Confirm Password");
        kGradientPanel38.add(jLabel111);
        jLabel111.setBounds(470, 10, 200, 30);

        kGradientPanel40.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel40.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel40.setkFillBackground(false);
        kGradientPanel40.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel40.setLayout(null);

        uConfirmPassword.setBackground(new java.awt.Color(0, 12, 19));
        uConfirmPassword.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        uConfirmPassword.setForeground(new java.awt.Color(255, 255, 255));
        uConfirmPassword.setBorder(null);
        kGradientPanel40.add(uConfirmPassword);
        uConfirmPassword.setBounds(10, 5, 170, 20);

        kGradientPanel38.add(kGradientPanel40);
        kGradientPanel40.setBounds(470, 40, 200, 30);

        kGradientPanel29.add(kGradientPanel38);
        kGradientPanel38.setBounds(80, 480, 760, 90);

        kButton15.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton15.setText("Clear");
        kButton15.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton15.setkEndColor(new java.awt.Color(240, 240, 240));
        kButton15.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton15.setkHoverEndColor(new java.awt.Color(250, 250, 250));
        kButton15.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton15.setkHoverStartColor(new java.awt.Color(250, 250, 250));
        kButton15.setkStartColor(new java.awt.Color(240, 240, 240));
        kButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton15ActionPerformed(evt);
            }
        });
        kGradientPanel29.add(kButton15);
        kButton15.setBounds(370, 610, 230, 40);

        kButton16.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton16.setText("Register");
        kButton16.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton16.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton16.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton16.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton16.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton16.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton16ActionPerformed(evt);
            }
        });
        kGradientPanel29.add(kButton16);
        kButton16.setBounds(610, 610, 230, 40);

        jLabel109.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel29.add(jLabel109);
        jLabel109.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel29);

        kGradientPanel41.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel41.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel41.setLayout(null);

        kGradientPanel42.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel42.setkBorderRadius(20);
        kGradientPanel42.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel42.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel42.setLayout(null);

        jLabel98.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(255, 255, 255));
        jLabel98.setText("Register New Books");
        kGradientPanel42.add(jLabel98);
        jLabel98.setBounds(10, 5, 690, 40);

        jLabel102.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(255, 255, 255));
        jLabel102.setText("Save the details of new books here to manage them easily.");
        kGradientPanel42.add(jLabel102);
        jLabel102.setBounds(10, 40, 410, 20);

        kGradientPanel41.add(kGradientPanel42);
        kGradientPanel42.setBounds(20, 70, 820, 70);

        kGradientPanel78.setBackground(new java.awt.Color(255, 255, 255,0));
        kGradientPanel78.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel78.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel78.setLayout(null);

        jLabel172.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel172.setForeground(new java.awt.Color(102, 102, 102));
        jLabel172.setText("B0001 , B0002 , B0003 , ... , B0023 etc...");
        kGradientPanel78.add(jLabel172);
        jLabel172.setBounds(10, 50, 360, 20);

        jLabel174.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel174.setForeground(new java.awt.Color(102, 102, 102));
        jLabel174.setText("Book ID should start with Capital \"B\" letter & it should be like");
        kGradientPanel78.add(jLabel174);
        jLabel174.setBounds(10, 30, 360, 20);

        jLabel175.setFont(new java.awt.Font("Leelawadee", 1, 16)); // NOI18N
        jLabel175.setForeground(new java.awt.Color(51, 51, 51));
        jLabel175.setText("Book ID");
        kGradientPanel78.add(jLabel175);
        jLabel175.setBounds(10, 10, 340, 20);

        kGradientPanel41.add(kGradientPanel78);
        kGradientPanel78.setBounds(80, 250, 390, 0);

        kGradientPanel43.setBackground(new java.awt.Color(255, 255, 255,0));
        kGradientPanel43.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel43.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel43.setLayout(null);

        kButton17.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton17.setText("Administrator");
        kButton17.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton17.setkBorderRadius(0);
        kButton17.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton17.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton17.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton17.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton17.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton17.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton17ActionPerformed(evt);
            }
        });
        kGradientPanel43.add(kButton17);
        kButton17.setBounds(0, 40, 200, 30);

        kButton18.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton18.setText("Library Member");
        kButton18.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton18.setkBorderRadius(0);
        kButton18.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton18.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton18.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton18.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton18.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton18.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton18ActionPerformed(evt);
            }
        });
        kGradientPanel43.add(kButton18);
        kButton18.setBounds(0, 10, 200, 30);

        kGradientPanel41.add(kGradientPanel43);
        kGradientPanel43.setBounds(80, 290, 200, 0);

        kGradientPanel45.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel45.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel45.setkFillBackground(false);
        kGradientPanel45.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel45.setLayout(null);

        new_bookName.setBackground(new java.awt.Color(0, 12, 19));
        new_bookName.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        new_bookName.setForeground(new java.awt.Color(255, 255, 255));
        new_bookName.setBorder(null);
        kGradientPanel45.add(new_bookName);
        new_bookName.setBounds(10, 5, 220, 20);

        kGradientPanel41.add(kGradientPanel45);
        kGradientPanel45.setBounds(320, 260, 240, 30);

        jLabel113.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel41.add(jLabel113);
        jLabel113.setBounds(40, 180, 10, 390);

        jLabel114.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel114.setForeground(new java.awt.Color(153, 153, 153));
        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel114.setText("02. Book Name/Title");
        kGradientPanel41.add(jLabel114);
        jLabel114.setBounds(320, 230, 240, 20);

        jLabel115.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel115.setForeground(new java.awt.Color(153, 153, 153));
        jLabel115.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel115.setText("03. Author");
        kGradientPanel41.add(jLabel115);
        jLabel115.setBounds(600, 230, 240, 20);

        kGradientPanel46.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel46.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel46.setkFillBackground(false);
        kGradientPanel46.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel46.setLayout(null);

        new_bookAuthor.setBackground(new java.awt.Color(0, 12, 19));
        new_bookAuthor.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        new_bookAuthor.setForeground(new java.awt.Color(255, 255, 255));
        new_bookAuthor.setBorder(null);
        kGradientPanel46.add(new_bookAuthor);
        new_bookAuthor.setBounds(10, 5, 220, 20);

        kGradientPanel41.add(kGradientPanel46);
        kGradientPanel46.setBounds(600, 260, 240, 30);

        jLabel116.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel116.setForeground(new java.awt.Color(153, 153, 153));
        jLabel116.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel116.setText("04. Rack Number");
        kGradientPanel41.add(jLabel116);
        jLabel116.setBounds(80, 310, 200, 20);

        kGradientPanel47.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel47.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel47.setkFillBackground(false);
        kGradientPanel47.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel47.setLayout(null);

        new_bookRack.setBackground(new java.awt.Color(0, 12, 19));
        new_bookRack.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        new_bookRack.setForeground(new java.awt.Color(255, 255, 255));
        new_bookRack.setBorder(null);
        kGradientPanel47.add(new_bookRack);
        new_bookRack.setBounds(10, 5, 170, 20);

        kGradientPanel41.add(kGradientPanel47);
        kGradientPanel47.setBounds(80, 340, 200, 30);

        jLabel117.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel117.setForeground(new java.awt.Color(153, 153, 153));
        jLabel117.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel117.setText("05. Register Date");
        kGradientPanel41.add(jLabel117);
        jLabel117.setBounds(320, 310, 240, 20);

        kGradientPanel48.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel48.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel48.setkFillBackground(false);
        kGradientPanel48.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel48.setLayout(null);

        new_bookRegister.setBackground(new java.awt.Color(0, 12, 19));
        new_bookRegister.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        new_bookRegister.setForeground(new java.awt.Color(255, 255, 255));
        new_bookRegister.setBorder(null);
        kGradientPanel48.add(new_bookRegister);
        new_bookRegister.setBounds(10, 5, 220, 20);

        kGradientPanel41.add(kGradientPanel48);
        kGradientPanel48.setBounds(320, 340, 240, 30);

        jLabel120.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(153, 153, 153));
        jLabel120.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel120.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_help_16.png"))); // NOI18N
        jLabel120.setText("01. Book ID                                        ");
        jLabel120.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel120.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel120MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel120MouseExited(evt);
            }
        });
        kGradientPanel41.add(jLabel120);
        jLabel120.setBounds(80, 230, 200, 20);

        kGradientPanel50.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel50.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel50.setkFillBackground(false);
        kGradientPanel50.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel50.setLayout(null);

        new_bookID.setBackground(new java.awt.Color(0, 12, 19));
        new_bookID.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        new_bookID.setForeground(new java.awt.Color(255, 255, 255));
        new_bookID.setBorder(null);
        kGradientPanel50.add(new_bookID);
        new_bookID.setBounds(10, 5, 170, 20);

        kGradientPanel41.add(kGradientPanel50);
        kGradientPanel50.setBounds(80, 260, 200, 30);

        jLabel119.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(255, 255, 255));
        jLabel119.setText("New Book Registration Form");
        kGradientPanel41.add(jLabel119);
        jLabel119.setBounds(80, 180, 390, 30);

        kButton19.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton19.setText("Clear");
        kButton19.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton19.setkEndColor(new java.awt.Color(240, 240, 240));
        kButton19.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton19.setkHoverEndColor(new java.awt.Color(250, 250, 250));
        kButton19.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton19.setkHoverStartColor(new java.awt.Color(250, 250, 250));
        kButton19.setkStartColor(new java.awt.Color(240, 240, 240));
        kButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton19ActionPerformed(evt);
            }
        });
        kGradientPanel41.add(kButton19);
        kButton19.setBounds(370, 430, 230, 40);

        kButton20.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton20.setText("Register");
        kButton20.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton20.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton20.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton20.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton20.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton20.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton20ActionPerformed(evt);
            }
        });
        kGradientPanel41.add(kButton20);
        kButton20.setBounds(610, 430, 230, 40);

        jLabel123.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel41.add(jLabel123);
        jLabel123.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel41);

        kGradientPanel44.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel44.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel44.setLayout(null);

        kGradientPanel49.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel49.setkBorderRadius(20);
        kGradientPanel49.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel49.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel49.setLayout(null);

        jLabel112.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(255, 255, 255));
        jLabel112.setText("Issue Library Books");
        kGradientPanel49.add(jLabel112);
        jLabel112.setBounds(10, 5, 310, 40);

        jLabel118.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel118.setForeground(new java.awt.Color(255, 255, 255));
        jLabel118.setText("Issue Library books to members");
        kGradientPanel49.add(jLabel118);
        jLabel118.setBounds(10, 40, 310, 20);

        kGradientPanel44.add(kGradientPanel49);
        kGradientPanel49.setBounds(20, 70, 540, 70);

        jLabel122.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel44.add(jLabel122);
        jLabel122.setBounds(330, 170, 10, 500);

        kGradientPanel52.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel52.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel52.setkFillBackground(false);
        kGradientPanel52.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel52.setLayout(null);

        issueSearch.setBackground(new java.awt.Color(0, 12, 19));
        issueSearch.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        issueSearch.setForeground(new java.awt.Color(255, 255, 255));
        issueSearch.setBorder(null);
        issueSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                issueSearchMouseClicked(evt);
            }
        });
        issueSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                issueSearchKeyReleased(evt);
            }
        });
        kGradientPanel52.add(issueSearch);
        issueSearch.setBounds(10, 10, 190, 20);

        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel124.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_search_16.png"))); // NOI18N
        kGradientPanel52.add(jLabel124);
        jLabel124.setBounds(210, 0, 20, 40);

        kGradientPanel44.add(kGradientPanel52);
        kGradientPanel52.setBounds(600, 100, 240, 40);

        jLabel125.setFont(new java.awt.Font("Leelawadee", 1, 16)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(255, 255, 255));
        jLabel125.setText("Book Details");
        kGradientPanel44.add(jLabel125);
        jLabel125.setBounds(20, 170, 260, 20);

        jScrollPane4.setBackground(new java.awt.Color(0, 12, 19));
        jScrollPane4.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 100)));

        jTable4.setBackground(new java.awt.Color(0, 0, 22));
        jTable4.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jTable4.setForeground(new java.awt.Color(204, 204, 204));
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"hasintha", "nayanaijith", "asdxa", "adadc"},
                {null, null, null, null},
                {null, "axax", "asdxadx", null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable4.setGridColor(new java.awt.Color(0, 0, 22));
        jTable4.setRowHeight(25);
        jTable4.setSelectionBackground(new java.awt.Color(0, 0, 100));
        jTable4.setSelectionForeground(new java.awt.Color(102, 204, 255));
        jTable4.setShowHorizontalLines(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jTable4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable4KeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);

        kGradientPanel44.add(jScrollPane4);
        jScrollPane4.setBounds(370, 180, 470, 480);

        jLabel129.setFont(new java.awt.Font("Leelawadee", 0, 11)); // NOI18N
        jLabel129.setForeground(new java.awt.Color(204, 204, 204));
        jLabel129.setText("Fill below fields with correct details to issue the book.");
        kGradientPanel44.add(jLabel129);
        jLabel129.setBounds(20, 190, 270, 20);

        kGradientPanel54.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel54.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel54.setkFillBackground(false);
        kGradientPanel54.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel54.setLayout(null);

        issueBookID.setBackground(new java.awt.Color(0, 12, 19));
        issueBookID.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        issueBookID.setForeground(new java.awt.Color(255, 255, 255));
        issueBookID.setBorder(null);
        kGradientPanel54.add(issueBookID);
        issueBookID.setBounds(10, 5, 250, 20);

        kGradientPanel44.add(kGradientPanel54);
        kGradientPanel54.setBounds(20, 260, 270, 30);

        jLabel130.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(255, 255, 255));
        jLabel130.setText("Member ID");
        kGradientPanel44.add(jLabel130);
        jLabel130.setBounds(20, 300, 180, 20);

        kGradientPanel55.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel55.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel55.setkFillBackground(false);
        kGradientPanel55.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel55.setLayout(null);

        issueMemberID.setBackground(new java.awt.Color(0, 12, 19));
        issueMemberID.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        issueMemberID.setForeground(new java.awt.Color(255, 255, 255));
        issueMemberID.setBorder(null);
        kGradientPanel55.add(issueMemberID);
        issueMemberID.setBounds(10, 5, 250, 20);

        kGradientPanel44.add(kGradientPanel55);
        kGradientPanel55.setBounds(20, 330, 270, 30);

        kButton21.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton21.setText("Issue Book Now");
        kButton21.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton21.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton21.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton21.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton21.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton21.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton21ActionPerformed(evt);
            }
        });
        kGradientPanel44.add(kButton21);
        kButton21.setBounds(20, 530, 270, 35);

        jLabel131.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(255, 255, 255));
        jLabel131.setText("Date of Return");
        kGradientPanel44.add(jLabel131);
        jLabel131.setBounds(20, 370, 180, 20);

        kGradientPanel56.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel56.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel56.setkFillBackground(false);
        kGradientPanel56.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel56.setLayout(null);

        issueReturn.setBackground(new java.awt.Color(0, 12, 19));
        issueReturn.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        issueReturn.setForeground(new java.awt.Color(255, 255, 255));
        issueReturn.setBorder(null);
        kGradientPanel56.add(issueReturn);
        issueReturn.setBounds(10, 5, 250, 20);

        kGradientPanel44.add(kGradientPanel56);
        kGradientPanel56.setBounds(20, 395, 270, 30);

        jLabel132.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(255, 255, 255));
        jLabel132.setText("Date of Issue (Today)");
        kGradientPanel44.add(jLabel132);
        jLabel132.setBounds(20, 440, 180, 20);

        kGradientPanel57.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel57.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel57.setkFillBackground(false);
        kGradientPanel57.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel57.setLayout(null);

        issueDate.setBackground(new java.awt.Color(0, 12, 19));
        issueDate.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        issueDate.setForeground(new java.awt.Color(255, 255, 255));
        issueDate.setBorder(null);
        kGradientPanel57.add(issueDate);
        issueDate.setBounds(10, 5, 250, 20);

        kGradientPanel44.add(kGradientPanel57);
        kGradientPanel57.setBounds(20, 465, 270, 30);

        jLabel133.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(255, 255, 255));
        jLabel133.setText("Book ID");
        kGradientPanel44.add(jLabel133);
        jLabel133.setBounds(20, 230, 180, 20);

        jLabel134.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 255, 255));
        jLabel134.setText("Search Book ID / Member ID / Issued Date");
        kGradientPanel44.add(jLabel134);
        jLabel134.setBounds(600, 70, 240, 30);

        kButton35.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton35.setText("Clear");
        kButton35.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton35.setkEndColor(new java.awt.Color(240, 240, 240));
        kButton35.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton35.setkHoverEndColor(new java.awt.Color(250, 250, 250));
        kButton35.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton35.setkHoverStartColor(new java.awt.Color(250, 250, 250));
        kButton35.setkStartColor(new java.awt.Color(240, 240, 240));
        kButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton35ActionPerformed(evt);
            }
        });
        kGradientPanel44.add(kButton35);
        kButton35.setBounds(20, 575, 270, 35);

        jLabel135.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel44.add(jLabel135);
        jLabel135.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel44);

        kGradientPanel53.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel53.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel53.setLayout(null);

        kGradientPanel58.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel58.setkBorderRadius(20);
        kGradientPanel58.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel58.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel58.setLayout(null);

        jLabel136.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(255, 255, 255));
        jLabel136.setText("Return Issued Books");
        kGradientPanel58.add(jLabel136);
        jLabel136.setBounds(10, 5, 690, 40);

        jLabel137.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(255, 255, 255));
        jLabel137.setText("Save the details of new books here to manage them easily.");
        kGradientPanel58.add(jLabel137);
        jLabel137.setBounds(10, 40, 410, 20);

        kGradientPanel53.add(kGradientPanel58);
        kGradientPanel58.setBounds(20, 70, 820, 70);

        kGradientPanel59.setBackground(new java.awt.Color(255, 255, 255,0));
        kGradientPanel59.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel59.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel59.setLayout(null);

        kButton25.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton25.setText("Administrator");
        kButton25.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton25.setkBorderRadius(0);
        kButton25.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton25.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton25.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton25.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton25.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton25.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton25ActionPerformed(evt);
            }
        });
        kGradientPanel59.add(kButton25);
        kButton25.setBounds(0, 40, 200, 30);

        kButton26.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton26.setText("Library Member");
        kButton26.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton26.setkBorderRadius(0);
        kButton26.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton26.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton26.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton26.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton26.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton26.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton26ActionPerformed(evt);
            }
        });
        kGradientPanel59.add(kButton26);
        kButton26.setBounds(0, 10, 200, 30);

        kGradientPanel53.add(kGradientPanel59);
        kGradientPanel59.setBounds(80, 290, 200, 0);

        jLabel138.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel53.add(jLabel138);
        jLabel138.setBounds(40, 180, 10, 290);

        jLabel139.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel139.setForeground(new java.awt.Color(255, 255, 255));
        jLabel139.setText("Enter issued book details");
        kGradientPanel53.add(jLabel139);
        jLabel139.setBounds(80, 180, 310, 30);

        jLabel140.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(153, 153, 153));
        jLabel140.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel140.setText("Book ID");
        kGradientPanel53.add(jLabel140);
        jLabel140.setBounds(80, 225, 310, 20);

        kGradientPanel60.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel60.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel60.setkFillBackground(false);
        kGradientPanel60.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel60.setLayout(null);

        returnBook.setBackground(new java.awt.Color(0, 12, 19));
        returnBook.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        returnBook.setForeground(new java.awt.Color(255, 255, 255));
        returnBook.setBorder(null);
        kGradientPanel60.add(returnBook);
        returnBook.setBounds(10, 10, 280, 20);

        kGradientPanel53.add(kGradientPanel60);
        kGradientPanel60.setBounds(80, 250, 310, 40);

        jLabel141.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel141.setForeground(new java.awt.Color(153, 153, 153));
        jLabel141.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel141.setText("Date of Return");
        kGradientPanel53.add(jLabel141);
        jLabel141.setBounds(80, 315, 310, 20);

        kGradientPanel61.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel61.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel61.setkFillBackground(false);
        kGradientPanel61.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel61.setLayout(null);

        returnDate.setBackground(new java.awt.Color(0, 12, 19));
        returnDate.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        returnDate.setForeground(new java.awt.Color(255, 255, 255));
        returnDate.setBorder(null);
        kGradientPanel61.add(returnDate);
        returnDate.setBounds(10, 10, 280, 20);

        kGradientPanel53.add(kGradientPanel61);
        kGradientPanel61.setBounds(80, 340, 310, 40);

        kButton7.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton7.setText("Mark as Returned");
        kButton7.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton7.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton7.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton7.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton7.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton7.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton7ActionPerformed(evt);
            }
        });
        kGradientPanel53.add(kButton7);
        kButton7.setBounds(80, 410, 310, 40);

        jLabel145.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel53.add(jLabel145);
        jLabel145.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel53);

        kGradientPanel62.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel62.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel62.setLayout(null);

        kGradientPanel63.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel63.setkBorderRadius(20);
        kGradientPanel63.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel63.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel63.setLayout(null);

        jLabel142.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel142.setForeground(new java.awt.Color(255, 255, 255));
        jLabel142.setText("Privacy Settings");
        kGradientPanel63.add(jLabel142);
        jLabel142.setBounds(10, 5, 690, 40);

        jLabel143.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(255, 255, 255));
        jLabel143.setText("Change your login password.");
        kGradientPanel63.add(jLabel143);
        jLabel143.setBounds(10, 40, 410, 20);

        kGradientPanel62.add(kGradientPanel63);
        kGradientPanel63.setBounds(20, 70, 820, 70);

        kGradientPanel64.setBackground(new java.awt.Color(255, 255, 255,0));
        kGradientPanel64.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel64.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel64.setLayout(null);

        kButton27.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton27.setText("Administrator");
        kButton27.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton27.setkBorderRadius(0);
        kButton27.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton27.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton27.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton27.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton27.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton27.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton27ActionPerformed(evt);
            }
        });
        kGradientPanel64.add(kButton27);
        kButton27.setBounds(0, 40, 200, 30);

        kButton28.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton28.setText("Library Member");
        kButton28.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton28.setkBorderRadius(0);
        kButton28.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton28.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton28.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton28.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton28.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton28.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton28ActionPerformed(evt);
            }
        });
        kGradientPanel64.add(kButton28);
        kButton28.setBounds(0, 10, 200, 30);

        kGradientPanel62.add(kGradientPanel64);
        kGradientPanel64.setBounds(80, 290, 200, 0);

        jLabel144.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel62.add(jLabel144);
        jLabel144.setBounds(40, 180, 10, 360);

        jLabel146.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel146.setForeground(new java.awt.Color(255, 255, 255));
        jLabel146.setText("Change your login password");
        kGradientPanel62.add(jLabel146);
        jLabel146.setBounds(80, 180, 310, 30);

        jLabel147.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel147.setForeground(new java.awt.Color(153, 153, 153));
        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel147.setText("Current Password");
        kGradientPanel62.add(jLabel147);
        jLabel147.setBounds(80, 225, 310, 20);

        kGradientPanel65.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel65.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel65.setkFillBackground(false);
        kGradientPanel65.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel65.setLayout(null);

        currentPasswordtxt.setBackground(new java.awt.Color(0, 12, 19));
        currentPasswordtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        currentPasswordtxt.setForeground(new java.awt.Color(255, 255, 255));
        currentPasswordtxt.setBorder(null);
        kGradientPanel65.add(currentPasswordtxt);
        currentPasswordtxt.setBounds(10, 10, 280, 20);

        kGradientPanel62.add(kGradientPanel65);
        kGradientPanel65.setBounds(80, 250, 310, 40);

        jLabel148.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(153, 153, 153));
        jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel148.setText("Confirm New Password");
        kGradientPanel62.add(jLabel148);
        jLabel148.setBounds(80, 405, 310, 20);

        kGradientPanel66.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel66.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel66.setkFillBackground(false);
        kGradientPanel66.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel66.setLayout(null);

        confirmPasswordtxt.setBackground(new java.awt.Color(0, 12, 19));
        confirmPasswordtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        confirmPasswordtxt.setForeground(new java.awt.Color(255, 255, 255));
        confirmPasswordtxt.setBorder(null);
        kGradientPanel66.add(confirmPasswordtxt);
        confirmPasswordtxt.setBounds(10, 10, 280, 20);

        kGradientPanel62.add(kGradientPanel66);
        kGradientPanel66.setBounds(80, 430, 310, 40);

        kButton10.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton10.setText("Change Password");
        kButton10.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton10.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton10.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton10.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton10.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton10.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton10ActionPerformed(evt);
            }
        });
        kGradientPanel62.add(kButton10);
        kButton10.setBounds(80, 490, 310, 40);

        jLabel150.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel150.setForeground(new java.awt.Color(153, 153, 153));
        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel150.setText("New Password");
        kGradientPanel62.add(jLabel150);
        jLabel150.setBounds(80, 315, 310, 20);

        kGradientPanel67.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel67.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel67.setkFillBackground(false);
        kGradientPanel67.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel67.setLayout(null);

        newPasswordtxt.setBackground(new java.awt.Color(0, 12, 19));
        newPasswordtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        newPasswordtxt.setForeground(new java.awt.Color(255, 255, 255));
        newPasswordtxt.setBorder(null);
        kGradientPanel67.add(newPasswordtxt);
        newPasswordtxt.setBounds(10, 10, 280, 20);

        kGradientPanel62.add(kGradientPanel67);
        kGradientPanel67.setBounds(80, 340, 310, 40);

        jLabel149.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel62.add(jLabel149);
        jLabel149.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel62);

        kGradientPanel68.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel68.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel68.setLayout(null);

        kGradientPanel69.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel69.setkBorderRadius(20);
        kGradientPanel69.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel69.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel69.setLayout(null);

        jLabel151.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel151.setForeground(new java.awt.Color(255, 255, 255));
        jLabel151.setText("About Us");
        kGradientPanel69.add(jLabel151);
        jLabel151.setBounds(10, 5, 690, 40);

        jLabel152.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel152.setForeground(new java.awt.Color(255, 255, 255));
        jLabel152.setText("Want to know who developed this system?");
        kGradientPanel69.add(jLabel152);
        jLabel152.setBounds(10, 40, 410, 20);

        kGradientPanel68.add(kGradientPanel69);
        kGradientPanel69.setBounds(20, 70, 820, 70);

        kGradientPanel70.setBackground(new java.awt.Color(255, 255, 255,0));
        kGradientPanel70.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel70.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel70.setLayout(null);

        kButton29.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton29.setText("Administrator");
        kButton29.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton29.setkBorderRadius(0);
        kButton29.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton29.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton29.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton29.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton29.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton29.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton29ActionPerformed(evt);
            }
        });
        kGradientPanel70.add(kButton29);
        kButton29.setBounds(0, 40, 200, 30);

        kButton30.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton30.setText("Library Member");
        kButton30.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton30.setkBorderRadius(0);
        kButton30.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton30.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton30.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton30.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton30.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton30.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton30ActionPerformed(evt);
            }
        });
        kGradientPanel70.add(kButton30);
        kButton30.setBounds(0, 10, 200, 30);

        kGradientPanel68.add(kGradientPanel70);
        kGradientPanel70.setBounds(80, 290, 200, 0);

        jLabel153.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel153.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Prifile DP.png"))); // NOI18N
        kGradientPanel68.add(jLabel153);
        jLabel153.setBounds(260, 180, 320, 270);

        jLabel154.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel154.setForeground(new java.awt.Color(153, 153, 153));
        jLabel154.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel154.setText("Developed by");
        kGradientPanel68.add(jLabel154);
        jLabel154.setBounds(260, 470, 320, 20);

        jLabel155.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel155.setForeground(new java.awt.Color(255, 255, 255));
        jLabel155.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel155.setText("Hasintha Nayanajith (E128066)");
        kGradientPanel68.add(jLabel155);
        jLabel155.setBounds(260, 490, 320, 30);

        jLabel156.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel156.setForeground(new java.awt.Color(153, 153, 153));
        jLabel156.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel156.setText("For DiSE Final Project");
        kGradientPanel68.add(jLabel156);
        jLabel156.setBounds(260, 540, 320, 20);

        kButton31.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton31.setText("That's Great");
        kButton31.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton31.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton31.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton31.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton31.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton31.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton31ActionPerformed(evt);
            }
        });
        kGradientPanel68.add(kButton31);
        kButton31.setBounds(260, 590, 320, 40);

        jLabel158.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel68.add(jLabel158);
        jLabel158.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel68);

        kGradientPanel71.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel71.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel71.setLayout(null);

        kGradientPanel72.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel72.setkBorderRadius(20);
        kGradientPanel72.setkEndColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel72.setkStartColor(new java.awt.Color(255,255, 255,50));
        kGradientPanel72.setLayout(null);

        jLabel157.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel157.setForeground(new java.awt.Color(255, 255, 255));
        jLabel157.setText("Update Your Details");
        kGradientPanel72.add(jLabel157);
        jLabel157.setBounds(10, 5, 690, 40);

        jLabel159.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel159.setForeground(new java.awt.Color(255, 255, 255));
        jLabel159.setText("chnage your address, mobile number & email address.");
        kGradientPanel72.add(jLabel159);
        jLabel159.setBounds(10, 40, 410, 20);

        kGradientPanel71.add(kGradientPanel72);
        kGradientPanel72.setBounds(20, 70, 820, 70);

        kGradientPanel73.setBackground(new java.awt.Color(255, 255, 255,0));
        kGradientPanel73.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel73.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel73.setLayout(null);

        kButton32.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton32.setText("Administrator");
        kButton32.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton32.setkBorderRadius(0);
        kButton32.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton32.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton32.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton32.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton32.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton32.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton32ActionPerformed(evt);
            }
        });
        kGradientPanel73.add(kButton32);
        kButton32.setBounds(0, 40, 200, 30);

        kButton33.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton33.setText("Library Member");
        kButton33.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton33.setkBorderRadius(0);
        kButton33.setkEndColor(new java.awt.Color(255, 255, 255));
        kButton33.setkForeGround(new java.awt.Color(51, 51, 51));
        kButton33.setkHoverEndColor(new java.awt.Color(240, 240, 240));
        kButton33.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton33.setkHoverStartColor(new java.awt.Color(240, 240, 240));
        kButton33.setkStartColor(new java.awt.Color(255, 255, 255));
        kButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton33ActionPerformed(evt);
            }
        });
        kGradientPanel73.add(kButton33);
        kButton33.setBounds(0, 10, 200, 30);

        kGradientPanel71.add(kGradientPanel73);
        kGradientPanel73.setBounds(80, 290, 200, 0);

        jLabel160.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel71.add(jLabel160);
        jLabel160.setBounds(40, 180, 10, 360);

        jLabel161.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel161.setForeground(new java.awt.Color(255, 255, 255));
        jLabel161.setText("Update your own details");
        kGradientPanel71.add(jLabel161);
        jLabel161.setBounds(80, 180, 310, 30);

        jLabel162.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel162.setForeground(new java.awt.Color(153, 153, 153));
        jLabel162.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel162.setText("Address");
        kGradientPanel71.add(jLabel162);
        jLabel162.setBounds(80, 225, 310, 20);

        kGradientPanel74.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel74.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel74.setkFillBackground(false);
        kGradientPanel74.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel74.setLayout(null);

        addresstxt.setBackground(new java.awt.Color(0, 12, 19));
        addresstxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        addresstxt.setForeground(new java.awt.Color(255, 255, 255));
        addresstxt.setBorder(null);
        kGradientPanel74.add(addresstxt);
        addresstxt.setBounds(10, 10, 280, 20);

        kGradientPanel71.add(kGradientPanel74);
        kGradientPanel74.setBounds(80, 250, 310, 40);

        jLabel163.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel163.setForeground(new java.awt.Color(153, 153, 153));
        jLabel163.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel163.setText("Email Address");
        kGradientPanel71.add(jLabel163);
        jLabel163.setBounds(80, 405, 310, 20);

        kGradientPanel75.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel75.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel75.setkFillBackground(false);
        kGradientPanel75.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel75.setLayout(null);

        emailtxt.setBackground(new java.awt.Color(0, 12, 19));
        emailtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        emailtxt.setForeground(new java.awt.Color(255, 255, 255));
        emailtxt.setBorder(null);
        kGradientPanel75.add(emailtxt);
        emailtxt.setBounds(10, 10, 280, 20);

        kGradientPanel71.add(kGradientPanel75);
        kGradientPanel75.setBounds(80, 430, 310, 40);

        kButton34.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton34.setText("Update Details");
        kButton34.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        kButton34.setkEndColor(new java.awt.Color(0, 102, 255));
        kButton34.setkHoverEndColor(new java.awt.Color(0, 102, 204));
        kButton34.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton34.setkHoverStartColor(new java.awt.Color(0, 102, 204));
        kButton34.setkStartColor(new java.awt.Color(0, 102, 255));
        kButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton34ActionPerformed(evt);
            }
        });
        kGradientPanel71.add(kButton34);
        kButton34.setBounds(80, 490, 310, 40);

        jLabel164.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel164.setForeground(new java.awt.Color(153, 153, 153));
        jLabel164.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel164.setText("Mobile Number");
        kGradientPanel71.add(jLabel164);
        jLabel164.setBounds(80, 315, 310, 20);

        kGradientPanel76.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel76.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel76.setkFillBackground(false);
        kGradientPanel76.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel76.setLayout(null);

        mobiletxt.setBackground(new java.awt.Color(0, 12, 19));
        mobiletxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mobiletxt.setForeground(new java.awt.Color(255, 255, 255));
        mobiletxt.setBorder(null);
        kGradientPanel76.add(mobiletxt);
        mobiletxt.setBounds(10, 10, 280, 20);

        kGradientPanel71.add(kGradientPanel76);
        kGradientPanel76.setBounds(80, 340, 310, 40);

        jLabel165.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel71.add(jLabel165);
        jLabel165.setBounds(-10, -11, 930, 730);

        jTabbedPane1.addTab("tab1", kGradientPanel71);

        jPanel1.add(jTabbedPane1);
        jTabbedPane1.setBounds(220, -30, 900, 750);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1106, 704));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void kButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton1ActionPerformed

        java.lang.System.exit(0);
    }//GEN-LAST:event_kButton1ActionPerformed

    private void kButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton2ActionPerformed

        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_kButton2ActionPerformed

    private void kGradientPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel1MouseDragged

        this.setLocation(evt.getXOnScreen()-xMouse, evt.getYOnScreen()-yMouse);

    }//GEN-LAST:event_kGradientPanel1MouseDragged

    private void kGradientPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel1MousePressed

        xMouse = evt.getX();
        yMouse = evt.getY();

    }//GEN-LAST:event_kGradientPanel1MousePressed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked

        jTabbedPane1.setSelectedIndex(0);

    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered

        

    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited

        

    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        
        returnDate.setText(time);
        jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseEntered
        jLabel6.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel6MouseEntered

    private void jLabel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseExited
        jLabel6.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel6MouseExited

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        
        HomeScreenTablesSummary();
        jTabbedPane1.setSelectedIndex(0);
        
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        jLabel7.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        jLabel7.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked

        new_bookRegister.setText(time);
        
        new_bookID.setText("");
                new_bookName.setText("");
                new_bookAuthor.setText("");
                new_bookRack.setText("");
                //new_bookRegister.setText("");
        jTabbedPane1.setSelectedIndex(5);
        
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseEntered
        jLabel8.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel8MouseEntered

    private void jLabel8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseExited
        jLabel8.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel8MouseExited

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        issueDate.setText(time);
        IssuedBookDataClear();
        jTabbedPane1.setSelectedIndex(6);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        jLabel10.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
        jLabel10.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel10MouseExited

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseEntered
        //jLabel11.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel11MouseEntered

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseExited
        //jLabel7.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel11MouseExited

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        
        getLocalTime();
        uRegisterDate.setText(time);
        jTabbedPane1.setSelectedIndex(4);
        uType.setText("Library Member            ");
        uName.setText("");
                        uID.setText("");
                        uAddress.setText("");
                        uMobile.setText("");
                        uEmail.setText("");
                        uNewPassword.setText("");
                        uConfirmPassword.setText("");
  
        
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        jLabel12.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jLabel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseExited
        jLabel12.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel12MouseExited

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        
        getLocalTime();
        uRegisterDate.setText(time);
        jTabbedPane1.setSelectedIndex(4);
        uType.setText("Administrator                ");
        uName.setText("");
                        uID.setText("");
                        uAddress.setText("");
                        uMobile.setText("");
                        uEmail.setText("");
                        uNewPassword.setText("");
                        uConfirmPassword.setText("");
  
        
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jLabel14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseEntered
        jLabel14.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel14MouseEntered

    private void jLabel14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseExited
        jLabel14.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel14MouseExited

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseEntered
        //jLabel15.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel15MouseEntered

    private void jLabel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseExited
        //jLabel15.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel15MouseExited

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        
        currentPasswordtxt.setText("");
        newPasswordtxt.setText("");
        confirmPasswordtxt.setText("");
        jTabbedPane1.setSelectedIndex(8);
        
    }//GEN-LAST:event_jLabel16MouseClicked

    private void jLabel16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseEntered
        jLabel16.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel16MouseEntered

    private void jLabel16MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseExited
        jLabel16.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel16MouseExited

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        
        LoginFrame obj = new LoginFrame();
        obj.setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_jLabel17MouseClicked

    private void jLabel17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseEntered
        jLabel17.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel17MouseEntered

    private void jLabel17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseExited
        jLabel17.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel17MouseExited

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        
        BookDataClear();
        jTabbedPane1.setSelectedIndex(1);
        
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseEntered
        jLabel18.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel18MouseEntered

    private void jLabel18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseExited
        jLabel18.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel18MouseExited

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        
        MemberDataClear();
        jTabbedPane1.setSelectedIndex(2);
        
    }//GEN-LAST:event_jLabel19MouseClicked

    private void jLabel19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseEntered
        jLabel19.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel19MouseEntered

    private void jLabel19MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseExited
        jLabel19.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel19MouseExited

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        
        AdminDataClear();
        jTabbedPane1.setSelectedIndex(3);
        
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jLabel20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseEntered
        jLabel20.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel20MouseEntered

    private void jLabel20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseExited
        jLabel20.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel20MouseExited

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        jTabbedPane1.setSelectedIndex(9);
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseEntered
        jLabel21.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel21MouseEntered

    private void jLabel21MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseExited
        jLabel21.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel21MouseExited

    private void kButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton3ActionPerformed
        
        addresstxt.setText(W_addresstxt.getText());
                        mobiletxt.setText(W_mobiletxt.getText());
                        emailtxt.setText(W_emailtxt.getText());
        jTabbedPane1.setSelectedIndex(10);
    }//GEN-LAST:event_kButton3ActionPerformed

    private void kButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton5ActionPerformed
        BookDataUpdate();
    }//GEN-LAST:event_kButton5ActionPerformed

    private void kButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton6ActionPerformed
        
        BookDispose();
        
    }//GEN-LAST:event_kButton6ActionPerformed

    private void kButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton8ActionPerformed
        MemberDataUpdate();
    }//GEN-LAST:event_kButton8ActionPerformed

    private void kButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton9ActionPerformed
        
        MemberDispose();
        
    }//GEN-LAST:event_kButton9ActionPerformed

    private void kButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton11ActionPerformed
        
        AdminDataUpdate();
        
    }//GEN-LAST:event_kButton11ActionPerformed

    private void kButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton12ActionPerformed
        AdminDispose();
    }//GEN-LAST:event_kButton12ActionPerformed

    private void kButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton13ActionPerformed
        
        uType.setText("Administrator                ");
        kGradientPanel33.setSize(200,0);
        
    }//GEN-LAST:event_kButton13ActionPerformed

    private void kButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton14ActionPerformed
        
        uType.setText("Library Member            ");
        kGradientPanel33.setSize(200,0);
        
    }//GEN-LAST:event_kButton14ActionPerformed

    private void uTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_uTypeMouseClicked
        
        int h = kGradientPanel33.getHeight();
        
        if(h==0){
        
            kGradientPanel33.setSize(200,80);
            
        }else{
        
            kGradientPanel33.setSize(200,0);
        
        }
        
    }//GEN-LAST:event_uTypeMouseClicked

    private void kButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton17ActionPerformed

    private void kButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton18ActionPerformed

    private void kButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton21ActionPerformed
        
        IssuedBookDataInsert();
        
    }//GEN-LAST:event_kButton21ActionPerformed

    private void kButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton25ActionPerformed

    private void kButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton26ActionPerformed

    private void kButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton27ActionPerformed

    private void kButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton28ActionPerformed

    private void kButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton29ActionPerformed

    private void kButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton30ActionPerformed

    private void kButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton31ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_kButton31ActionPerformed

    private void kButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton32ActionPerformed

    private void kButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton33ActionPerformed

    private void Home_SrchTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Home_SrchTxtKeyReleased
        
        String one  = Home_SrchTxt.getText();
        
        if(one.equals("")){
        
                home_srch_bookName.setText("-");
                home_srch_bookAva.setText("-");    
            
        }else{
        
            HomeScreenBookSearch();
        
        }
        
    }//GEN-LAST:event_Home_SrchTxtKeyReleased

    private void kButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_kButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        
        BookTableDataSelect();
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        
        BookTableDataSelect();
        
    }//GEN-LAST:event_jTable1KeyReleased

    private void bookSearchtxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bookSearchtxtKeyReleased
        
        BookSearch();
        
    }//GEN-LAST:event_bookSearchtxtKeyReleased

    private void kButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton22ActionPerformed
        
        BookDataClear();
        
    }//GEN-LAST:event_kButton22ActionPerformed

    private void bookSearchtxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookSearchtxtMouseClicked
        
        BookDataClear();
        
    }//GEN-LAST:event_bookSearchtxtMouseClicked

    private void kButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton16ActionPerformed
        
        try {
            RegisterNewUser();
        } catch (SQLException ex) {
            Logger.getLogger(AdminFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_kButton16ActionPerformed

    private void kButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton15ActionPerformed
        
        uName.setText("");
                        uID.setText("");
                        uAddress.setText("");
                        uMobile.setText("");
                        uEmail.setText("");
                        uNewPassword.setText("");
                        uConfirmPassword.setText("");
  
                        AdminTableLoad();
                        MemberTableLoad();
        
    }//GEN-LAST:event_kButton15ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        
        MemberDataSelect();
        
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTable2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyReleased
        
        MemberDataSelect();
        
    }//GEN-LAST:event_jTable2KeyReleased

    private void mem_emaillblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mem_emaillblActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mem_emaillblActionPerformed

    private void kButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton23ActionPerformed
        MemberDataClear();
    }//GEN-LAST:event_kButton23ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        
        AdminDataSelect();
        
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTable3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable3KeyReleased
        
        AdminDataSelect();
        
    }//GEN-LAST:event_jTable3KeyReleased

    private void adminSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminSearchMouseClicked
       
        AdminDataClear();
        
    }//GEN-LAST:event_adminSearchMouseClicked

    private void kButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton24ActionPerformed
        
        AdminDataClear();
        
    }//GEN-LAST:event_kButton24ActionPerformed

    private void adminSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_adminSearchKeyReleased
        
        AdminDataSearch();
        
    }//GEN-LAST:event_adminSearchKeyReleased

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        IssueTableDataSelect();
    }//GEN-LAST:event_jTable4MouseClicked

    private void jTable4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable4KeyReleased
        IssueTableDataSelect();
    }//GEN-LAST:event_jTable4KeyReleased

    private void issueSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_issueSearchMouseClicked
        IssuedBookDataClear();
    }//GEN-LAST:event_issueSearchMouseClicked

    private void kButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton35ActionPerformed
        
        IssuedBookDataClear();
        
    }//GEN-LAST:event_kButton35ActionPerformed

    private void kButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton19ActionPerformed
        
        new_bookRegister.setText(time);
        
        new_bookID.setText("");
                new_bookName.setText("");
                new_bookAuthor.setText("");
                new_bookRack.setText("");
                //new_bookRegister.setText("");
                
                BookTableLoad();
        
    }//GEN-LAST:event_kButton19ActionPerformed

    private void kButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton20ActionPerformed
        BookInsert();
    }//GEN-LAST:event_kButton20ActionPerformed

    private void kButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton7ActionPerformed
        
        IssuedBookReturn();
        
    }//GEN-LAST:event_kButton7ActionPerformed

    private void kButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton10ActionPerformed
        PasswordChange();
    }//GEN-LAST:event_kButton10ActionPerformed

    private void kButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton34ActionPerformed
        
        UserDataUpdate();
        HomeScreenUserDetails();
    }//GEN-LAST:event_kButton34ActionPerformed

    private void issueSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_issueSearchKeyReleased
        
        IssuedBookDataSearch();
        
    }//GEN-LAST:event_issueSearchKeyReleased

    private void mem_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mem_searchKeyReleased
        
        MemberDataSearch();
        
    }//GEN-LAST:event_mem_searchKeyReleased

    private void jLabel121MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel121MouseEntered
        
        kGradientPanel77.setSize(390,150);
        
    }//GEN-LAST:event_jLabel121MouseEntered

    private void jLabel121MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel121MouseExited
        kGradientPanel77.setSize(390,0);
    }//GEN-LAST:event_jLabel121MouseExited

    private void jLabel120MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel120MouseEntered
        kGradientPanel78.setSize(390,80);
    }//GEN-LAST:event_jLabel120MouseEntered

    private void jLabel120MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel120MouseExited
        kGradientPanel78.setSize(390,0);
    }//GEN-LAST:event_jLabel120MouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Home_SrchTxt;
    private javax.swing.JLabel T_Admins;
    private javax.swing.JLabel T_Books;
    private javax.swing.JLabel T_IssuedBooks;
    private javax.swing.JLabel T_Members;
    private javax.swing.JLabel W_addresstxt;
    private javax.swing.JLabel W_emailtxt;
    private javax.swing.JLabel W_mobiletxt;
    private javax.swing.JLabel W_nametxt;
    private javax.swing.JLabel W_registertxt;
    private javax.swing.JLabel WelcomeIdTxt;
    private javax.swing.JLabel WelcomeTxt;
    private javax.swing.JTextField addresstxt;
    private javax.swing.JTextField adminAddress;
    private javax.swing.JTextField adminEmail;
    private javax.swing.JLabel adminID;
    private javax.swing.JTextField adminMobile;
    private javax.swing.JTextField adminName;
    private javax.swing.JLabel adminRegister;
    private javax.swing.JTextField adminSearch;
    private javax.swing.JTextField bookAuthorlbl;
    private javax.swing.JLabel bookAvailabilitylbl;
    private javax.swing.JLabel bookIDlbl;
    private javax.swing.JTextField bookNamelbl;
    private javax.swing.JTextField bookRacklbl;
    private javax.swing.JLabel bookRegisterlbl;
    private javax.swing.JTextField bookSearchtxt;
    private javax.swing.JTextField confirmPasswordtxt;
    private javax.swing.JTextField currentPasswordtxt;
    private javax.swing.JTextField emailtxt;
    private javax.swing.JLabel home_srch_bookAva;
    private javax.swing.JLabel home_srch_bookName;
    private javax.swing.JTextField issueBookID;
    private javax.swing.JTextField issueDate;
    private javax.swing.JTextField issueMemberID;
    private javax.swing.JTextField issueReturn;
    private javax.swing.JTextField issueSearch;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private com.k33ptoo.components.KButton kButton1;
    private com.k33ptoo.components.KButton kButton10;
    private com.k33ptoo.components.KButton kButton11;
    private com.k33ptoo.components.KButton kButton12;
    private com.k33ptoo.components.KButton kButton13;
    private com.k33ptoo.components.KButton kButton14;
    private com.k33ptoo.components.KButton kButton15;
    private com.k33ptoo.components.KButton kButton16;
    private com.k33ptoo.components.KButton kButton17;
    private com.k33ptoo.components.KButton kButton18;
    private com.k33ptoo.components.KButton kButton19;
    private com.k33ptoo.components.KButton kButton2;
    private com.k33ptoo.components.KButton kButton20;
    private com.k33ptoo.components.KButton kButton21;
    private com.k33ptoo.components.KButton kButton22;
    private com.k33ptoo.components.KButton kButton23;
    private com.k33ptoo.components.KButton kButton24;
    private com.k33ptoo.components.KButton kButton25;
    private com.k33ptoo.components.KButton kButton26;
    private com.k33ptoo.components.KButton kButton27;
    private com.k33ptoo.components.KButton kButton28;
    private com.k33ptoo.components.KButton kButton29;
    private com.k33ptoo.components.KButton kButton3;
    private com.k33ptoo.components.KButton kButton30;
    private com.k33ptoo.components.KButton kButton31;
    private com.k33ptoo.components.KButton kButton32;
    private com.k33ptoo.components.KButton kButton33;
    private com.k33ptoo.components.KButton kButton34;
    private com.k33ptoo.components.KButton kButton35;
    private com.k33ptoo.components.KButton kButton4;
    private com.k33ptoo.components.KButton kButton5;
    private com.k33ptoo.components.KButton kButton6;
    private com.k33ptoo.components.KButton kButton7;
    private com.k33ptoo.components.KButton kButton8;
    private com.k33ptoo.components.KButton kButton9;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel10;
    private com.k33ptoo.components.KGradientPanel kGradientPanel11;
    private com.k33ptoo.components.KGradientPanel kGradientPanel12;
    private com.k33ptoo.components.KGradientPanel kGradientPanel13;
    private com.k33ptoo.components.KGradientPanel kGradientPanel14;
    private com.k33ptoo.components.KGradientPanel kGradientPanel15;
    private com.k33ptoo.components.KGradientPanel kGradientPanel16;
    private com.k33ptoo.components.KGradientPanel kGradientPanel17;
    private com.k33ptoo.components.KGradientPanel kGradientPanel18;
    private com.k33ptoo.components.KGradientPanel kGradientPanel19;
    private com.k33ptoo.components.KGradientPanel kGradientPanel2;
    private com.k33ptoo.components.KGradientPanel kGradientPanel20;
    private com.k33ptoo.components.KGradientPanel kGradientPanel21;
    private com.k33ptoo.components.KGradientPanel kGradientPanel22;
    private com.k33ptoo.components.KGradientPanel kGradientPanel23;
    private com.k33ptoo.components.KGradientPanel kGradientPanel24;
    private com.k33ptoo.components.KGradientPanel kGradientPanel25;
    private com.k33ptoo.components.KGradientPanel kGradientPanel26;
    private com.k33ptoo.components.KGradientPanel kGradientPanel27;
    private com.k33ptoo.components.KGradientPanel kGradientPanel28;
    private com.k33ptoo.components.KGradientPanel kGradientPanel29;
    private com.k33ptoo.components.KGradientPanel kGradientPanel3;
    private com.k33ptoo.components.KGradientPanel kGradientPanel30;
    private com.k33ptoo.components.KGradientPanel kGradientPanel31;
    private com.k33ptoo.components.KGradientPanel kGradientPanel32;
    private com.k33ptoo.components.KGradientPanel kGradientPanel33;
    private com.k33ptoo.components.KGradientPanel kGradientPanel34;
    private com.k33ptoo.components.KGradientPanel kGradientPanel35;
    private com.k33ptoo.components.KGradientPanel kGradientPanel36;
    private com.k33ptoo.components.KGradientPanel kGradientPanel37;
    private com.k33ptoo.components.KGradientPanel kGradientPanel38;
    private com.k33ptoo.components.KGradientPanel kGradientPanel39;
    private com.k33ptoo.components.KGradientPanel kGradientPanel4;
    private com.k33ptoo.components.KGradientPanel kGradientPanel40;
    private com.k33ptoo.components.KGradientPanel kGradientPanel41;
    private com.k33ptoo.components.KGradientPanel kGradientPanel42;
    private com.k33ptoo.components.KGradientPanel kGradientPanel43;
    private com.k33ptoo.components.KGradientPanel kGradientPanel44;
    private com.k33ptoo.components.KGradientPanel kGradientPanel45;
    private com.k33ptoo.components.KGradientPanel kGradientPanel46;
    private com.k33ptoo.components.KGradientPanel kGradientPanel47;
    private com.k33ptoo.components.KGradientPanel kGradientPanel48;
    private com.k33ptoo.components.KGradientPanel kGradientPanel49;
    private com.k33ptoo.components.KGradientPanel kGradientPanel5;
    private com.k33ptoo.components.KGradientPanel kGradientPanel50;
    private com.k33ptoo.components.KGradientPanel kGradientPanel51;
    private com.k33ptoo.components.KGradientPanel kGradientPanel52;
    private com.k33ptoo.components.KGradientPanel kGradientPanel53;
    private com.k33ptoo.components.KGradientPanel kGradientPanel54;
    private com.k33ptoo.components.KGradientPanel kGradientPanel55;
    private com.k33ptoo.components.KGradientPanel kGradientPanel56;
    private com.k33ptoo.components.KGradientPanel kGradientPanel57;
    private com.k33ptoo.components.KGradientPanel kGradientPanel58;
    private com.k33ptoo.components.KGradientPanel kGradientPanel59;
    private com.k33ptoo.components.KGradientPanel kGradientPanel6;
    private com.k33ptoo.components.KGradientPanel kGradientPanel60;
    private com.k33ptoo.components.KGradientPanel kGradientPanel61;
    private com.k33ptoo.components.KGradientPanel kGradientPanel62;
    private com.k33ptoo.components.KGradientPanel kGradientPanel63;
    private com.k33ptoo.components.KGradientPanel kGradientPanel64;
    private com.k33ptoo.components.KGradientPanel kGradientPanel65;
    private com.k33ptoo.components.KGradientPanel kGradientPanel66;
    private com.k33ptoo.components.KGradientPanel kGradientPanel67;
    private com.k33ptoo.components.KGradientPanel kGradientPanel68;
    private com.k33ptoo.components.KGradientPanel kGradientPanel69;
    private com.k33ptoo.components.KGradientPanel kGradientPanel7;
    private com.k33ptoo.components.KGradientPanel kGradientPanel70;
    private com.k33ptoo.components.KGradientPanel kGradientPanel71;
    private com.k33ptoo.components.KGradientPanel kGradientPanel72;
    private com.k33ptoo.components.KGradientPanel kGradientPanel73;
    private com.k33ptoo.components.KGradientPanel kGradientPanel74;
    private com.k33ptoo.components.KGradientPanel kGradientPanel75;
    private com.k33ptoo.components.KGradientPanel kGradientPanel76;
    private com.k33ptoo.components.KGradientPanel kGradientPanel77;
    private com.k33ptoo.components.KGradientPanel kGradientPanel78;
    private com.k33ptoo.components.KGradientPanel kGradientPanel8;
    private com.k33ptoo.components.KGradientPanel kGradientPanel9;
    private javax.swing.JLabel localTime;
    private javax.swing.JTextField mem_addresslbl;
    private javax.swing.JTextField mem_emaillbl;
    private javax.swing.JLabel mem_idlbl;
    private javax.swing.JTextField mem_mobilelbl;
    private javax.swing.JTextField mem_namelbl;
    private javax.swing.JLabel mem_regdatelbl;
    private javax.swing.JTextField mem_search;
    private javax.swing.JTextField mobiletxt;
    private javax.swing.JTextField newPasswordtxt;
    private javax.swing.JTextField new_bookAuthor;
    private javax.swing.JTextField new_bookID;
    private javax.swing.JTextField new_bookName;
    private javax.swing.JTextField new_bookRack;
    private javax.swing.JTextField new_bookRegister;
    private javax.swing.JTextField returnBook;
    private javax.swing.JTextField returnDate;
    private javax.swing.JTextField uAddress;
    private javax.swing.JTextField uConfirmPassword;
    private javax.swing.JTextField uEmail;
    private javax.swing.JTextField uID;
    private javax.swing.JTextField uMobile;
    private javax.swing.JTextField uName;
    private javax.swing.JTextField uNewPassword;
    private javax.swing.JTextField uRegisterDate;
    private javax.swing.JLabel uType;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icons8_books_32.png")));
        
    }
}

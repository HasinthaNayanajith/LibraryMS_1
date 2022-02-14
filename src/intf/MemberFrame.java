
package intf;

import dise_lib_ms.DB_Connect;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import net.proteanit.sql.DbUtils;



public class MemberFrame extends javax.swing.JFrame {

    int xMouse;
    int yMouse;
    
    String UserID;
    String PWord;
    
    //Connecting package with database
    Connection conn = null;
    PreparedStatement pet = null;
    ResultSet rst = null;
    
    public MemberFrame() {
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
        
        BookTableLoad();
        getLocalTime();
        HomeScreenBorrowedBooks();
        
        
    }
    
    public void WelcomeHome(String id,String pw){
    
        String uID = UserID =id;
        PWord = pw;
        
        HomeScreenUserDetails();
        
    }
    
    public void HomeScreenUserDetails(){
    
        try {
            
            String sql = "select * from member_table where MemberID='"+UserID+"'";
            pet=conn.prepareStatement(sql);
            rst=pet.executeQuery();
            
            if(rst.next()){
            
            String one = rst.getString("FullName");
            W_nametxt.setText(one);
            W_addressTxt.setText(rst.getString("Address"));
            W_mobileTxt.setText(rst.getString("Contact"));
            W_emailTxt.setText(rst.getString("Email"));
            W_registerOnTxt.setText(rst.getString("RegDate"));
            
            WelcomeTxt.setText("Welcome back, "+one);
            WelcomeIdTxt.setText("Member Registration ID : "+UserID);
                
            }
            
         } catch (Exception e) {
            
            customMessageFrame("An unexpected query error occured! \nError code - 0002 \n");
            
        }
        
        HomeScreenBorrowedBooks();
        getLocalTime();
        BookTableLoad();
        
    }
    
    public void getLocalTime(){
    
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy 'at' HH:mm");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String one = now.format(dtf);
        
        String two = now.format(dtf2);
        localTime.setText(two);
        
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
    
    public void HomeScreenBorrowedBooks(){
    
        String id = UserID;
        
        try {
            
            String sql = "select * from issued_books where MemberID='"+id+"'";
            pet = conn.prepareStatement(sql);
            rst = pet.executeQuery();
            
            if(rst.next()){
            
                home_borrowed_books.setText("You have borrowed 1 books.");
                String bookID = rst.getString("BookID");
                String returnDate = rst.getString("DateOfReturn");
                
                String sql2 = "select * from book_table where BookID='"+bookID+"'";
                pet = conn.prepareStatement(sql2);
                rst = pet.executeQuery();
                
                if(rst.next()){
                
                    String bookName = rst.getString("BookName");
                    home_borrowed_name.setText(bookName+" ("+bookID+")");
                    home_borrowed_name1.setText("Return this book to library on or before "+returnDate);
                }
                
                
            }else{
            
                home_borrowed_books.setText("You have borrowed 0 books.");
                home_borrowed_name.setText("");
                home_borrowed_name1.setText("");
                
            }
            
        } catch (Exception e) {
            
            String txt = "An error occured while searching borrowed books.";
            customMessageFrame(txt);
            
        }
    
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

    public void BookDataSelect(){
    
        int row = jTable1.getSelectedRow();
        
        bookIDlbl.setText(jTable1.getValueAt(row,0).toString());
        bookNamelbl.setText(jTable1.getValueAt(row,1).toString());
        bookAuthorlbl.setText(jTable1.getValueAt(row,2).toString());
        bookAvailabilitylbl.setText(jTable1.getValueAt(row,5).toString());
        bookRacklbl.setText(jTable1.getValueAt(row,3).toString());
        
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
            obj.Error("An unexpected error occured! \nError code - 0002 (MemberView Book Search Query) \n"+e);
            obj.setVisible(true);
            
        }
        
    }
    
    public void BookDataClear(){
    
        bookSearchtxt.setText("");
        bookIDlbl.setText("-");
        bookNamelbl.setText("-");
        bookAuthorlbl.setText("-");
        bookRacklbl.setText("-");
        bookAvailabilitylbl.setText("-");
        
        BookTableLoad();
        
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
    
    public void MemberDataUpdate(){
    
        String one = addresstxt.getText();
        String two = mobiletxt.getText();
        String three = emailtxt.getText();
        
        if(one.equals("") || two.equals("") || three.equals("")){
        
            ErrorFrame obj = new ErrorFrame();
            obj.Error("Address , Mobile Number & Email can not be null values.\nPlease keep existing details if you have no any new details to add.\n\nThen, Try again.");
            obj.setVisible(true);
            
            addresstxt.setText(W_addressTxt.getText());
                        mobiletxt.setText(W_mobileTxt.getText());
                        emailtxt.setText(W_emailTxt.getText());
            
        }else{
        
            try {
                
            String sql = "UPDATE member_table SET Address='"+one+"',Contact='"+two+"',Email='"+three+"' WHERE MemberID='"+UserID+"'";
            pet=conn.prepareStatement(sql);
            pet.execute();
            
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Success! Your Address, Mobile & Email has been successfully updated.\nAddress : "+one+"\nMobile Number : "+two+"\nEmail Address : "+three);
                        obj.setVisible(true);
                        
                        HomeScreenUserDetails();
                        
                        addresstxt.setText(W_addressTxt.getText());
                        mobiletxt.setText(W_mobileTxt.getText());
                        emailtxt.setText(W_emailTxt.getText());
            
                
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
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel3 = new com.k33ptoo.components.KGradientPanel();
        WelcomeTxt = new javax.swing.JLabel();
        WelcomeIdTxt = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        localTime = new javax.swing.JLabel();
        kButton3 = new com.k33ptoo.components.KButton();
        jLabel3 = new javax.swing.JLabel();
        W_nametxt = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        W_addressTxt = new javax.swing.JLabel();
        W_mobileTxt = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        W_emailTxt = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        W_registerOnTxt = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        home_borrowed_books = new javax.swing.JLabel();
        home_borrowed_name = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        home_srch_bookName = new javax.swing.JLabel();
        kGradientPanel5 = new com.k33ptoo.components.KGradientPanel();
        Home_SrchTxt = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        home_srch_bookAva = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        kButton4 = new com.k33ptoo.components.KButton();
        home_borrowed_name1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        kGradientPanel4 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel6 = new com.k33ptoo.components.KGradientPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        kGradientPanel7 = new com.k33ptoo.components.KGradientPanel();
        bookSearchtxt = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel39 = new javax.swing.JLabel();
        bookIDlbl = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        bookAvailabilitylbl = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        bookAuthorlbl = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        bookNamelbl = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        bookRacklbl = new javax.swing.JLabel();
        kButton22 = new com.k33ptoo.components.KButton();
        jLabel13 = new javax.swing.JLabel();
        kGradientPanel8 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel9 = new com.k33ptoo.components.KGradientPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        kGradientPanel10 = new com.k33ptoo.components.KGradientPanel();
        currentPasswordtxt = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        kGradientPanel11 = new com.k33ptoo.components.KGradientPanel();
        newPasswordtxt = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        kGradientPanel12 = new com.k33ptoo.components.KGradientPanel();
        confirmPasswordtxt = new javax.swing.JTextField();
        kButton5 = new com.k33ptoo.components.KButton();
        jLabel66 = new javax.swing.JLabel();
        kGradientPanel13 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel14 = new com.k33ptoo.components.KGradientPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        kGradientPanel15 = new com.k33ptoo.components.KGradientPanel();
        addresstxt = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        kGradientPanel16 = new com.k33ptoo.components.KGradientPanel();
        mobiletxt = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        kGradientPanel17 = new com.k33ptoo.components.KGradientPanel();
        emailtxt = new javax.swing.JTextField();
        kButton6 = new com.k33ptoo.components.KButton();
        jLabel67 = new javax.swing.JLabel();
        kGradientPanel18 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel19 = new com.k33ptoo.components.KGradientPanel();
        jLabel65 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        kButton7 = new com.k33ptoo.components.KButton();
        jLabel74 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Esoft Library Member Window");
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
        jLabel9.setText("ESOFT LIBRARY MANAGEMENT SYSTEM ( MEMBER VIEW )");
        kGradientPanel1.add(jLabel9);
        jLabel9.setBounds(25, 20, 370, 20);

        jPanel1.add(kGradientPanel1);
        kGradientPanel1.setBounds(-10, -10, 1120, 50);

        jLabel2.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_security_lock_16.png"))); // NOI18N
        jLabel2.setText(" Privacy");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 180, 160, 30);

        jLabel4.setFont(new java.awt.Font("Leelawadee", 1, 30)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_menu_32.png"))); // NOI18N
        jLabel4.setText(" MENU");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 63, 140, 30);

        jLabel5.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_home_16.png"))); // NOI18N
        jLabel5.setText(" Overview");
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
        jLabel5.setBounds(20, 110, 160, 30);

        jLabel6.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_book_16.png"))); // NOI18N
        jLabel6.setText(" Books");
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
        jLabel6.setBounds(20, 145, 160, 30);

        jLabel7.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_logout_rounded_up_16.png"))); // NOI18N
        jLabel7.setText(" Logout");
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
        jLabel7.setBounds(20, 650, 160, 30);

        jLabel34.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_about_16.png"))); // NOI18N
        jLabel34.setText(" About Us");
        jLabel34.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel34MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel34MouseExited(evt);
            }
        });
        jPanel1.add(jLabel34);
        jLabel34.setBounds(20, 215, 160, 30);

        jLabel50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Menu Panel.png"))); // NOI18N
        jPanel1.add(jLabel50);
        jLabel50.setBounds(-10, -10, 250, 730);

        kGradientPanel2.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel2.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel2.setLayout(null);

        kGradientPanel3.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel3.setkBorderRadius(20);
        kGradientPanel3.setkEndColor(new java.awt.Color(255,255,255,50));
        kGradientPanel3.setkStartColor(new java.awt.Color(255,255,255,50));
        kGradientPanel3.setLayout(null);

        WelcomeTxt.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        WelcomeTxt.setForeground(new java.awt.Color(255, 255, 255));
        WelcomeTxt.setText("Welcome back, Hasintha Nayanajith");
        kGradientPanel3.add(WelcomeTxt);
        WelcomeTxt.setBounds(10, 5, 540, 40);

        WelcomeIdTxt.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        WelcomeIdTxt.setForeground(new java.awt.Color(255, 255, 255));
        WelcomeIdTxt.setText("welcomeIdtxt");
        kGradientPanel3.add(WelcomeIdTxt);
        WelcomeIdTxt.setBounds(10, 40, 540, 20);

        jLabel73.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Today");
        kGradientPanel3.add(jLabel73);
        jLabel73.setBounds(680, 10, 70, 30);

        localTime.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        localTime.setForeground(new java.awt.Color(255, 255, 255));
        localTime.setText("2021-12-25");
        kGradientPanel3.add(localTime);
        localTime.setBounds(680, 30, 130, 30);

        kGradientPanel2.add(kGradientPanel3);
        kGradientPanel3.setBounds(10, 80, 810, 70);

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
        kButton3.setBounds(20, 480, 190, 40);

        jLabel3.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Member Details ( You )");
        kGradientPanel2.add(jLabel3);
        jLabel3.setBounds(20, 190, 200, 20);

        W_nametxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_nametxt.setForeground(new java.awt.Color(102, 204, 255));
        W_nametxt.setText("Hasintha Nayanajith BW");
        kGradientPanel2.add(W_nametxt);
        W_nametxt.setBounds(20, 240, 240, 20);

        jLabel14.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Type Book ID / Name / Author");
        kGradientPanel2.add(jLabel14);
        jLabel14.setBounds(350, 210, 180, 30);

        jLabel15.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Address");
        kGradientPanel2.add(jLabel15);
        jLabel15.setBounds(20, 270, 180, 20);

        W_addressTxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_addressTxt.setForeground(new java.awt.Color(102, 204, 255));
        W_addressTxt.setText("Hettipola, Kurunegala");
        kGradientPanel2.add(W_addressTxt);
        W_addressTxt.setBounds(20, 290, 240, 20);

        W_mobileTxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_mobileTxt.setForeground(new java.awt.Color(102, 204, 255));
        W_mobileTxt.setText("077 55 47 47 3");
        kGradientPanel2.add(W_mobileTxt);
        W_mobileTxt.setBounds(20, 340, 240, 20);

        jLabel18.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Mobile Number");
        kGradientPanel2.add(jLabel18);
        jLabel18.setBounds(20, 320, 180, 20);

        W_emailTxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_emailTxt.setForeground(new java.awt.Color(102, 204, 255));
        W_emailTxt.setText("hasintha.payoneer@gmail.com");
        kGradientPanel2.add(W_emailTxt);
        W_emailTxt.setBounds(20, 390, 240, 20);

        jLabel20.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Email");
        kGradientPanel2.add(jLabel20);
        jLabel20.setBounds(20, 370, 180, 20);

        W_registerOnTxt.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        W_registerOnTxt.setForeground(new java.awt.Color(102, 204, 255));
        W_registerOnTxt.setText("2021-12-25");
        kGradientPanel2.add(W_registerOnTxt);
        W_registerOnTxt.setBounds(20, 440, 240, 20);

        jLabel22.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Registered On");
        kGradientPanel2.add(jLabel22);
        jLabel22.setBounds(20, 420, 180, 20);

        jLabel23.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Borrowed Books");
        kGradientPanel2.add(jLabel23);
        jLabel23.setBounds(350, 410, 220, 20);

        home_borrowed_books.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        home_borrowed_books.setForeground(new java.awt.Color(255, 255, 255));
        home_borrowed_books.setText("You have borrowed 0 books");
        kGradientPanel2.add(home_borrowed_books);
        home_borrowed_books.setBounds(350, 440, 220, 20);

        home_borrowed_name.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        home_borrowed_name.setForeground(new java.awt.Color(102, 204, 255));
        home_borrowed_name.setText("Robinson Crooso ( B0002 )");
        kGradientPanel2.add(home_borrowed_name);
        home_borrowed_name.setBounds(350, 460, 220, 20);

        jLabel26.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel2.add(jLabel26);
        jLabel26.setBounds(300, 190, 10, 335);

        jLabel27.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Search Books Availability");
        kGradientPanel2.add(jLabel27);
        jLabel27.setBounds(350, 190, 240, 20);

        jLabel28.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Book Name");
        kGradientPanel2.add(jLabel28);
        jLabel28.setBounds(350, 290, 110, 30);

        home_srch_bookName.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        home_srch_bookName.setForeground(new java.awt.Color(102, 204, 255));
        home_srch_bookName.setText(" -");
        kGradientPanel2.add(home_srch_bookName);
        home_srch_bookName.setBounds(350, 320, 180, 20);

        kGradientPanel5.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel5.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel5.setkFillBackground(false);
        kGradientPanel5.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel5.setLayout(null);

        Home_SrchTxt.setBackground(new java.awt.Color(0, 12, 19));
        Home_SrchTxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        Home_SrchTxt.setForeground(new java.awt.Color(255, 255, 255));
        Home_SrchTxt.setBorder(null);
        Home_SrchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Home_SrchTxtKeyReleased(evt);
            }
        });
        kGradientPanel5.add(Home_SrchTxt);
        Home_SrchTxt.setBounds(10, 10, 250, 20);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_search_16.png"))); // NOI18N
        kGradientPanel5.add(jLabel30);
        jLabel30.setBounds(270, 0, 16, 40);

        kGradientPanel2.add(kGradientPanel5);
        kGradientPanel5.setBounds(350, 240, 310, 40);

        home_srch_bookAva.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        home_srch_bookAva.setForeground(new java.awt.Color(102, 204, 255));
        home_srch_bookAva.setText(" -");
        kGradientPanel2.add(home_srch_bookAva);
        home_srch_bookAva.setBounds(540, 320, 120, 20);

        jLabel32.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Availability");
        kGradientPanel2.add(jLabel32);
        jLabel32.setBounds(540, 290, 120, 30);

        jLabel33.setFont(new java.awt.Font("Leelawadee", 0, 10)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Name");
        kGradientPanel2.add(jLabel33);
        jLabel33.setBounds(20, 220, 180, 20);

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
        kButton4.setBounds(350, 350, 120, 30);

        home_borrowed_name1.setFont(new java.awt.Font("Leelawadee", 1, 12)); // NOI18N
        home_borrowed_name1.setForeground(new java.awt.Color(102, 204, 255));
        kGradientPanel2.add(home_borrowed_name1);
        home_borrowed_name1.setBounds(350, 480, 310, 20);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel2.add(jLabel8);
        jLabel8.setBounds(-10, 3, 900, 723);

        jTabbedPane1.addTab("tab1", kGradientPanel2);

        kGradientPanel4.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel4.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel4.setLayout(null);

        kGradientPanel6.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel6.setkBorderRadius(20);
        kGradientPanel6.setkEndColor(new java.awt.Color(255,255,255,50));
        kGradientPanel6.setkStartColor(new java.awt.Color(255,255,255,50));
        kGradientPanel6.setLayout(null);

        jLabel35.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Explore Library Books");
        kGradientPanel6.add(jLabel35);
        jLabel35.setBounds(10, 5, 540, 40);

        jLabel36.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Search Book ID / Name or Author");
        kGradientPanel6.add(jLabel36);
        jLabel36.setBounds(10, 40, 540, 20);

        kGradientPanel4.add(kGradientPanel6);
        kGradientPanel6.setBounds(10, 80, 810, 70);

        kGradientPanel7.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel7.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel7.setkFillBackground(false);
        kGradientPanel7.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel7.setLayout(null);

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
        kGradientPanel7.add(bookSearchtxt);
        bookSearchtxt.setBounds(10, 10, 190, 20);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_search_16.png"))); // NOI18N
        kGradientPanel7.add(jLabel37);
        jLabel37.setBounds(210, 0, 20, 40);

        kGradientPanel4.add(kGradientPanel7);
        kGradientPanel7.setBounds(20, 200, 240, 40);

        jLabel38.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel4.add(jLabel38);
        jLabel38.setBounds(300, 190, 10, 490);

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

        kGradientPanel4.add(jScrollPane1);
        jScrollPane1.setBounds(350, 200, 470, 470);

        jLabel39.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Book ID");
        kGradientPanel4.add(jLabel39);
        jLabel39.setBounds(20, 300, 180, 20);

        bookIDlbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookIDlbl.setForeground(new java.awt.Color(102, 204, 255));
        bookIDlbl.setText("-");
        kGradientPanel4.add(bookIDlbl);
        bookIDlbl.setBounds(20, 320, 250, 20);

        jLabel41.setFont(new java.awt.Font("Leelawadee", 1, 16)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Selected Book Details");
        kGradientPanel4.add(jLabel41);
        jLabel41.setBounds(20, 270, 200, 20);

        jLabel42.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Availability");
        kGradientPanel4.add(jLabel42);
        jLabel42.setBounds(20, 480, 180, 20);

        bookAvailabilitylbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookAvailabilitylbl.setForeground(new java.awt.Color(102, 204, 255));
        bookAvailabilitylbl.setText("-");
        kGradientPanel4.add(bookAvailabilitylbl);
        bookAvailabilitylbl.setBounds(20, 500, 250, 20);

        jLabel44.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Author");
        kGradientPanel4.add(jLabel44);
        jLabel44.setBounds(20, 420, 180, 20);

        bookAuthorlbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookAuthorlbl.setForeground(new java.awt.Color(102, 204, 255));
        bookAuthorlbl.setText("-");
        kGradientPanel4.add(bookAuthorlbl);
        bookAuthorlbl.setBounds(20, 440, 250, 20);

        jLabel46.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Book Name/Title");
        kGradientPanel4.add(jLabel46);
        jLabel46.setBounds(20, 360, 180, 20);

        bookNamelbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookNamelbl.setForeground(new java.awt.Color(102, 204, 255));
        bookNamelbl.setText("-");
        kGradientPanel4.add(bookNamelbl);
        bookNamelbl.setBounds(20, 380, 250, 20);

        jLabel48.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Rack Number");
        kGradientPanel4.add(jLabel48);
        jLabel48.setBounds(20, 540, 180, 20);

        bookRacklbl.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        bookRacklbl.setForeground(new java.awt.Color(102, 204, 255));
        bookRacklbl.setText("-");
        kGradientPanel4.add(bookRacklbl);
        bookRacklbl.setBounds(20, 560, 250, 20);

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
        kGradientPanel4.add(kButton22);
        kButton22.setBounds(20, 600, 140, 35);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel4.add(jLabel13);
        jLabel13.setBounds(-10, 3, 900, 723);

        jTabbedPane1.addTab("tab1", kGradientPanel4);

        kGradientPanel8.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel8.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel8.setLayout(null);

        kGradientPanel9.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel9.setkBorderRadius(20);
        kGradientPanel9.setkEndColor(new java.awt.Color(255,255,255,50));
        kGradientPanel9.setkStartColor(new java.awt.Color(255,255,255,50));
        kGradientPanel9.setLayout(null);

        jLabel51.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Privacy Settings");
        kGradientPanel9.add(jLabel51);
        jLabel51.setBounds(10, 5, 540, 40);

        jLabel52.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Change your login password");
        kGradientPanel9.add(jLabel52);
        jLabel52.setBounds(10, 40, 540, 20);

        kGradientPanel8.add(kGradientPanel9);
        kGradientPanel9.setBounds(10, 80, 810, 70);

        jLabel53.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(153, 153, 153));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("Current Password");
        kGradientPanel8.add(jLabel53);
        jLabel53.setBounds(80, 245, 310, 20);

        kGradientPanel10.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel10.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel10.setkFillBackground(false);
        kGradientPanel10.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel10.setLayout(null);

        currentPasswordtxt.setBackground(new java.awt.Color(0, 12, 19));
        currentPasswordtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        currentPasswordtxt.setForeground(new java.awt.Color(255, 255, 255));
        currentPasswordtxt.setBorder(null);
        kGradientPanel10.add(currentPasswordtxt);
        currentPasswordtxt.setBounds(10, 10, 280, 20);

        kGradientPanel8.add(kGradientPanel10);
        kGradientPanel10.setBounds(80, 270, 310, 40);

        jLabel54.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Change Your Login Password");
        kGradientPanel8.add(jLabel54);
        jLabel54.setBounds(80, 200, 310, 30);

        jLabel55.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel8.add(jLabel55);
        jLabel55.setBounds(40, 200, 10, 370);

        jLabel56.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(153, 153, 153));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel56.setText("New Password");
        kGradientPanel8.add(jLabel56);
        jLabel56.setBounds(80, 335, 310, 20);

        kGradientPanel11.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel11.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel11.setkFillBackground(false);
        kGradientPanel11.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel11.setLayout(null);

        newPasswordtxt.setBackground(new java.awt.Color(0, 12, 19));
        newPasswordtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        newPasswordtxt.setForeground(new java.awt.Color(255, 255, 255));
        newPasswordtxt.setBorder(null);
        kGradientPanel11.add(newPasswordtxt);
        newPasswordtxt.setBounds(10, 10, 280, 20);

        kGradientPanel8.add(kGradientPanel11);
        kGradientPanel11.setBounds(80, 360, 310, 40);

        jLabel57.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(153, 153, 153));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("Confirm New Password");
        kGradientPanel8.add(jLabel57);
        jLabel57.setBounds(80, 425, 310, 20);

        kGradientPanel12.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel12.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel12.setkFillBackground(false);
        kGradientPanel12.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel12.setLayout(null);

        confirmPasswordtxt.setBackground(new java.awt.Color(0, 12, 19));
        confirmPasswordtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        confirmPasswordtxt.setForeground(new java.awt.Color(255, 255, 255));
        confirmPasswordtxt.setBorder(null);
        kGradientPanel12.add(confirmPasswordtxt);
        confirmPasswordtxt.setBounds(10, 10, 280, 20);

        kGradientPanel8.add(kGradientPanel12);
        kGradientPanel12.setBounds(80, 450, 310, 40);

        kButton5.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton5.setText("Change Password");
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
        kGradientPanel8.add(kButton5);
        kButton5.setBounds(80, 520, 310, 40);

        jLabel66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel8.add(jLabel66);
        jLabel66.setBounds(-10, 3, 900, 723);

        jTabbedPane1.addTab("tab1", kGradientPanel8);

        kGradientPanel13.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel13.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel13.setLayout(null);

        kGradientPanel14.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel14.setkBorderRadius(20);
        kGradientPanel14.setkEndColor(new java.awt.Color(255,255,255,50));
        kGradientPanel14.setkStartColor(new java.awt.Color(255,255,255,50));
        kGradientPanel14.setLayout(null);

        jLabel58.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Update Member Details");
        kGradientPanel14.add(jLabel58);
        jLabel58.setBounds(10, 5, 540, 40);

        jLabel59.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("You can update your address, mobile number & email.");
        kGradientPanel14.add(jLabel59);
        jLabel59.setBounds(10, 40, 540, 20);

        kGradientPanel13.add(kGradientPanel14);
        kGradientPanel14.setBounds(10, 80, 810, 70);

        jLabel60.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(153, 153, 153));
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel60.setText("Address");
        kGradientPanel13.add(jLabel60);
        jLabel60.setBounds(80, 245, 310, 20);

        kGradientPanel15.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel15.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel15.setkFillBackground(false);
        kGradientPanel15.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel15.setLayout(null);

        addresstxt.setBackground(new java.awt.Color(0, 12, 19));
        addresstxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        addresstxt.setForeground(new java.awt.Color(255, 255, 255));
        addresstxt.setBorder(null);
        kGradientPanel15.add(addresstxt);
        addresstxt.setBounds(10, 10, 280, 20);

        kGradientPanel13.add(kGradientPanel15);
        kGradientPanel15.setBounds(80, 270, 310, 40);

        jLabel61.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Enter new user data");
        kGradientPanel13.add(jLabel61);
        jLabel61.setBounds(80, 200, 310, 30);

        jLabel62.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(255, 255, 255,30)));
        kGradientPanel13.add(jLabel62);
        jLabel62.setBounds(40, 200, 10, 370);

        jLabel63.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(153, 153, 153));
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel63.setText("Mobile Number");
        kGradientPanel13.add(jLabel63);
        jLabel63.setBounds(80, 335, 310, 20);

        kGradientPanel16.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel16.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel16.setkFillBackground(false);
        kGradientPanel16.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel16.setLayout(null);

        mobiletxt.setBackground(new java.awt.Color(0, 12, 19));
        mobiletxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        mobiletxt.setForeground(new java.awt.Color(255, 255, 255));
        mobiletxt.setBorder(null);
        kGradientPanel16.add(mobiletxt);
        mobiletxt.setBounds(10, 10, 280, 20);

        kGradientPanel13.add(kGradientPanel16);
        kGradientPanel16.setBounds(80, 360, 310, 40);

        jLabel64.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(153, 153, 153));
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel64.setText("Email");
        kGradientPanel13.add(jLabel64);
        jLabel64.setBounds(80, 425, 310, 20);

        kGradientPanel17.setBackground(new java.awt.Color(0, 12, 19));
        kGradientPanel17.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel17.setkFillBackground(false);
        kGradientPanel17.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel17.setLayout(null);

        emailtxt.setBackground(new java.awt.Color(0, 12, 19));
        emailtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        emailtxt.setForeground(new java.awt.Color(255, 255, 255));
        emailtxt.setBorder(null);
        kGradientPanel17.add(emailtxt);
        emailtxt.setBounds(10, 10, 280, 20);

        kGradientPanel13.add(kGradientPanel17);
        kGradientPanel17.setBounds(80, 450, 310, 40);

        kButton6.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton6.setText("Update Details");
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
        kGradientPanel13.add(kButton6);
        kButton6.setBounds(80, 520, 310, 40);

        jLabel67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel13.add(jLabel67);
        jLabel67.setBounds(-10, 3, 900, 723);

        jTabbedPane1.addTab("tab1", kGradientPanel13);

        kGradientPanel18.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel18.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel18.setLayout(null);

        kGradientPanel19.setBackground(new java.awt.Color(0, 12, 19,0));
        kGradientPanel19.setkBorderRadius(20);
        kGradientPanel19.setkEndColor(new java.awt.Color(255,255,255,50));
        kGradientPanel19.setkStartColor(new java.awt.Color(255,255,255,50));
        kGradientPanel19.setLayout(null);

        jLabel65.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("About Us");
        kGradientPanel19.add(jLabel65);
        jLabel65.setBounds(10, 5, 540, 40);

        jLabel68.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setText("Want to know who developed this system?");
        kGradientPanel19.add(jLabel68);
        jLabel68.setBounds(10, 40, 540, 20);

        kGradientPanel18.add(kGradientPanel19);
        kGradientPanel19.setBounds(10, 80, 810, 70);

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Prifile DP.png"))); // NOI18N
        kGradientPanel18.add(jLabel69);
        jLabel69.setBounds(260, 180, 310, 270);

        jLabel70.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("Hasintha Nayanajith (E128066)");
        kGradientPanel18.add(jLabel70);
        jLabel70.setBounds(260, 490, 310, 30);

        jLabel71.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(153, 153, 153));
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("For DiSE Final Project");
        kGradientPanel18.add(jLabel71);
        jLabel71.setBounds(260, 540, 310, 20);

        jLabel72.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(153, 153, 153));
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("Developed by");
        kGradientPanel18.add(jLabel72);
        jLabel72.setBounds(260, 470, 310, 20);

        kButton7.setBackground(new java.awt.Color(0, 12, 19,0));
        kButton7.setText("That's Great");
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
        kGradientPanel18.add(kButton7);
        kButton7.setBounds(260, 590, 310, 40);

        jLabel74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Tabs Image.png"))); // NOI18N
        kGradientPanel18.add(jLabel74);
        jLabel74.setBounds(-10, 3, 900, 723);

        jTabbedPane1.addTab("tab1", kGradientPanel18);

        jPanel1.add(jTabbedPane1);
        jTabbedPane1.setBounds(230, -40, 890, 760);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Member Frame Menu Main Image.png"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(-10, 0, 1120, 710);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
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

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        
        jLabel5.setFont(new java.awt.Font("Leelawadee", 1, 14));
        
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseEntered
        
        jLabel6.setFont(new java.awt.Font("Leelawadee", 1, 14));
        
    }//GEN-LAST:event_jLabel6MouseEntered

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        
        jLabel2.setFont(new java.awt.Font("Leelawadee", 1, 14));
        
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        
        jLabel5.setFont(new java.awt.Font("Leelawadee", 0, 14));
        
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseExited
        
        jLabel6.setFont(new java.awt.Font("Leelawadee", 0, 14));
        
    }//GEN-LAST:event_jLabel6MouseExited

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        
        jLabel2.setFont(new java.awt.Font("Leelawadee", 0, 14));
        
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        jLabel7.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        jLabel7.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        
        HomeScreenUserDetails();
        jTabbedPane1.setSelectedIndex(0);
        
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        
        BookDataClear();
        jTabbedPane1.setSelectedIndex(1);
        
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel34MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseEntered
        jLabel34.setFont(new java.awt.Font("Leelawadee", 1, 14));
    }//GEN-LAST:event_jLabel34MouseEntered

    private void jLabel34MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseExited
        jLabel34.setFont(new java.awt.Font("Leelawadee", 0, 14));
    }//GEN-LAST:event_jLabel34MouseExited

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        
        LoginFrame obj = new LoginFrame();
        obj.setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        
        currentPasswordtxt.setText("");
                        newPasswordtxt.setText("");
                        confirmPasswordtxt.setText("");
        jTabbedPane1.setSelectedIndex(2);
        
    }//GEN-LAST:event_jLabel2MouseClicked

    private void kButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton3ActionPerformed
        
        addresstxt.setText(W_addressTxt.getText());
                        mobiletxt.setText(W_mobileTxt.getText());
                        emailtxt.setText(W_emailTxt.getText());
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_kButton3ActionPerformed

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_jLabel34MouseClicked

    private void kButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton7ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_kButton7ActionPerformed

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
        
        BookDataSelect();
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        
        BookDataSelect();
        
    }//GEN-LAST:event_jTable1KeyReleased

    private void bookSearchtxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bookSearchtxtKeyReleased
        
        BookSearch();
        
    }//GEN-LAST:event_bookSearchtxtKeyReleased

    private void bookSearchtxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookSearchtxtMouseClicked
        
        BookDataClear();
        
    }//GEN-LAST:event_bookSearchtxtMouseClicked

    private void kButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton22ActionPerformed
        BookDataClear();
    }//GEN-LAST:event_kButton22ActionPerformed

    private void kButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton5ActionPerformed
        
        PasswordChange();
        
    }//GEN-LAST:event_kButton5ActionPerformed

    private void kButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton6ActionPerformed
        
        MemberDataUpdate();
        
    }//GEN-LAST:event_kButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(MemberFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MemberFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MemberFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MemberFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MemberFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Home_SrchTxt;
    private javax.swing.JLabel W_addressTxt;
    private javax.swing.JLabel W_emailTxt;
    private javax.swing.JLabel W_mobileTxt;
    private javax.swing.JLabel W_nametxt;
    private javax.swing.JLabel W_registerOnTxt;
    private javax.swing.JLabel WelcomeIdTxt;
    private javax.swing.JLabel WelcomeTxt;
    private javax.swing.JTextField addresstxt;
    private javax.swing.JLabel bookAuthorlbl;
    private javax.swing.JLabel bookAvailabilitylbl;
    private javax.swing.JLabel bookIDlbl;
    private javax.swing.JLabel bookNamelbl;
    private javax.swing.JLabel bookRacklbl;
    private javax.swing.JTextField bookSearchtxt;
    private javax.swing.JTextField confirmPasswordtxt;
    private javax.swing.JTextField currentPasswordtxt;
    private javax.swing.JTextField emailtxt;
    private javax.swing.JLabel home_borrowed_books;
    private javax.swing.JLabel home_borrowed_name;
    private javax.swing.JLabel home_borrowed_name1;
    private javax.swing.JLabel home_srch_bookAva;
    private javax.swing.JLabel home_srch_bookName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private com.k33ptoo.components.KButton kButton1;
    private com.k33ptoo.components.KButton kButton2;
    private com.k33ptoo.components.KButton kButton22;
    private com.k33ptoo.components.KButton kButton3;
    private com.k33ptoo.components.KButton kButton4;
    private com.k33ptoo.components.KButton kButton5;
    private com.k33ptoo.components.KButton kButton6;
    private com.k33ptoo.components.KButton kButton7;
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
    private com.k33ptoo.components.KGradientPanel kGradientPanel3;
    private com.k33ptoo.components.KGradientPanel kGradientPanel4;
    private com.k33ptoo.components.KGradientPanel kGradientPanel5;
    private com.k33ptoo.components.KGradientPanel kGradientPanel6;
    private com.k33ptoo.components.KGradientPanel kGradientPanel7;
    private com.k33ptoo.components.KGradientPanel kGradientPanel8;
    private com.k33ptoo.components.KGradientPanel kGradientPanel9;
    private javax.swing.JLabel localTime;
    private javax.swing.JTextField mobiletxt;
    private javax.swing.JTextField newPasswordtxt;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icons8_books_32.png")));
        
    }
}


package intf;

import dise_lib_ms.DB_Connect;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;


public class LoginFrame extends javax.swing.JFrame {

    int xMouse;
    int yMouse;
    
    //Connecting package with database
    Connection conn = null;
    PreparedStatement pet = null;
    ResultSet rst = null;
    
    public LoginFrame() {
        initComponents();
        
        setIcon();
        
        conn = DB_Connect.connect();
        
        this.getRootPane().setBorder(BorderFactory.createLineBorder(new java.awt.Color(0,12,19),1));
        
    }
    
    public void LoginCheck(){
    
        String one,two;
        
        one = userIDtxt.getText();
        two = passwordtxt.getText();
        
        if(one.equals("") || two.equals("")){
        
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("User ID or Password can not be empty values. \n\nPlease enter your User ID & Password.\nThen, try again.");
                    obj.setVisible(true);
                    
            
        }else{
        
            try {
                
                String sql = "select * from login_table where UserID='"+one+"' and PWord='"+two+"'";
                pet=conn.prepareStatement(sql);
                rst=pet.executeQuery();
                
                if(rst.next()){
                
                    String three = rst.getString("UserType");
                    String a = rst.getString("UserID");
                    String b = rst.getString("PWord");
                    
                    if(three.equals("Administrator")){
                    
                        AdminFrame obj = new AdminFrame();
                        obj.WelcomeHome(a, b);
                        obj.setVisible(true);
                        this.dispose();
                        
                    }else if(three.equals("Library Member")){
                    
                        MemberFrame obj = new MemberFrame();
                        obj.WelcomeHome(a,b);
                        obj.setVisible(true);
                        this.dispose();
                        
                    }else{
                    
                        ErrorFrame obj = new ErrorFrame();
                        obj.Error("Sorry! \nSystem was unabled to identify your User Type.");
                        obj.setVisible(true); 
                        
                    }                   
                    
                }else{
                
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("Invalid User ID or Password. Please Try Again!");
                    obj.setVisible(true);    
                    
                }
                
            } catch (Exception e) {
                
                    ErrorFrame obj = new ErrorFrame();
                    obj.Error("An unexpected error occured! \nError code - 0001 \n"+e);
                    obj.setVisible(true);  
                
            }
            
        }
        
        
        
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
        dcfgvhj = new com.k33ptoo.components.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        passwordtxt = new javax.swing.JPasswordField();
        gdxgb = new com.k33ptoo.components.KGradientPanel();
        userIDtxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        kButton3 = new com.k33ptoo.components.KButton();
        jLabel5 = new javax.swing.JLabel();
        errorlbl = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Esoft Library Login");
        setUndecorated(true);

        jPanel1.setLayout(null);

        kGradientPanel1.setkBorderRadius(0);
        kGradientPanel1.setkEndColor(new java.awt.Color(252, 252, 252));
        kGradientPanel1.setkStartColor(new java.awt.Color(252, 252, 252));
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
        kButton1.setkEndColor(new java.awt.Color(0, 7, 43));
        kButton1.setkForeGround(new java.awt.Color(204, 204, 204));
        kButton1.setkHoverEndColor(new java.awt.Color(255, 51, 51));
        kButton1.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton1.setkHoverStartColor(new java.awt.Color(255, 51, 51));
        kButton1.setkStartColor(new java.awt.Color(0, 7, 43));
        kButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton1ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton1);
        kButton1.setBounds(921, 10, 50, 40);

        kButton2.setText("-");
        kButton2.setFont(new java.awt.Font("Leelawadee", 0, 36)); // NOI18N
        kButton2.setkBorderRadius(0);
        kButton2.setkEndColor(new java.awt.Color(0, 7, 43));
        kButton2.setkForeGround(new java.awt.Color(204, 204, 204));
        kButton2.setkHoverEndColor(new java.awt.Color(230, 230, 230));
        kButton2.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton2.setkHoverStartColor(new java.awt.Color(230, 230, 230));
        kButton2.setkStartColor(new java.awt.Color(0, 7, 43));
        kButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton2ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton2);
        kButton2.setBounds(871, 10, 50, 40);

        dcfgvhj.setBackground(new java.awt.Color(0, 12, 19));
        dcfgvhj.setkEndColor(new java.awt.Color(255, 255, 255));
        dcfgvhj.setkFillBackground(false);
        dcfgvhj.setkStartColor(new java.awt.Color(255, 255, 255));
        dcfgvhj.setLayout(null);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_password16_16.png"))); // NOI18N
        dcfgvhj.add(jLabel3);
        jLabel3.setBounds(10, 0, 20, 40);

        passwordtxt.setBackground(new java.awt.Color(0, 12, 19));
        passwordtxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        passwordtxt.setForeground(new java.awt.Color(255, 255, 255));
        passwordtxt.setBorder(null);
        passwordtxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passwordtxtMouseClicked(evt);
            }
        });
        passwordtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordtxtActionPerformed(evt);
            }
        });
        dcfgvhj.add(passwordtxt);
        passwordtxt.setBounds(40, 10, 250, 20);

        kGradientPanel1.add(dcfgvhj);
        dcfgvhj.setBounds(330, 353, 310, 40);

        gdxgb.setBackground(new java.awt.Color(0, 12, 19));
        gdxgb.setkEndColor(new java.awt.Color(255, 255, 255));
        gdxgb.setkFillBackground(false);
        gdxgb.setkStartColor(new java.awt.Color(255, 255, 255));
        gdxgb.setLayout(null);

        userIDtxt.setBackground(new java.awt.Color(0, 12, 19));
        userIDtxt.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        userIDtxt.setForeground(new java.awt.Color(255, 255, 255));
        userIDtxt.setBorder(null);
        userIDtxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userIDtxtMouseClicked(evt);
            }
        });
        userIDtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userIDtxtActionPerformed(evt);
            }
        });
        gdxgb.add(userIDtxt);
        userIDtxt.setBounds(40, 10, 250, 20);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/icons8_username16_16.png"))); // NOI18N
        gdxgb.add(jLabel2);
        jLabel2.setBounds(10, 0, 20, 40);

        kGradientPanel1.add(gdxgb);
        gdxgb.setBounds(330, 280, 310, 40);

        jLabel4.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DiSE Final Project");
        kGradientPanel1.add(jLabel4);
        jLabel4.setBounds(330, 600, 310, 30);

        kButton3.setText("Login");
        kButton3.setFont(new java.awt.Font("Leelawadee", 1, 18)); // NOI18N
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
        kGradientPanel1.add(kButton3);
        kButton3.setBounds(330, 410, 310, 45);

        jLabel5.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Password");
        kGradientPanel1.add(jLabel5);
        jLabel5.setBounds(330, 330, 310, 20);

        errorlbl.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        errorlbl.setForeground(new java.awt.Color(255, 51, 51));
        errorlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kGradientPanel1.add(errorlbl);
        errorlbl.setBounds(330, 480, 310, 30);

        jLabel7.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Developed by Hasintha Nayanajith (E128066)");
        kGradientPanel1.add(jLabel7);
        jLabel7.setBounds(330, 580, 310, 30);

        jLabel8.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 153, 153));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Esoft Metro Campus | Login");
        kGradientPanel1.add(jLabel8);
        jLabel8.setBounds(23, 18, 310, 20);

        jLabel9.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 153, 153));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("User ID");
        kGradientPanel1.add(jLabel9);
        jLabel9.setBounds(330, 257, 310, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/intf/Login UI Image.png"))); // NOI18N
        kGradientPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 1060, 660);

        jPanel1.add(kGradientPanel1);
        kGradientPanel1.setBounds(-10, -10, 980, 660);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(962, 640));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void kButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton1ActionPerformed
        
        java.lang.System.exit(0);
        
    }//GEN-LAST:event_kButton1ActionPerformed

    private void kGradientPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel1MousePressed
        
        xMouse = evt.getX();
        yMouse = evt.getY();
        
    }//GEN-LAST:event_kGradientPanel1MousePressed

    
    private void kGradientPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel1MouseDragged
        
        this.setLocation(evt.getXOnScreen()-xMouse, evt.getYOnScreen()-yMouse);
        
    }//GEN-LAST:event_kGradientPanel1MouseDragged

    private void kButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton2ActionPerformed
        
        this.setExtendedState(ICONIFIED);
        
    }//GEN-LAST:event_kButton2ActionPerformed

    private void kButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton3ActionPerformed
        
        LoginCheck();
        
    }//GEN-LAST:event_kButton3ActionPerformed

    private void userIDtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userIDtxtActionPerformed
        
    }//GEN-LAST:event_userIDtxtActionPerformed

    private void passwordtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordtxtActionPerformed
        
    }//GEN-LAST:event_passwordtxtActionPerformed

    private void userIDtxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userIDtxtMouseClicked
        errorlbl.setText("");
    }//GEN-LAST:event_userIDtxtMouseClicked

    private void passwordtxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordtxtMouseClicked
        errorlbl.setText("");
    }//GEN-LAST:event_passwordtxtMouseClicked

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
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                
                new LoginFrame().setVisible(true);
            }
        });
        
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.k33ptoo.components.KGradientPanel dcfgvhj;
    private javax.swing.JLabel errorlbl;
    private com.k33ptoo.components.KGradientPanel gdxgb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private com.k33ptoo.components.KButton kButton1;
    private com.k33ptoo.components.KButton kButton2;
    private com.k33ptoo.components.KButton kButton3;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private javax.swing.JPasswordField passwordtxt;
    private javax.swing.JTextField userIDtxt;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
     
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icons8_books_32.png")));

    }
}

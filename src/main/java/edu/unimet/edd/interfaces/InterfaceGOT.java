package edu.unimet.edd.interfaces;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author Chantal
 */
public class InterfaceGOT extends javax.swing.JFrame {

    /**
     * Creates new form InterfazGOT
     */
    
    int xMouse, yMouse;
    
    public InterfaceGOT() {
        initComponents();
        Page0 p0 = new Page0();
            }
    
    private void ShowPanel(JPanel p) {
        p.setSize(520, 390);
        p.setLocation(0, 0);

        content.removeAll();
        content.setLayout(new BorderLayout());
        content.add(p, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Background = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        Record = new javax.swing.JButton();
        searchName = new javax.swing.JButton();
        showAncestors = new javax.swing.JButton();
        searchTitle = new javax.swing.JButton();
        membersGeneration = new javax.swing.JButton();
        Separator = new javax.swing.JSeparator();
        loadTree = new javax.swing.JButton();
        xBar = new javax.swing.JPanel();
        xBackground = new javax.swing.JPanel();
        Exit = new javax.swing.JLabel();
        content = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        Background.setBackground(new java.awt.Color(255, 255, 255));
        Background.setPreferredSize(new java.awt.Dimension(760, 530));
        Background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(96, 110, 121));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Record.setBackground(new java.awt.Color(128, 139, 147));
        Record.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        Record.setForeground(new java.awt.Color(255, 255, 255));
        Record.setText("Record");
        Record.setBorder(null);
        Record.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Record.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecordActionPerformed(evt);
            }
        });
        Menu.add(Record, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 220, 40));

        searchName.setBackground(new java.awt.Color(128, 139, 147));
        searchName.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        searchName.setForeground(new java.awt.Color(255, 255, 255));
        searchName.setText("Search Name");
        searchName.setBorder(null);
        searchName.setBorderPainted(false);
        searchName.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchNameActionPerformed(evt);
            }
        });
        Menu.add(searchName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 220, 40));

        showAncestors.setBackground(new java.awt.Color(128, 139, 147));
        showAncestors.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        showAncestors.setForeground(new java.awt.Color(255, 255, 255));
        showAncestors.setText("Show Ancestors");
        showAncestors.setBorder(null);
        showAncestors.setBorderPainted(false);
        showAncestors.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        showAncestors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAncestorsActionPerformed(evt);
            }
        });
        Menu.add(showAncestors, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 220, 40));

        searchTitle.setBackground(new java.awt.Color(128, 139, 147));
        searchTitle.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        searchTitle.setForeground(new java.awt.Color(255, 255, 255));
        searchTitle.setText("Search Title");
        searchTitle.setBorder(null);
        searchTitle.setBorderPainted(false);
        searchTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTitleActionPerformed(evt);
            }
        });
        Menu.add(searchTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 220, 40));

        membersGeneration.setBackground(new java.awt.Color(128, 139, 147));
        membersGeneration.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        membersGeneration.setForeground(new java.awt.Color(255, 255, 255));
        membersGeneration.setText("Members of a Generation");
        membersGeneration.setBorder(null);
        membersGeneration.setBorderPainted(false);
        membersGeneration.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        membersGeneration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                membersGenerationActionPerformed(evt);
            }
        });
        Menu.add(membersGeneration, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 220, 40));

        Separator.setForeground(new java.awt.Color(255, 255, 255));
        Menu.add(Separator, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 112, 180, 30));

        Background.add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 530));

        loadTree.setBackground(new java.awt.Color(128, 139, 147));
        loadTree.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        loadTree.setForeground(new java.awt.Color(255, 255, 255));
        loadTree.setText("Load Tree");
        loadTree.setBorder(null);
        loadTree.setBorderPainted(false);
        loadTree.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadTreeActionPerformed(evt);
            }
        });
        Background.add(loadTree, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 480, 120, 30));

        xBar.setBackground(new java.awt.Color(255, 255, 255));
        xBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                xBarMouseDragged(evt);
            }
        });
        xBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                xBarMousePressed(evt);
            }
        });
        xBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        xBackground.setBackground(new java.awt.Color(255, 255, 255));

        Exit.setBackground(new java.awt.Color(255, 255, 255));
        Exit.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        Exit.setForeground(new java.awt.Color(102, 102, 102));
        Exit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Exit.setText("X");
        Exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Exit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExitMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ExitMouseExited(evt);
            }
        });

        javax.swing.GroupLayout xBackgroundLayout = new javax.swing.GroupLayout(xBackground);
        xBackground.setLayout(xBackgroundLayout);
        xBackgroundLayout.setHorizontalGroup(
            xBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Exit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
        );
        xBackgroundLayout.setVerticalGroup(
            xBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xBackgroundLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        xBar.add(xBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, -1, 40));

        Background.add(xBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 40));

        content.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );

        Background.add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 520, 390));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void xBarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_xBarMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_xBarMousePressed

    private void xBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_xBarMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_xBarMouseDragged

    private void ExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_ExitMouseClicked

    private void ExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExitMouseEntered
        xBackground.setBackground(Color.red);
        Exit.setForeground(Color.white);
    }//GEN-LAST:event_ExitMouseEntered

    private void ExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExitMouseExited
        xBackground.setBackground(Color.white);
        Exit.setForeground(Color.gray);
    }//GEN-LAST:event_ExitMouseExited

    private void RecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RecordActionPerformed
        Page1 p1 = new Page1();
            ShowPanel(p1);
           // genealogyGUI.onRegister();
    }//GEN-LAST:event_RecordActionPerformed

    private void searchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchNameActionPerformed
        Page2 p2 = new Page2();
            ShowPanel(p2);
    }//GEN-LAST:event_searchNameActionPerformed

    private void showAncestorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAncestorsActionPerformed

        Page3 p3 = new Page3();
            ShowPanel(p3);
    }//GEN-LAST:event_showAncestorsActionPerformed

    private void searchTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTitleActionPerformed
        Page4 p4 = new Page4();
            ShowPanel(p4);
    }//GEN-LAST:event_searchTitleActionPerformed

    private void membersGenerationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_membersGenerationActionPerformed
        Page5 p5 = new Page5();
            ShowPanel(p5);
    }//GEN-LAST:event_membersGenerationActionPerformed

    private void loadTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadTreeActionPerformed
//        if (genealogyGUI == null) {  // Solo crea la instancia una vez
//        genealogyGUI = new GenealogyGUI();
//        }
//        
//        genealogyGUI.loadTreeLoaded(); 
//
//        if (!genealogyGUI.isVisible()) {
//            genealogyGUI.setVisible(true);  // Asegúrate de que solo se abra si no está visible
//        } else {
//            genealogyGUI.requestFocus();  // Si ya está abierta, tráela al frente
//        }
            
           

    }//GEN-LAST:event_loadTreeActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfaceGOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceGOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceGOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceGOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new InterfaceGOT().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Background;
    private javax.swing.JLabel Exit;
    private javax.swing.JPanel Menu;
    private javax.swing.JButton Record;
    private javax.swing.JSeparator Separator;
    private javax.swing.JPanel content;
    private javax.swing.JButton loadTree;
    private javax.swing.JButton membersGeneration;
    private javax.swing.JButton searchName;
    private javax.swing.JButton searchTitle;
    private javax.swing.JButton showAncestors;
    private javax.swing.JPanel xBackground;
    private javax.swing.JPanel xBar;
    // End of variables declaration//GEN-END:variables
}

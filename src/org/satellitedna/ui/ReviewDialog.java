/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.ui;

/**
 *
 * @author clopez
 */
public class ReviewDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form ReviewDialog
     * @param parent
     * @param modal
     */
    public ReviewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public void addLine(String line)
    {
        this.jTextArea1.append(line +"\n");
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/satellitedna/ui/emblem-symbolic-link-2.png"))); // NOI18N
        jClose.setText("Close");
        jClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCloseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(jClose)
                .addContainerGap(293, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jClose))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCloseMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jCloseMouseClicked

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jClose;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}

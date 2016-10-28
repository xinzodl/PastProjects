package interfazSGF;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


class dialogoBqAvanzada extends JDialog implements ActionListener {
  JPanel panel1 = new JPanel();
  JPanel insetsPanel1 = new JPanel();
  JButton button1 = new JButton();
  JButton button2 = new JButton();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  JLabel jLabel1 = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea jTextArea1 = new JTextArea();

  public dialogoBqAvanzada(Frame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {
    titledBorder1 = new TitledBorder("");
    this.setTitle("Búsquedas Avanzadas...");
    panel1.setLayout(borderLayout1);
    button1.setMinimumSize(new Dimension(95, 25));
    button1.setPreferredSize(new Dimension(95, 25));
    button1.setToolTipText("volver a la aplicación");
    button1.setText("Cancelar");
    button1.addActionListener(this);
    panel1.setBackground(Color.white);
    panel1.setForeground(Color.blue);
    panel1.setBorder(BorderFactory.createLineBorder(Color.black));
    panel1.setMaximumSize(new Dimension(200, 180));
    panel1.setMinimumSize(new Dimension(200, 180));
    panel1.setPreferredSize(new Dimension(200, 180));
    panel1.setRequestFocusEnabled(true);
    insetsPanel1.setBackground(new Color(215, 215, 215));
    insetsPanel1.setMaximumSize(new Dimension(100, 75));
    insetsPanel1.setMinimumSize(new Dimension(100, 75));
    insetsPanel1.setPreferredSize(new Dimension(100, 75));
    insetsPanel1.setRequestFocusEnabled(true);
    button2.addActionListener(this);
    button2.setText("Comenzar...");
    button2.setPreferredSize(new Dimension(95, 25));
    button2.setToolTipText("ejecutar la búsqueda");
    jLabel1.setBackground(new Color(215, 215, 215));
    jLabel1.setText(" Introduce aquí el texto de la búsqueda:");
    panel1.add(insetsPanel1, BorderLayout.SOUTH);
    insetsPanel1.add(button2, null);
    insetsPanel1.add(button1, null);
    panel1.add(jLabel1, BorderLayout.NORTH);
    panel1.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jTextArea1, null);
    this.getContentPane().add(panel1, BorderLayout.CENTER);
    setResizable(false);
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }
  //Close the dialog
  void cancel() {
    jTextArea1.setText("");
    dispose();
  }
  void accept() {
    button2.setActionCommand("OK!");
    dispose();
  }
  //Close the dialog on a button event
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
      cancel();
    }
    if (e.getSource() == button2) {
      accept();
    }
  }
}


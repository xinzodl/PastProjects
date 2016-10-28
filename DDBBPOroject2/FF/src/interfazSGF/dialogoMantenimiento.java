package interfazSGF;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

class dialogoMantenimiento extends JDialog implements ActionListener{
  JPanel panel1 = new JPanel();
  JPanel insetsPanel1 = new JPanel();
  JButton button1 = new JButton();
  JButton button2 = new JButton();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  JLabel mantlabel1 = new JLabel();
  JTextField mantfield1 = new JTextField();
  JPanel jPanel1 = new JPanel();
  GridLayout gridLayout1 = new GridLayout(2,2);
  JLabel mantlabel2 = new JLabel();
  JTextField mantfield2 = new JTextField();

  public dialogoMantenimiento(Frame parent) {
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
    this.setTitle("Mantenimiento del Sistema");
    panel1.setLayout(borderLayout1);
    button1.setMaximumSize(new Dimension(110, 27));
    button1.setMinimumSize(new Dimension(110, 25));
    button1.setPreferredSize(new Dimension(110, 25));
    button1.setToolTipText("volver a la aplicación");
    button1.setText("Cancelar");
    button1.addActionListener(this);
    panel1.setBackground(Color.white);
    panel1.setForeground(Color.blue);
    panel1.setBorder(BorderFactory.createLineBorder(Color.black));
    panel1.setMaximumSize(new Dimension(350, 100));
    panel1.setMinimumSize(new Dimension(350, 100));
    panel1.setPreferredSize(new Dimension(350, 100));
    panel1.setRequestFocusEnabled(true);
    insetsPanel1.setBackground(new Color(215, 215, 215));
    insetsPanel1.setMaximumSize(new Dimension(100, 35));
    insetsPanel1.setMinimumSize(new Dimension(100, 35));
    insetsPanel1.setPreferredSize(new Dimension(100, 35));
    insetsPanel1.setRequestFocusEnabled(true);
    button2.addActionListener(this);
    button2.setText("Comenzar...");
    button2.setMaximumSize(new Dimension(110, 27));
    button2.setMinimumSize(new Dimension(110, 27));
    button2.setPreferredSize(new Dimension(110, 25));
    button2.setToolTipText("ejecutar la operación");
    mantlabel1.setBackground(new Color(215, 215, 215));
    mantlabel1.setHorizontalAlignment(SwingConstants.RIGHT);
    mantlabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
    mantlabel1.setLabelFor(mantfield1);
    mantlabel1.setText("  Introduce clave de indización");
    jPanel1.setLayout(gridLayout1);
    mantlabel2.setText("  Introduce tipo de indización");
    mantlabel2.setLabelFor(mantfield2);
    mantlabel2.setBackground(new Color(215, 215, 215));
    mantlabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    mantlabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
    jPanel1.setMaximumSize(new Dimension(110, 32767));
    jPanel1.setMinimumSize(new Dimension(110, 42));
    jPanel1.setPreferredSize(new Dimension(110, 42));
    panel1.add(insetsPanel1, BorderLayout.SOUTH);
    insetsPanel1.add(button2, null);
    insetsPanel1.add(button1, null);
    panel1.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(mantlabel1, null);
    jPanel1.add(mantfield1, null);
    jPanel1.add(mantlabel2, null);
    jPanel1.add(mantfield2, null);
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



package interfazSGF;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


class dialogoCreditos  extends JDialog implements ActionListener {

  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  JPanel insetsPanel1 = new JPanel();
  JPanel insetsPanel2 = new JPanel();
  JPanel insetsPanel3 = new JPanel();
  JButton button1 = new JButton();
  JLabel imageLabel = new JLabel();
  JLabel label1 = new JLabel();
  JLabel label2 = new JLabel();
  JLabel label3 = new JLabel();
  JLabel label4 = new JLabel();
  ImageIcon image1 = new ImageIcon();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  GridLayout gridLayout1 = new GridLayout();
  String product = "";
  String version = "1.3";
  String copyright = "Copyright (c) 2003-"+BufferDeRegistro.fecha;
  String comments = "";
  TitledBorder titledBorder1;
  JLabel label5 = new JLabel();
  JLabel label6 = new JLabel();
  public dialogoCreditos (Frame parent) {
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
    image1 = new ImageIcon("img\\logo.png");
    titledBorder1 = new TitledBorder("");
    imageLabel.setToolTipText("Ficheros");
    imageLabel.setHorizontalAlignment(SwingConstants.LEFT);
    imageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
    imageLabel.setIcon(image1);
    imageLabel.setIconTextGap(1);
    imageLabel.setVerticalAlignment(SwingConstants.CENTER);
    this.setTitle("Acerca de...");
    panel1.setLayout(borderLayout1);
    panel2.setLayout(borderLayout2);
    insetsPanel1.setLayout(flowLayout1);
    insetsPanel2.setLayout(flowLayout1);
    insetsPanel2.setBackground(Color.white);
    insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    insetsPanel2.setMaximumSize(new Dimension(150, 120));
    insetsPanel2.setMinimumSize(new Dimension(150, 120));
    insetsPanel2.setPreferredSize(new Dimension(150, 120));
    gridLayout1.setRows(6);
    gridLayout1.setVgap(1);
    gridLayout1.setColumns(1);
    gridLayout1.setHgap(0);
    label1.setFont(new java.awt.Font("Dialog", 3, 12));
    label1.setHorizontalAlignment(SwingConstants.LEFT);
    label1.setIconTextGap(0);
    label1.setText("");
    label2.setHorizontalAlignment(SwingConstants.LEFT);
    label2.setIconTextGap(0);
    label2.setText("v "+BufferDeRegistro.version+", 2003 - "+BufferDeRegistro.fecha);
    label3.setHorizontalAlignment(SwingConstants.LEFT);
    label3.setHorizontalTextPosition(SwingConstants.LEFT);
    label3.setIconTextGap(0);
    label3.setText("Copyright LABDA  ( http://basesdatos.uc3m.es )");
    label4.setHorizontalAlignment(SwingConstants.LEFT);
    label4.setIconTextGap(0);
    label4.setText("Dpto. Inform�tica, Universidad Carlos III de Madrid");
    insetsPanel3.setLayout(gridLayout1);
    insetsPanel3.setBackground(Color.white);
    insetsPanel3.setAlignmentX((float) 0.0);
    insetsPanel3.setAlignmentY((float) 0.5);
    insetsPanel3.setBorder(null);
    insetsPanel3.setMaximumSize(new Dimension(700, 110));
    insetsPanel3.setMinimumSize(new Dimension(700, 110));
    insetsPanel3.setPreferredSize(new Dimension(700, 110));
    insetsPanel3.setToolTipText("");
    button1.setToolTipText("volver a la aplicaci�n");
    button1.setText("Cerrar");
    button1.addActionListener(this);
    panel1.setBackground(Color.white);
    panel1.setForeground(Color.blue);
    panel1.setBorder(BorderFactory.createLineBorder(Color.black));
    panel1.setMaximumSize(new Dimension(500, 180));
    panel1.setMinimumSize(new Dimension(500, 180));
    panel1.setPreferredSize(new Dimension(500, 180));
    panel1.setRequestFocusEnabled(true);
    panel1.setToolTipText("visita nuestra web");
    image1.setDescription("vfs:///file:///C:/Documents and Settings/jcalle/jbproject/aplicaci�n/classes/aplicaci�n/logo.png");
    insetsPanel1.setBackground(Color.white);
    insetsPanel1.setMaximumSize(new Dimension(100, 35));
    insetsPanel1.setMinimumSize(new Dimension(100, 35));
    insetsPanel1.setPreferredSize(new Dimension(100, 35));
    insetsPanel1.setRequestFocusEnabled(true);
    flowLayout1.setAlignment(FlowLayout.CENTER);
    flowLayout1.setHgap(1);
    flowLayout1.setVgap(2);
    panel2.setMinimumSize(new Dimension(621, 120));
    panel2.setOpaque(true);
    panel2.setPreferredSize(new Dimension(621, 120));
    label5.setText("Aplicaci�n Gestora de Ficheros");
    label5.setIconTextGap(0);
    label5.setHorizontalAlignment(SwingConstants.LEFT);
    label5.setFont(new java.awt.Font("Dialog", 3, 12));
    label6.setText(" Laboratorio de Bases de Datos Avanzadas");
    label6.setIconTextGap(0);
    label6.setToolTipText("");
    label6.setHorizontalAlignment(SwingConstants.LEFT);
    insetsPanel3.add(label1, null);
    insetsPanel3.add(label5, null);
    insetsPanel3.add(label2, null);
    insetsPanel3.add(label3, null);
    insetsPanel3.add(label6, null);
    insetsPanel3.add(label4, null);
    panel2.add(insetsPanel2, BorderLayout.WEST);
    insetsPanel2.add(imageLabel, null);
    panel2.add(insetsPanel3, BorderLayout.CENTER);
    panel1.add(insetsPanel1, BorderLayout.SOUTH);
    insetsPanel1.add(button1, null);
    panel1.add(panel2, BorderLayout.NORTH);
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
  //Close the dialog on a button event
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
      cancel();
    }
  }
}
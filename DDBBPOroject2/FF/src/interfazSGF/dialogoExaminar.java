package interfazSGF;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


class dialogoExaminar extends JDialog implements ActionListener {

  JPanel panel1 = new JPanel();
  ImageIcon image1 = new ImageIcon();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  JFileChooser jFileChooser1 = new JFileChooser();
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  TitledBorder titledBorder4;
  public dialogoExaminar(Frame parent, String tipo, String titulo) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit(tipo, titulo);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit(String tipo, String titulo) throws Exception  {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");
    panel1.setLayout(borderLayout1);
    panel1.setBackground(SystemColor.textHighlight);
    panel1.setForeground(Color.blue);
    panel1.setBorder(BorderFactory.createLineBorder(Color.black));
    panel1.setMaximumSize(new Dimension(750, 400));
    panel1.setMinimumSize(new Dimension(750, 400));
    panel1.setPreferredSize(new Dimension(750, 400));
    panel1.setToolTipText("tienes que seleccionar un fichero...");
    jFileChooser1.setApproveButtonText(tipo);
    jFileChooser1.setApproveButtonToolTipText(tipo + " fichero seleccionado");
    jFileChooser1.setDialogTitle(titulo);
    jFileChooser1.addActionListener(new dialogoExaminar_jFileChooser1_actionAdapter(this));
    jFileChooser1.setForeground(Color.blue);
    jFileChooser1.setBorder(titledBorder4);
    jFileChooser1.setDoubleBuffered(false);
    jFileChooser1.setPreferredSize(new Dimension(750, 400));
    jFileChooser1.setAcceptAllFileFilterUsed(true);
    jFileChooser1.setFileFilter(null);
    jFileChooser1.setFileSelectionMode(0);
    panel1.add(jFileChooser1, BorderLayout.CENTER);
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
 //   if (e.getSource() == button1) {
      cancel();
 //   }
  }

  void jFileChooser1_actionPerformed(ActionEvent e) {
    cancel();
  }
}

class dialogoExaminar_jFileChooser1_actionAdapter implements java.awt.event.ActionListener {
  dialogoExaminar adaptee;

  dialogoExaminar_jFileChooser1_actionAdapter(dialogoExaminar adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jFileChooser1_actionPerformed(e);
  }
}
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class BillNameIInput extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    public BillNameIInput() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        try {

            ResultSet rs=MainScreen.stmt.executeQuery("select name from people order by name;");
            String ar[]=new String[150];
            int i=0;
            DefaultComboBoxModel m= (DefaultComboBoxModel) comboBox1.getModel();
           m.removeAllElements();
            m.addElement("Select customer");
            while(rs.next())
            {
                ar[i]=rs.getString("name");
                m.addElement(ar[i]);
                i++;
            }
            comboBox1.setModel(m);
        } catch (SQLException e) {
            e.printStackTrace();
        }

pack();
    }
    BillNameIInput(int id,int option){
       this();
        this.id=id;
        this.option=option;
}
    private void onOK()  {
        // add your code here
        if (option==0){
        if(comboBox1.getSelectedIndex()==0)
        {

        }

        else{
           MainScreen.newBill= new NewBill(id,comboBox1.getSelectedItem().toString());
           MainScreen.newBill.setVisible(true);

            dispose();
        }
        }
        else{
            if(comboBox1.getSelectedIndex()!=0){

                Bill window=new Bill(comboBox1.getSelectedItem().toString());
                window.setVisible(true);
                dispose();
            }
        }
    }
    private void onCancel() {
        // add your code here if necessary
        MainScreen.order.setVisible(true);

        dispose();

    }
    int option=1;
    int id;

    public static void main(String[] args) {
        BillNameIInput dialog = new BillNameIInput();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

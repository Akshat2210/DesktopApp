import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddPeople extends JFrame{
    private JTextField textField1;
    private JButton ADDButton;
    private JComboBox comboBox1;
    private JTextField textField2;
    private JPanel panel1;
    private JButton BACKButton;
    Statement stmt;
    public static void main(String args[]){
AddPeople a= new AddPeople();
    }
    boolean transac=false;
    int id;
        void returnToTransaction(){
        transac=true;
    }
    public AddPeople() {
        setSize(MainScreen.fullx,MainScreen.fully);
        setContentPane(panel1);
        try {
            DefaultComboBoxModel m=new DefaultComboBoxModel<>();

            Class.forName("com.mysql.cj.jdbc.Driver");
//here sonoo is database name, root is username and password
            ResultSet rs=MainScreen.stmt.executeQuery("select type from category");
            while(rs.next()){
                m.addElement(rs.getString("type"));
            }
comboBox1.setModel(m);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!MainScreen.n1.getVerification()){
                    System.exit(0);
                }

                try {
                    MainScreen.stmt.executeUpdate("insert into people(name,type) values(\""+textField1.getText()+"\",+\""+comboBox1.getSelectedItem().toString()+"\");");
                    if(transac){
                        setVisible(false);
                    }
                    MainScreen.accounts.refreshButton.doClick();
                    MainScreen.transaction.makePeopleList();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            }
        });
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                if(! (id==1))
                MainScreen.accounts.setVisible(true);
                else{MainScreen.accountOf.setVisible(true);
                MainScreen.transaction.setVisible(true);
                id=0;
                }
            }
        });
    }
}

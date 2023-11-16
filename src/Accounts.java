import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Accounts extends JFrame {
    private JList list1;
    private JPanel panel1;
     JButton refreshButton;
    private JButton NEWTRANSACTIONButton;
    private JButton BACKButton;
    private JScrollPane scrollPane;
    private JComboBox filter;




    void collectInfo() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//here sonoo is database name, root is username and password
            ResultSet rs;
            rs= MainScreen.stmt.executeQuery("select type from category");
            DefaultComboBoxModel filterModel=(DefaultComboBoxModel) filter.getModel();
            filterModel.addElement("All");
            while(rs.next())
            {
                filterModel.addElement(rs.getString("type"));

            }
            filter.setModel(filterModel);

            String selectedFilter=filter.getSelectedItem().toString();
            if(selectedFilter.contentEquals("All"))
                rs = MainScreen.stmt.executeQuery("select name from people  order by name  ;");
else
            rs = MainScreen.stmt.executeQuery("select name from people where type='"+selectedFilter+"' order by name  ;");

            String ar[] = new String[150];
            int i = 0;
            DefaultListModel m = (DefaultListModel) list1.getModel();
            m.clear();
            m.addElement("Add new customer/karigar/others");
            m.addElement("cash");
            while (rs.next()) {
                ar[i] = rs.getString("name");
                m.addElement(ar[i]);
                i++;
            }
            list1.setModel(m);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Accounts() {
        setContentPane(panel1);
        collectInfo();
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (list1.getSelectedValue().toString().contentEquals("Add new customer/karigar/others")) {
                    MainScreen.addPeople.setVisible(true);
                    setVisible(false);
                } else {
                    try {
                        MainScreen.accountOf = new AccountOf(list1.getSelectedValue().toString());
                        setVisible(false);
                        MainScreen.accountOf.setVisible(true);
                    } finally {
                    }
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collectInfo();
                list1.requestFocus();
            }
        });
        NEWTRANSACTIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreen.transaction=new newTransaction();

                MainScreen.transaction.setVisible(true);
setVisible(false);
                MainScreen.accountOf.setVisible(true);
                MainScreen.accountOf.BACKButton.setVisible(false);
            }
        });
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainScreen.loggedIn.setVisible(true);
            }
        });
        list1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == ' ') {
                    if (list1.getSelectedValue().toString().contentEquals("Add new customer/karigar/others")) {
                        MainScreen.addPeople.setVisible(true);
                        setVisible(false);
                    } else {
                        try {
                            MainScreen.accountOf = new AccountOf(list1.getSelectedValue().toString());
                            setVisible(false);
                            MainScreen.accountOf.setVisible(true);
                        } finally {
                        }
                    }
                }
            }
        });
        panel1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        scrollPane.setVisible(true);

        pack();
        list1.requestFocusInWindow();

        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collectInfo();
            }
        });
    }

    private void createUIComponents() {

        // TODO: place custom component creation code here
    }
}

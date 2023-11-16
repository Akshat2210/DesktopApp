
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public class newTransaction extends JFrame {
    private JButton registerButton;
    private JComboBox nameB;
    private JTextField amountT;
    private JRadioButton आवकRadioButton;
    private JRadioButton जावकRadioButton;
    private JTextField remark;
    private JTextField medium;
    private JPanel panel1;
    private JButton BACKButton;
    private JComboBox dateB;
    private JCheckBox material;
    private JButton UNDOButton;

    public static void main(String[] args) {
        new newTransaction();
    }

    void makePeopleList() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            ResultSet rs = MainScreen.stmt.executeQuery("select name from people order by name;");
            String[] n = new String[150];
            int i = 0;
            while (rs.next()) {
                n[i] = rs.getString("name");
                i++;
            }
            DefaultComboBoxModel m = new DefaultComboBoxModel<>(n);
            m.addElement("new");
            nameB.setModel(m);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    String selectedDate;
  public boolean requestDateFilter=false;
    newTransaction() {
        setSize(MainScreen.fullx, MainScreen.fully);
        setContentPane(panel1);
        setBounds(0, 0, MainScreen.fullx / 2, MainScreen.fully);

        MainScreen.accountOf = new AccountOf("cash",1);
        MainScreen.accountOf.setBounds(MainScreen.fullx / 2, 0, MainScreen.fullx / 2, MainScreen.fully);


        MainScreen.accountOf.setVisible(false);
        makePeopleList();
        DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yy");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // Add dates to the combo box
        int days = MainScreen.n1.getUser().contentEquals("root") ? 360 : 7;
        for (int i = 0; i < days; i++) {
            dateB.addItem(dateFormat1.format(calendar.getTime()));
            calendar.add(Calendar.DATE, -1);
        }
        String todayDate = dateFormat1.format(Calendar.getInstance().getTime());
        dateB.addItem(todayDate); // Add today's date
        dateB.setSelectedItem(todayDate); // Set today's date as the default selected value

        // Save the selected date in the format of yyyy-mm-dd
        AtomicReference<String> selectedDate = new AtomicReference<>();
        try {
            selectedDate.set(dateFormat2.format(dateFormat1.parse((String) dateB.getSelectedItem())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Add a listener to the combo box to update the selected date
        dateB.addActionListener(e -> {
            try {
                selectedDate.set(dateFormat2.format(dateFormat1.parse((String) dateB.getSelectedItem())));
                this.selectedDate=selectedDate.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String R_G = "";
                    if (आवकRadioButton.isSelected()) {
                        R_G = "R";

                    } else if (जावकRadioButton.isSelected()) {
                        R_G = "G";
                    } else {
                        JOptionPane.showMessageDialog(panel1, "Please select avak or javak", "error", JOptionPane.ERROR_MESSAGE);
                    }
                    if(!material.isSelected()) {
                        MainScreen.stmt.executeUpdate("insert into accounts(name,amount,medium,remark,R_G,date_,incash) values(\"" + nameB.getSelectedItem().toString() + "\",+" + Double.parseDouble(amountT.getText()) + ",\"" + medium.getText() + "\",\"" + remark.getText() + "\",\"" + R_G + "\",\"" + "" + selectedDate + "" + "\",1);");
                    }
                    else
                    {
                        MainScreen.stmt.executeUpdate("insert into accounts(name,amount,medium,remark,R_G,date_,incash) values(\"" + nameB.getSelectedItem().toString() + "\",+" + Double.parseDouble(amountT.getText()) + ",\"" + medium.getText() + "\",\"" + remark.getText() + "\",\"" + R_G + "\",\"" + "" + selectedDate + "" + "\",0);");
                    }

                    nameB.setSelectedIndex(0);
                    amountT.setText("");
                    medium.setText("");
                    remark.setText("");
                    nameB.transferFocus();
                    int lastRow = MainScreen.accountOf.table1.getRowCount() - 1;
                    MainScreen.accountOf.table1.setRowSelectionInterval(lastRow, lastRow);
                    lastRow = MainScreen.accountOf.table2.getRowCount() - 1;
                    MainScreen.accountOf.table2.setRowSelectionInterval(lastRow, lastRow);

                    MainScreen.accountOf.refresh();
                    MainScreen.stmt.executeUpdate("set @balance=0;");
                    if(!material.isSelected()) {
                        MainScreen.stmt.executeUpdate("UPDATE accounts\n" +
                                "SET balance =\n" +
                                "  CASE\n" +
                                "    WHEN r_G = 'R' THEN @balance := @balance + AMOUNT\n" +
                                "    WHEN r_G = 'G' THEN @balance := @balance - AMOUNT\n" +
                                "  END\n" +
                                "WHERE inCash = 1\n" + // <-- added WHERE clause
                                "ORDER BY date_;");
                        System.out.print("updated");
                    }

                    String query = "UPDATE people SET outstandings = (SELECT SUM(CASE WHEN r_G = 'G' THEN amount ELSE 0 END) - SUM(CASE WHEN r_G = 'R' THEN amount ELSE 0 END) FROM accounts WHERE accounts.name = people.name);";
                MainScreen.stmt.executeUpdate(query);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainScreen.accountOf.setVisible(false);
                MainScreen.accounts.setVisible(true);
            }
        });
        nameB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (nameB.getSelectedItem().toString().contentEquals("new")) {
                    MainScreen.addPeople.setVisible(true);
                    setVisible(false);
                    MainScreen.accountOf.setVisible(false);
                    MainScreen.addPeople.id=1;
                    MainScreen.addPeople.returnToTransaction();


                }
            }
        });
        UNDOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name;
                double amount;
                String R_G;
                Date date;
                boolean incash;

// Retrieve the values before deletion
                ResultSet result = null;
                try {
                    result = MainScreen.stmt.executeQuery("SELECT * FROM accounts WHERE id = (SELECT MAX(id) FROM accounts)");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    if (result.next()) {
                        name = result.getString("name");
                        amount = result.getDouble("amount");
                        R_G = result.getString("R_G");
                        date = result.getDate("date_");
                        incash = result.getBoolean("incash");
                        String mediumValue = result.getString("medium");
                        String remarkValue = result.getString("remark");
                        // Populate the corresponding text fields and radio buttons with the saved values
                        nameB.setSelectedItem(name);
                        amountT.setText(Double.toString(amount));
                        remark.setText(remarkValue);
                        medium.setText(mediumValue);
                        dateB.setSelectedItem(dateFormat1.format(date));

                        System.out.print("\nhi "+date.toString());// Set the appropriate radio button based on the value of R_G
                        if (R_G.equals("R")) {
                            आवकRadioButton.setSelected(true);
                        } else {
                            जावकRadioButton.setSelected(true);
                        }
                        if(incash){
                            material.setSelected(false);
                        }
                        else material.setSelected(true);
                        // Update other fields as needed
                        // ...
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    MainScreen.stmt.executeUpdate("DELETE FROM accounts WHERE id = (SELECT max_id FROM (SELECT MAX(id) AS max_id FROM accounts) AS subquery);\n");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }                    MainScreen.accountOf.refresh();

            }
        });

        dateB.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                requestDateFilter=true;
                MainScreen.accountOf.refresh();
                requestDateFilter=false;
            }
        });
    }
    String date(){
       String date= dateB.getSelectedItem().toString();
        DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yy");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date d=dateFormat1.parse(date);
            date=dateFormat2.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public void setNameB(String name) {
        nameB.setSelectedItem(name);
    }

    public void setAmountT(double amount) {
        amountT.setText(""+amount);
    }

    public void setआवकRadioButton() {
        आवकRadioButton.setSelected(true);
    }
    public void setNonMonetary(boolean a){
        material.setSelected(a);
    }

    public void setजावकRadioButton() {
        जावकRadioButton.setSelected(true);
    }

    public void setDateB(String date) {
        dateB.setSelectedItem(date);
    }


    public void setRemark(String remark){
        this.remark.setText(remark);
    }
public void edit(){
        try{
            String query;
            String R_G = "";
            if (आवकRadioButton.isSelected()) {
                R_G = "R";

            } else if (जावकRadioButton.isSelected()) {
                R_G = "G";
            } else {
                JOptionPane.showMessageDialog(panel1, "Please select avak or javak", "error", JOptionPane.ERROR_MESSAGE);
            }
            String value;
            int ID=MainScreen.accountOf.getTransactionID();
            int incashValue = material.isSelected() ? 0 : 1;
            String rGValue = आवकRadioButton.isSelected() ? "R" : "G";

            value = amountT.getText();
            query = "UPDATE accounts SET AMOUNT = " + value + " WHERE ID = " + ID;
            MainScreen.stmt.executeUpdate(query);

            value = remark.getText();
            query = "UPDATE accounts SET REMARK = '" + value + "' WHERE ID = " + ID;
            MainScreen.stmt.executeUpdate(query);

            value = nameB.getSelectedItem().toString();
            query = "UPDATE accounts SET name = '" + value + "' WHERE ID = " + ID;
            MainScreen.stmt.executeUpdate(query);

            value = medium.getText();
            query = "UPDATE accounts SET medium = '" + value + "' WHERE ID = " + ID;
            MainScreen.stmt.executeUpdate(query);

            query = "UPDATE accounts SET r_G = '" + rGValue + "' WHERE ID = " + ID;
            MainScreen.stmt.executeUpdate(query);

            query = "UPDATE accounts SET InCash = " + incashValue + " WHERE ID = " + ID;
            MainScreen.stmt.executeUpdate(query);

            nameB.setSelectedIndex(0);
            amountT.setText("");
            medium.setText("");
            remark.setText("");
            nameB.transferFocus();
            try {
                int lastRow = MainScreen.accountOf.table1.getRowCount() - 1;
                MainScreen.accountOf.table1.setRowSelectionInterval(lastRow, lastRow);
                lastRow = MainScreen.accountOf.table2.getRowCount() - 1;
                MainScreen.accountOf.table2.setRowSelectionInterval(lastRow, lastRow);
            } catch (NullPointerException e){JOptionPane.showMessageDialog(this,"first transaction");}
            MainScreen.accountOf.refresh();


            MainScreen.stmt.executeUpdate("set @balance=0;");
            if(!material.isSelected()) {
                MainScreen.stmt.executeUpdate("UPDATE accounts\n" +
                        "SET balance =\n" +
                        "  CASE\n" +
                        "    WHEN r_G = 'R' THEN @balance := @balance + AMOUNT\n" +
                        "    WHEN r_G = 'G' THEN @balance := @balance - AMOUNT\n" +
                        "  END\n" +
                        "WHERE inCash = 1\n" + // <-- added WHERE clause
                        "ORDER BY date_;");
                System.out.print("updated");
            }

            query = "UPDATE people SET outstandings = (SELECT SUM(CASE WHEN r_G = 'G' THEN amount ELSE 0 END) - SUM(CASE WHEN r_G = 'R' THEN amount ELSE 0 END) FROM accounts WHERE accounts.name = people.name);";
            MainScreen.stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
}
public void setEnabledReg(boolean en){
        registerButton.setEnabled(en);
}
}



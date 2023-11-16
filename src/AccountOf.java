import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AccountOf extends JFrame {
    JTable table1;
    private JPanel panel1;
    JButton BACKButton;
    JTable table2;
    private JButton balance;
    JScrollPane pane1;
    private JButton editButton;
    JScrollPane pane2;
    private JButton filterButton;
    private JButton REFRESHButton;
    String title;
    int activeTable;


    public static void main(String args[]) {

    }


    public AccountOf(String nam) {
        this.title = nam;
        balance.setEnabled(false);
        setSize(MainScreen.fullx, MainScreen.fully);
        setContentPane(panel1);
        String columns1[] = {"Transaction_id", "date", "Name", "amount", "medium", "remark"};
        String columns2[] = {"Transaction_id", "date", "Name", "amount", "medium", "remark"};

        String data[][] = new String[6][];
        DefaultTableModel m1 = new DefaultTableModel(columns1, 0);
        DefaultTableModel m2 = new DefaultTableModel(columns2, 0);
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Tester().openPanel(new Filter(), "Select Filters");
            }
        });

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            ResultSet rs;
            boolean r_g = true;
            String query = "error";

            if (nam == "cash") {
                query = MainScreen.n1.getUser().contentEquals("root") ? "select id,date_,name,amount,r_g,remark,medium from accounts where inCash=true order by date_,id;" : "SELECT id, date_, name, amount, r_g, remark, medium FROM accounts WHERE inCash = true AND date_ >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY date_, id;";
                rs = MainScreen.stmt.executeQuery(query);

            } else {
                query = MainScreen.n1.getUser().contentEquals("root") ? "select id,date_,name,amount,r_g,remark,medium from accounts where name=\"" + nam + "\"  order by date_,id;" : "SELECT id, date_, name, amount, r_g, remark, medium FROM accounts WHERE name=\"" + nam + "\" AND date_ >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY date_, id;";
                rs = MainScreen.stmt.executeQuery(query);
            }
            setTitle(nam);
            String[] d = new String[7];
            int i = 0;
            Locale l = new Locale("en", "IN");
            DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, l);
            int count = 0;
            while (rs.next()) {
                for (i = 0; i < 7; i++) {
                    if (i == 4) {
                        if (rs.getString("R_G").contentEquals("R")) {
                            r_g = true;
                        } else {
                            r_g = false;
                        }
                    } else if (i == 1) {

                        String a = rs.getString("date_");
                        if (a != null)
                            d[i] = a.substring(8) + "-" + a.substring(5, 7) + "-" + a.substring(2, 4);
                    } else {
                        d[count] = rs.getString(i + 1);
                    }
                    count++;
                }
                count = 0;
                if (r_g) {
                    m1.addRow(d);
                } else {
                    m2.addRow(d);
                }
            }
            BACKButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    MainScreen.accounts.setVisible(true);
                }
            });

            table1.setModel(m1);
            table2.setModel(m2);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                showBalance(Integer.parseInt(table1.getValueAt(table1.getSelectedRow(), 0).toString()));
                setActiveTable(1);

            }
        });
        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showBalance(Integer.parseInt(table2.getValueAt(table2.getSelectedRow(), 0).toString()));
                setActiveTable(2);
            }
        });
        table1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                showBalance(Integer.parseInt(table1.getValueAt(table1.getSelectedRow(), 0).toString()));
                setActiveTable(1);
            }
        });
        table2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                showBalance(Integer.parseInt(table2.getValueAt(table2.getSelectedRow(), 0).toString()));
                setActiveTable(2);

            }
        });

        REFRESHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
    }

    ActionListener mode1;

    ActionListener mode2;

    public AccountOf(String nam, int act) {
        //if act is 0, then the call is from Accounts class
        //if act is 1 then call is from newTransaction of class
        this(nam);
        if (act == 1) {
            AccountOf dump = this;
            mode2 = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainScreen.transaction.edit();
                    editButton.setText("Edit transaction");
                    MainScreen.transaction.setEnabledReg(true);
                    editButton.removeActionListener(this);
                    editButton.addActionListener(mode1);
                    System.out.print("\nback to mode 2");

                }
            };
            mode1 = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (getActiveTable() == 1) {
                        retrieveAndSetData(table1);
                    }
                    if (getActiveTable() == 2) {
                        retrieveAndSetData(table2);
                    }
                    if (flagForNoRowSelection) return;
                    editButton.setText("Submit edited response");
                    editButton.removeActionListener(this);
                    editButton.addActionListener(mode2);
                }
            };
            editButton.addActionListener(mode1);

        } else if (act == 2) {
        }

    }


    void setActiveTable(int id) {
        activeTable = id;
    }

    int getActiveTable() {
        return activeTable;
    }

    private int transactionID;

    int getTransactionID() {
        return transactionID;
    }

    boolean flagForNoRowSelection = false;

    void retrieveAndSetData(JTable table) {
        System.out.print("oh boy");
        int id = 0;
        try {
            id = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please select a record");
            flagForNoRowSelection = true;
            return;

        }
        System.out.print(id + "\n");

        transactionID = id;

        String query = "select* from accounts where id=" + id + ";";
        try {
            ResultSet rs = MainScreen.stmt.executeQuery(query);
            String strings;
            System.out.print("oh boy2");
            if (rs.next()) {
                strings = rs.getString("date_");
                Date originalDate = new SimpleDateFormat("yyyy-MM-dd").parse(strings);
                String formattedDate = new SimpleDateFormat("dd/MM/yy").format(originalDate);
                MainScreen.transaction.setDateB(formattedDate);
                strings = rs.getString("amount");
                MainScreen.transaction.setAmountT(Double.parseDouble(strings));
                strings = rs.getString("name");
                MainScreen.transaction.setNameB(strings);
                strings = rs.getString("REMARK");
                MainScreen.transaction.setRemark(strings);
                strings = rs.getString("r_G");
                if (strings.contentEquals("R")) {
                    MainScreen.transaction.setआवकRadioButton();

                } else if (strings.contentEquals("G")) {
                    MainScreen.transaction.setजावकRadioButton();
                }
                strings = rs.getString("incash");
                if (Integer.parseInt(strings) == 1) {
                    MainScreen.transaction.setNonMonetary(false);
                } else {
                    MainScreen.transaction.setNonMonetary(true);
                }
            }
            MainScreen.transaction.setEnabledReg(false);


        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showBalance(int id) {
        try {
            String sqlQuery = "";
            if (title.contentEquals("cash")) {
                sqlQuery = "SELECT balance FROM accounts WHERE id=(" +
                        "SELECT MAX(id) FROM accounts WHERE date_=(" +
                        "SELECT MAX(date_) FROM accounts WHERE date_<(" +
                        "SELECT date_ FROM accounts WHERE id=" + id + ")) AND incash=1);";
            } else {
                sqlQuery = "select outstandings from people where name ='" + title + "';";
            }
            ResultSet r = MainScreen.stmt.executeQuery(sqlQuery);
            while (r.next())
                balance.setText("balance: " + r.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void refresh() {
        DefaultTableModel m1 = (DefaultTableModel) table1.getModel();
        DefaultTableModel m2 = (DefaultTableModel) table2.getModel();
        m1.setRowCount(0);
        m2.setRowCount(0);
        ResultSet rs;
        boolean r_g = false;
        try {
            System.out.print(MainScreen.transaction.requestDateFilter);
            String query;
            if (title.contentEquals("cash")) {
                if (!MainScreen.transaction.requestDateFilter)
                    query = MainScreen.n1.getUser().contentEquals("root") ? "select id,date_,name,amount,r_g,remark,medium from accounts where inCash=true order by date_,id;" : "SELECT id, date_, name, amount, r_g, remark, medium FROM accounts WHERE inCash = true AND date_ >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY date_, id;";
                else {
                    query = "select id,date_,name,amount,r_g,remark,medium from accounts where (inCash=true and date_='" + MainScreen.transaction.date() + "') order by date_,id;";

                }
                rs = MainScreen.stmt.executeQuery(query);
            } else {
                query = MainScreen.n1.getUser().contentEquals("root") ?
                        "select id,date_,name,amount,r_g,remark,medium from accounts where name=\"" + title + "\"  order by date_,id;" :
                        "SELECT id, date_, name, amount, r_g, remark, medium FROM accounts WHERE name=\"" + title + "\" AND date_ >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY date_, id;";
                rs = MainScreen.stmt.executeQuery(query);
            }

            String[] d = new String[7];
            int i = 0;
            Locale l = new Locale("en", "IN");
            DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, l);
            int count = 0;
            while (rs.next()) {
                for (i = 0; i < 7; i++) {
                    if (i == 4) {
                        if (rs.getString("R_G").contentEquals("R")) {
                            r_g = true;
                        } else {
                            r_g = false;
                        }
                    } else if (i == 1) {

                        String a = rs.getString("date_");
                        if (a != null)
                            d[i] = a.substring(8) + "-" + a.substring(5, 7) + "-" + a.substring(2, 4);
                    } else {
                        d[count] = rs.getString(i + 1);
                    }
                    count++;
                }
                count = 0;
                if (r_g) {
                    m1.addRow(d);
                } else {
                    m2.addRow(d);
                }
            }

            Tester t = new Tester();
            t.scrollDown(MainScreen.accountOf.pane1);
            t.scrollDown(MainScreen.accountOf.pane2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


//    public AccountOf() {
//
//        setVisible(true);
//        setSize(MainScreen.fullx, MainScreen.fully);
//        setContentPane(panel1);
//        String columns[] = {"Transaction_id", "Name", "amount", "R/G", "remark", "medium"};
//        String data[][] = new String[6][];
//        DefaultTableModel m = new DefaultTableModel(columns, 0);
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
////here sonoo is database name, root is username and password
//            ResultSet rs = MainScreen.stmt.executeQuery("select*from accounts where InCash=true;");
//            String[] d = new String[6];
//            int i = 0;
//            while (rs.next()) {
//
//                for (i = 0; i < 6; i++) {
//                    if (i == 3) {
//                        if (rs.getString("R_G").contentEquals("R")) {
//                            d[i] = "आवक";
//
//                        } else {
//                            d[i] = "जावक";
//                        }
//                    } else {
//                        d[i] = rs.getString(i + 1);
//                    }
//                }
//                m.addRow(d);
//            }
//
//            table1.setModel(m);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        BACKButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                setVisible(false);
//                MainScreen.accounts.setVisible(true);
//            }
//        });
//
//
//
//    }
}

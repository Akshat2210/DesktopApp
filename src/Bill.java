import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Bill extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JButton prevButton;
    private JButton nextButton;
    private JButton backButton;
    private JTextArea Area;
    private JLabel label;
    String title;
    DefaultTableModel m;
    int id;
    int idC = 0;
    Statement st = null;



    public static void main(String args[]) {

        Bill b = new Bill();
        b.idC = 2;
        b.setVisible(true);

    }
    void showBill(int id) {
        String ar[] = new String[11];
        try {
            m.setRowCount(0);
            ResultSet rs = MainScreen.stmt.executeQuery("select itemname,k,l,c,cm,m,quantity,gold,goldrate,total from billnumber where id=" + id + ";");
            int i = 1;
            while (rs.next()) {
                while (i < 11) {
                    ar[i - 1] = rs.getString(i);
                    i++;
                }
                m.addRow(ar);
                i = 1;
                rs=MainScreen.stmt.executeQuery("select sum(amount) from people where name= '"+title+"';");
                int out=0;
                while(rs.next())
                {
                    out=Integer.parseInt(rs.getString("outstandings"));
                }
                rs=MainScreen.stmt.executeQuery("select amount,date_ from accounts where bill_id="+idC);
                while(rs.next())
                {
                    Area.append(rs.getString("date_")+" recieved "+ rs.getString("amount")+"\n");
                }
                Area.append("Outstandings= "+out);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

        }
//        String[] a={"Name","K","L","C","CM","M","quantity","Gold","goldPrice","Total"};

        table1.setModel(m);
    }
    void addItem(String[] a) {
        PreparedStatement stmt = null;

        String ar[] = new String[11];
        try {
            String query = "INSERT INTO billnumber  (customerName,itemname, K, L, C, CM, M, Gold, GoldRate, Total, id,quantity) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
            stmt = MainScreen.con.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, a[1]);
            stmt.setInt(3, Integer.parseInt(a[2]));
            stmt.setInt(4, Integer.parseInt(a[3]));
            stmt.setInt(5, Integer.parseInt(a[4]));
            stmt.setInt(6, Integer.parseInt(a[5]));
            stmt.setInt(7, Integer.parseInt(a[6]));
            stmt.setFloat(8, Float.parseFloat(a[7]));
            stmt.setInt(9, Integer.parseInt(a[8]));
            stmt.setInt(10, Integer.parseInt(a[9]));
            stmt.setInt(11, Integer.parseInt(a[10]));
            stmt.setInt(12, Integer.parseInt(a[11]));
            stmt.executeUpdate();
            MainScreen.stmt.executeUpdate("update billnumber set total=((K+L+C+CM+M)*quantity)+(gold*goldrate)");
            ResultSet rs = stmt.executeQuery("select itemname,k,l,c,cm,m,quantity,gold,goldrate,total from billnumber where id=" + id + ";");
            int i = 1;
            while (rs.next()) {
                while (i < 11) {
                    ar[i - 1] = rs.getString(i);
                    i++;
                }
                i = 1;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
//        String[] a={"Name","K","L","C","CM","M","quantity","Gold","goldPrice","Total"};
        m.addRow(ar);
        table1.setModel(m);
    }
    public Bill(String title) {
        this();
        try {
            this.title=title;

            ResultSet rs=MainScreen.stmt.executeQuery("select id from bills where custname='"+title+"';");
            while(rs.next()) {
                idC = Integer.parseInt(rs.getString("id"));

            }
            showBill(idC);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Bill() {

        setContentPane(panel1);
        String[] a = {"Name", "K", "L", "C", "CM", "M", "quantity", "Gold", "goldPrice", "Total"};
        m = new DefaultTableModel(a, 0);
        nextButton.setEnabled(true);
        prevButton.setEnabled(true);


        pack();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        setVisible(false);
        MainScreen.order.setVisible(true);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
Area.setText("");
                ResultSet rs = null;
                try {

                    rs = MainScreen.stmt.executeQuery("select * from bills where id>" + idC + ";");
                    while (rs.next()) {

                        if (rs.getString("custname").toLowerCase().contentEquals(title.toLowerCase())) {
                            idC = Integer.parseInt(rs.getString("id"));

                            showBill(idC);
                            break;
                        }

                    }

                    rs = MainScreen.stmt.executeQuery("select itemname,k,l,c,cm,m,quantity,gold,goldrate,total from billnumber where id=" + id + ";");

                    while (rs.next()) {
                        if (rs.getString("custname").contentEquals(title)) {
                            showBill(idC);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultSet rs;
                Area.setText("");



                try {
                    rs = MainScreen.stmt.executeQuery("select * from bills where id<" + idC + ";");
                    while (rs.next());
                    while(rs.previous())
                    {
                        if (rs.getString("custname").toLowerCase().contentEquals(title.toLowerCase())) {
                            idC = Integer.parseInt(rs.getString("id"));
                            showBill(idC);
                            break;
                        }

                    }

                    rs = MainScreen.stmt.executeQuery("select itemname,k,l,c,cm,m,quantity,gold,goldrate,total from billnumber where id=" + id + ";");

                    while (rs.next()) {
                        if (rs.getString("custname").contentEquals(title)) {
                            showBill(idC);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
    public Bill(String title, int id, int x, int y) {

        this();
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainScreen.newBill.setVisible(false);
                MainScreen.order.setVisible(true);
            }
        });
        this.id = id;
        this.title = title;
        setTitle(id + title);
        setLocation(x, y);
        setVisible(true);

        pack();
    }



}

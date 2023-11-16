    import com.sun.tools.javac.Main;

    import javax.swing.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.sql.*;

    public class NewBill extends JFrame {
        private JTextField textField1;
        private JTextField textField2;
        private JTextField textField4;
        private JTextField textField5;
        private JTextField textField6;
        private JTextField textField7;
        private JTextField textField9;
        private JButton addItemButton;
        private JButton submitBillButton;
        private JTextField textField3;
        private JPanel panel1;
        private JPanel panels;
        private JLabel title1;
        private JTextField textField8;
        private JButton backButton;
        Bill window=null;


        public static void main(String args[]) {
            new NewBill().setVisible(true);
        }
        int itemNo;

        NewBill(int id , String title) {

            setContentPane(panels);
            pack();
            setSize(getWidth() + 25, getHeight() + 25);
            setLocation(440, 200);
            ResultSet rs;
            try {

               MainScreen.stmt.executeUpdate("insert into bills(id,custname) values("+id+",'"+title+"');");
            } catch (SQLException ex) {
                ex.printStackTrace();

            }
            title1.setText(id +" "+ title);
            setTitle(title + " " + id);
            addItemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String t = "";
                    if (itemNo == 0) {
                        window = new Bill(title+ " ", id, getX() + getWidth(), getY());
                        window.setVisible(true);
                    }
                    String itemName=textField1.getText();
                    String K=textField2.getText(),
                    L=textField8.getText(),
                            C=textField5.getText(),
                            CM=textField3.getText()
                            ,M=textField4.getText()
                            ,Gold=textField6.getText(),
                            GoldRate=textField7.getText(),
                            Total="",q=textField9.getText();
                    String a[]={title,itemName,K,L,C,CM,M,Gold,GoldRate,Total="0",id+"",q};
                    window.addItem(a);
                    textField1.setText("");
                    textField2.setText("");
                    textField3.setText("");
                    textField4.setText("");
                    textField5.setText("");
                    textField6.setText("");
                    textField8.setText("");
                    textField9.setText("");
        backButton.setEnabled(false);
    //here sonoo is database name, root is username and password

                    itemNo++;
                }
            });
            submitBillButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Statement stmt2= MainScreen.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs= MainScreen.stmt.executeQuery("select*from accounts where name='"+title+"';");
                        while(rs.next()) {
                            if (rs.getString("bill_id")==null) {
                                stmt2.executeUpdate("update accounts set bill_id="+id +" where id="+rs.getString("id"));
                            }
                        }
                        stmt2.close();
                        rs= MainScreen.stmt.executeQuery("select sum(total) from billnumber where id="+id);
                        int total=0;
                        while (rs.next())
                            total=Integer.parseInt(rs.getString(1));
                        MainScreen.stmt.executeUpdate("update people set outstandings=outstandings-"+total+";");
                        rs= MainScreen.stmt.executeQuery("select id from bills where name = "+title);
                        rs.last();
                        rs.previous();
                        int t=rs.getInt("forwardamount");
                        MainScreen.stmt.executeUpdate("update bills set curamount="+t+";");



                    }
                    catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
            });

        }






        NewBill(){
        this(0,"hi");

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    MainScreen.order.setVisible(true);
                }
            });

        }
    }

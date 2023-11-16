import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Order extends JFrame {
    private JButton currentOrdersButton;
    private JButton backButton;
    private JButton newBillButton;
    private JButton checkAvailableStockButton;
    private JButton purchasesButton;
    private JButton salesButton;
    private JButton NEWORDERButton;
    private JPanel panel1;
    private JButton checkBillsButton;

    public static void main(String args[]){
new Order().setVisible(true);
    }
    Order(){
        setContentPane(panel1);
        pack();
        newBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result="0";
                String title;
                try {
                    ResultSet rs= MainScreen.stmt.executeQuery("select max(id) from bills;");
                    while(rs.next())result=rs.getString(1);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                int id=Integer.parseInt(result);
                id++;
                setVisible(false);

                new BillNameIInput(id,0).setVisible(true);


            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainScreen.loggedIn.setVisible(true);
            }
        });
        checkBillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new BillNameIInput().setVisible(true);
            }
        });
    }
}

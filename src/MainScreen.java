import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainScreen extends JFrame {
    private String pass = "";
    static int fullx = 1375, fully = 700;
    private final int port = 1254;
    private JPanel panel1;
    private String user;
    public static String databasename;
    private JTextField USER;
    private JTextField PASS;
    private JButton SUBMITFORMButton;
    private JButton signUpButton;
    private JTextField database;
    private JCheckBox betaModeCheckBox;
    private boolean verified = false;
    static MainScreen n1;
    static LoggedIn loggedIn;
    static Accounts accounts;
    static AccountOf accountOf;
    static newTransaction transaction;
    static AddPeople addPeople;
    static Order order;
    static NewBill newBill;

    static Statement stmt;
    static Connection con;

    private void LoggedIn() {

    }

    public String getUser() {
        return user;
    }

    public void clearPass() {
        pass = "";
    }

    public boolean getVerification() {
        return verified;
    }
public String getDatabasename(){
        return database.getText();
}
    public static void main(String args[]) {

        n1 = new MainScreen(false);
        n1.setVisible(true);

    }

    public String getPass() {
        return pass;
    }

    MainScreen(boolean beta) {
        betaModeCheckBox.setSelected(beta);
        if(betaModeCheckBox.isSelected()) url="jdbc:mysql://gurukripa.cvnekrmf4svl.eu-north-1.rds.amazonaws.com:3306/";
        else url="jdbc:mysql://localhost/";
        setVisible(true);
        add(panel1);
        setSize(MainScreen.fullx, fully);
        loggedIn = new LoggedIn();
        SUBMITFORMButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                user = USER.getText();
                pass = PASS.getText();
                MainScreen.loggedIn.backUpAndRestoreButton.setEnabled(MainScreen.n1.getUser().contentEquals("root"));

                databasename = database.getText();
                System.out.print(databasename);
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection(
                            url + databasename, user, pass);
//here sonoo is database ysqname, root is username and password
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    verified = true;
                    setVisible(false);
                    loggedIn.init();
                    loggedIn.setVisible(true);
                    USER.setText("");
                    PASS.setText("");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MainScreen.n1, "You might have entererd wrong password or id");
                }
                if (verified) {
                    LoggedIn();
                }
            }

        });
        PASS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SUBMITFORMButton.doClick();
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                signup();
            }
        });
        betaModeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean b=betaModeCheckBox.isSelected();
                n1.setVisible(false);
                n1=null;
                n1=new MainScreen(b);
                n1.setVisible(true);

                System.gc();

            }
        });
    }
    String url="jdbc:mysql://gurukripa.cvnekrmf4svl.eu-north-1.rds.amazonaws.com:3306/";

    void signup(){
        user = USER.getText();
        pass = PASS.getText();
        databasename = user;
        System.out.print(databasename);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    url, user, pass);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            USER.setText("");
            PASS.setText("");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MainScreen.n1, "You might have entererd wrong password or id");
                return;
        }
        setVisible(false);
        new SignUp().setVisible(true);
    }

}





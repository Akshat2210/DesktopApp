import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUp extends JFrame {
    private JButton backButton;
    private JButton signUpButton;
    private JTextField user;
    private JTextField password;
    private JPanel panel;
    private JComboBox auth;

    void clear() {
        user.setText("");
        password.setText("");
    }

    public static void main(String args[]) {

    }

    SignUp() {
        SignUp obj = this;
        setLocation(500,500);
        setContentPane(panel);
        try {
            MainScreen.stmt.execute("use userauth;");
            ResultSet rs = MainScreen.stmt.executeQuery("select distinct auth from users;");
            auth.addItem("select authority");
            while (rs.next()){
               auth.addItem(rs.getString("auth"));
            }

        } catch (ArithmeticException ex) {
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(auth.getSelectedIndex()==0 || user.getText().isBlank() || password.getText().isBlank())return;

                    ResultSet rs = MainScreen.stmt.executeQuery("SELECT SCHEMA_NAME\n" +
                            "FROM information_schema.SCHEMATA\n" +
                            "WHERE SCHEMA_NAME = '" + user.getText() + "';");
                    while (rs.next()) {
                        JOptionPane.showMessageDialog(obj, "user already exists", "Warning", JOptionPane.ERROR_MESSAGE);
                        clear();
                        return;

                    }
                    MainScreen.stmt.executeUpdate("create database " + user.getText() + ";");
                    MainScreen.stmt.executeUpdate("use " + user.getText() + ";");
                    MainScreen.stmt.executeUpdate("create user " + user.getText() + "@'%' identified by" + "'" + password.getText() + "';");
                    MainScreen.stmt.executeUpdate("Grant all privileges on " + user.getText() + ".* to " + user.getText() + ";");

                    MainScreen.stmt.execute("use userauth;");
                    MainScreen.stmt.executeUpdate("insert into users values('"+user.getText()+"','"+auth.getSelectedItem().toString()+"');");
                    Tester t = new Tester();
                    //CREATE USER akshat IDENTIFIED BY 'your_password';
                    //GRANT ALL PRIVILEGES ON askhat.* TO akshat;

                    MainScreen.stmt.execute("use "+user.getText());
                    String dump = t.readFile("E:\\Akshat\\PROGRAMMING\\JAVA\\ManagementProject\\src\\structure.sql");
                    String[] queries = dump.split(";");
                    for (String query : queries) {
                        MainScreen.stmt.execute(query+";");
                        System.out.println(query+";");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(obj, "unexpected error occured", "Warning", JOptionPane.ERROR_MESSAGE);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainScreen.n1.setVisible(true);

            }
        });
        pack();
    }

}

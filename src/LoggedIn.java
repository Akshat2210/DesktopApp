import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoggedIn extends JFrame {
    private JButton accountsButton;
    private JPanel panel1;
    private JButton signOutButton;
    private JButton ORDERSButton;
    private JButton stocksButton;
    public JButton backUpAndRestoreButton;

    public static void main(String args[])
    {
        LoggedIn i=new LoggedIn();

    }
    public void init(){
        MainScreen.accounts=new Accounts();
        MainScreen.transaction=new newTransaction();
        MainScreen.addPeople=new AddPeople();
        MainScreen.order=new Order();

    }
    public LoggedIn()
    {
        setSize(300,300);
        setLocation(MainScreen.fullx/2-150,MainScreen.fully/2-  150);
        setContentPane(panel1);


        accountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainScreen.accounts.setVisible(true);
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreen.n1.clearPass();
                dispose();
                MainScreen.n1.setVisible(true);
            }
        });

        ORDERSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreen.order.setVisible(true);
                setVisible(false);
            }
        });
        backUpAndRestoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create an array of options
                Object[] options = {"Backup", "Restore", "Cancel"};

                // Show the message dialog and store the user's choice in the result variable
                int result = JOptionPane.showOptionDialog(null, "Select an option:", "Options",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                // Check the user's choice and perform actions accordingly
                if (result == 0) {
                    JOptionPane.showMessageDialog(null, "Backup selected");
                    // Add your code for the "Backup" action here
                } else if (result == 1) {
                    JOptionPane.showMessageDialog(null, "Restore selected");
                    // Add your code for the "Restore" action here
                } else if (result == 2) {
                    JOptionPane.showMessageDialog(null, "Cancel selected");
                    // Add your code for the "Cancel" action here
                } else {
                    JOptionPane.showMessageDialog(null, "Dialog closed");
                }

            }
        });

        stocksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StockManager().setVisible(true);
            }
        });
    }
    void backup(){

        Tester t=new Tester();
        String user=MainScreen.n1.getUser();
        System.out.print(user);
        try {
            MainScreen.stmt.execute("use userAuth");
            ResultSet rs=MainScreen.stmt.executeQuery("select dbnam from dbset;");
            String fileContent="";
            String databaseList="";
            ArrayList<String> a=new ArrayList<>();
            while (rs.next()) {
                fileContent+="mysqldump -u root -p" + MainScreen.n1.getPass() +" "+ rs.getString("dbnam") + ">C:\\Windows\\Temp\\" + rs.getString("dbnam") + "\n";
                a.add(rs.getString("dbnam"));
                databaseList+=rs.getString("dbnam")+"\n";
            }
            fileContent+="cls\ncd E:\\Akshat\\PROGRAMMING\\JAVA\\ManagementProject\\src\\" + "\n" + "python E:\\Akshat\\PROGRAMMING\\JAVA\\ManagementProject\\src\\app.py \n";
            fileContent+="cd C:\\Windows\\Temp\nexit";
            t.createFile("C:\\Windows\\Temp\\del.bat", fileContent);
            t.createFile("list",databaseList);
            t.runCommand("C:\\Windows\\Temp\\del.bat");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

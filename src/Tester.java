
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tester {
    public static void main(String args[]){

    }
    void runCommand(String command,String path){
        try {
            ProcessBuilder p=new ProcessBuilder(new String[]{"cmd.exe","/c","start "+command});
            p.directory(new File(path));
            p.start();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("bhai gaya");
        }
    }
    void runCommand(String command){
        try {
            ProcessBuilder p=new ProcessBuilder(new String[]{"cmd.exe","/c","start "+command+ " /wait"});

            Process pr=p.start();

            while(pr.isAlive()){System.out.print(true);};

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("bhai gaya");
        }
    }
     void createFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            System.out.println("File created successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
     void scrollDown(JScrollPane p) {
        JScrollBar verticalScrollBar = p.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }
     ResultSet filter(String[] names) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Build a dynamic SQL query based on the provided names
            StringBuilder query = new StringBuilder("SELECT * FROM accounts WHERE name IN (");
            for (int i = 0; i < names.length; i++) {
                query.append("?");
                if (i < names.length - 1) {
                    query.append(",");
                }
            }
            query.append(")");

            // Create a PreparedStatement using the provided Connection object (MainScreen.con)
            preparedStatement = MainScreen.con.prepareStatement(query.toString());

            // Set the names as parameters in the PreparedStatement
            for (int i = 0; i < names.length; i++) {
                preparedStatement.setString(i + 1, names[i]);
            }

            // Execute the query and get the ResultSet
            resultSet = preparedStatement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during database interaction
            return null;
        } finally {
            // Close resources in reverse order: ResultSet, PreparedStatement
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
     void openPanel(JPanel panel, String title) {
        JOptionPane.showMessageDialog(null, panel, title, JOptionPane.PLAIN_MESSAGE);
    }
    public static void setResultSet(ResultSet rs) {
        Tester.rs = rs;
    }
    private static ResultSet rs;
    public static ResultSet getResultSet() {
        return Tester.rs;
    }
    public static void setContextMenu(JPopupMenu pm, String[] contents) {
        for (String item : contents) {
            JMenuItem menuItem = new JMenuItem(item);
            pm.add(menuItem);
        }
    }
    public static void showPopupMenu(JPopupMenu pm, Component component, MouseEvent e) {
        if (e.isPopupTrigger()) {
            pm.show(component, e.getX(), e.getY());
        }
    }
    public static void makeListMulti(JList<?> list) {
        // Set the selection mode to multiple interval selection
    }
    public boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    // Function to delete a file
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        } else {
            return false; // File does not exist
        }
    }
    public String readFile(String filepath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String Line; // Corrected to use an uppercase L in "Line"
            while ((Line = reader.readLine()) != null) {
                content.append(Line).append("\n");
            }
        }
        return content.toString();
    }
}

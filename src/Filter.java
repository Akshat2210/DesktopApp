
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Filter extends JPanel {
    private JList<String> nameList;
    private DefaultListModel<String> listModel;

    public Filter() {
        setLayout(new BorderLayout());

        // Create a DefaultListModel for the JList
        listModel = new DefaultListModel<>();

        // Create the JList
        nameList = new JList<>(listModel);
        nameList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


        Tester.makeListMulti(nameList);
        JScrollPane listScrollPane = new JScrollPane(nameList);
//        new Tester().
        // Create OK and Cancel buttons

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Add action listeners to the buttons
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected names
                String[] selectedNames = nameList.getSelectedValuesList().toArray(new String[0]);

                // Call the filter function with the selected names
                filter(selectedNames);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the panel (not the whole program)
                SwingUtilities.getWindowAncestor(Filter.this).dispose();
            }
        });

        // Create a JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Add components to the Filter panel
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load names from MySQL table "people"
        loadNamesFromDatabase();
    }

    // Load names from MySQL table "people" using existing connection and statement
    private void loadNamesFromDatabase() {
        try {
            // Create a SQL query to retrieve names from the "people" table
            String query = "SELECT name FROM people";

            // Execute the query using the existing statement
            ResultSet resultSet = MainScreen.stmt.executeQuery(query);

            // Populate the listModel with names
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                listModel.addElement(name);
            }

            // Close the result set (statement and connection should not be closed here)
            Tester.setResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Define your filter function here
    private void filter(String[] selectedNames) {
        // Implement your filtering logic here
        // You can use the selectedNames array to perform filtering operations
        // For example, print the selected names for demonstration purposes
        for (String name : selectedNames) {
            System.out.println("Selected Name: " + name);
        }
    }
}

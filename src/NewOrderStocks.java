import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewOrderStocks extends JFrame{
    private JComboBox nameCombo;
    public NewOrderStocks(){
        try {
            ResultSet rs=MainScreen.stmt.executeQuery("select name from people;");
            while (rs.next()) {
                String name = rs.getString("name");
                nameCombo.addItem(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

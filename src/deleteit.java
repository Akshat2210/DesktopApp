import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class deleteit {



        public static void main(String[] args) {
            try {
                // Register the MySQL driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Set the JDBC URL, username, and password
                String url = "jdbc:mysql://gurukripa.cvnekrmf4svl.eu-north-1.rds.amazonaws.com:3306/chillai";
                String username = "root";
                String password = "KryptoGraphy";

                // Establish the connection
                Connection connection = DriverManager.getConnection(url, username, password);

                // Do your database operations here

                // Don't forget to close the connection when done
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
}

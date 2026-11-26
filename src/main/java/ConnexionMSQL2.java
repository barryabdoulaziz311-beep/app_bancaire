import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionMSQL2 {
    private static final String URL = "jdbc:mysql://localhost:3306/banque_bdd?serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "2002";

    private static Connection ouvrir() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
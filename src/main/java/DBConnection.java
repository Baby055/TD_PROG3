import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getDBConnection() {
        String jdbcUrl = System.getenv("jdbc:postgresql://localhost:5432/mini_dish_db");
        String username = System.getenv("mini_dish_db_manager");
        String password = System.getenv("password123");

        if (jdbcUrl == null || username == null || password == null) {
            throw new IllegalStateException("Les variables d’environnement JDBC_URL, USERNAME ou PASSWORD ne sont pas définies.");
        }

        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion à la base de données", e);
        }
    }
}


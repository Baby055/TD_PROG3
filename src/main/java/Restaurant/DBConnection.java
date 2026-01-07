package Restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/mini_dish_db";
    private static final String USERNAME = "mini_dish_db_manager";
    private static final String PASSWORD = "password123";

    public static Connection getDBConnection() {
        try {
            Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("✅ Connexion réussie à PostgreSQL !");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la connexion : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}


package Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        String dishQuery = "SELECT * FROM Dish WHERE id = ?";
        String ingredientsQuery = "SELECT i.* FROM Ingredient i JOIN Dish_Ingredient di ON i.id = di.ingredient_id WHERE di.dish_id = ?";

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement dishStmt = conn.prepareStatement(dishQuery);
             PreparedStatement ingredientsStmt = conn.prepareStatement(ingredientsQuery)) {

            dishStmt.setInt(1, id);
            ResultSet dishRs = dishStmt.executeQuery();

            if (!dishRs.next()) return null;

            Dish dish = new Dish(dishRs.getInt("id"), dishRs.getString("name"));

            ingredientsStmt.setInt(1, id);
            ResultSet ingredientsRs = ingredientsStmt.executeQuery();

            List<Ingredient> ingredients = new ArrayList<>();
            while (ingredientsRs.next()) {
                ingredients.add(new Ingredient(
                        ingredientsRs.getInt("id"),
                        ingredientsRs.getString("name")
                ));
            }

            dish.setIngredients(ingredients);
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du plat", e);
        }
    }

    public List<Ingredient> findIngredients(int page, int size) {
        String query = "SELECT * FROM Ingredient LIMIT ? OFFSET ?";
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, size);
            stmt.setInt(2, page * size);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ingrédients", e);
        }

        return ingredients;
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        String checkQuery = "SELECT COUNT(*) FROM Ingredient WHERE name = ?";
        String insertQuery = "INSERT INTO Ingredient (name) VALUES (?)";

        try (Connection conn = DBConnection.getDBConnection()) {
            conn.setAutoCommit(false);

            for (Ingredient ing : newIngredients) {
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, ing.getName());
                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        conn.rollback();
                        throw new RuntimeException("L'ingrédient existe déjà : " + ing.getName());
                    }
                }
            }

            for (Ingredient ing : newIngredients) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, ing.getName());
                    insertStmt.executeUpdate();
                    ResultSet keys = insertStmt.getGeneratedKeys();
                    if (keys.next()) {
                        ing.setId(keys.getInt(1));
                    }
                }
            }

            conn.commit();
            return newIngredients;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création des ingrédients", e);
        }
    }
}

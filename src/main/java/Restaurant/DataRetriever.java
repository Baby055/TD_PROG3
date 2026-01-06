package Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        String dishQuery = "SELECT * FROM Dish WHERE id = ?";
        String ingredientsQuery = "SELECT * FROM Ingredient WHERE dish_id = ?";

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement dishStmt = conn.prepareStatement(dishQuery);
             PreparedStatement ingStmt = conn.prepareStatement(ingredientsQuery)) {

            dishStmt.setInt(1, id);
            ResultSet dishRs = dishStmt.executeQuery();

            if (!dishRs.next()) return null;

            Dish dish = new Dish(
                    dishRs.getInt("id"),
                    dishRs.getString("name"),
                    DishTypeEnum.valueOf(dishRs.getString("dish_type"))
            );

            ingStmt.setInt(1, id);
            ResultSet ingRs = ingStmt.executeQuery();

            List<Ingredient> ingredients = new ArrayList<>();
            while (ingRs.next()) {
                Ingredient ing = new Ingredient(
                        ingRs.getInt("id"),
                        ingRs.getString("name"),
                        ingRs.getDouble("price"),
                        CategoryEnum.valueOf(ingRs.getString("category")),
                        dish
                );
                ingredients.add(ing);
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
                Ingredient ing = new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        CategoryEnum.valueOf(rs.getString("category")),
                        null
                );
                ingredients.add(ing);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ingrédients", e);
        }

        return ingredients;
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        String checkQuery = "SELECT COUNT(*) FROM Ingredient WHERE name = ?";
        String insertQuery = "INSERT INTO Ingredient (name, price, category, dish_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getDBConnection()) {
            conn.setAutoCommit(false);

            for (Ingredient ing : newIngredients) {
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, ing.getName());
                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        conn.rollback(); // Annule toute opération
                        throw new RuntimeException("L'ingrédient existe déjà : " + ing.getName());
                    }
                }
            }

            // Insertion des ingrédients
            for (Ingredient ing : newIngredients) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, ing.getName());
                    insertStmt.setDouble(2, ing.getPrice());
                    insertStmt.setString(3, ing.getCategory().name());
                    insertStmt.setObject(4, ing.getDish() != null ? ing.getDish().getId() : null);
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
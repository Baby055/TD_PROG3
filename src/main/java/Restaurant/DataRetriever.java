package Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        String dishQuery = "SELECT * FROM Dish WHERE id = ?";
        String ingredientsQuery = "SELECT * FROM Ingredient WHERE id_dish = ?";

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement dishStmt = conn.prepareStatement(dishQuery);
             PreparedStatement ingStmt = conn.prepareStatement(ingredientsQuery)) {

            dishStmt.setInt(1, id);
            ResultSet dishRs = dishStmt.executeQuery();

            if (!dishRs.next()) {
                throw new RuntimeException("Plat introuvable avec l'ID : " + id);
            }

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
                        dish,
                        ingRs.getObject("required_quantity") != null ? ingRs.getDouble("required_quantity") : null
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
        String query = "SELECT * FROM Ingredient LIMIT ? OFFSET ?"; // LIMIT : nombre maximumu d'ingrédients à récupérer , OFFSET : décalage (page * taille)
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
                        null,
                        rs.getObject("required_quantity") != null ? rs.getDouble("required_quantity") : null
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
        String insertQuery = "INSERT INTO Ingredient (name, price, category, id_dish, required_quantity) VALUES (?, ?, ?, ?, ?)";

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
                    insertStmt.setDouble(2, ing.getPrice());
                    insertStmt.setString(3, ing.getCategory().name());
                    insertStmt.setObject(4, ing.getDish() != null ? ing.getDish().getId() : null);
                    if (ing.getRequiredQuantity() != null) {
                        insertStmt.setDouble(5, ing.getRequiredQuantity());
                    } else {
                        insertStmt.setNull(5, Types.NUMERIC);
                    }
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

    public void saveDishIngredient(DishIngredient di) throws SQLException {
        String sql = "INSERT INTO DishIngredient (id_dish, id_ingredient, quantity_required, unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, di.getDishId());
            ps.setInt(2, di.getIngredientId());
            ps.setDouble(3, di.getQuantityRequired());
            ps.setString(4, di.getUnit().name());
            ps.executeUpdate();
        }
    }

    public List<DishIngredient> findDishIngredientsByDish(int dishId) throws SQLException {
        String sql = "SELECT * FROM DishIngredient WHERE id_dish = ?";
        List<DishIngredient> list = new ArrayList<>();
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dishId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DishIngredient(
                        rs.getInt("id"),
                        rs.getInt("id_dish"),
                        rs.getInt("id_ingredient"),
                        rs.getDouble("quantity_required"),
                        UnitType.valueOf(rs.getString("unit"))
                ));
            }
        }
        return list;
    }
}
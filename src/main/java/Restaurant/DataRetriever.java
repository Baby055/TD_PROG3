package Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

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

    public String findIngredientNameById(int ingredientId) throws SQLException {
        String sql = "SELECT name FROM Ingredient WHERE id = ?";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ingredientId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Ingrédient introuvable (id=" + ingredientId + ")");
            }
            return rs.getString("name");
        }
    }

    public Order findOrderByReference(String reference) {
        String sql = """
                SELECT
                    o.id as order_id,
                    o.reference as order_reference,
                    o.creation_datetime as order_creation_datetime,
                    o.payment_status as order_payment_status,
                    s.id as sale_id,
                    s.creation_datetime as sale_creation_datetime
                FROM orders o
                LEFT JOIN sale s ON s.id = o.id_sale
                WHERE o.reference = ?
                """;

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reference);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Sale sale = null;
            Integer saleId = (Integer) rs.getObject("sale_id");
            if (saleId != null) {
                Timestamp saleTs = rs.getTimestamp("sale_creation_datetime");
                sale = new Sale(saleId, saleTs != null ? saleTs.toInstant() : null);
            }

            Timestamp orderTs = rs.getTimestamp("order_creation_datetime");
            return new Order(
                    rs.getInt("order_id"),
                    rs.getString("order_reference"),
                    orderTs != null ? orderTs.toInstant() : null,
                    PaymentStatusEnum.valueOf(rs.getString("order_payment_status")),
                    sale
            );
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de findOrderByReference(reference=" + reference + ")", e);
        }
    }

    public Order saveOrder(Order orderToSave) {
        if (orderToSave == null) {
            throw new IllegalArgumentException("orderToSave ne peut pas être null.");
        }
        if (orderToSave.getReference() == null || orderToSave.getReference().isBlank()) {
            throw new IllegalArgumentException("La référence de la commande est obligatoire.");
        }
        if (orderToSave.getPaymentStatus() == null) {
            throw new IllegalArgumentException("paymentStatus est obligatoire.");
        }

        Order existing = findOrderByReference(orderToSave.getReference());

        if (existing != null && existing.getPaymentStatus() == PaymentStatusEnum.PAID) {
            boolean isStrictNoOp =
                    orderToSave.getPaymentStatus() == PaymentStatusEnum.PAID
                            && (orderToSave.getId() == null || orderToSave.getId().equals(existing.getId()));
            if (isStrictNoOp) {
                return existing;
            }
            throw new RuntimeException("La commande " + existing.getReference() + " est déjà payée et ne peut plus être modifiée.");
        }

        if (existing == null) {
            String insert = "INSERT INTO orders(reference, creation_datetime, payment_status) VALUES (?, ?, ?) RETURNING id, creation_datetime";
            try (Connection conn = DBConnection.getDBConnection();
                 PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, orderToSave.getReference());
                Instant created = orderToSave.getCreationDatetime() != null ? orderToSave.getCreationDatetime() : Instant.now();
                ps.setTimestamp(2, Timestamp.from(created));
                ps.setString(3, orderToSave.getPaymentStatus().name());

                ResultSet rs = ps.executeQuery();
                rs.next();
                orderToSave.setId(rs.getInt("id"));
                Timestamp ts = rs.getTimestamp("creation_datetime");
                orderToSave.setCreationDatetime(ts != null ? ts.toInstant() : created);
                return orderToSave;
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de saveOrder (insert)", e);
            }
        }

        String update = "UPDATE orders SET payment_status = ? WHERE reference = ? RETURNING id, reference, creation_datetime, payment_status, id_sale";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setString(1, orderToSave.getPaymentStatus().name());
            ps.setString(2, orderToSave.getReference());
            ResultSet rs = ps.executeQuery();
            rs.next();

            Integer idSale = (Integer) rs.getObject("id_sale");
            Sale sale = null;
            if (idSale != null) {
                sale = new Sale(idSale, null);
            }

            Timestamp ts = rs.getTimestamp("creation_datetime");
            return new Order(
                    rs.getInt("id"),
                    rs.getString("reference"),
                    ts != null ? ts.toInstant() : null,
                    PaymentStatusEnum.valueOf(rs.getString("payment_status")),
                    sale
            );
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de saveOrder (update)", e);
        }
    }

    public Sale createSaleFrom(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order ne peut pas être null.");
        }
        if (order.getReference() == null || order.getReference().isBlank()) {
            throw new IllegalArgumentException("La commande doit avoir une référence.");
        }

        Order persisted = findOrderByReference(order.getReference());
        if (persisted == null) {
            throw new RuntimeException("Impossible de créer une vente: la commande n'existe pas (reference=" + order.getReference() + ").");
        }

        if (persisted.getPaymentStatus() != PaymentStatusEnum.PAID) {
            throw new RuntimeException("Une vente ne peut être créée que pour une commande payée (reference=" + persisted.getReference() + ").");
        }

        if (persisted.getSale() != null) {
            throw new RuntimeException("La commande " + persisted.getReference() + " est déjà associée à une vente (id=" + persisted.getSale().getId() + ").");
        }

        String insertSale = "INSERT INTO sale(creation_datetime) VALUES (?) RETURNING id, creation_datetime";
        String linkOrder = "UPDATE orders SET id_sale = ? WHERE reference = ?";

        try (Connection conn = DBConnection.getDBConnection()) {
            conn.setAutoCommit(false);

            Sale sale;
            try (PreparedStatement ps = conn.prepareStatement(insertSale)) {
                Instant now = Instant.now();
                ps.setTimestamp(1, Timestamp.from(now));
                ResultSet rs = ps.executeQuery();
                rs.next();
                Timestamp ts = rs.getTimestamp("creation_datetime");
                sale = new Sale(rs.getInt("id"), ts != null ? ts.toInstant() : now);
            }

            try (PreparedStatement ps = conn.prepareStatement(linkOrder)) {
                ps.setInt(1, sale.getId());
                ps.setString(2, persisted.getReference());
                ps.executeUpdate();
            }

            conn.commit();
            return sale;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de createSaleFrom(reference=" + order.getReference() + ")", e);
        }
    }
}
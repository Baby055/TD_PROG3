package Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class OrderService {
    private final DataRetriever dataRetriever;

    public OrderService(DataRetriever dataRetriever) {
        this.dataRetriever = dataRetriever;
    }


    public Map<String, Double> saveOrder(int dishId, int numberOfDishes, Map<String, Double> currentStockKg) {
        if (numberOfDishes <= 0) {
            throw new IllegalArgumentException("numberOfDishes doit être > 0.");
        }

        try {
            List<DishIngredient> recipe = dataRetriever.findDishIngredientsByDish(dishId);
            List<StockMovement> outMovements = new ArrayList<>();

            for (DishIngredient di : recipe) {
                String ingredientName = dataRetriever.findIngredientNameById(di.getIngredientId());
                double totalQty = di.getQuantityRequired() * numberOfDishes;

                outMovements.add(new StockMovement(
                        0,
                        ingredientName,
                        totalQty,
                        di.getUnit(),
                        StockMovementType.OUT,
                        "saveOrder(dishId=" + dishId + ", qty=" + numberOfDishes + ")",
                        LocalDateTime.now()
                ));
            }

            return StockService.applyMovementsInKg(currentStockKg, outMovements);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de saveOrder", e);
        }
    }
}


package Restaurant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class StockService {
    private StockService() {}

    public static Map<String, Double> applyMovementsInKg(
            Map<String, Double> initialStockKgByIngredient,
            List<StockMovement> movements
    ) {
        Map<String, Double> stock = new LinkedHashMap<>();
        for (Map.Entry<String, Double> e : initialStockKgByIngredient.entrySet()) {
            stock.put(e.getKey(), e.getValue());
        }

        for (StockMovement m : movements) {
            double deltaKg = UnitConverter.toKg(m.getIngredientName(), m.getQuantity(), m.getUnit());
            if (m.getType() == StockMovementType.OUT) {
                deltaKg = -deltaKg;
            }

            stock.merge(m.getIngredientName(), deltaKg, Double::sum);
        }

        return stock;
    }
}


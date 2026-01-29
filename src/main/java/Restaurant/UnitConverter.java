package Restaurant;

import java.util.Locale;
import java.util.Map;

public final class UnitConverter { // Sert principalement à convertir des unités en kg
    private static final Map<String, IngredientUnitConversion> CONFIG_BY_INGREDIENT = Map.of(
            "tomate", new IngredientUnitConversion(10.0, 0.0),
            "laitue", new IngredientUnitConversion(2.0, 0.0),
            "chocolat", new IngredientUnitConversion(10.0, 2.5),
            "poulet", new IngredientUnitConversion(8.0, 0.0),
            "beurre", new IngredientUnitConversion(4.0, 5.0)
    );

    private UnitConverter() {}

    public static double toKg(String ingredientName, double quantity, UnitType unit) {
        if (ingredientName == null || ingredientName.isBlank()) {
            throw new IllegalArgumentException("ingredientName est requis.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("La quantité doit être positive.");
        }
        if (unit == null) {
            throw new IllegalArgumentException("unit est requis.");
        }

        String key = ingredientName.trim().toLowerCase(Locale.ROOT);
        IngredientUnitConversion cfg = CONFIG_BY_INGREDIENT.get(key);
        if (cfg == null) {
            throw new IllegalArgumentException("Aucune conversion définie pour l'ingrédient: " + ingredientName);
        }

        return switch (unit) {
            case KG -> quantity;
            case PCS -> {
                double pcsPerKg = cfg.getPcsPerKg();
                if (pcsPerKg <= 0) {
                    throw new IllegalArgumentException("Conversion PCS -> KG impossible pour: " + ingredientName);
                }
                yield quantity / pcsPerKg;
            }
            case L -> {
                double litersPerKg = cfg.getLitersPerKg();
                if (litersPerKg <= 0) {
                    throw new IllegalArgumentException("Conversion L -> KG impossible pour: " + ingredientName);
                }
                yield quantity / litersPerKg;
            }
        };
    }
}


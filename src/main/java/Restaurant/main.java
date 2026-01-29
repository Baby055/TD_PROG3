package Restaurant;

//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
//        DataRetriever retriever = new DataRetriever();
//
//        // === Test 1 : Récupérer un plat existant ===
//        System.out.println("=== Test findDishById(1) ===");
//        try {
//            Dish dish = retriever.findDishById(1);
//            System.out.println("Plat trouvé : " + dish.getName());
//            System.out.println("Type : " + dish.getDishType());
//            System.out.println("Nombre d'ingrédients : " + dish.getIngredients().size());
//            System.out.println("Coût du plat : " + dish.getDishCost());
//        } catch (Exception e) {
//            System.out.println("Erreur : " + e.getMessage());
//        }
//
//        // === Test 2 : Récupérer un plat inexistant ===
//        System.out.println("\n=== Test findDishById(999) ===");
//        try {
//            Dish dish = retriever.findDishById(999);
//            System.out.println("Plat trouvé : " + dish.getName());
//        } catch (Exception e) {
//            System.out.println("Exception attendue : " + e.getMessage());
//        }
//
//        // === Test 3 : Récupérer une liste d’ingrédients avec pagination ===
//        System.out.println("\n=== Test findIngredients(page=0, size=5) ===");
//        try {
//            List<Ingredient> ingredients = retriever.findIngredients(0, 5);
//            for (Ingredient ing : ingredients) {
//                System.out.println("- " + ing.getName() + " (" + ing.getPrice() + ")");
//            }
//        } catch (Exception e) {
//            System.out.println("Erreur : " + e.getMessage());
//        }
//
//        // === Test 4 : Créer de nouveaux ingrédients ===
//        System.out.println("\n=== Test createIngredients ===");
//        try {
//            Dish dummyDish = new Dish(1, "Plat factice", DishTypeEnum.START);
//
//            Ingredient ing1 = new Ingredient(0, "Carotte", 300.0, CategoryEnum.VEGETABLE, dummyDish, 2.0);
//            Ingredient ing2 = new Ingredient(0, "Pomme de terre", 500.0, CategoryEnum.VEGETABLE, dummyDish, 3.0);
//
//            List<Ingredient> newIngredients = retriever.createIngredients(List.of(ing1, ing2));
//
//            for (Ingredient ing : newIngredients) {
//                System.out.println("Ingrédient créé : " + ing.getName() + " (id=" + ing.getId() + ")");
//            }
//        } catch (Exception e) {
//            System.out.println("Erreur : " + e.getMessage());
//        }
//
//        // === BONUS K1 : Conversion d'unités (en mémoire) ===
//        System.out.println("\n=== BONUS K1 : Test conversion unités -> KG ===");
//
//        // Stock initial (KG) - selon l'énoncé
//        Map<String, Double> stockInitialKg = new LinkedHashMap<>();
//        stockInitialKg.put("Laitue", 5.0);
//        stockInitialKg.put("Tomate", 4.0);
//        stockInitialKg.put("Poulet", 10.0);
//        stockInitialKg.put("Chocolat", 3.0);
//        stockInitialKg.put("Beurre", 2.5);
//
//        // Nouveaux mouvements (tous OUT) - selon l'énoncé
//        List<StockMovement> movements = new ArrayList<>();
//        movements.add(new StockMovement(1, "Tomate", 5, UnitType.PCS, StockMovementType.OUT, "Préparation salade", LocalDateTime.now()));
//        movements.add(new StockMovement(2, "Laitue", 2, UnitType.PCS, StockMovementType.OUT, "Préparation salade", LocalDateTime.now()));
//        movements.add(new StockMovement(3, "Chocolat", 1, UnitType.L, StockMovementType.OUT, "Dessert", LocalDateTime.now()));
//        movements.add(new StockMovement(4, "Poulet", 4, UnitType.PCS, StockMovementType.OUT, "Plat principal", LocalDateTime.now()));
//        movements.add(new StockMovement(5, "Beurre", 1, UnitType.L, StockMovementType.OUT, "Pâtisserie", LocalDateTime.now()));
//
//        // Calcul des sorties en KG (pour affichage) + stock final
//        Map<String, Double> sortiesKg = new LinkedHashMap<>();
//        for (StockMovement m : movements) {
//            double kg = UnitConverter.toKg(m.getIngredientName(), m.getQuantity(), m.getUnit());
//            sortiesKg.merge(m.getIngredientName(), kg, Double::sum);
//        }
//        Map<String, Double> stockFinalKg = StockService.applyMovementsInKg(stockInitialKg, movements);
//
//        System.out.println("\nIngrédient | Stock avant (KG) | Sortie (KG) | Stock final (KG)");
//        for (Map.Entry<String, Double> e : stockInitialKg.entrySet()) {
//            String ing = e.getKey();
//            double before = e.getValue();
//            double outKg = sortiesKg.getOrDefault(ing, 0.0);
//            double after = stockFinalKg.getOrDefault(ing, before);
//            System.out.printf("%-9s | %-15.1f | %-10.1f | %-14.1f%n", ing, before, outKg, after);
//        }

        DataRetriever data = new DataRetriever();
        Order o = data.findOrderByReference("201");
                System.out.println(o);
    }
}
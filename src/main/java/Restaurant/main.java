package Restaurant;

import java.util.List;

public class main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();

        // === Test 1 : Récupérer un plat existant ===
        System.out.println("=== Test findDishById(1) ===");
        try {
            Dish dish = retriever.findDishById(1);
            System.out.println("Plat trouvé : " + dish.getName());
            System.out.println("Type : " + dish.getDishType());
            System.out.println("Nombre d'ingrédients : " + dish.getIngredients().size());
            System.out.println("Coût du plat : " + dish.getDishCost());
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        // === Test 2 : Récupérer un plat inexistant ===
        System.out.println("\n=== Test findDishById(999) ===");
        try {
            Dish dish = retriever.findDishById(999);
            System.out.println("Plat trouvé : " + dish.getName());
        } catch (Exception e) {
            System.out.println("Exception attendue : " + e.getMessage());
        }

        // === Test 3 : Récupérer une liste d’ingrédients avec pagination ===
        System.out.println("\n=== Test findIngredients(page=0, size=5) ===");
        try {
            List<Ingredient> ingredients = retriever.findIngredients(0, 5);
            for (Ingredient ing : ingredients) {
                System.out.println("- " + ing.getName() + " (" + ing.getPrice() + ")");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        // === Test 4 : Créer de nouveaux ingrédients ===
        System.out.println("\n=== Test createIngredients ===");
        try {
            Dish dummyDish = new Dish(1, "Plat factice", DishTypeEnum.START);

            Ingredient ing1 = new Ingredient(0, "Carotte", 300.0, CategoryEnum.VEGETABLE, dummyDish, 2.0);
            Ingredient ing2 = new Ingredient(0, "Pomme de terre", 500.0, CategoryEnum.VEGETABLE, dummyDish, 3.0);

            List<Ingredient> newIngredients = retriever.createIngredients(List.of(ing1, ing2));

            for (Ingredient ing : newIngredients) {
                System.out.println("Ingrédient créé : " + ing.getName() + " (id=" + ing.getId() + ")");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
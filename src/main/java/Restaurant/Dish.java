package Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;

    public Dish() {
        this.ingredients = new ArrayList<>();
    }

    public Dish(int id, String name, DishTypeEnum dishType) {
        this();
        this.id = id;
        this.name = name;
        this.dishType = dishType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        if (ingredients != null) {
            for (Ingredient ingredient : ingredients) {
                ingredient.setDish(this);
            }
        }
    }

    public Double getDishCost() {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRequiredQuantity() == null) {
                throw new RuntimeException("Quantité inconnue pour l'ingrédient: " + ingredient.getName());
            }
            total += ingredient.getPrice() * ingredient.getRequiredQuantity();
        }
        return total;
    }

    public void addIngredient(Ingredient ingredient) {
        if (this.ingredients == null) {
            this.ingredients = new ArrayList<>();
        }
        this.ingredients.add(ingredient);
        ingredient.setDish(this);
    }

    public boolean removeIngredient(Ingredient ingredient) {
        if (this.ingredients != null) {
            boolean removed = this.ingredients.remove(ingredient);
            if (removed) {
                ingredient.setDish(null);
            }
            return removed;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredients=" + (ingredients != null ? ingredients.size() : 0) +
                ", dishCost=" + getDishCost() +
                '}';
    }
}
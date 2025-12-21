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
    }

    public double getDishPrice() {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0.0;
        }

        return ingredients.stream()
                .mapToDouble(Ingredient::getPrice)
                .sum();
    }

    public void addIngredient(Ingredient ingredient) {
        if (this.ingredients == null) {
            this.ingredients = new ArrayList<>();
        }
        this.ingredients.add(ingredient);
    }

    public boolean removeIngredient(Ingredient ingredient) {
        if (this.ingredients != null) {
            return this.ingredients.remove(ingredient);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredients=" + ingredients +
                ", dishPrice=" + getDishPrice() +
                '}';
    }
}

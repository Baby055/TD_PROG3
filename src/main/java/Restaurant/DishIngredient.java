package Restaurant;

public class DishIngredient {
    private int id;
    private int dishId;
    private int ingredientId;
    private double quantityRequired;
    private UnitType unit;

    public DishIngredient(int id, int dishId, int ingredientId, double quantityRequired, UnitType unit) {
        this.id = id;
        this.dishId = dishId;
        this.ingredientId = ingredientId;
        this.quantityRequired = quantityRequired;
        this.unit = unit;
    }

    public DishIngredient(int dishId, int ingredientId, double quantityRequired, UnitType unit) {
        this.dishId = dishId;
        this.ingredientId = ingredientId;
        this.quantityRequired = quantityRequired;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(double quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", dishId=" + dishId +
                ", ingredientId=" + ingredientId +
                ", quantityRequired=" + quantityRequired +
                ", unit=" + unit +
                '}';
    }
}
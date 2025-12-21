package Restaurant;

public class Ingredient {
    private int id;
    private String name;
    private double price;  // ou BigDecimal pour plus de précision
    private IngredientCategoryEnum category;
    private int dishId;  // Référence au plat

    public Ingredient() {}

    public Ingredient(int id, String name, double price, IngredientCategoryEnum category, int dishId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dishId = dishId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public IngredientCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(IngredientCategoryEnum category) {
        this.category = category;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", dishId=" + dishId +
                '}';
    }
}

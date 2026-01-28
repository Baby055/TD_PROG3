package Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    private int id;
    private String name;
    private Double price;
    private CategoryEnum category;
    private Dish dish;
    private Double requiredQuantity;
    private List<StockMovement> stockMovementList;

    public Ingredient(int id, String name, Double price, CategoryEnum category, Dish dish, Double requiredQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dish = dish;
        this.requiredQuantity = requiredQuantity;
        this.stockMovementList = new ArrayList<>(); // Initialisation
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public CategoryEnum getCategory() { return category; }
    public void setCategory(CategoryEnum category) { this.category = category; }

    public Dish getDish() { return dish; }
    public void setDish(Dish dish) { this.dish = dish; }

    public Double getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(Double requiredQuantity) { this.requiredQuantity = requiredQuantity; }

    public List<StockMovement> getStockMovementList() { return stockMovementList; }
    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList;
    }

    public void addStockMovement(StockMovement movement) {
        if (stockMovementList == null) {
            stockMovementList = new ArrayList<>();
        }
        stockMovementList.add(movement);
    }

    public String getDishName() {
        return dish != null ? dish.getName() : "Aucun plat";
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", requiredQuantity=" + requiredQuantity +
                ", dish=" + (dish != null ? dish.getName() : "null") +
                '}';
    }
}
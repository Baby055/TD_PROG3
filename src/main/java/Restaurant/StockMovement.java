package Restaurant;

import java.time.LocalDateTime;

public class StockMovement {
    private int id;
    private int ingredientId;
    private double quantity;
    private String unit;
    private LocalDateTime movementDate;

    public StockMovement(int id, int ingredientId, double quantity, String unit, LocalDateTime movementDate) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.unit = unit;
        this.movementDate = movementDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMovementDate() { return movementDate; }
    public void setMovementDate(LocalDateTime movementDate) { this.movementDate = movementDate; }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", ingredientId=" + ingredientId +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", movementDate=" + movementDate +
                '}';
    }
}

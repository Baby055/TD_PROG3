package Restaurant;

import java.time.LocalDateTime;

public class StockMovement {
    private int id;
    private String ingredientName;
    private double quantity;
    private UnitType unit;
    private StockMovementType type;
    private String comment;
    private LocalDateTime movementDate;

    public StockMovement(
            int id,
            String ingredientName,
            double quantity,
            UnitType unit,
            StockMovementType type,
            String comment,
            LocalDateTime movementDate
    ) {
        this.id = id;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.type = type;
        this.comment = comment;
        this.movementDate = movementDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public UnitType getUnit() { return unit; }
    public void setUnit(UnitType unit) { this.unit = unit; }

    public StockMovementType getType() { return type; }
    public void setType(StockMovementType type) { this.type = type; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getMovementDate() { return movementDate; }
    public void setMovementDate(LocalDateTime movementDate) { this.movementDate = movementDate; }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", ingredientName='" + ingredientName + '\'' +
                ", quantity=" + quantity +
                ", unit=" + unit +
                ", type=" + type +
                ", comment='" + comment + '\'' +
                ", movementDate=" + movementDate +
                '}';
    }
}

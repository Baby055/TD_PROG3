package Restaurant;

public final class IngredientUnitConversion {
    private final double pcsPerKg; // - final → empêche toute modification après la construction
    private final double litersPerKg; // private final : champs cachés et immuables.

    public IngredientUnitConversion(double pcsPerKg, double litersPerKg) {
        if (pcsPerKg < 0 || litersPerKg < 0) {
            throw new IllegalArgumentException("Les facteurs de conversion doivent être positifs.");
        }
        this.pcsPerKg = pcsPerKg;
        this.litersPerKg = litersPerKg;
    }

    public double getPcsPerKg() {
        return pcsPerKg;
    }

    public double getLitersPerKg() {
        return litersPerKg;
    }
}


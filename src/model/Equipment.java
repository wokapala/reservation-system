package model;

/**
 * Wyposażenie sali (projektor, tablica interaktywna, etc.).
 */
public class Equipment {
    private String name;
    private int quantity;

    public Equipment(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name + " (x" + quantity + ")";
    }
}

package model;
import enums.*;

public class Card {
    private String name;
    private Rarity rarity;
    private Variant variant;
    private double baseValue;
    
    public Card(String name, Rarity rarity, Variant variant, double baseValue) {
        this.name = name;
        this.rarity = rarity;
        this.variant = variant;
        this.baseValue = baseValue;
    }
    
    public String getName() { return name; }
    public Rarity getRarity() { return rarity; }
    public Variant getVariant() { return variant; }
    public double getBaseValue() { return baseValue; }
    
    public double getTotalValue() {
        return baseValue * variant.getMultiplier();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return name.equals(card.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return name + " (" + rarity + ", " + variant + ")";
    }
}
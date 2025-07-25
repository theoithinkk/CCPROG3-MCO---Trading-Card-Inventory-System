package model;

import enums.*;

public class Binder extends CardContainer {
    private BinderType type;
    private double sellingPrice;
    
    public Binder(String name, BinderType type) {
        super(name, 20);
        this.type = type;
        this.sellingPrice = 0;
    }
    
    public BinderType getType() { return type; }
    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double price) { this.sellingPrice = price; }
    
    public boolean canAddCard(Card card) {
        if (getUniqueCards() >= capacity) return false;
        
        switch (type) {
            case PAUPER:
                return card.getRarity() == Rarity.COMMON || card.getRarity() == Rarity.UNCOMMON;
            case RARES:
                return card.getRarity() == Rarity.RARE || card.getRarity() == Rarity.LEGENDARY;
            case LUXURY:
                return card.getVariant() != Variant.NORMAL;
            case COLLECTOR:
                return (card.getRarity() == Rarity.RARE || card.getRarity() == Rarity.LEGENDARY) 
                       && card.getVariant() != Variant.NORMAL;
            default:
                return true;
        }
    }
    
    public boolean isSellable() {
        return type == BinderType.PAUPER || type == BinderType.RARES || type == BinderType.LUXURY;
    }
    
    public boolean isTradeable() {
        return type == BinderType.NON_CURATED || type == BinderType.COLLECTOR;
    }
    
    public double getSellingValue() {
        double value = getTotalValue();
        if (type == BinderType.LUXURY) {
            value = Math.max(value, sellingPrice);
        }
        return value * 1.1;
    }
}
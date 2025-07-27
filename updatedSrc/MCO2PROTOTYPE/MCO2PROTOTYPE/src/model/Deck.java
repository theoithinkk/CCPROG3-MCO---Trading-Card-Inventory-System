package model;

import enums.*;

public class Deck extends CardContainer implements Sellable{
    private DeckType type;
    
    public Deck(String name, DeckType type) {
        super(name, 10);
        this.type = type;
    }
    
    @Override
    public boolean canAddCard(Card card) {
        return getTotalCards() < getCapacity();
    }
    
    public DeckType getType() { return type; }
    
    public boolean isSellable() {
        return type == DeckType.SELLABLE;
    }

	@Override
	protected double getSellingValue() {
		return getTotalValue();
	}
}

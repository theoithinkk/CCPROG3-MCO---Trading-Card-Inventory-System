package model;

import enums.*;

public class Deck extends CardContainer {
    private DeckType type;
    
    public Deck(String name, DeckType type) {
        super(name, 10);
        this.type = type;
    }
    
    public DeckType getType() { return type; }
    
    public boolean isSellable() {
        return type == DeckType.SELLABLE;
    }
}

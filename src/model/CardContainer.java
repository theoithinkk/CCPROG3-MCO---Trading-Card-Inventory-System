package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class CardContainer {
    protected String name;
    protected Map<Card, Integer> cards;
    protected int capacity;
    
    public CardContainer(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.cards = new HashMap<>();
    }
    
    public abstract boolean canAddCard(Card card);
    
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    
    public void addCard(Card card) {
        cards.put(card, cards.getOrDefault(card, 0) + 1);
    }
    
    public void removeCard(Card card) {
        if (cards.containsKey(card)) {
            int count = cards.get(card);
            if (count > 1) {
                cards.put(card, count - 1);
            } else {
                cards.put(card, 0);
            }
        }
    }
    
    public boolean hasCard(String cardName) {
        Card lookup = new Card(cardName, null, null, 0.0);
        return cards.containsKey(lookup);
    }
    
    public int getCardCount(Card card) {
        return cards.getOrDefault(card, 0);
    }
    
    public int getTotalCards() {
        return cards.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public int getUniqueCards() {
        return (int) cards.entrySet().stream().filter(entry -> entry.getValue() > 0).count();
    }
    
    public double getTotalValue() {
        return cards.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getTotalValue() * entry.getValue())
                .sum();
    }
    
    public Set<Card> getCards() {
        return cards.keySet();
    }
    
    public java.util.List<Card> getAllCards() {
    	return new ArrayList<>(cards.keySet());
    }
    
    public Map<Card, Integer> getCardsWithCounts() {
        return new HashMap<>(cards);
    }

	protected abstract boolean isSellable();

	protected abstract double getSellingValue();
}

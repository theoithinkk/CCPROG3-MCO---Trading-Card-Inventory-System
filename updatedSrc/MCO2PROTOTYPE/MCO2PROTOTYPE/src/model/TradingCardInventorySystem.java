package model;

import java.util.*;
import enums.*;

public class TradingCardInventorySystem {
    private Collection collection;
    private List<CardContainer> containers;
    private double money;
    
    public TradingCardInventorySystem() {
        this.collection = new Collection();
        this.containers = new ArrayList<>();
        this.money = 0.0;
    }
    
    // Getters
    public Collection getCollection() { return collection; }
    public List<Deck> getDecks() {
        return containers.stream().filter(c -> c instanceof Deck).map(c -> (Deck) c).toList();
    }

    public List<Binder> getBinders() {
        return containers.stream().filter(c -> c instanceof Binder).map(c -> (Binder) c).toList();
    }
    
    public double getMoney() { return money; }
    
    public boolean sellCard(Card card) {
        if (collection.getCardCount(card) > 0) {
            collection.removeCard(card);
            money += card.getTotalValue();
            return true;
        }
        return false;
    }
   
    public void createBinder(String name, BinderType type) {
        containers.add(new Binder(name, type));
    }
    
    public void createDeck(String name, DeckType type) {
        containers.add(new Deck(name, type));
    }
    
    public void deleteContainer(CardContainer container) {
        // Move all cards back to collection
        for (Map.Entry<Card, Integer> entry : container.getCardsWithCounts().entrySet()) {
            Card card = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                collection.addCard(card);
            }
        }
        containers.remove(container);
    }
    
    public void moveCard(Card card, CardContainer destination) {
        if (collection.getCardCount(card) > 0 && destination.canAddCard(card)) {
            collection.removeCard(card);
            destination.addCard(card);
        }
    }
    
    public void sellContainer(CardContainer container) {
        if (container.isSellable()) {
            money += container.getSellingValue();
            containers.remove(container);
        }
    }
    
    public void tradeCard(Binder binder, Card cardToTrade, Card newCard) {
        if (binder.isTradeable() && binder.getCardCount(cardToTrade) > 0 && collection.getCardCount(newCard) > 0) {
            binder.removeCard(cardToTrade);
            binder.addCard(newCard);
            collection.removeCard(newCard);
        }
    }
    
    public void addMoney(double amount) {
        money += amount;
    }

    public int getTotalCardCount() {
        int total = collection.getTotalCards();
        total += containers.stream().mapToInt(CardContainer::getTotalCards).sum();
        return total;
    }
}

package model;

import java.util.*;
import enums.*;

public class TradingCardInventorySystem {
    private Collection collection;
    private List<Binder> binders;
    private List<Deck> decks;
    private double money;
    
    public TradingCardInventorySystem() {
        this.collection = new Collection();
        this.binders = new ArrayList<>();
        this.decks = new ArrayList<>();
        this.money = 0.0;
    }
    
    // Getters
    public Collection getCollection() { return collection; }
    public List<Binder> getBinders() { return binders; }
    public List<Deck> getDecks() { return decks; }
    public double getMoney() { return money; }
    
    // Card operations
    public boolean sellCard(Card card) {
        if (collection.getCardCount(card) > 0) {
            collection.removeCard(card);
            money += card.getTotalValue();
            return true;
        }
        return false;
    }
    
    // Binder operations
    public void createBinder(String name, BinderType type) {
        binders.add(new Binder(name, type));
    }
    
    public void deleteBinder(Binder binder) {
        // Move all cards back to collection
        for (Map.Entry<Card, Integer> entry : binder.getCardsWithCounts().entrySet()) {
            Card card = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                collection.addCard(card);
            }
        }
        binders.remove(binder);
    }
    
    public void moveCardToBinder(Card card, Binder binder) {
        if (collection.getCardCount(card) > 0 && binder.canAddCard(card)) {
            collection.removeCard(card);
            binder.addCard(card);
        }
    }
    
    public void sellBinder(Binder binder) {
        if (binder.isSellable()) {
            money += binder.getSellingValue();
            binders.remove(binder);
        }
    }
    
    public void tradeCard(Binder binder, Card cardToTrade, Card newCard) {
        if (binder.isTradeable() && binder.getCardCount(cardToTrade) > 0) {
            binder.removeCard(cardToTrade);
            binder.addCard(newCard);
        }
    }
    
    // Deck operations
    public void createDeck(String name, DeckType type) {
        decks.add(new Deck(name, type));
    }
    
    public void deleteDeck(Deck deck) {
        // Move all cards back to collection
        for (Map.Entry<Card, Integer> entry : deck.getCardsWithCounts().entrySet()) {
            Card card = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                collection.addCard(card);
            }
        }
        decks.remove(deck);
    }
    
    public void moveCardToDeck(Card card, Deck deck) {
        if (collection.getCardCount(card) > 0 && deck.getUniqueCards() < deck.getCapacity()) {
            collection.removeCard(card);
            deck.addCard(card);
        }
    }
    
    public void sellDeck(Deck deck) {
        if (deck.isSellable()) {
            money += deck.getTotalValue();
            decks.remove(deck);
        }
    }
    
    public int getTotalCardsCount() {
        int total = collection.getTotalCards();
        for (Binder binder : binders) {
            total += binder.getTotalCards();
        }
        for (Deck deck : decks) {
            total += deck.getTotalCards();
        }
        return total;
    }
    
    public void addMoney(double amount) {
        money += amount;
    }

    public int getTotalCardCount() {
        int total = 0;

        // Add all cards in the main collection
        total += collection.getTotalCards();

        // Add all cards in each binder
        for (Binder binder : binders) {
            total += binder.getTotalCards();
        }

        // Add all cards in each deck
        for (Deck deck : decks) {
            total += deck.getTotalCards();
        }

        return total;
    }
}

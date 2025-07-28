/**
 * TradingCardInventorySystem.java
 *
 * Main model class that manages the entire trading card inventory system.
 * It handles the user's main card collection, binders, decks, and money.
 * Provides functionality to sell cards, trade cards, move cards between containers,
 * and compute overall stats like total cards and balance.
 *
 * @version 2.0
 * author Theodore Garcia
 * author Ronin Zerna
 */

package model;

import java.util.*;
import enums.*;

public class TradingCardInventorySystem {

    /** The user's main card collection (unrestricted). */
    private Collection collection;

    /** All decks and binders owned by the user. */
    private List<CardContainer> containers;

    /** User's total money balance. */
    private double money;

    /**
     * Constructs the trading card inventory system.
     * Initializes an empty collection, empty container list, and zero money.
     */
    public TradingCardInventorySystem() {
        this.collection = new Collection();
        this.containers = new ArrayList<>();
        this.money = 0.0;
    }

    /**
     * Gets the main card collection.
     *
     * @return The collection object.
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     * Gets a list of all decks in the system.
     *
     * @return A list of Deck objects.
     */
    public List<Deck> getDecks() {
        List<Deck> decks = new ArrayList<>();
        for (CardContainer c : containers) {
            if (c instanceof Deck) {
                decks.add((Deck) c);
            }
        }
        return decks;
    }

    /**
     * Gets a list of all binders in the system.
     *
     * @return A list of Binder objects.
     */
    public List<Binder> getBinders() {
        List<Binder> binders = new ArrayList<>();
        for (CardContainer c : containers) {
            if (c instanceof Binder) {
                binders.add((Binder) c);
            }
        }
        return binders;
    }

    /**
     * Gets the current money balance.
     *
     * @return Total money.
     */
    public double getMoney() {
        return money;
    }

    /**
     * Sells a card from the collection. If successful, adds the card's value to the balance.
     *
     * @param card The card to sell.
     * @return True if sold, false otherwise.
     */
    public boolean sellCard(Card card) {
        if (collection.getCardCount(card) > 0) {
            collection.removeCard(card);
            money += card.getTotalValue();
            return true;
        }
        return false;
    }

    /**
     * Creates a new binder with the given name and type, and adds it to the container list.
     *
     * @param name The binder name.
     * @param type The binder type.
     */
    public void createBinder(String name, BinderType type) {
        containers.add(new Binder(name, type));
    }

    /**
     * Creates a new deck with the given name and type, and adds it to the container list.
     *
     * @param name The deck name.
     * @param type The deck type.
     */
    public void createDeck(String name, DeckType type) {
        containers.add(new Deck(name, type));
    }

    /**
     * Deletes a container (binder or deck) and returns all its cards to the main collection.
     *
     * @param container The container to delete.
     */
    public void deleteContainer(CardContainer container) {
        for (Map.Entry<Card, Integer> entry : container.getCardsWithCounts().entrySet()) {
            Card template = entry.getKey();
            int count = entry.getValue();
            Card realKey = collection.findMatchingCard(template);
            if (realKey == null) realKey = template;
            for (int i = 0; i < count; i++) {
                collection.addCard(realKey);
            }
        }
        containers.remove(container);
    }

    /**
     * Moves a card from the collection into a destination container if allowed.
     *
     * @param card       The card to move.
     * @param destination The destination container (deck or binder).
     */
    public void moveCard(Card card, CardContainer destination) {
        if (collection.getCardCount(card) > 0 && destination.canAddCard(card)) {
            collection.removeCard(card);
            destination.addCard(card);
        }
    }

    /**
     * Sells a container and removes it from the system.
     * Only works if the container is marked as sellable.
     *
     * @param container The container to sell.
     */
    public void sellContainer(CardContainer container) {
        if (container.isSellable()) {
            money += container.getSellingValue();
            containers.remove(container);
        }
    }

    /**
     * Trades a card inside a binder with a card from the collection.
     * Only works if the binder is tradeable and both cards exist.
     *
     * @param binder       The binder involved in the trade.
     * @param cardToTrade  The card to remove from the binder.
     * @param newCard      The card to add to the binder from the collection.
     */
    public void tradeCard(Binder binder, Card cardToTrade, Card newCard) {
        if (binder.isTradeable() &&
            binder.getCardCount(cardToTrade) > 0 &&
            collection.getCardCount(newCard) > 0) {

            binder.removeCard(cardToTrade);
            binder.addCard(newCard);
            collection.removeCard(newCard);
        }
    }

    /**
     * Adds money to the user's balance.
     *
     * @param amount The amount to add.
     */
    public void addMoney(double amount) {
        money += amount;
    }

    /**
     * Returns the total number of cards owned, including all containers and the collection.
     *
     * @return Total card count.
     */
    public int getTotalCardCount() {
        int total = collection.getTotalCards();
        for (CardContainer container : containers) {
            total += container.getTotalCards();
        }
        return total;
    }
}

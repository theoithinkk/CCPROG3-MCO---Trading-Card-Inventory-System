/**
 * Abstract base class representing any container of cards (e.g., Collection, Binder, Deck).
 * Provides common operations such as adding/removing cards, tracking total and unique card counts,
 * computing the total value, and querying card presence.
 *
 * Subclasses must define specific behaviors for rules such as which cards can be added,
 * and whether the container can be sold.
 * 
 * This class uses a Map to store cards and their counts, allowing flexible access and updates.
 * 
 * @version 2.0
 * @author Theodore Garcia
 * @author Ronin Zerna
 */

package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class CardContainer {

    /** The name of the container. */
    protected String name;

    /** Stores each card and how many copies are in the container. */
    protected Map<Card, Integer> cards;

    /** The maximum number of unique cards allowed in this container. */
    protected int capacity;

    /**
     * Constructs a new CardContainer with the given name and capacity.
     *
     * @param name     The name of the container.
     * @param capacity The max number of unique cards allowed.
     */
    public CardContainer(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.cards = new HashMap<>();
    }

    /**
     * Determines whether a card can be added to this container based on its rules.
     * Must be implemented by the subclass.
     *
     * @param card The card to check.
     * @return True if the card is allowed, false otherwise.
     */
    public abstract boolean canAddCard(Card card);

    /**
     * Returns the name of this container.
     *
     * @return The container's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the maximum number of unique cards this container can hold.
     *
     * @return The capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Adds a card to the container. If the card already exists, its count is increased.
     *
     * @param card The card to add.
     */
    public void addCard(Card card) {
        cards.put(card, cards.getOrDefault(card, 0) + 1);
    }

    /**
     * Removes one copy of a card from the container. If it's the last copy, sets count to 0.
     *
     * @param card The card to remove.
     */
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

    public void remove(Card card) {
        cards.remove(card);
    }
    /**
     * Checks if the container has a card by name.
     *
     * @param cardName The name of the card to search for.
     * @return True if the card exists, false otherwise.
     */
    public boolean hasCard(String cardName) {
        Card lookup = new Card(cardName, null, null, 0.0);
        return cards.containsKey(lookup);
    }

    /**
     * Returns how many copies of a specific card are in the container.
     *
     * @param card The card to check.
     * @return The number of copies (0 if not present).
     */
    public int getCardCount(Card card) {
        if (cards.containsKey(card)) {
            return cards.get(card);
        }
        return 0;
    }

    /**
     * Returns the total number of cards in the container, including duplicates.
     *
     * @return Total card count.
     */
    public int getTotalCards() {
        int total = 0;
        for (Integer count : cards.values()) {
            total += count;
        }
        return total;
    }

    /**
     * Returns how many different cards are in the container (with count > 0).
     *
     * @return Number of unique cards.
     */
    public int getUniqueCards() {
        int unique = 0;
        for (Map.Entry<Card, Integer> entry : cards.entrySet()) {
            if (entry.getValue() > 0) {
                unique++;
            }
        }
        return unique;
    }

    /**
     * Calculates the total value of all cards in the container, including duplicates.
     *
     * @return The total value.
     */
    public double getTotalValue() {
        double total = 0.0;
        for (Map.Entry<Card, Integer> entry : cards.entrySet()) {
            total += entry.getKey().getTotalValue() * entry.getValue();
        }
        return total;
    }

    /**
     * Returns a Set of all unique cards in the container.
     * This does not include quantities.
     *
     * @return Set of cards.
     */
    public Set<Card> getCards() {
        return cards.keySet();
    }

    /**
     * Returns a List of all unique cards in the container.
     * Useful for iteration in a GUI.
     *
     * @return List of cards.
     */
    public java.util.List<Card> getAllCards() {
        return new ArrayList<>(cards.keySet());
    }

    /**
     * Returns a Map of cards and their counts.
     * This is a copy, so changes to it won't affect the actual container.
     *
     * @return Map of cards to counts.
     */
    public Map<Card, Integer> getCardsWithCounts() {
        return new HashMap<>(cards);
    }

    /**
     * Determines if the container is allowed to be sold.
     * Must be implemented by subclasses like Binder or Deck.
     *
     * @return True if sellable, false otherwise.
     */
    protected abstract boolean isSellable();

    /**
     * Returns the container's selling value.
     * Must be implemented by subclasses that define pricing.
     *
     * @return The monetary selling value.
     */
    protected abstract double getSellingValue();
}


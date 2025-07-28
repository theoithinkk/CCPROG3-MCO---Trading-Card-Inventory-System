/**
 * Represents a deck of cards in the Trading Card Inventory System.
 * A deck can hold up to 10 cards and may or may not be sellable depending on its {@link DeckType}.
 * 
 * This class extends {@link CardContainer} and implements {@link Sellable}.
 * 
 * @version 2.0
 * author Theodore Garcia
 * author Ronin Zerna
 */

package model;

import enums.*;

/**
 * A container for cards used as a playable or sellable deck.
 * Decks have a fixed capacity of 10 cards and limited behavior based on their type.
 */
public class Deck extends CardContainer implements Sellable {

    /** The type of the deck, which determines if it is sellable. */
    private DeckType type;

    /**
     * Constructs a new deck with the given name and type.
     * Decks have a fixed capacity of 10 cards.
     *
     * @param name The name of the deck.
     * @param type The type of the deck (NORMAL or SELLABLE).
     */
    public Deck(String name, DeckType type) {
        super(name, 10);
        this.type = type;
    }

    /**
     * Checks whether the deck has room for more cards.
     * Decks do not restrict based on card type.
     *
     * @param card The card to evaluate.
     * @return True if the card can be added, false otherwise.
     */
    @Override
    public boolean canAddCard(Card card) {
        return getTotalCards() < getCapacity();
    }

    /**
     * Returns the type of the deck.
     *
     * @return The deck's {@link DeckType}.
     */
    public DeckType getType() {
        return type;
    }

    /**
     * Determines if the deck is sellable based on its type.
     * Only decks of type SELLABLE are considered sellable.
     *
     * @return True if sellable, false otherwise.
     */
    public boolean isSellable() {
        return type == DeckType.SELLABLE;
    }

    /**
     * Returns the total value of the deck's contents.
     * Used when the deck is being sold.
     *
     * @return Total value of the deck.
     */
    @Override
    protected double getSellingValue() {
        return getTotalValue();
    }
}

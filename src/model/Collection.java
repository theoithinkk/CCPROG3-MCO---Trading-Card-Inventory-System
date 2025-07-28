/**
 * Represents the user's main card collection.
 * This container has effectively unlimited capacity and accepts all card types.
 * Implements default sellable behavior with no additional selling rules.
 * 
 * Provides functionality to find a card by matching its name, rarity, and variant.
 * 
 * @version 2.0
 * @author Theodore Garcia
 * @author Ronin Zerna
 */

package model;

/**
 * The Collection class represents the user's central repository of cards.
 * It allows adding any card without restriction and has unlimited capacity.
 */
public class Collection extends CardContainer {

    /**
     * Constructs the main collection with a default name and unlimited capacity.
     */
    public Collection() {
        super("Main Collection", Integer.MAX_VALUE);
    }

    /**
     * Searches the collection for a card that matches the given template.
     * A match requires identical name, rarity, and variant.
     *
     * @param template The card to match against.
     * @return The matching card in the collection, or null if not found.
     */
    public Card findMatchingCard(Card template) {
        for (Card c : cards.keySet()) {
            if (c.getName().equals(template.getName())
                && c.getRarity() == template.getRarity()
                && c.getVariant() == template.getVariant()) {
                return c;
            }
        }
        return null;
    }

    /**
     * Always returns true if there is space; collections accept all card types.
     *
     * @param card The card to evaluate.
     * @return True if the card can be added.
     */
    @Override
    public boolean canAddCard(Card card) {
        return getTotalCards() < getCapacity();
    }

    /**
     * Indicates that the collection is sellable.
     *
     * @return True (always).
     */
    @Override
    protected boolean isSellable() {
        return true;
    }

    /**
     * Returns the total value of all cards in the collection.
     *
     * @return Total collection value.
     */
    @Override
    protected double getSellingValue() {
        return getTotalValue();
    }
}

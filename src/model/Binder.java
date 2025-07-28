/**
 * Binder.java
 *
 * Represents a specialized card container with rules and behaviors depending on its {@link BinderType}.
 * A binder can be sellable or tradeable based on its type and may have unique restrictions on card additions.
 * Maintains a dynamic selling price that updates based on card contents.
 * 
 * Implements {@link Sellable} and {@link Tradeable} interfaces to determine market eligibility.
 * 
 * Examples:
 * - PAUPER binders accept only common and uncommon cards and are sellable.
 * - COLLECTOR binders accept only rare+variant cards and are tradeable.
 * 
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */

package model;

import enums.*;

/**
 * A binder is a card container with specific rules based on its {@link BinderType}.
 * It supports operations for selling, trading, and enforcing card-type constraints.
 */
public class Binder extends CardContainer implements Sellable, Tradeable {

    /** Type of the binder determining its rules and behavior. */
    private BinderType type;

    /** The assigned or computed selling price of the binder. */
    private double sellingPrice;

    /**
     * Constructs a binder with a given name and type.
     * The default capacity is set to 20 cards.
     *
     * @param name The name of the binder.
     * @param type The type of the binder.
     */
    public Binder(String name, BinderType type) {
        super(name, 20);
        this.type = type;
        this.sellingPrice = 0;
    }

    /**
     * Gets the binder type.
     *
     * @return The {@link BinderType} of this binder.
     */
    public BinderType getType() {
        return type;
    }

    /**
     * Gets the current selling price of the binder.
     *
     * @return The selling price.
     */
    public double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * Sets a new selling price for the binder.
     *
     * @param price The price to set.
     */
    public void setSellingPrice(double price) {
        this.sellingPrice = price;
    }

    /**
     * Checks if a card can be added to this binder based on its type restrictions.
     *
     * @param card The card to check.
     * @return True if the card is allowed in this binder, false otherwise.
     */
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

    /**
     * Determines if this binder is sellable based on its type.
     *
     * @return True if sellable, false otherwise.
     */
    public boolean isSellable() {
        return type == BinderType.PAUPER || type == BinderType.RARES || type == BinderType.LUXURY;
    }

    /**
     * Determines if this binder is tradeable based on its type.
     *
     * @return True if tradeable, false otherwise.
     */
    public boolean isTradeable() {
        return type == BinderType.NON_CURATED || type == BinderType.COLLECTOR;
    }

    /**
     * Computes the binder's selling value.
     * For RARES and LUXURY types, the higher between total value and user-set price is used,
     * with a 10% bonus applied.
     *
     * @return The computed selling value.
     */
    public double getSellingValue() {
        double value = getTotalValue();
        if (type == BinderType.LUXURY || type == BinderType.RARES) {
            value = Math.max(value, sellingPrice);
            return value * 1.1;
        }
        return value;
    }

    /**
     * Adds a card to the binder and updates the selling price accordingly.
     *
     * @param card The card to add.
     */
    @Override
    public void addCard(Card card) {
        super.addCard(card);
        this.sellingPrice = getTotalValue();
    }

    /**
     * Removes a card from the binder and updates the selling price accordingly.
     *
     * @param card The card to remove.
     */
    @Override
    public void removeCard(Card card) {
        super.removeCard(card);
        this.sellingPrice = getTotalValue();
    }
}

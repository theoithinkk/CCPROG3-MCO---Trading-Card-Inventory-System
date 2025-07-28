/**
 * Card.java
 *
 * Represents a trading card with a name, rarity, visual variant, and base monetary value.
 * The total value of the card is determined by multiplying the base value with the variant's multiplier.
 * Equality and hashing are based solely on the card's name.
 * 
 * Used across containers like collections, binders, and decks within the Trading Card Inventory System.
 * 
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */

package model;

import enums.*;

/**
 * Represents a collectible card with a rarity, variant, and value.
 */
public class Card {

    /** The name of the card (serves as unique identifier). */
    private String name;

    /** The rarity level of the card. */
    private Rarity rarity;

    /** The visual variant of the card. */
    private Variant variant;

    /** The base monetary value of the card before applying variant multiplier. */
    private double baseValue;

    /**
     * Constructs a new card instance.
     *
     * @param name      The name of the card.
     * @param rarity    The card's rarity.
     * @param variant   The card's visual variant.
     * @param baseValue The card's base value.
     */
    public Card(String name, Rarity rarity, Variant variant, double baseValue) {
        this.name = name;
        this.rarity = rarity;
        this.variant = variant;
        this.baseValue = baseValue;
    }

    /**
     * Returns the name of the card.
     *
     * @return Card name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the rarity of the card.
     *
     * @return Card rarity.
     */
    public Rarity getRarity() {
        return rarity;
    }

    /**
     * Returns the visual variant of the card.
     *
     * @return Card variant.
     */
    public Variant getVariant() {
        return variant;
    }

    /**
     * Returns the base monetary value of the card.
     *
     * @return Base value.
     */
    public double getBaseValue() {
        return baseValue;
    }

    /**
     * Returns the total value of the card, calculated as base value multiplied by variant multiplier.
     *
     * @return Total card value.
     */
    public double getTotalValue() {
        return baseValue * variant.getMultiplier();
    }

    /**
     * Compares this card to another object for equality based on the name.
     *
     * @param obj The object to compare.
     * @return True if the names match; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return name.equals(card.name);
    }

    /**
     * Generates a hash code for the card using its name.
     *
     * @return Hash code based on card name.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Returns a formatted string representation of the card.
     *
     * @return Card details as string.
     */
    @Override
    public String toString() {
        return String.format(
            "Name: %s | Rarity: %s | Variant: %s | Base Value: $%.2f | Total Value: $%.2f",
            name,
            rarity,
            variant,
            baseValue,
            getTotalValue()
        );
    }
}

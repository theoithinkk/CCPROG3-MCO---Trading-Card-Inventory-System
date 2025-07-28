/**
 * Enumeration defining visual or cosmetic variants of cards, each with an associated value multiplier.
 * Variants affect the card's final value through their assigned multiplier.
 * - NORMAL: Base card with no enhancements.
 * - EXTENDED_ART: Slightly enhanced visual design.
 * - FULL_ART: More elaborate art with higher value.
 * - ALT_ART: Alternative design with the highest value boost.
 * 
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */
package enums;

public enum Variant {
    NORMAL(1.0),
    EXTENDED_ART(1.5),
    FULL_ART(2.0),
    ALT_ART(3.0);

    /** Value multiplier based on variant type. */
    private final double multiplier;

    Variant(double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Returns the value multiplier for the variant.
     *
     * @return The multiplier used to calculate total card value.
     */
    public double getMultiplier() {
        return multiplier;
    }
}

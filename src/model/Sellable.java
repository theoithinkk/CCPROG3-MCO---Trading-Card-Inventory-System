/**
 * Interface for containers or entities that can be sold in the Trading Card Inventory System.
 * Classes that implement this interface must define whether they are sellable and provide their total value.
 *
 * @version 2.0
 * @author Theodore Garcia
 * @author Ronin Zerna
 */

package model;

/**
 * Defines the contract for any container or object that can be sold.
 */
public interface Sellable {

    /**
     * Checks whether the object is eligible for sale.
     *
     * @return True if sellable, false otherwise.
     */
    boolean isSellable();

    /**
     * Returns the total value of the object (e.g., binder, deck) when sold.
     *
     * @return Total monetary value.
     */
    double getTotalValue();
}

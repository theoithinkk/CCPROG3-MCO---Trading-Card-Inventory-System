/**
 * Interface for containers or entities that can be traded in the Trading Card Inventory System.
 * Classes that implement this interface must define whether they are tradeable.
 *
 * @version 2.0
 * author Theodore Garcia
 * author Ronin Zerna
 */

package model;

/**
 * Defines the contract for any container or object that can be traded.
 */
public interface Tradeable {

    /**
     * Checks whether the object is eligible for trading.
     *
     * @return True if tradeable, false otherwise.
     */
    boolean isTradeable();
}

/**
 * ContainerController.java
 *
 * Abstract base controller for managing card containers such as binders and collections.
 * Provides shared logic for viewing, adding, removing, and selling cards from containers.
 * Subclasses may override behavior like pricing logic for specific container types.
 * 
 * This controller acts as the foundation for more specific container-based controllers
 * and enables interaction between the data model and GUI components.
 * 
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */

package controller;

import model.*;
import enums.*;
import view.*;
import javax.swing.*;

/**
 * Abstract base controller for card container operations.
 * Provides common functionality for managing card containers (binders/collections).
 */
public abstract class ContainerController {

    /** Reference to the main system model. */
    protected final TradingCardInventorySystem tcis;

    /** Reference to the main GUI view. */
    protected final TCISGUI gui;

    /**
     * Constructs a ContainerController with references to the system and GUI.
     *
     * @param tcis The main system model instance.
     * @param gui  The GUI view instance.
     */
    public ContainerController(TradingCardInventorySystem tcis, TCISGUI gui) {
        this.tcis = tcis;
        this.gui = gui;
    }

    /**
     * Displays detailed information about a container using a helper view.
     *
     * @param container The container to inspect.
     */
    public void handleViewDetails(CardContainer container) {
        CardContainerViewHelper.showContainerDetails(container);
    }

    /**
     * Attempts to add a card to the specified container with validation steps:
     * <ul>
     *   <li>Checks if the card exists in the main collection.</li>
     *   <li>Prevents duplicate entries within the container.</li>
     *   <li>Validates card compatibility with the container rules.</li>
     * </ul>
     * On success, updates the system and GUI, and notifies the user.
     *
     * @param container The target container.
     * @param card      The card to be added.
     */
    public void handleAddCard(CardContainer container, Card card) {
        try {
            if (!tcis.getCollection().hasCard(card.getName())) {
                throw new IllegalArgumentException("Card not found in collection");
            }

            if (container.getCards().contains(card)) {
                throw new IllegalArgumentException("Card already exists in this binder");
            }

            if (!container.canAddCard(card)) {
                throw new IllegalArgumentException("Card type not allowed.");
            }

            if(tcis.getCollection().

            container.addCard(card);
            tcis.getCollection().removeCard(card);
            gui.refreshAll();

            BinderViewHelper.showInfoDialog(
                "Card Added",
                String.format("Added %s to %s", card, container.getName())
            );

        } catch (Exception ex) {
            BinderViewHelper.showErrorDialog(ex.getMessage());
        }
    }

    /**
     * Handles deletion of a container from the system, with UI feedback and confirmation dialog.
     *
     * @param container The container to delete.
     */
    public void handleDeleteContainer(CardContainer container) {
        try {
            tcis.deleteContainer(container);
            gui.refreshAll();
            BinderViewHelper.showInfoDialog(
                "Container Deleted",
                "Successfully deleted " + container.getName()
            );
        } catch (Exception ex) {
            BinderViewHelper.showErrorDialog("Failed to delete container.");
        }
    }

    /**
     * Handles the sale process for a container.
     * Shows a confirmation dialog with the sale value, and proceeds if confirmed.
     *
     * @param container The container to sell.
     */
    public void handleSale(CardContainer container) {
        double value = calculateSaleValue(container);

        if (JOptionPane.YES_OPTION ==
            JOptionPane.showConfirmDialog(
                null,
                "Sell for $" + value + "?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
            )) {
            tcis.sellContainer(container);
            gui.refreshAll();
        }
    }

    /**
     * Calculates the sale value of a container.
     * Can be overridden by subclasses for container-specific pricing logic (e.g., bonuses or penalties).
     *
     * @param container The container whose value is being calculated.
     * @return The total sale value.
     */
    protected double calculateSaleValue(CardContainer container) {
        return container.getTotalValue();
    }

    /**
     * Removes a card from the container and returns it to the main collection.
     * Refreshes the GUI afterward.
     *
     * @param container The container to remove the card from.
     * @param card      The card to be removed and returned to the collection.
     */
    public void handleRemoveCard(CardContainer container, Card card) {
        container.removeCard(card);
        tcis.getCollection().addCard(card);
        gui.refreshAll();
    }
}


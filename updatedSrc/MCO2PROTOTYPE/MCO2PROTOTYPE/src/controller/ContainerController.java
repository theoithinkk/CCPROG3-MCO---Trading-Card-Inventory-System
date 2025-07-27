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
    // Reference to the main system model
    protected final TradingCardInventorySystem tcis;
    // Reference to the main GUI view
    protected final TCISGUI gui;

    /**
     * Constructor for ContainerController.
     * @param tcis The main system model instance
     * @param gui The main GUI view instance
     */
    public ContainerController(TradingCardInventorySystem tcis, TCISGUI gui) {
        this.tcis = tcis;
        this.gui = gui;
    }
    
    /**
     * Handles displaying detailed information about a container.
     * @param container The container to view
     */
    public void handleViewDetails(CardContainer container) {
        CardContainerViewHelper.showContainerDetails(container);
    }
    
    /**
     * Handles adding a card to a container with validation.
     * @param container The target container
     * @param card The card to add
     */
    public void handleAddCard(CardContainer container, Card card) {
        try {
            // Validation 1: Verify card exists in main collection
            if (!tcis.getCollection().hasCard(card.getName())) {
                throw new IllegalArgumentException("Card not found in collection");
            }

            // Validation 2: Prevent duplicate cards in container
            if (container.getCards().contains(card)) {
                throw new IllegalArgumentException("Card already exists in this binder");
            }    
            
            // Validation 3: Check container-specific restrictions
            if(!container.canAddCard(card)) {
                throw new IllegalArgumentException("Card type not allowed.");
            }

            // Transaction: Move card from collection to container
            container.addCard(card);
            tcis.getCollection().removeCard(card);
            gui.refreshAll(); // Update UI

            // Show success notification
            BinderViewHelper.showInfoDialog(
                "Card Added", 
                String.format("Added %s to %s", card, container.getName())
            );

        } catch (Exception ex) {
            BinderViewHelper.showErrorDialog(ex.getMessage());
        }
    }
    
    /**
     * Handles container deletion with confirmation.
     * @param container The container to delete
     */
    public void handleDeleteContainer(CardContainer container) {
        try {
            // Step 1: Remove from data model
            tcis.deleteContainer(container);
            
            // Step 2: Refresh UI
            gui.refreshAll();
            
            // Step 3: Show confirmation
            BinderViewHelper.showInfoDialog(
                "Container Deleted", 
                "Successfully deleted " + container.getName()
            );
        } catch (Exception ex) {
            BinderViewHelper.showErrorDialog("Failed to delete container.");
        }
    }
    
    /**
     * Handles the sale of a container's contents.
     * @param container The container to sell
     */
    public void handleSale(CardContainer container) {
        // Calculate value (potential override in subclasses)
        double value = calculateSaleValue(container);
        
        // Confirm sale with user
        if (JOptionPane.YES_OPTION == 
            JOptionPane.showConfirmDialog(
                null, 
                "Sell for $" + value + "?", 
                "Confirm", 
                JOptionPane.YES_NO_OPTION
            )) {
            // Process sale if confirmed
            tcis.sellContainer(container);
            gui.refreshAll();
        }
    }
    
    /**
     * Calculates the sale value of a container.
     * Can be overridden by subclasses for custom pricing logic.
     * @param container The container to value
     * @return The calculated sale value
     */
    protected double calculateSaleValue(CardContainer container) {
        // Base implementation returns full value
        // Subclasses may apply discounts or bonuses
        return container.getTotalValue();
    }
}
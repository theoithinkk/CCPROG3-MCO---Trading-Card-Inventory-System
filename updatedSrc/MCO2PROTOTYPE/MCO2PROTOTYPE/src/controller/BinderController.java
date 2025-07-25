package controller;

import model.*;
import enums.*;
import view.*;
import javax.swing.*;

public class BinderController {
    private final TradingCardInventorySystem tcis;
    private final TCISGUI gui;

    public BinderController(TradingCardInventorySystem tcis, TCISGUI gui) {
        this.tcis = tcis;
        this.gui = gui;
    }

    /**
     * Displays detailed information about a specific binder.
     * @param binder The binder to view details for
     */
    public void handleViewDetails(Binder binder) {
        BinderViewHelper.showBinderDetails(binder);
    }

    /**
     * Handles the complete trade process between cards in a binder and the collection.
     * @param binder The binder involved in the trade
     */
    public void handleTrade(Binder binder) {
        
        Card outgoingCard = BinderViewHelper.selectOutgoingCard(binder);
        if (outgoingCard == null) return; // User cancelled selection

        Card incomingCard = BinderViewHelper.createIncomingCardDialog();
        if (incomingCard == null) return; // User cancelled creation

        try {
            tcis.getCollection().addCard(incomingCard);
            
            double valueDifference = incomingCard.getTotalValue() - outgoingCard.getTotalValue();
            
            if (Math.abs(valueDifference) >= 1.0) {
                // Ask user to confirm unbalanced trade
                boolean proceed = BinderViewHelper.confirmUnbalancedTrade(
                    outgoingCard.getName(), 
                    incomingCard.getName(), 
                    valueDifference
                );
                if (!proceed) {
                    tcis.getCollection().removeCard(incomingCard);
                    return;
                }
            }

            binder.addCard(incomingCard);
            gui.refreshAll(); // Update UI
            
            // Show success message
            BinderViewHelper.showTradeSuccess(outgoingCard.getName(), incomingCard.getName());

        } catch (Exception ex) {
            // Cleanup if any error occurs during the trade process
            tcis.getCollection().removeCard(incomingCard);
            BinderViewHelper.showErrorDialog("Trade failed: " + ex.getMessage());
        }
    }
    
    /**
     * Adds a card from the collection to a specific binder.
     * @param binder The binder to add the card to
     * @param card The card to add
     */
    public void handleAddCard(Binder binder, Card card) {
        try {
            // Validation 1: Card must exist in main collection
            if (!tcis.getCollection().hasCard(card.getName())) {
                throw new IllegalArgumentException("Card not found in collection");
            }

            // Validation 2: Card must not already be in the binder
            if (binder.getCards().contains(card)) {
                throw new IllegalArgumentException("Card already exists in this binder");
            }
            
            // Validation 3: Card type must be compatible with binder type
            if(!binder.canAddCard(card)) {
                throw new IllegalArgumentException("Card type not allowed.");
            }

            // Add card to binder and refresh UI
            binder.addCard(card);
            gui.refreshAll();

            // Show success message
            BinderViewHelper.showInfoDialog(
                "Card Added", 
                String.format("Added %s to %s", card, binder.getName())
            );

        } catch (Exception ex) {
            BinderViewHelper.showErrorDialog(ex.getMessage());
        }
    }
    
    /**
     * Deletes a binder from the system.
     * @param binder The binder to delete
     */
    public void handleDeleteBinder(Binder binder) {
        try {
            // Step 1: Remove binder from model
            tcis.deleteBinder(binder);
            
            // Step 2: Update UI to reflect changes
            gui.refreshAll();
            
            // Step 3: Show confirmation message
            BinderViewHelper.showInfoDialog(
                "Binder Deleted", 
                "Successfully deleted " + binder.getName()
            );
        } catch (Exception ex) {
            BinderViewHelper.showErrorDialog("Failed to delete binder.");
        }
    }

    /**
     * Handles the sale of an entire binder.
     * @param binder The binder to sell
     */
    public void handleSale(Binder binder) {
        // Calculate sale value (with potential discounts)
        double value = calculateSaleValue(binder);
        
        // Confirm sale with user
        if (JOptionPane.YES_OPTION == 
            JOptionPane.showConfirmDialog(null, "Sell for $" + value + "?", "Confirm", JOptionPane.YES_NO_OPTION)) {
            // Process sale and update UI
            tcis.sellBinder(binder);
            gui.refreshAll();
        }
    }

    /**
     * Calculates the sale value of a binder, applying appropriate discounts.
     * @param binder The binder to value
     * @return The calculated sale value
     */
    private double calculateSaleValue(Binder binder) {
        double value = binder.getTotalValue();
        // Apply 10% discount for rare/luxury binders
        if (binder.getType() == BinderType.RARES || binder.getType() == BinderType.LUXURY) {
            value *= 0.9;
        }
        return value;
    }
}
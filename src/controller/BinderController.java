/**
 * BinderController.java
 *
 * Controller for handling operations related to binders within the Trading Card Inventory System.
 * This class extends {@link ContainerController} to provide specific functionality such as handling
 * trades between a binder and the collection, setting prices, and calculating the sale value with binder-specific logic.
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
 * Controller class for managing binder-specific behavior in the Trading Card Inventory System.
 */
public class BinderController extends ContainerController {

    /**
     * Constructs a BinderController for managing interactions between a binder and the GUI.
     *
     * @param tcis The main TradingCardInventorySystem instance.
     * @param gui  The GUI interface to be updated after actions.
     */
    public BinderController(TradingCardInventorySystem tcis, TCISGUI gui) {
        super(tcis, gui);
    }

    /**
     * Handles the complete trade process between a card from a binder and a card from the collection.
     * Prompts the user to select an outgoing card and input an incoming card.
     * If the trade is unbalanced by more than 1.0 in value, it asks for user confirmation.
     * After a successful trade, updates the binder and refreshes the GUI.
     *
     * @param binder The binder where the trade is taking place.
     */
    public void handleTrade(Binder binder) {
        Card outgoingCard = BinderViewHelper.selectOutgoingCard(binder);
        if (outgoingCard == null) return;

        Card incomingCard = BinderViewHelper.createIncomingCardDialog(binder);
        if (incomingCard == null) return;

        try {
            double valueDifference = incomingCard.getTotalValue() - outgoingCard.getTotalValue();

            if (Math.abs(valueDifference) >= 1.0) {
                boolean proceed = BinderViewHelper.confirmUnbalancedTrade(
                    outgoingCard.getName(),
                    incomingCard.getName(),
                    valueDifference
                );
                if (!proceed) return;
            }

            binder.addCard(incomingCard);
            binder.removeCard(outgoingCard);

            gui.refreshAll();
            BinderViewHelper.showTradeSuccess(outgoingCard.getName(), incomingCard.getName());

        } catch (Exception ex) {
            BinderViewHelper.showErrorDialog("Trade failed: " + ex.getMessage());
        }
    }

    /**
     * Calculates the sale value of a container. If the container is a binder of type RARES or LUXURY,
     * an additional 10% is added to the total value.
     *
     * @param container The card container whose sale value is being calculated.
     * @return The total sale value after applying applicable adjustments.
     */
    @Override
    protected double calculateSaleValue(CardContainer container) {
        if (!(container instanceof Binder)) {
            return super.calculateSaleValue(container);
        }

        Binder binder = (Binder) container;
        double value = super.calculateSaleValue(binder);

        if (binder.getType() == BinderType.RARES || binder.getType() == BinderType.LUXURY) {
            value *= 1.1;
        }

        return value;
    }

    /**
     * Sets a new selling price for the specified binder and refreshes the GUI.
     *
     * @param binder   The binder whose price is to be updated.
     * @param newPrice The new selling price to be set.
     */
    public void handleSetPrice(Binder binder, double newPrice) {
        binder.setSellingPrice(newPrice);
        gui.refreshAll();
    }
}

package controller;
import model.*;
import enums.*;
import view.*;
import javax.swing.*;

public class BinderController extends ContainerController{
	
    public BinderController(TradingCardInventorySystem tcis, TCISGUI gui) {
        super(tcis, gui);
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
            binder.removeCard(outgoingCard);
            gui.refreshAll(); // Update UI
            
            // Show success message
            BinderViewHelper.showTradeSuccess(outgoingCard.getName(), incomingCard.getName());

        } catch (Exception ex) {
            // Cleanup if any error occurs during the trade process
            tcis.getCollection().removeCard(incomingCard);
            BinderViewHelper.showErrorDialog("Trade failed: " + ex.getMessage());
        }
    }

    @Override
    protected double calculateSaleValue(CardContainer container) {
    	if (!(container instanceof Binder)) {
            return super.calculateSaleValue(container);
        }
        
        Binder binder = (Binder) container;
        double value = super.calculateSaleValue(binder);
        
        // Apply 10% additional fee only for specific binder types
        if (binder.getType() == BinderType.RARES || binder.getType() == BinderType.LUXURY) {
            value *= 1.1;
        }
        return value;
    }
}

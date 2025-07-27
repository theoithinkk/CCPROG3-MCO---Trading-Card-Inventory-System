package view;

import model.*;
import controller.*;
import enums.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

/**
 * View helper class for deck-related UI components.
 * Handles creation and display of deck panels and dialogs.
 */
public class DeckViewHelper extends CardContainerViewHelper {

    /**
     * Creates a panel displaying deck information with action buttons.
     * @param container The deck to display (must be castable to Deck)
     * @param tcis Reference to the main system
     * @param controller The deck controller (must be castable to DeckController)
     * @return Configured JPanel with deck information and actions
     */
    @Override
    public JPanel createContainerPanel(CardContainer container, TradingCardInventorySystem tcis, 
                                     ContainerController controller) {
        // Cast parameters to specific types
        Deck deck = (Deck)container;
        DeckController deckController = (DeckController)controller;
        
        // Create and configure main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Add deck information labels
        panel.add(new JLabel(deck.getName()));
        panel.add(new JLabel("Type: " + deck.getType()));
        panel.add(new JLabel("Cards: " + deck.getTotalCards() + "/10")); // Show capacity
        
        // Add standard action buttons
        addActionButton(panel, "View Details", e -> deckController.handleViewDetails(deck));
        addActionButton(panel, "Add Card", e -> showAddCardDialog(deck, tcis, deckController));
        
        // Add conditional buttons
        if (deck.isSellable()) {
            addActionButton(panel, "Sell Deck", e -> deckController.handleSale(deck));
        }
        addActionButton(panel, "Delete Deck", e -> confirmDelete(deck, deckController));

        return panel;   
    }
    
    /**
     * Shows dialog for creating a new deck.
     * @param tcis Reference to the main system
     * @param gui Reference to the main GUI for refreshing
     */
    public static void openCreateDeckDialog(TradingCardInventorySystem tcis, TCISGUI gui) {
        // Create input fields
        JTextField nameField = new JTextField();
        JComboBox<DeckType> typeBox = new JComboBox<>(DeckType.values());

        // Configure dialog layout
        JPanel dialogPanel = new JPanel(new GridLayout(0, 2));
        dialogPanel.add(new JLabel("Deck Name:")); 
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Deck Type:")); 
        dialogPanel.add(typeBox);

        // Show dialog and process result
        if (JOptionPane.showConfirmDialog(null, dialogPanel, "Create Deck", 
            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                // Create deck with provided parameters
                tcis.createDeck(nameField.getText(), (DeckType) typeBox.getSelectedItem());
                gui.refreshAll(); // Update UI
            } catch (Exception ex) {
                showErrorDialog("Error: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Shows dialog for adding cards to a deck with validation.
     * @param container The deck to add to (must be castable to Deck)
     * @param tcis Reference to the main system
     * @param controller The deck controller (must be castable to DeckController)
     */
	@Override
	protected void showAddCardDialog(CardContainer container, TradingCardInventorySystem tcis,
			ContainerController controller) {
		  Deck deck = (Deck)container;
		  DeckController deckController = (DeckController)controller;
		    
		    // Check deck capacity first
		    if (deck.getTotalCards() >= deck.getCapacity()) {
		        showErrorDialog("Deck is already full!");
		        return;
		    }

		    // Get available cards (excluding those already in deck)
		    List<Card> availableCards = tcis.getCollection().getCardsWithCounts().entrySet().stream()
		            .filter(entry -> entry.getValue() > 0) // Only cards with available copies
		            .map(Map.Entry::getKey) // Get the Card object
		            .filter(card -> !deck.hasCard(card.getName())) // Not already in deck
		            .toList();

		    if (availableCards.isEmpty()) {
		        showErrorDialog("No available cards in collection that aren't already in this deck!");
		        return;
		    }
		
		 // Create card selection dropdown
	        JComboBox<Card> cardCombo = new JComboBox<>(availableCards.toArray(new Card[0]));

	        JPanel panel = new JPanel();
	        panel.add(new JLabel("Select card from collection:"));
	        panel.add(cardCombo);

	        if (JOptionPane.showConfirmDialog(
	                null, panel, "Add Card to Deck", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
	            deckController.handleAddCard(container, (Card) cardCombo.getSelectedItem());
	        }
	}

}

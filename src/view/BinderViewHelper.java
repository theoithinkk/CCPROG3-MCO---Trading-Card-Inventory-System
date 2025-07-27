package view;

import model.*;
import controller.*;
import enums.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;

/**
 * Helper class for creating and managing binder-related UI components.
 * Contains static methods for various dialog windows and panel creations.
 */
public class BinderViewHelper extends CardContainerViewHelper{
    
    /**
     * Creates a panel displaying binder information with action buttons.
     * @param binder The binder to display
     * @param tcis Reference to the main system
     * @param controller The binder controller for handling actions
     * @return Configured JPanel with binder information
     */
	public JPanel createContainerPanel(CardContainer container, TradingCardInventorySystem tcis, ContainerController controller) {
	    Binder binder = (Binder) container;
	    BinderController binderController = (BinderController) controller;

	    // Content panel with binder details
	    JPanel contentPanel = new JPanel();
	    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
	    contentPanel.setBackground(getBinderColor(binder.getType()));
	    contentPanel.setOpaque(false);

	    JLabel nameLabel = new JLabel(binder.getName());
	    nameLabel.setFont(FontManager.NEXA_H.deriveFont(20f));
	    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    contentPanel.add(nameLabel);
	    contentPanel.add(Box.createVerticalStrut(8));

	    JLabel typeLabel = new JLabel("Type: " + binder.getType());
	    typeLabel.setFont(FontManager.NEXA_H.deriveFont(15f));
	    typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    contentPanel.add(typeLabel);
	    contentPanel.add(Box.createVerticalStrut(5));

	    JLabel cardsLabel = new JLabel("Cards: " + binder.getTotalCards() + "/20");
	    cardsLabel.setFont(FontManager.NEXA_H.deriveFont(15f));
	    cardsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    cardsLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    contentPanel.add(cardsLabel);
	    contentPanel.add(Box.createVerticalStrut(20));

	    // Button panel
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    buttonPanel.setBackground(contentPanel.getBackground());
	    buttonPanel.setOpaque(false);

	    addStyledButton(buttonPanel, "View Details", e -> controller.handleViewDetails(binder));
	    addStyledButton(buttonPanel, "Add Card", e -> showAddCardDialog(binder, tcis, binderController));

	    if (binder.isTradeable()) {
	        addStyledButton(buttonPanel, "Trade", e -> binderController.handleTrade(binder));
	    }

	    if (binder.isSellable()) {
	        addStyledButton(buttonPanel, "Sell Binder", e -> binderController.handleSale(binder));
	    }

	    addStyledButton(buttonPanel, "Remove Card", e -> {
	        Card toRemove = CardContainerViewHelper.selectCardToRemove(binder);
	        if (toRemove != null) {
	            binderController.handleRemoveCard(binder, toRemove);
	        }
	    });

	    addStyledButton(buttonPanel, "Delete Binder", e -> confirmDelete(binder, binderController));

	    // Wrap in card-like panel
	    JPanel cardPanel = new JPanel(new BorderLayout());
	    cardPanel.setPreferredSize(new Dimension(220, 320));
	    cardPanel.setBackground(getBinderColor(binder.getType()));
	    cardPanel.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
	        BorderFactory.createEmptyBorder(15, 15, 15, 15)
	    ));

	    cardPanel.add(contentPanel, BorderLayout.CENTER);
	    cardPanel.add(buttonPanel, BorderLayout.SOUTH);

	    return cardPanel;
	}
    
    public static Color getBinderColor(BinderType type) {
        return switch (type) {
        case NON_CURATED -> new Color(200, 200, 200); // simple gray
        case PAUPER -> new Color(173, 216, 230); // soft blue
        case RARES -> new Color(216, 191, 216); // light purple
        case LUXURY -> new Color(255, 200, 100); // soft orange
        case COLLECTOR -> new Color(255, 100, 100); // light red
        };
    }
    
    /**
     * Opens a dialog for creating a new binder.
     * @param tcis Reference to the main system
     * @param gui Reference to the main GUI for refreshing
     */
    public static void openCreateBinderDialog(TradingCardInventorySystem tcis, TCISGUI gui) {
        // Input fields for binder creation
        JTextField nameField = new JTextField();
        JComboBox<BinderType> typeBox = new JComboBox<>(BinderType.values());

        // Configure dialog layout
        JPanel dialogPanel = new JPanel(new GridLayout(0, 2));
        dialogPanel.add(new JLabel("Binder Name:")); dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Binder Type:")); dialogPanel.add(typeBox);

        // Show dialog and process result
        if (JOptionPane.showConfirmDialog(null, dialogPanel, "Create Binder", 
            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                tcis.createBinder(nameField.getText(), (BinderType) typeBox.getSelectedItem());
                gui.refreshAll(); // Update UI
            } catch (Exception ex) {
                showErrorDialog("Error: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Shows dialog for selecting a card to trade away.
     * @param binder The binder to select from
     * @return Selected card or null if cancelled
     */
    public static Card selectOutgoingCard(Binder binder) {
        if (binder.getCards().isEmpty()) {
            showErrorDialog("No cards available to trade!");
            return null;
        }

        // Create dropdown of available cards
        JComboBox<Card> cardCombo = new JComboBox<>(
            binder.getCards().toArray(new Card[0])
        );

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select card to trade away:"));
        panel.add(cardCombo);

        if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
            null, panel, "Outgoing Card", JOptionPane.OK_CANCEL_OPTION)) {
            return (Card) cardCombo.getSelectedItem();
        }
        return null;
    }

    /**
     * Shows dialog for creating/selecting an incoming trade card.
     * @return The created card or null if cancelled
     */
    public static Card createIncomingCardDialog() {
        // Input fields for new card
        JTextField nameField = new JTextField();
        JComboBox<Rarity> rarityBox = new JComboBox<>(Rarity.values());
        JComboBox<Variant> variantBox = new JComboBox<>(Variant.values());
        JTextField valueField = new JTextField(10);

        // Configure dialog layout
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("New Card Name:")); panel.add(nameField);
        panel.add(new JLabel("Rarity:")); panel.add(rarityBox);
        panel.add(new JLabel("Variant:")); panel.add(variantBox);
        panel.add(new JLabel("Base Value:")); panel.add(valueField);

        // Keep showing dialog until valid input or cancellation
        while (true) {
            int result = JOptionPane.showConfirmDialog(
                null, panel, "Incoming Card", JOptionPane.OK_CANCEL_OPTION
            );

            if (result != JOptionPane.OK_OPTION) return null;

            try {
                // Attempt to create new card from inputs
                return new Card(
                    nameField.getText().trim(),
                    (Rarity) rarityBox.getSelectedItem(),
                    (Variant) variantBox.getSelectedItem(),
                    Double.parseDouble(valueField.getText())
                );
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid value! Must be a number.");
            } catch (IllegalArgumentException e) {
                showErrorDialog(e.getMessage());
            }
        }
    }
    
    /**
     * Shows confirmation dialog for unbalanced trades.
     * @param outgoingStr Description of outgoing card
     * @param incomingStr Description of incoming card
     * @param difference The value difference between cards
     * @return true if user confirms, false otherwise
     */
    public static boolean confirmUnbalancedTrade(String outgoingStr, String incomingStr, double difference) {
        String message = String.format(
            "Trade Imbalance!\n\n" +
            "Outgoing: %s\n" +
            "Incoming: %s\n\n" +
            "Difference: $%.2f\n\n" +
            "Proceed anyway?",
            outgoingStr, incomingStr, difference
        );

        return JOptionPane.showConfirmDialog(
            null, message, "Warning", JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }

    /**
     * Shows trade success confirmation.
     * @param outgoingStr Description of traded card
     * @param incomingStr Description of received card
     */
    public static void showTradeSuccess(String outgoingStr, String incomingStr) {
        String message = String.format(
            "Trade Complete!\n\n" +
            "Traded away: %s\n" +
            "Received: %s",
            outgoingStr, incomingStr
        );

        JOptionPane.showMessageDialog(null, message);
    }
    
    /**
     * Shows a dialog for adding cards from the collection to a specific binder.
     * Implements the abstract method from CardContainerViewHelper with binder-specific behavior.
     * 
     * @param container The binder to add cards to (must be castable to Binder)
     * @param tcis Reference to the main trading card system
     * @param controller The controller to handle the add operation (must be castable to BinderController)
     */
	@Override
	protected void showAddCardDialog(CardContainer container, TradingCardInventorySystem tcis, ContainerController controller) {
		// Filter to only cards not already in this binder
		Binder binder = (Binder)container;
        BinderController binderController = (BinderController)controller;
        List<Card> availableCards = tcis.getCollection().getAllCards().stream()
            .filter(card -> !binder.getCards().contains(card))
            .toList();

        if (availableCards.isEmpty()) {
            showErrorDialog("No available cards in collection to add!");
            return;
        }

        // Create card selection dropdown
        JComboBox<Card> cardCombo = new JComboBox<>(availableCards.toArray(new Card[0]));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select card from collection:"));
        panel.add(cardCombo);

        if (JOptionPane.showConfirmDialog(
                null, panel, "Add Card to Binder", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            binderController.handleAddCard(container, (Card) cardCombo.getSelectedItem());
        }
		
	}
}
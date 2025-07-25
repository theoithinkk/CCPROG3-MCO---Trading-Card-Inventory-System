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
public class BinderViewHelper {
    
    /**
     * Creates a panel displaying binder information with action buttons.
     * @param binder The binder to display
     * @param tcis Reference to the main system
     * @param controller The binder controller for handling actions
     * @return Configured JPanel with binder information
     */
    public static JPanel createBinderPanel(Binder binder, TradingCardInventorySystem tcis, BinderController controller) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Basic binder information section
        panel.add(new JLabel(binder.getName()));
        panel.add(new JLabel("Type: " + binder.getType()));
        panel.add(new JLabel("Cards: " + binder.getTotalCards() + "/20")); // Shows capacity

        // Standard action buttons
        addActionButton(panel, "View Details", e -> controller.handleViewDetails(binder));
        addActionButton(panel, "Add Card", e -> showAddCardDialog(binder, tcis, controller));
        
        // Conditional buttons based on binder properties
        if (binder.isTradeable()) {
            addActionButton(panel, "Trade", e -> controller.handleTrade(binder));
        }
        if (binder.isSellable()) {
            addActionButton(panel, "Sell Binder", e -> controller.handleSale(binder));
        }
        addActionButton(panel, "Delete Binder", e -> confirmDeleteBinder(binder, controller));

        return panel;
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
     * Helper method to add styled action buttons to panels.
     * @param panel The panel to add the button to
     * @param text Button text
     * @param listener Action to perform when clicked
     */
    private static void addActionButton(JPanel panel, String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.addActionListener(listener);
        panel.add(btn);
    }
    
    /**
     * Shows confirmation dialog before deleting a binder.
     * @param binder The binder to potentially delete
     * @param controller The binder controller to handle deletion
     */
    public static void confirmDeleteBinder(Binder binder, BinderController controller) {
        int response = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to delete '" + binder.getName() + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            controller.handleDeleteBinder(binder);
        }
    }

    /**
     * Displays detailed information about a binder's contents.
     * @param binder The binder to display
     */
    public static void showBinderDetails(Binder binder) {
        StringBuilder sb = new StringBuilder();
        // Format each card's information
        for (Card c : binder.getCards()) {
            sb.append(String.format(
                "%s | %s | %s | Base: $%.2f\n",
                c.getName(), c.getRarity(), c.getVariant(), c.getBaseValue()
            ));
        }
        // Append total value at bottom
        sb.append("\nTotal Value: $").append(binder.getTotalValue());
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    /**
     * Displays an error message dialog.
     * @param message The error message to display
     */
    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            null, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
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
     * Shows dialog for adding a card from collection to binder.
     * @param binder The target binder
     * @param tcis Reference to the system
     * @param controller The binder controller
     */
    public static void showAddCardDialog(Binder binder, TradingCardInventorySystem tcis, BinderController controller) {
        // Filter to only cards not already in this binder
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
            controller.handleAddCard(binder, (Card) cardCombo.getSelectedItem());
        }
    }

    /**
     * Creates a panel for trade information input (unused in current implementation).
     * @return Configured trade dialog panel
     */
    public static JPanel createTradeDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Card Name:")); panel.add(new JTextField());
        panel.add(new JLabel("Rarity:")); panel.add(new JComboBox<>(Rarity.values()));
        panel.add(new JLabel("Variant:")); panel.add(new JComboBox<>(Variant.values()));
        panel.add(new JLabel("Base Value:")); panel.add(new JTextField());
        return panel;
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
     * Displays an informational message dialog.
     * @param title Dialog title
     * @param message The message to display
     */
    public static void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(
            null, 
            message, 
            title, 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
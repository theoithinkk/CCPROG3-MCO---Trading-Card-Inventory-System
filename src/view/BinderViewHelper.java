/**
 * Helper class for generating and managing UI elements related to binders.
 * Includes dialog windows for binder creation and card trading, as well as the layout logic for binder cards.
 * Extends {@link CardContainerViewHelper} to reuse shared logic across container views.
 *
 * This class is used by {@link BinderPanel} and interacts with {@link BinderController}.
 *
 * @version 2.0
 * @author Theodore Garcia
 * @author Ronin Zerna
 */

package view;

import model.*;
import controller.*;
import enums.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;

public class BinderViewHelper extends CardContainerViewHelper {

    /**
     * Creates a panel displaying a single binder's information and action buttons.
     *
     * @param container  The binder to display (casted from CardContainer).
     * @param tcis       The trading card inventory system.
     * @param controller The controller for binder operations.
     * @return A JPanel containing the visual and interactive representation of the binder.
     */
    public JPanel createContainerPanel(CardContainer container, TradingCardInventorySystem tcis, ContainerController controller) {
        Binder binder = (Binder) container;
        BinderController binderController = (BinderController) controller;

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

        // Action Buttons
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

        if (binder.getType() == BinderType.LUXURY) {
            addStyledButton(buttonPanel, "Set Price", e -> {
                double min = binder.getTotalValue();
                String input = JOptionPane.showInputDialog(
                    null,
                    String.format("Enter new price for '%s' (min $%.2f):", binder.getName(), min),
                    String.format("%.2f", min)
                );
                if (input != null) {
                    try {
                        double newPrice = Double.parseDouble(input);
                        if (newPrice < min) {
                            showErrorDialog("Price must be at least $" + String.format("%.2f", min));
                        } else {
                            binderController.handleSetPrice(binder, newPrice);
                        }
                    } catch (NumberFormatException ex) {
                        showErrorDialog("Invalid number: " + ex.getMessage());
                    }
                }
            });
        }

        addStyledButton(buttonPanel, "Remove Card", e -> {
            Card toRemove = CardContainerViewHelper.selectCardToRemove(binder);
            if (toRemove != null) {
                binderController.handleRemoveCard(binder, toRemove);
            }
        });

        addStyledButton(buttonPanel, "Delete Binder", e -> confirmDelete(binder, binderController));

        // Final wrapper panel
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

    /**
     * Returns the UI color associated with each binder type.
     *
     * @param type The binder type.
     * @return A Color representing the binder.
     */
    public static Color getBinderColor(BinderType type) {
        return switch (type) {
            case NON_CURATED -> new Color(200, 200, 200);
            case PAUPER -> new Color(173, 216, 230);
            case RARES -> new Color(216, 191, 216);
            case LUXURY -> new Color(255, 200, 100);
            case COLLECTOR -> new Color(255, 100, 100);
        };
    }

    /**
     * Opens a dialog window to create a new binder.
     *
     * @param tcis The trading card system.
     * @param gui  The main GUI to refresh after creation.
     */
    public static void openCreateBinderDialog(TradingCardInventorySystem tcis, TCISGUI gui) {
        JTextField nameField = new JTextField();
        JComboBox<BinderType> typeBox = new JComboBox<>(BinderType.values());

        JPanel dialogPanel = new JPanel(new GridLayout(0, 2));
        dialogPanel.add(new JLabel("Binder Name:"));
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Binder Type:"));
        dialogPanel.add(typeBox);

        if (JOptionPane.showConfirmDialog(null, dialogPanel, "Create Binder", 
            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                tcis.createBinder(nameField.getText(), (BinderType) typeBox.getSelectedItem());
                gui.refreshAll();
            } catch (Exception ex) {
                showErrorDialog("Error: " + ex.getMessage());
            }
        }
    }

    /**
     * Prompts the user to select a card from a binder to trade away.
     *
     * @param binder The binder to trade from.
     * @return The selected card, or null if cancelled.
     */
    public static Card selectOutgoingCard(Binder binder) {
        if (binder.getCards().isEmpty()) {
            showErrorDialog("No cards available to trade!");
            return null;
        }

        JComboBox<Card> cardCombo = new JComboBox<>(binder.getCards().toArray(new Card[0]));
        JPanel panel = new JPanel();
        panel.add(new JLabel("Select card to trade away:"));
        panel.add(cardCombo);

        if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, panel, "Outgoing Card", JOptionPane.OK_CANCEL_OPTION)) {
            return (Card) cardCombo.getSelectedItem();
        }
        return null;
    }

    /**
     * Opens a dialog to create the incoming card in a trade.
     *
     * @return A new Card object if created successfully, or null if cancelled.
     */
    public static Card createIncomingCardDialog(Binder binder) {
        JTextField nameField = new JTextField();
        JComboBox<Rarity> rarityBox = new JComboBox<>(Rarity.values());
        JComboBox<Variant> variantBox = new JComboBox<>(Variant.values());
        JTextField valueField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("New Card Name:")); panel.add(nameField);
        panel.add(new JLabel("Rarity:")); panel.add(rarityBox);
        panel.add(new JLabel("Variant:")); panel.add(variantBox);
        panel.add(new JLabel("Base Value:")); panel.add(valueField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(null, panel, "Incoming Card", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) return null;

            try {
                Card card = new Card(
                        nameField.getText().trim(),
                        (Rarity) rarityBox.getSelectedItem(),
                        (Variant) variantBox.getSelectedItem(),
                        Double.parseDouble(valueField.getText())
                    );
            	if(!binder.canAddCard(card)) {
            		throw new IllegalArgumentException("Card does not meet the requirements of the binder.");
            	}
            	
                return card;
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid value! Must be a number.");
            } catch (IllegalArgumentException e) {
                showErrorDialog(e.getMessage());
            }
        }
    }

    /**
     * Shows a confirmation prompt for trades with large value differences.
     *
     * @param outgoingStr Description of the outgoing card.
     * @param incomingStr Description of the incoming card.
     * @param difference  The value difference.
     * @return True if the user confirms, false otherwise.
     */
    public static boolean confirmUnbalancedTrade(String outgoingStr, String incomingStr, double difference) {
        String message = String.format(
            "Trade Imbalance!\n\nOutgoing: %s\nIncoming: %s\n\nDifference: $%.2f\n\nProceed anyway?",
            outgoingStr, incomingStr, difference
        );

        return JOptionPane.showConfirmDialog(null, message, "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /**
     * Displays a message confirming that a trade was successful.
     *
     * @param outgoingStr The outgoing card name.
     * @param incomingStr The incoming card name.
     */
    public static void showTradeSuccess(String outgoingStr, String incomingStr) {
        String message = String.format(
            "Trade Complete!\n\nTraded away: %s\nReceived: %s",
            outgoingStr, incomingStr
        );

        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Opens a dialog to add a card from the collection to the specified binder.
     *
     * @param container  The binder receiving the card.
     * @param tcis       The main system instance.
     * @param controller The controller handling the action.
     */
    @Override
    protected void showAddCardDialog(CardContainer container, TradingCardInventorySystem tcis, ContainerController controller) {
        Binder binder = (Binder) container;
        BinderController binderController = (BinderController) controller;

        List<Card> availableCards = tcis.getCollection().getAllCards().stream()
            .filter(card -> !binder.getCards().contains(card))
            .toList();

        if (availableCards.isEmpty()) {
            showErrorDialog("No available cards in collection to add!");
            return;
        }

        JComboBox<Card> cardCombo = new JComboBox<>(availableCards.toArray(new Card[0]));
        JPanel panel = new JPanel();
        panel.add(new JLabel("Select card from collection:"));
        panel.add(cardCombo);

        if (JOptionPane.showConfirmDialog(null, panel, "Add Card to Binder", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            binderController.handleAddCard(container, (Card) cardCombo.getSelectedItem());
        }
    }
}

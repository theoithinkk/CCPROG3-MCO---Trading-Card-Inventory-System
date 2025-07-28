/**
 * The DeckViewHelper class provides utility methods for generating and managing
 * deck-related UI components in the Trading Card Inventory System.
 * This includes rendering styled deck panels, handling deck creation and card addition dialogs,
 * and supporting interaction logic via the DeckController.
 * 
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */
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
     *
     * @param container  the deck to display (must be castable to Deck)
     * @param tcis       reference to the main system
     * @param controller the deck controller (must be castable to DeckController)
     * @return configured JPanel with deck information and actions
     */
    public JPanel createContainerPanel(CardContainer container, TradingCardInventorySystem tcis, ContainerController controller) {
        Deck deck = (Deck) container;
        DeckController deckController = (DeckController) controller;

        // Content panel with deck details
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(getDeckColor(deck.getType()));
        contentPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(deck.getName());
        nameLabel.setFont(FontManager.NEXA_H.deriveFont(15f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(8));

        JLabel typeLabel = new JLabel("Type: " + deck.getType());
        typeLabel.setFont(FontManager.NEXA_H.deriveFont(15f));
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(typeLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        JLabel cardsLabel = new JLabel("Cards: " + deck.getTotalCards() + "/10");
        cardsLabel.setFont(FontManager.NEXA_H.deriveFont(15f)); // fixed typo from 115f to 15f
        cardsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(cardsLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(contentPanel.getBackground());
        buttonPanel.setOpaque(false);

        addStyledButton(buttonPanel, "View Details", e -> deckController.handleViewDetails(deck));
        addStyledButton(buttonPanel, "Add Card", e -> showAddCardDialog(deck, tcis, deckController));

        if (deck.isSellable()) {
            addStyledButton(buttonPanel, "Sell Deck", e -> deckController.handleSale(deck));
        }

        addStyledButton(buttonPanel, "Remove Card", e -> {
            Card toRemove = CardContainerViewHelper.selectCardToRemove(deck);
            if (toRemove != null) {
                deckController.handleRemoveCard(deck, toRemove);
            }
        });

        addStyledButton(buttonPanel, "Delete Deck", e -> confirmDelete(deck, deckController));

        // Wrap in card-like panel
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(220, 320));
        cardPanel.setBackground(getDeckColor(deck.getType()));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        cardPanel.add(contentPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cardPanel;
    }

    /**
     * Returns a background color depending on the {@code DeckType}.
     *
     * @param type the deck type
     * @return a color representing the type
     */
    public static Color getDeckColor(DeckType type) {
        return switch (type) {
            case NORMAL -> new Color(220, 220, 220);        // soft gray
            case SELLABLE -> new Color(144, 238, 144);      // light green
        };
    }

    /**
     * Shows a dialog for creating a new deck with user-defined name and type.
     *
     * @param tcis the trading card inventory system
     * @param gui  the GUI instance for triggering UI refresh
     */
    public static void openCreateDeckDialog(TradingCardInventorySystem tcis, TCISGUI gui) {
        JTextField nameField = new JTextField();
        JComboBox<DeckType> typeBox = new JComboBox<>(DeckType.values());

        JPanel dialogPanel = new JPanel(new GridLayout(0, 2));
        dialogPanel.add(new JLabel("Deck Name:"));
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Deck Type:"));
        dialogPanel.add(typeBox);

        if (JOptionPane.showConfirmDialog(null, dialogPanel, "Create Deck",
            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                tcis.createDeck(nameField.getText(), (DeckType) typeBox.getSelectedItem());
                gui.refreshAll(); // Update UI
            } catch (Exception ex) {
                showErrorDialog("Error: " + ex.getMessage());
            }
        }
    }

    /**
     * Shows a dialog for adding a card to a deck, with filtering and validation.
     *
     * @param container  the target deck (castable to Deck)
     * @param tcis       the trading card inventory system
     * @param controller the controller handling the addition
     */
    @Override
    protected void showAddCardDialog(CardContainer container, TradingCardInventorySystem tcis,
                                     ContainerController controller) {
        Deck deck = (Deck) container;
        DeckController deckController = (DeckController) controller;

        // Check deck capacity first
        if (deck.getTotalCards() >= deck.getCapacity()) {
            showErrorDialog("Deck is already full!");
            return;
        }

        // Get available cards from collection
        List<Card> availableCards = tcis.getCollection().getCardsWithCounts().entrySet().stream()
            .filter(entry -> entry.getValue() > 0)
            .map(Map.Entry::getKey)
            .filter(card -> !deck.hasCard(card.getName()))
            .toList();

        if (availableCards.isEmpty()) {
            showErrorDialog("No available cards in collection that aren't already in this deck!");
            return;
        }

        // Dropdown selection
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

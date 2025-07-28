/**
 * CollectionController.java
 *
 * Provides static methods for managing the collection view and actions within the Trading Card Inventory System.
 * This includes creating new cards through a dialog, rendering card panels with interactive controls,
 * and enabling card adjustments such as selling and modifying counts.
 *
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */

package controller;

import model.*;
import view.*;
import enums.*;
import javax.swing.*;
import java.awt.*;

/**
 * A utility controller class responsible for managing user interactions with the card collection,
 * including adding cards, selling cards, and adjusting their counts.
 */
public class CollectionController {

    /**
     * Opens a dialog to allow the user to input details for a new card and adds it to the collection if valid.
     * Also updates the UI components (panel and stats).
     *
     * @param tcis   The main TradingCardInventorySystem instance.
     * @param gui    The main GUI reference to update components after changes.
     * @param panel  The CollectionPanel to refresh once a card is added.
     */
    public static void openAddCardDialog(TradingCardInventorySystem tcis, TCISGUI gui, CollectionPanel panel) {
        JPanel dialogPanel = new JPanel(new GridLayout(0, 2));
        JTextField nameField = new JTextField();
        JComboBox<Rarity> rarityBox = new JComboBox<>(Rarity.values());
        JComboBox<Variant> variantBox = new JComboBox<>(Variant.values());
        JTextField valueField = new JTextField();

        dialogPanel.add(new JLabel("Name:"));
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Rarity:"));
        dialogPanel.add(rarityBox);
        dialogPanel.add(new JLabel("Variant:"));
        dialogPanel.add(variantBox);
        dialogPanel.add(new JLabel("Base Value:"));
        dialogPanel.add(valueField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Add New Card", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Card name cannot be empty.");
                }

                Rarity rarity = (Rarity) rarityBox.getSelectedItem();
                Variant variant = Variant.NORMAL;

                if (rarity == Rarity.RARE || rarity == Rarity.LEGENDARY) {
                    variant = (Variant) variantBox.getSelectedItem();
                }

                double value = Double.parseDouble(valueField.getText());

                if (value < 0) {
                    throw new IllegalArgumentException("Value must not be negative.");
                }

                Card card = new Card(name, rarity, variant, value);
                tcis.getCollection().addCard(card);
                panel.refreshCards();
                gui.updateStatsPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
            }
        }
    }

    /**
     * Creates a visual panel representing a card in the collection with relevant information and action buttons.
     * Allows users to view details, sell, or adjust the count of the card.
     *
     * @param card   The card object to display.
     * @param tcis   The main TradingCardInventorySystem instance.
     * @param gui    The GUI instance to refresh display elements.
     * @param panel  The panel that holds the collection view.
     * @return A JPanel with card details and interactive options.
     */
    public static JPanel createCardPanel(Card card, TradingCardInventorySystem tcis, TCISGUI gui, CollectionPanel panel) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel(card.getName());
        JLabel countLabel = new JLabel("Copies: " + tcis.getCollection().getCardCount(card));
        JLabel valueLabel = new JLabel("Value: $" + String.format("%.2f", card.getTotalValue()));

        // Details Button
        JButton detailsBtn = new JButton("Details");
        detailsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                "Name: " + card.getName() +
                "\nRarity: " + card.getRarity() +
                "\nVariant: " + card.getVariant() +
                "\nBase Value: $" + card.getBaseValue() +
                "\nTotal Value: $" + card.getTotalValue());
        });

        // Sell Button
        JButton sellBtn = new JButton("Sell");
        sellBtn.addActionListener(e -> {
            if (tcis.sellCard(card)) {
                gui.updateMoneyDisplay();
                gui.updateStatsPanel();
                panel.refreshCards();
                JOptionPane.showMessageDialog(null, "Sold successfully.");
                gui.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Could not sell card.");
            }
        });

        // Adjust Count Button
        JButton adjustCountBtn = new JButton("Adjust Count");
        adjustCountBtn.addActionListener(e -> {
            JPanel adjustPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            JButton plusBtn = new JButton("+");
            JButton minusBtn = new JButton("-");

            plusBtn.addActionListener(a -> {
                tcis.getCollection().addCard(card);
                gui.updateStatsPanel();
                panel.refreshCards();
            });

            minusBtn.addActionListener(a -> {
                if (tcis.getCollection().getCardCount(card) > 0) {
                    tcis.getCollection().removeCard(card);
                    gui.updateStatsPanel();
                    panel.refreshCards();
                } else {
                    JOptionPane.showMessageDialog(null, "Card already at 0 copies.");
                }
            });

            adjustPanel.add(plusBtn);
            adjustPanel.add(minusBtn);

            JOptionPane.showMessageDialog(null, adjustPanel, "Adjust Card Count", JOptionPane.PLAIN_MESSAGE);
        });

        // Add components to panel
        cardPanel.add(nameLabel);
        cardPanel.add(countLabel);
        cardPanel.add(valueLabel);
        cardPanel.add(detailsBtn);
        cardPanel.add(sellBtn);
        cardPanel.add(adjustCountBtn);

        // Set background color based on rarity
        Color color;
        switch (card.getRarity()) {
            case COMMON: color = Color.LIGHT_GRAY; break;
            case UNCOMMON: color = Color.CYAN; break;
            case RARE: color = new Color(186, 85, 211); break;
            case LEGENDARY: color = Color.ORANGE; break;
            default: color = Color.WHITE; break;
        }
        cardPanel.setBackground(color);

        return cardPanel;
    }
}

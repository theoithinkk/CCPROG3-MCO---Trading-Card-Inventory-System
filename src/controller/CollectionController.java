package controller;

import model.*;
import view.*;
import enums.*;
import javax.swing.*;
import java.awt.*;

public class CollectionController {
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
                Rarity rarity = (Rarity) rarityBox.getSelectedItem();
                Variant variant = (Variant) variantBox.getSelectedItem();
                double value = Double.parseDouble(valueField.getText());
                Card card = new Card(name, rarity, variant, value);
                tcis.getCollection().addCard(card);
                panel.refreshCards();
                gui.updateStatsPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
            }
        }
    }

    public static JPanel createCardPanel(Card card, TradingCardInventorySystem tcis, TCISGUI gui, CollectionPanel panel) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel(card.getName());
        JLabel countLabel = new JLabel("Copies: " + tcis.getCollection().getCardCount(card));
        JLabel valueLabel = new JLabel("Value: $" + String.format("%.2f", card.getTotalValue()));

        JButton detailsBtn = new JButton("Details");
        detailsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Name: " + card.getName() +
                            "\nRarity: " + card.getRarity() +
                            "\nVariant: " + card.getVariant() +
                            "\nBase Value: $" + card.getBaseValue() +
                            "\nTotal Value: $" + card.getTotalValue());
        });

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

        cardPanel.add(nameLabel);
        cardPanel.add(countLabel);
        cardPanel.add(valueLabel);
        cardPanel.add(detailsBtn);
        cardPanel.add(sellBtn);

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

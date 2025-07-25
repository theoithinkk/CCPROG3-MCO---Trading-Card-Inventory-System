package controller;

import model.*;
import enums.*;
import view.*;
import javax.swing.*;
import java.awt.*;

public class BinderController {
	public static void openCreateBinderDialog(TradingCardInventorySystem tcis, TCISGUI gui, BinderPanel panel) {
	    JPanel dialogPanel = new JPanel(new GridLayout(0, 2));
	    JTextField nameField = new JTextField();
	    JComboBox<BinderType> typeBox = new JComboBox<>(BinderType.values());

	    dialogPanel.add(new JLabel("Binder Name:"));
	    dialogPanel.add(nameField);
	    dialogPanel.add(new JLabel("Binder Type:"));
	    dialogPanel.add(typeBox);

	    int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Create Binder", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	        try {
	            tcis.createBinder(nameField.getText(), (BinderType) typeBox.getSelectedItem());
	            panel.refreshBinders(tcis, gui);
	            gui.updateStatsPanel();
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
	        }
	    }
	}

    public static JPanel createBinderPanel(Binder binder, TradingCardInventorySystem tcis, TCISGUI gui) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.add(new JLabel(binder.getName()));
        panel.add(new JLabel("Type: " + binder.getType()));
        panel.add(new JLabel("Cards: " + binder.getTotalCards() + "/20"));

        JButton viewBtn = new JButton("View Details");
        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Card c : binder.getCards()) {
                sb.append(c.getName()).append(" | ")
                  .append(c.getRarity()).append(" | ")
                  .append(c.getVariant()).append(" | Base: $")
                  .append(c.getBaseValue()).append("\n");
            }
            sb.append("\nTotal Value: $").append(binder.getTotalValue());
            JOptionPane.showMessageDialog(null, sb.toString());
        });
        panel.add(viewBtn);

        if (binder.isTradeable()) {
            JButton tradeBtn = new JButton("Trade");
            tradeBtn.addActionListener(e -> {
                JTextField tradeCardName = new JTextField();
                JComboBox<Rarity> rarityBox = new JComboBox<>(Rarity.values());
                JComboBox<Variant> variantBox = new JComboBox<>(Variant.values());
                JTextField baseValueField = new JTextField();

                JPanel tradePanel = new JPanel(new GridLayout(0, 2));
                tradePanel.add(new JLabel("New Card Name:")); tradePanel.add(tradeCardName);
                tradePanel.add(new JLabel("Rarity:")); tradePanel.add(rarityBox);
                tradePanel.add(new JLabel("Variant:")); tradePanel.add(variantBox);
                tradePanel.add(new JLabel("Base Value:")); tradePanel.add(baseValueField);

                int confirm = JOptionPane.showConfirmDialog(null, tradePanel, "Trade Card", JOptionPane.OK_CANCEL_OPTION);
                if (confirm == JOptionPane.OK_OPTION) {
                    try {
                        Card newCard = new Card(tradeCardName.getText(), (Rarity) rarityBox.getSelectedItem(), (Variant) variantBox.getSelectedItem(), Double.parseDouble(baseValueField.getText()));
                        double difference = newCard.getTotalValue();
                        binder.addCard(newCard);
                        JOptionPane.showMessageDialog(null, "Trade completed. Value of traded card: $" + difference);
                        gui.updateStatsPanel();
                        gui.repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error during trade: " + ex.getMessage());
                    }
                }
            });
            panel.add(tradeBtn);
        }

        if (binder.isSellable()) {
            JButton sellBtn = new JButton("Sell Binder");
            sellBtn.addActionListener(e -> {
                double value = binder.getTotalValue();
                if (binder.getType() == BinderType.RARES || binder.getType() == BinderType.LUXURY)
                    value *= 0.9;

                int confirm = JOptionPane.showConfirmDialog(null, "Sell for $" + value + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tcis.sellBinder(binder);
                    gui.updateMoneyDisplay();
                    gui.updateStatsPanel();
                    gui.repaint();
                }
            });
            panel.add(sellBtn);
        }

        return panel;
    }
}
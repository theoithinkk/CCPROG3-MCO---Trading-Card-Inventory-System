package controller;

import model.*;
import enums.*;
import view.*;
import javax.swing.*;
import java.awt.*;

public class DeckController {
	public static void openCreateDeckDialog(TradingCardInventorySystem tcis, TCISGUI gui, DeckPanel panel) {
	    JPanel dialogPanel = new JPanel(new GridLayout(0, 2));
	    JTextField nameField = new JTextField();
	    JComboBox<DeckType> typeBox = new JComboBox<>(DeckType.values());

	    dialogPanel.add(new JLabel("Deck Name:"));
	    dialogPanel.add(nameField);
	    dialogPanel.add(new JLabel("Deck Type:"));
	    dialogPanel.add(typeBox);

	    int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Create Deck", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	        try {
	            tcis.createDeck(nameField.getText(), (DeckType) typeBox.getSelectedItem());
	            panel.refreshDecks(tcis, gui);
	            gui.updateStatsPanel();
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
	        }
	    }
	}

    public static JPanel createDeckPanel(Deck deck, TradingCardInventorySystem tcis, TCISGUI gui) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.add(new JLabel(deck.getName()));
        panel.add(new JLabel("Type: " + deck.getType()));
        panel.add(new JLabel("Cards: " + deck.getTotalCards() + "/10"));

        JButton viewBtn = new JButton("View Details");
        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Card c : deck.getCards()) {
                sb.append(c.getName()).append(" | ")
                  .append(c.getRarity()).append(" | ")
                  .append(c.getVariant()).append(" | Base: $")
                  .append(c.getBaseValue()).append("\n");
            }
            sb.append("\nTotal Value: $").append(deck.getTotalValue());
            JOptionPane.showMessageDialog(null, sb.toString());
        });
        panel.add(viewBtn);

        if (deck.getType() == DeckType.SELLABLE) {
            JButton sellBtn = new JButton("Sell");
            sellBtn.addActionListener(e -> {
                double value = deck.getTotalValue();
                int confirm = JOptionPane.showConfirmDialog(null, "Sell for $" + value + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tcis.sellDeck(deck);
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

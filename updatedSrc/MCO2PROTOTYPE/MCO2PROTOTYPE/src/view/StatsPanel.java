package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private JLabel moneyLabel;
    private JLabel totalCardsLabel;
    private JLabel binderCountLabel;
    private JLabel deckCountLabel;
    private TradingCardInventorySystem tcis;

    public StatsPanel(TradingCardInventorySystem tcis) {
        this.tcis = tcis;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        moneyLabel = new JLabel();
        totalCardsLabel = new JLabel();
        binderCountLabel = new JLabel();
        deckCountLabel = new JLabel();

        add(moneyLabel);
        add(totalCardsLabel);
        add(binderCountLabel);
        add(deckCountLabel);

        refreshStats();
    }

    public void refreshStats() {
        moneyLabel.setText("Total Money: $" + String.format("%.2f", tcis.getMoney()));
        totalCardsLabel.setText("Total Cards: " + tcis.getTotalCardCount());
        binderCountLabel.setText("Total Binders: " + tcis.getBinders().size());
        deckCountLabel.setText("Total Decks: " + tcis.getDecks().size());
    }
}
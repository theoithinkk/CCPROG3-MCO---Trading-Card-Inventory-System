package view;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CollectionPanel extends JPanel {
    private TradingCardInventorySystem tcis;
    private TCISGUI parentGui;
    private JPanel cardsGrid;

    public CollectionPanel(TradingCardInventorySystem tcis, TCISGUI parentGui) {
        this.tcis = tcis;
        this.parentGui = parentGui;
        setLayout(new BorderLayout());

        JButton addCardButton = new JButton("Add Card");
        addCardButton.addActionListener(e -> CollectionController.openAddCardDialog(tcis, parentGui, this));
        add(addCardButton, BorderLayout.NORTH);

        cardsGrid = new JPanel();
        cardsGrid.setLayout(new GridLayout(0, 3, 10, 10));
        refreshCards();

        JScrollPane scrollPane = new JScrollPane(cardsGrid);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshCards() {
        cardsGrid.removeAll();
        List<Card> allCards = tcis.getCollection().getAllCards();
        for (Card card : allCards) {
            JPanel cardPanel = CollectionController.createCardPanel(card, tcis, parentGui, this);
            cardsGrid.add(cardPanel);
        }
        revalidate();
        repaint();
    }
}
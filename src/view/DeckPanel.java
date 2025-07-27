package view;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;

public class DeckPanel extends JPanel {
    private JPanel deckGrid;
    private DeckController controller;
    private DeckViewHelper helper;

    public DeckPanel(TradingCardInventorySystem tcis, TCISGUI parentGui) {
        this.controller = new DeckController(tcis, parentGui);
        this.helper = new DeckViewHelper();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // Styled Create Deck Button (matching BinderPanel style)
        JButton createDeckBtn = new JButton("Create Deck");
        createDeckBtn.setFont(FontManager.NEXA_H.deriveFont(22f));
        createDeckBtn.setBackground(new Color(100, 140, 240));
        createDeckBtn.setForeground(Color.WHITE);
        createDeckBtn.setFocusPainted(false);
        createDeckBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        createDeckBtn.addActionListener(e -> DeckViewHelper.openCreateDeckDialog(tcis, parentGui));

        // Top Bar Styling (same as BinderPanel)
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(245, 245, 250));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        topBar.add(createDeckBtn);

        // GridBagLayout for decks
        deckGrid = new JPanel(new GridBagLayout());
        deckGrid.setBackground(new Color(245, 245, 250));
        deckGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(deckGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(topBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshDecks(tcis, parentGui);
    }

    public void refreshDecks(TradingCardInventorySystem tcis, TCISGUI gui) {
        deckGrid.removeAll();

        int col = 0, row = 0;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        for (Deck deck : tcis.getDecks()) {
            JPanel panel = helper.createContainerPanel(deck, tcis, controller);
            gbc.gridx = col;
            gbc.gridy = row;
            deckGrid.add(panel, gbc);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }

        deckGrid.revalidate();
        deckGrid.repaint();
    }
}

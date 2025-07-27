package view;

import model.*;
import controller.*;
import enums.*;
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
        setBackground(new Color(245, 245, 250));

        // Styled Add Card Button
        JButton addCardButton = new JButton("Add Card");
        addCardButton.setFont(FontManager.NEXA_H.deriveFont(22f));
        addCardButton.setBackground(new Color(100, 140, 240));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.setFocusPainted(false);
        addCardButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addCardButton.addActionListener(e -> CollectionController.openAddCardDialog(tcis, parentGui, this));

        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(245, 245, 250));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        topBar.add(addCardButton);

        add(topBar, BorderLayout.NORTH);

        // Cards Grid - Use GridBagLayout for 3 per row + scroll
        cardsGrid = new JPanel(new GridBagLayout());
        cardsGrid.setBackground(new Color(245, 245, 250));
        cardsGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(cardsGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        refreshCards();
    }

    public void refreshCards() {
        cardsGrid.removeAll();
        List<Card> allCards = tcis.getCollection().getAllCards();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;

        for (int i = 0; i < allCards.size(); i++) {
            Card card = allCards.get(i);
            JPanel cardPanel = createFixedCardPanel(card);
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            cardsGrid.add(cardPanel, gbc);
        }

        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    private JPanel createFixedCardPanel(Card card) {
        JPanel cardPanel = new JPanel();

        cardPanel.setPreferredSize(new Dimension(220, 320));
        cardPanel.setBackground(getColorByRarity(card.getRarity()));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(cardPanel.getBackground());
        contentPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(card.getName());
        nameLabel.setFont(FontManager.NEXA_H.deriveFont(20f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(8));

        JLabel copiesLabel = new JLabel("Copies: " + tcis.getCollection().getCardCount(card));
        copiesLabel.setFont(FontManager.NEXA_EL.deriveFont(15f));
        copiesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        copiesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(copiesLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        JLabel valueLabel = new JLabel("Value: $" + String.format("%.2f", card.getTotalValue()));
        valueLabel.setFont(FontManager.NEXA_H.deriveFont(15f));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(valueLabel);

        contentPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(cardPanel.getBackground());
        buttonPanel.setOpaque(false);

        JPanel originalCardPanel = CollectionController.createCardPanel(card, tcis, parentGui, this);

        for (Component comp : originalCardPanel.getComponents()) {
            if (comp instanceof JButton btn) {
                btn.setFont(FontManager.NEXA_H.deriveFont(15f));
                btn.setBackground(new Color(240, 245, 255));
                btn.setForeground(Color.DARK_GRAY);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(160, 160, 200)),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setMaximumSize(new Dimension(180, 28));
                btn.setPreferredSize(new Dimension(180, 28));
                buttonPanel.add(btn);
                buttonPanel.add(Box.createVerticalStrut(8));
            }
        }

        cardPanel.add(contentPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cardPanel;
    }

    private Color getColorByRarity(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> new Color(230, 230, 230);
            case UNCOMMON -> new Color(200, 225, 245);
            case RARE -> new Color(225, 200, 245);
            case LEGENDARY -> new Color(255, 220, 180);
            default -> Color.WHITE;
        };
    }
}

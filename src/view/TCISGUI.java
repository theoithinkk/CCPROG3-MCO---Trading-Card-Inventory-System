package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TCISGUI extends JFrame {
    private TradingCardInventorySystem tcis;
    private JLabel moneyLabel;
    private StatsPanel statsPanel;
    private JPanel contentPanel;

    private CollectionPanel collectionPanel;
    private BinderPanel binderPanel;
    private DeckPanel deckPanel;

    public TCISGUI(TradingCardInventorySystem tcis) {
        this.tcis = tcis;
        setTitle("Trading Card Inventory System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false); // Make window not resizable

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(0, 70, 140));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel titleLabel = new JLabel("Trading Card Inventory System", SwingConstants.CENTER);
        titleLabel.setFont(FontManager.KETCHUM.deriveFont(42f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        moneyLabel = new JLabel("$0.0", SwingConstants.CENTER);
        moneyLabel.setFont(FontManager.NEXA_H.deriveFont(22f));
        moneyLabel.setForeground(Color.WHITE);
        moneyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(titleLabel);
        header.add(moneyLabel);
        add(header, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(4, 1, 0, 10));
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setBackground(new Color(30, 60, 120));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        String[] buttons = {"Collection", "Binders", "Decks", "Statistics"};

        for (String name : buttons) {
            JButton btn = new JButton(name);
            btn.setPreferredSize(new Dimension(200, 60));
            btn.setBackground(new Color(30, 60, 120));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(FontManager.NEXA_H.deriveFont(18f));
            btn.setBorderPainted(false);
            btn.setOpaque(true);

            // Hover effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(50, 90, 170));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(30, 60, 120));
                }
            });

            btn.addActionListener(e -> switchPanel(name));

            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        // Main content area
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Load default panel
        showCollectionPanel();

        setVisible(true);
    }

    private void switchPanel(String name) {
        contentPanel.removeAll();
        switch (name) {
            case "Collection" -> showCollectionPanel();
            case "Binders" -> {
                binderPanel = new BinderPanel(tcis, this);
                contentPanel.add(binderPanel);
            }
            case "Decks" -> {
                deckPanel = new DeckPanel(tcis, this);
                contentPanel.add(deckPanel);
            }
            case "Statistics" -> {
                statsPanel = new StatsPanel(tcis);
                setStatsPanel(statsPanel);
                contentPanel.add(statsPanel);
            }
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showCollectionPanel() {
        collectionPanel = new CollectionPanel(tcis, this);
        contentPanel.add(collectionPanel);
    }

    public void updateMoneyDisplay() {
        moneyLabel.setText("$" + String.format("%.2f", tcis.getMoney()));
    }

    public void updateStatsPanel() {
        if (statsPanel != null) {
            statsPanel.refreshStats();
        }
    }

    public void setStatsPanel(StatsPanel panel) {
        this.statsPanel = panel;
    }

    public void refreshAll() {
        updateMoneyDisplay();
        if (collectionPanel != null) collectionPanel.refreshCards();
        if (binderPanel != null) binderPanel.refreshBinders();
        if (deckPanel != null) deckPanel.refreshDecks(tcis, this);
        updateStatsPanel();
        repaint();
    }

    public BinderPanel getBinderPanel() {
        return binderPanel;
    }
}
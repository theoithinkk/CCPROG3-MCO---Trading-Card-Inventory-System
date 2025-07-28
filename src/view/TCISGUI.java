/**
 * A Swing-based main window for the Trading Card Inventory System.
 * This GUI serves as the central hub for navigating between the collection, binders,
 * decks, and statistics panels. It features a fixed-size layout with a styled header,
 * sidebar navigation, and dynamic panel switching based on user interaction.
 * Each panel integrates with the {@link TradingCardInventorySystem} to reflect
 * up-to-date data, and the GUI supports live updates to money and statistics display.
 *
 * @version 2.0  
 * @author Theodore Garcia  
 * @author Ronin Zerna  
 */
package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The main GUI window that manages layout and navigation between the different views
 * (Collection, Binders, Decks, Statistics) of the Trading Card Inventory System.
 */
public class TCISGUI extends JFrame {
    private TradingCardInventorySystem tcis;
    private JLabel moneyLabel;
    private StatsPanel statsPanel;
    private JPanel contentPanel;

    private CollectionPanel collectionPanel;
    private BinderPanel binderPanel;
    private DeckPanel deckPanel;

    /**
     * Constructs the GUI for the Trading Card Inventory System.
     *
     * @param tcis the trading card inventory system instance
     */
    public TCISGUI(TradingCardInventorySystem tcis) {
        this.tcis = tcis;
        setTitle("Trading Card Inventory System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Header setup
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

        // Sidebar setup
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
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btn.setBackground(new Color(50, 90, 170));
                }

                public void mouseExited(MouseEvent evt) {
                    btn.setBackground(new Color(30, 60, 120));
                }
            });

            btn.addActionListener(e -> switchPanel(name));
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        // Content panel setup
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Load initial panel
        showCollectionPanel();

        setVisible(true);
    }

    /**
     * Switches the center panel based on the sidebar selection.
     *
     * @param name the name of the panel to display ("Collection", "Binders", "Decks", or "Statistics")
     */
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

    /**
     * Displays the CollectionPanel in the main content area.
     */
    public void showCollectionPanel() {
        collectionPanel = new CollectionPanel(tcis, this);
        contentPanel.add(collectionPanel);
    }

    /**
     * Updates the money label in the header to reflect current funds.
     */
    public void updateMoneyDisplay() {
        moneyLabel.setText("$" + String.format("%.2f", tcis.getMoney()));
    }

    /**
     * Updates the statistics panel if it is currently active.
     */
    public void updateStatsPanel() {
        if (statsPanel != null) {
            statsPanel.refreshStats();
        }
    }

    /**
     * Sets the reference to the current StatsPanel.
     *
     * @param panel the statistics panel
     */
    public void setStatsPanel(StatsPanel panel) {
        this.statsPanel = panel;
    }

    /**
     * Refreshes all panels and UI elements with the latest data from the system.
     */
    public void refreshAll() {
        updateMoneyDisplay();
        if (collectionPanel != null) collectionPanel.refreshCards();
        if (binderPanel != null) binderPanel.refreshBinders();
        if (deckPanel != null) deckPanel.refreshDecks(tcis, this);
        updateStatsPanel();
        repaint();
    }

    /**
     * Returns the current binder panel, if it has been initialized.
     *
     * @return the active {@link BinderPanel}, or null if not created yet
     */
    public BinderPanel getBinderPanel() {
        return binderPanel;
    }
}

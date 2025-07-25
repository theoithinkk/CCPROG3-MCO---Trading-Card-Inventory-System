package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class TCISGUI extends JFrame {
    private TradingCardInventorySystem tcis;
    private JLabel moneyLabel;
    private JTabbedPane tabbedPane;
    private StatsPanel statsPanel;

    public TCISGUI(TradingCardInventorySystem tcis) {
        this.tcis = tcis;
        setTitle("Trading Card Inventory System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Trading Card Inventory System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moneyLabel = new JLabel("Money: $" + String.format("%.2f", tcis.getMoney()));
        moneyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        moneyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(moneyLabel);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Collection", new CollectionPanel(tcis, this));
        tabbedPane.addTab("Binders", new BinderPanel(tcis, this));
        tabbedPane.addTab("Decks", new DeckPanel(tcis, this));
        statsPanel = new StatsPanel(tcis);
        tabbedPane.addTab("Statistics", statsPanel);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void updateMoneyDisplay() {
        moneyLabel.setText("Money: $" + String.format("%.2f", tcis.getMoney()));
    }
    
    public void updateStatsPanel() {
        statsPanel.refreshStats();
    }
    
}
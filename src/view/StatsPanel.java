package view;

import model.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StatsPanel extends JPanel {
    private JLabel moneyLabel;
    private JLabel totalCardsLabel;
    private JLabel binderCountLabel;
    private JLabel deckCountLabel;
    private TradingCardInventorySystem tcis;

    public StatsPanel(TradingCardInventorySystem tcis) {
        this.tcis = tcis;
        setLayout(new GridLayout(2, 2, 20, 20));
        setBackground(new Color(240, 240, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        moneyLabel = new JLabel();
        totalCardsLabel = new JLabel();
        binderCountLabel = new JLabel();
        deckCountLabel = new JLabel();

        add(createStatCard("Total Money", moneyLabel, new Color(100, 140, 240)));
        add(createStatCard("Total Cards", totalCardsLabel, new Color(120, 200, 255)));
        add(createStatCard("Total Binders", binderCountLabel, new Color(180, 150, 255)));
        add(createStatCard("Total Decks", deckCountLabel, new Color(255, 180, 120)));

        refreshStats();
    }

    private JPanel createStatCard(String title, JLabel statLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedShadowBorder(accentColor, 12),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontManager.NEXA_H.deriveFont(24f));
        titleLabel.setForeground(accentColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        statLabel.setFont(FontManager.NEXA_H.deriveFont(38f));
        statLabel.setForeground(Color.DARK_GRAY);
        statLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statLabel.setVerticalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(statLabel, BorderLayout.CENTER);
        return card;
    }

    public void refreshStats() {
        moneyLabel.setText("$" + String.format("%.2f", tcis.getMoney()));
        totalCardsLabel.setText(String.valueOf(tcis.getTotalCardCount()));
        binderCountLabel.setText(String.valueOf(tcis.getBinders().size()));
        deckCountLabel.setText(String.valueOf(tcis.getDecks().size()));
    }

    /**
     * Custom border for rounded corners and accent shadow
     */
    static class RoundedShadowBorder extends AbstractBorder {
        private final Color accent;
        private final int radius;

        public RoundedShadowBorder(Color accent, int radius) {
            this.accent = accent;
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Shadow
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(x + 2, y + 2, width - 4, height - 4, radius, radius);

            // Border
            g2.setColor(accent);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);

            g2.dispose();
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(radius, radius, radius, radius);
            return insets;
        }
    }
}

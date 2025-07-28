/**
 * A Swing panel that shows live statistics from the Trading Card Inventory System in a clean and interactive layout.
 * This panel displays total money, total cards, number of binders, and number of decks,
 * each inside visually styled cards with rounded corners, accent colors, and hover effects.
 * It updates dynamically using system data and is designed to give users a quick overview
 * of their collection at a glance.

 * @version 2.0  
 * @author Theodore Garcia  
 * @author Ronin Zerna  
 */
package view;

import model.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel displaying real-time statistics from the inventory system in card-style boxes.
 * Includes cards for total money, total cards, total binders, and total decks.
 */
public class StatsPanel extends JPanel {
    private JLabel moneyLabel;
    private JLabel totalCardsLabel;
    private JLabel binderCountLabel;
    private JLabel deckCountLabel;
    private TradingCardInventorySystem tcis;

    /**
     * Constructs a {@code StatsPanel} to show summary stats from the given system.
     *
     * @param tcis the trading card inventory system instance
     */
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

    /**
     * Creates a styled card component containing a statistic.
     *
     * @param title       the title label to display (e.g. "Total Cards")
     * @param statLabel   the JLabel to update dynamically with the value
     * @param accentColor the color accent for the card's theme
     * @return a styled {@code JPanel} representing the stat card
     */
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

    /**
     * Updates the displayed statistics with current data from {@code tcis}.
     */
    public void refreshStats() {
        moneyLabel.setText("$" + String.format("%.2f", tcis.getMoney()));
        totalCardsLabel.setText(String.valueOf(tcis.getTotalCardCount()));
        binderCountLabel.setText(String.valueOf(tcis.getBinders().size()));
        deckCountLabel.setText(String.valueOf(tcis.getDecks().size()));
    }

    /**
     * Custom border for stat cards with rounded corners and subtle shadows.
     */
    static class RoundedShadowBorder extends AbstractBorder {
        private final Color accent;
        private final int radius;

        /**
         * Constructs a {@code RoundedShadowBorder} with the given accent color and corner radius.
         *
         * @param accent the border color
         * @param radius the corner radius
         */
        public RoundedShadowBorder(Color accent, int radius) {
            this.accent = accent;
            this.radius = radius;
        }

        /**
         * Paints the border with rounded corners and a drop shadow.
         */
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

        /**
         * Returns the insets for this border.
         */
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        /**
         * Returns the insets for this border with custom input.
         */
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(radius, radius, radius, radius);
            return insets;
        }
    }
}

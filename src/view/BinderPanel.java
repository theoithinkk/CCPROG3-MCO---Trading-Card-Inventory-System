/**
 * A Swing panel that displays and manages all binder containers visually.
 * Includes a styled button to create new binders and a scrollable grid layout to view them.
 * This panel uses {@link BinderViewHelper} for layout components and {@link BinderController} for actions.
 * 
 * Part of the main GUI in the Trading Card Inventory System.
 *
 * @version 2.0
 * author Theodore Garcia
 * author Ronin Zerna
 */

package view;

import controller.BinderController;
import model.*;
import enums.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel for displaying and interacting with all Binders, styled similarly to CollectionPanel.
 */
public class BinderPanel extends JPanel {

    /** The system's data model. */
    private final TradingCardInventorySystem tcis;

    /** Controller used to handle binder-specific logic. */
    private final BinderController controller;

    /** Reference to the main GUI for refreshing panels and state. */
    private final TCISGUI gui;

    /** Grid layout panel that holds each binder card panel. */
    private final JPanel binderGrid;

    /**
     * Constructs the BinderPanel with references to the system and GUI.
     * Adds a button to create binders and initializes the scrollable layout of binder panels.
     *
     * @param tcis The trading card inventory system.
     * @param gui  The main GUI frame.
     */
    public BinderPanel(TradingCardInventorySystem tcis, TCISGUI gui) {
        this.tcis = tcis;
        this.gui = gui;
        this.controller = new BinderController(tcis, gui);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // Create Binder Button
        JButton createBtn = new JButton("Create Binder");
        createBtn.setFont(FontManager.NEXA_H.deriveFont(22f));
        createBtn.setBackground(new Color(100, 140, 240));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        createBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        createBtn.addActionListener(e -> BinderViewHelper.openCreateBinderDialog(tcis, gui));

        // Top bar setup
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(245, 245, 250));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        topBar.add(createBtn);

        // Grid layout for binder cards
        binderGrid = new JPanel(new GridBagLayout());
        binderGrid.setBackground(new Color(245, 245, 250));
        binderGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Scroll pane for grid
        JScrollPane scrollPane = new JScrollPane(binderGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(topBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshBinders();
    }

    /**
     * Refreshes the binder grid with the latest binder data.
     * Clears the old content, regenerates panels, and repaints the layout.
     */
    public void refreshBinders() {
        binderGrid.removeAll();
        BinderViewHelper helper = new BinderViewHelper();

        int col = 0, row = 0;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        for (Binder binder : tcis.getBinders()) {
            JPanel panel = helper.createContainerPanel(binder, tcis, controller);
            panel.setPreferredSize(new Dimension(220, 320));

            gbc.gridx = col;
            gbc.gridy = row;
            binderGrid.add(panel, gbc);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }

        binderGrid.revalidate();
        binderGrid.repaint();
    }
}

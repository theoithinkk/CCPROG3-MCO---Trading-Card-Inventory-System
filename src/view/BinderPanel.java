package view;

import controller.BinderController;
import model.*;
import enums.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel for displaying and interacting with all Binders, styled like CollectionPanel.
 */
public class BinderPanel extends JPanel {
    private final TradingCardInventorySystem tcis;
    private final BinderController controller;
    private final TCISGUI gui;
    private final JPanel binderGrid;

    public BinderPanel(TradingCardInventorySystem tcis, TCISGUI gui) {
        this.tcis = tcis;
        this.gui = gui;
        this.controller = new BinderController(tcis, gui);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // Styled Create Binder Button
        JButton createBtn = new JButton("Create Binder");
        createBtn.setFont(FontManager.NEXA_H.deriveFont(22f));;
        createBtn.setBackground(new Color(100, 140, 240));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        createBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        createBtn.addActionListener(e -> BinderViewHelper.openCreateBinderDialog(tcis, gui));

        // Top Bar Styling
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(245, 245, 250));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        topBar.add(createBtn);

        // GridBagLayout for cards
        binderGrid = new JPanel(new GridBagLayout());
        binderGrid.setBackground(new Color(245, 245, 250));
        binderGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(binderGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(topBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshBinders();
    }

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

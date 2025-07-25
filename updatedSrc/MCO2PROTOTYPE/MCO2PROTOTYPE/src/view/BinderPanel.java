package view;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;

public class BinderPanel extends JPanel {
    private final BinderController controller;
    private JPanel binderListPanel;

    public BinderPanel(TradingCardInventorySystem tcis, TCISGUI parentGui) {
        this.controller = new BinderController(tcis, parentGui);
        setLayout(new BorderLayout());

        // Create Binder Button
        JButton createBtn = new JButton("Create Binder");
        createBtn.addActionListener(e -> BinderViewHelper.openCreateBinderDialog(tcis, parentGui));
        add(createBtn, BorderLayout.NORTH);

        // Binder List
        binderListPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        add(new JScrollPane(binderListPanel), BorderLayout.CENTER);

        refreshBinders(tcis);
    }

    public void refreshBinders(TradingCardInventorySystem tcis) {
        binderListPanel.removeAll();
        for (Binder binder : tcis.getBinders()) {
            binderListPanel.add(BinderViewHelper.createBinderPanel(binder, tcis, controller));
        }
        binderListPanel.revalidate();
        binderListPanel.repaint();
    }
}
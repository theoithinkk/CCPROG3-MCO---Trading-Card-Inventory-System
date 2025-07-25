package view;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;

public class BinderPanel extends JPanel {
    private JPanel binderListPanel;

    public BinderPanel(TradingCardInventorySystem tcis, TCISGUI parentGui) {
        setLayout(new BorderLayout());

        JButton createBinderBtn = new JButton("Create Binder");
        createBinderBtn.addActionListener(e -> {
            BinderController.openCreateBinderDialog(tcis, parentGui, this);
        });
        add(createBinderBtn, BorderLayout.NORTH);

        binderListPanel = new JPanel();
        binderListPanel.setLayout(new GridLayout(0, 3, 10, 10));

        JScrollPane scrollPane = new JScrollPane(binderListPanel);
        add(scrollPane, BorderLayout.CENTER);

        refreshBinders(tcis, parentGui);
    }

    public void refreshBinders(TradingCardInventorySystem tcis, TCISGUI gui) {
        binderListPanel.removeAll();
        for (Binder binder : tcis.getBinders()) {
            JPanel panel = BinderController.createBinderPanel(binder, tcis, gui);
            binderListPanel.add(panel);
        }
        binderListPanel.revalidate();
        binderListPanel.repaint();
    }
}

package view;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;

public class DeckPanel extends JPanel {
	private JPanel deckListPanel;
	
	public DeckPanel(TradingCardInventorySystem tcis, TCISGUI parentGui) {
	    setLayout(new BorderLayout());

	    JButton createDeckBtn = new JButton("Create Deck");
	    createDeckBtn.addActionListener(e -> {
	        DeckController.openCreateDeckDialog(tcis, parentGui, this);
	    });
	    add(createDeckBtn, BorderLayout.NORTH);

	    deckListPanel = new JPanel(new GridLayout(0, 3, 10, 10));
	    JScrollPane scrollPane = new JScrollPane(deckListPanel);
	    add(scrollPane, BorderLayout.CENTER);

	    refreshDecks(tcis, parentGui);
	}
	
	public void refreshDecks(TradingCardInventorySystem tcis, TCISGUI gui) {
	    deckListPanel.removeAll();
	    for (Deck deck : tcis.getDecks()) {
	        JPanel panel = DeckController.createDeckPanel(deck, tcis, gui);
	        deckListPanel.add(panel);
	    }
	    deckListPanel.revalidate();
	    deckListPanel.repaint();
	}
}
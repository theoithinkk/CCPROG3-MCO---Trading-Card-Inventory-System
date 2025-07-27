package view;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;

public class DeckPanel extends JPanel {
	private JPanel deckListPanel;
	private DeckController controller;
	private DeckViewHelper helper;
	
	public DeckPanel(TradingCardInventorySystem tcis, TCISGUI parentGui) {
		this.controller = new DeckController(tcis, parentGui);
		this.helper = new DeckViewHelper();
	    setLayout(new BorderLayout());

	    JButton createDeckBtn = new JButton("Create Deck");
	    createDeckBtn.addActionListener(e -> {
	        DeckViewHelper.openCreateDeckDialog(tcis, parentGui);
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
	        JPanel panel = helper.createContainerPanel(deck, tcis, controller);
	        deckListPanel.add(panel);
	    }
	    deckListPanel.revalidate();
	    deckListPanel.repaint();
	}
	
}
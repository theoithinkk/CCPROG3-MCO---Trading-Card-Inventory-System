/**
 * Abstract helper class for creating and managing UI components 
 * related to CardContainer objects such as Binders and Decks.
 * Provides shared methods for dialog prompts, confirmations, 
 * styled buttons, and displaying container details.
 *
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */


package view;
import model.*;
import controller.*;
import enums.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


public abstract class CardContainerViewHelper {
	
	  /**
     * Adds a standardized action button to a container panel.
     * 
     * @param panel The parent panel to add the button to
     * @param text The button display text
     * @param listener The action to perform when clicked
     */
	protected static void addActionButton(JPanel panel, String text, ActionListener listener) {
		JButton btn = new JButton(text);
        btn.addActionListener(listener);
        panel.add(btn);
	}
	
	/**
     * Displays an error message dialog with standard formatting.
     * 
     * @param message The error message to display
     */
	public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
	
	/**
     * Displays an informational message dialog with standard formatting.
     * 
     * @param title The dialog window title
     * @param message The information message to display
     */
    public static void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }	
    
    /**
     * Shows a confirmation dialog before deleting a container.
     * 
     * @param container The container to potentially delete
     * @param controller The controller to handle the deletion
     */
    protected static void confirmDelete(CardContainer container, ContainerController controller) {
        int response = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to delete '" + container.getName() + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            controller.handleDeleteContainer(container);
        }
    }
    
    /**
     * Opens a dialog to select a card for removal from the given container.
     * Only cards with a count greater than 0 are shown.
     *
     * @param container The card container to select from
     * @return The selected card to remove, or null if the dialog was cancelled or no cards are available
     */

    protected static Card selectCardToRemove(CardContainer container) {
        List<Card> available = container.getCards().stream()
            .filter(c -> container.getCardCount(c) > 0)
            .toList();
        if (available.isEmpty()) {
            showErrorDialog("No cards available to remove.");
            return null;
        }

        JComboBox<Card> cardCombo = new JComboBox<>(available.toArray(new Card[0]));
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(new JLabel("Select card to remove:"));
        panel.add(cardCombo);

        int result = JOptionPane.showConfirmDialog(
            null,
            panel,
            "Remove Card",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        if (result == JOptionPane.OK_OPTION) {
            return (Card) cardCombo.getSelectedItem();
        }
        return null;
    }
    
    /**
     * Displays detailed contents of a card container in a formatted dialog.
     * 
     * @param container The container whose contents to display
     */
    public static void showContainerDetails(CardContainer container) {
    	StringBuilder sb = new StringBuilder();
        int i = 1;
        // Format each card's information
        for (Card c : container.getCards()) {
            sb.append(String.format("[%d]. Name : %s | Rarity: %s | Variant: %s | Base: $%.2f\n", 
            						i, c.getName(), c.getRarity(), c.getVariant(), c.getBaseValue()));
            i++;
        }
        // Append total value at bottom
        sb.append("\nTotal Value: $").append(container.getTotalValue());
        if (container instanceof Binder binder && binder.isSellable()) {
            sb.append(String.format("\nSell Price: $%.2f", binder.getSellingValue()));
        } else if (container instanceof Deck deck && deck.isSellable()) {
            sb.append(String.format("\nSell Price: $%.2f", deck.getTotalValue()));
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    /**
     * Adds a consistently styled button to the specified panel.
     * This version features custom font, padding, borders, and spacing.
     * 
     * @param panel The panel to add the styled button to
     * @param text The label to display on the button
     * @param listener The action to perform when the button is clicked
     */
    public void addStyledButton(JPanel panel, String text, ActionListener listener) {
	    JButton btn = new JButton(text);
	    btn.addActionListener(listener);
	    btn.setFont(FontManager.NEXA_H.deriveFont(15f));
	    btn.setBackground(new Color(240, 245, 255));
	    btn.setForeground(Color.DARK_GRAY);
	    btn.setFocusPainted(false);
	    btn.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(new Color(160, 160, 200)),
	        BorderFactory.createEmptyBorder(6, 12, 6, 12)
	    ));
	    btn.setAlignmentX(Component.CENTER_ALIGNMENT);
	    btn.setMaximumSize(new Dimension(180, 28));
	    btn.setPreferredSize(new Dimension(180, 28));
	    panel.add(btn);
	    panel.add(Box.createVerticalStrut(8));
	}
    
    /**
     * Abstract method to show card addition dialog (implemented by subclasses).
     * 
     * @param container The container to add cards to
     * @param tcis Reference to the main system
     * @param controller The container's controller
     */
    protected abstract void showAddCardDialog(CardContainer container, TradingCardInventorySystem tcis, ContainerController controller);
    
    /**
     * Abstract method to create container display panel (implemented by subclasses).
     * 
     * @param container The container to display
     * @param tcis Reference to the main system
     * @param controller The container's controller
     * @return Configured JPanel displaying the container
     */
    public abstract JPanel createContainerPanel(CardContainer container, TradingCardInventorySystem tcis, ContainerController controller);
}

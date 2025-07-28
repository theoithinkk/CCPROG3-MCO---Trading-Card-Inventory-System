/**
 * The entry point for launching the Trading Card Inventory System application.
 * Initializes the system backend and loads the main GUI on the Event Dispatch Thread (EDT)
 * 
 * @version 2.0  
 * @author Theodore Garcia  
 * @author Ronin Zerna  
 */
package view;

import controller.*;
import model.*;
import javax.swing.*;
import java.awt.*;

public class TCISMain {

    /**
     * Main method that initializes the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TradingCardInventorySystem tcis = new TradingCardInventorySystem();
            TCISGUI gui = new TCISGUI(tcis);
            gui.setVisible(true);
        });
    }
}

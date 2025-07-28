/**
 * DeckController.java
 *
 * Controller for managing operations related to decks in the Trading Card Inventory System.
 * Currently inherits behavior from {@link ContainerController} without any custom logic.
 * Can be extended in the future to support deck-specific interactions and rules.
 * 
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */

package controller;

import model.*;
import enums.*;
import view.*;
import javax.swing.*;
import java.awt.*;

/**
 * Controller class for managing deck containers.
 * Inherits all default behaviors from {@link ContainerController}.
 */
public class DeckController extends ContainerController {

    /**
     * Constructs a DeckController with access to the main system model and GUI.
     *
     * @param tcis The TradingCardInventorySystem model.
     * @param gui  The GUI reference for updating views.
     */
    public DeckController(TradingCardInventorySystem tcis, TCISGUI gui) {
        super(tcis, gui);
    }
}

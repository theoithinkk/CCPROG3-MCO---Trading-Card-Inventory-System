package view;

import controller.*;
import model.*;
import javax.swing.*;
import java.awt.*;

public class TCISMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TradingCardInventorySystem tcis = new TradingCardInventorySystem();
            TCISGUI gui = new TCISGUI(tcis);
            gui.setVisible(true);
        });
    }
}
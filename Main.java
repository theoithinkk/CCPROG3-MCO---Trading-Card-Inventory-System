package main;
import com.tciss.*;
import java.util.Scanner;
/**
 * Main entry point for the Trading Card Inventory System
 * This class serves as the application launcher
 *
 * @author Theodore Garcia
 * @version 1.0
 */
public class Main {
    /**
     * Main method to launch the Trading Card Inventory System
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Input input = new Input(scanner);
        TradingCardInventorySystem tcis = new TradingCardInventorySystem(input);
        boolean running = true;

        while (running) {
            tcis.displayMainMenu();
            int choice = input.getIntInput("Enter your choice: ");

            if (choice == 1) {
                System.out.println("\n=== Add Card ===");
                Card cardToAdd = tcis.addCard('C');
                if (cardToAdd != null)
                {
                    tcis.getCollection().addCard(cardToAdd);
                    cardToAdd = null;
                    System.out.println("Card added successfully!");
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                }
            } else if (choice == 2 && tcis.hasCardsInCollection()) {
                tcis.manageCardCount();
            } else if (choice == 3 && tcis.hasCardsInCollection()) {
                tcis.getCollection().displayCard();
            } else if (choice == 4 && tcis.hasCardsInCollection()) {
                tcis.getCollection().displayCollection();
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
            } else if (choice == 5) {
               if (tcis.getBinderManager().getBinderCount() == 0) {
                    tcis.getBinderManager().createBinder();
                } else {
                    tcis.getBinderManager().manageBinders(tcis);
                }
            } else if (choice == 6) {
                if (tcis.getDeckManager().getDeckCount() == 0) {
                    tcis.getDeckManager().createDeck();
                } else {
                    tcis.getDeckManager().manageDecks(tcis);
                }
            } else if (choice == 0) {
                running = false;
                System.out.println("Thank you for using Trading Card Inventory System!");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

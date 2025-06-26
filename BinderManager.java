package com.tciss;

public class BinderManager {
    private Input scanner;
    private Binder[] binders;
    private int binderCount;
    private final int MAX_COUNT = 50;
    public BinderManager(Input sc){
        this.scanner = sc;
        this.binders  = new Binder[MAX_COUNT];
        this.binderCount = 0;
    }
    
    public Binder[] getBinders() {
        return this.binders;
    }
    
    public void addBinderCount(int count) {
        this.binderCount += count;
    }
    
    public int getBinderCount() {
        return this.binderCount;
    }
    
    private Binder getBinder(String name) {
        for (int i = 0; i < binderCount; i++) {
            if (binders[i] != null && binders[i].getName().equalsIgnoreCase(name)) {
                return binders[i];
            }
        }
        return null;
    }


    /**
     * Manages existing binders
     */
    public void manageBinders(TradingCardInventorySystem tcis) {
        boolean inBinderMenu = true;

        while (inBinderMenu) {
            System.out.println("\n=== Manage Binders ===");
            System.out.println("1. Create a new Binder");
            System.out.println("2. Delete a Binder");
            System.out.println("3. Add Card to Binder");
            System.out.println("4. Remove Card from Binder");
            System.out.println("5. Trade Card");
            System.out.println("6. View Binder");
            System.out.println("0. Go back to Main Menu");

            int choice = scanner.getIntInput("Enter your choice: ");

            if (choice == 1) {
                createBinder();
            } else if (choice == 2) {
                deleteBinder(tcis);
            } else if (choice == 3) {
                addCardToBinder(tcis);
            } else if (choice == 4) {
                removeCardFromBinder(tcis);
            } else if (choice == 5) {
                tradeCard(tcis);
            } else if (choice == 6) {
                viewBinder(tcis);
            } else if (choice == 0) {
                inBinderMenu = false;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    /**
     * Creates a new binder
     */
    public void createBinder() {

        System.out.println("\n=== Create New Binder ===");
        String name = scanner.ask("Enter binder name: ");

        if (name.isEmpty()) {
            System.out.println("Binder name cannot be empty.");
            return;
        }

        if(getBinder(name) != null) {
        	System.out.println("Binder with this name already exists.");
        	return;
        }

        binders[binderCount] = new Binder(name);
        addBinderCount(1);
        System.out.println("Binder created successfully!");
        scanner.hitEnter();
    }

    /**
     * Deletes a binder and returns cards to collection
     */
    public void deleteBinder(TradingCardInventorySystem tcis) {
        System.out.println("\n=== Delete Binder ===");
        
        if (binderCount == 0) {
            System.out.println("No binders to delete.");
            return;
        }

        displayBinders();

        String name = scanner.ask("Enter binder name to delete: ");

        for (int i = 0; i < binderCount; i++) {
            if (binders[i].getName().equals(name)) {
                Card[] binderCards = binders[i].getAllCards();
                for (int j = 0; j < binderCards.length; j++) {
                    if (binderCards[j] != null) {
                        tcis.getCollection().addCard(binderCards[j]);
                    }
                }

                for (int j = i; j < binderCount - 1; j++) {
                    binders[j] = binders[j + 1];
                }
                binders[binderCount - 1] = null;
                addBinderCount(-1);

                System.out.println("Binder deleted successfully! All cards returned to collection.");
                return;
            }
        }

        System.out.println("Binder not found.");
        scanner.hitEnter();
    }

    /**
     * Displays all binders
     */
    public void displayBinders() {
        System.out.println("Available Binders:");
        for (int i = 0; i < binderCount; i++) {
            System.out.println("- " + binders[i].getName());
        }
        scanner.hitEnter();
    }

    /**
     * Adds a card to a binder
     */
    public void addCardToBinder(TradingCardInventorySystem tcis) {
        System.out.println("\n=== Add Card to Binder ===");

        if (binderCount == 0) {
            System.out.println("No binders available.");
            scanner.hitEnter();
            return;
        }

        if (!tcis.getCollection().hasAvailableCards()) {
            System.out.println("No available cards in collection.");
            scanner.hitEnter();
            return;
        }

        displayBinders();

        String binderName = scanner.ask("Enter binder name: ");

        Binder selectedBinder = getBinder(binderName);

        if (selectedBinder == null) {
            System.out.println("Binder not found.");
            scanner.hitEnter();
            return;
        }

        if (selectedBinder.isFull()) {
            System.out.println("Binder is full. Cannot add more cards.");
            scanner.hitEnter();
            return;
        }

        tcis.getCollection().displayAvailableCards();

        String cardName = scanner.ask("Enter card name to add: ");

        Card card = tcis.getCollection().getAvailableCard(cardName);
        if (card != null) {
            selectedBinder.addCard(card);
            tcis.getCollection().decreaseCardCount(cardName);
            System.out.println("Card added to binder successfully!");
            scanner.hitEnter();
        } else {
            System.out.println("Card not available in collection.");
            scanner.hitEnter();
        }
    }

    /**
     * Removes a card from a binder
     */
    public void removeCardFromBinder(TradingCardInventorySystem tcis) {
        System.out.println("\n=== Remove Card from Binder ===");

        if (binderCount == 0) {
            System.out.println("No binders available.");
            scanner.hitEnter();
            return;
        }

        displayBinders();

        String binderName = scanner.ask("Enter binder name: ");

        Binder selectedBinder = getBinder(binderName);

        if (selectedBinder == null) {
            System.out.println("Binder not found.");
            scanner.hitEnter();
            return;
        }

        if (selectedBinder.isEmpty()) {
            System.out.println("Binder is empty.");
            scanner.hitEnter();
            return;
        }

        selectedBinder.displayCards();

        String cardName = scanner.ask("Enter card name to remove: ");
        
        Card card = selectedBinder.removeCard(cardName);
        if (card != null) {
            tcis.getCollection().addCard(card);
            System.out.println("Card removed from binder and returned to collection!");
            scanner.hitEnter();
        } else {
            System.out.println("Card not found in binder.");
            scanner.hitEnter();
        }
    }

    /**
     * Handles trading cards
     */
    public void tradeCard(TradingCardInventorySystem tcis) {
        System.out.println("\n=== Trade Card ===");

        // Display available binders
        System.out.println("Available binders:");
        for (int i = 0; i < binderCount; i++) {
            if (binders[i] != null) {
                System.out.println("- " + binders[i].getName());
            }
        }

        if (getBinderCount() == 0) {
            System.out.println("No binders available for trading.");
            scanner.hitEnter();
            return;
        }

        // Select binder
        String binderName = scanner.ask("Enter binder name: ");

        Binder selectedBinder = getBinder(binderName);

        if (selectedBinder == null) {
            System.out.println("Binder not found.");
            scanner.hitEnter();
            return;
        }

        if (selectedBinder.isEmpty()) {
            System.out.println("Binder is empty. No cards to trade.");
            scanner.hitEnter();
            return;
        }

        // Select outgoing card
        selectedBinder.displayCards();
        String outgoingCardName = scanner.ask("Enter name of card to trade away: ");
        Card outgoingCard = selectedBinder.getCard(outgoingCardName);

        if (outgoingCard == null) {
            System.out.println("Card not found in binder.");
            scanner.hitEnter();
            return;
        }

        // Create incoming card
        System.out.println("Enter details of the card you want to receive:");
        Card incomingCard;
        do{
            incomingCard = tcis.addCard('T');
        } while (incomingCard == null);

        // Compare values of cards
        double valueDifference = Math.abs(incomingCard.getTotalValue() - outgoingCard.getTotalValue());

        if (valueDifference >= 1.0) {
            System.out.printf("Warning: Value difference is $%.2f\n", valueDifference);
            String response = scanner.askLowerTrimmed("Do you want to proceed with the trade? (y/n): ");

            if (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                System.out.println("Trade cancelled.");
                scanner.hitEnter();
                return;
            }
        }

        // If everything successful, execute trade
        selectedBinder.removeCard(outgoingCardName);
        tcis.getCollection().addCard(incomingCard);
        System.out.println("Trade completed successfully!");
        scanner.hitEnter();
    }

    /**
     * Views contents of a binder
     */
    public void viewBinder(TradingCardInventorySystem tcis) {
        System.out.println("\n=== View Binder ===");

        if (binderCount == 0) {
            System.out.println("No binders available.");
	        scanner.hitEnter();
            return;
        }

        displayBinders();

        String binderName = scanner.ask("Enter binder name: ");
        
        Binder binder = getBinder(binderName);
        
        if(binder == null) {
        	System.out.println("Binder not found.");
        	scanner.hitEnter();
        	return;
        }

        binder.displayCards();
    }
}

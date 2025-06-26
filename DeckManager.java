package com.tciss;

public class DeckManager {
    private Input scanner;
    private Deck[] decks;
    private int deckCount;
    private final int MAX_DECKS = 50;

    public DeckManager(Input sc){
        this.scanner = sc;
        this.decks = new Deck[MAX_DECKS];
        this.deckCount = 0;
    }
    
    public Deck[] getDecks() {
        return this.decks;
    }
    
    private Deck getDeck(String name) {
        for (int i = 0; i < deckCount; i++) {
            if (decks[i] != null && decks[i].getName().equalsIgnoreCase(name)) {
                return decks[i];
            }
        }
        return null;
    }

    public int getDeckCount() {
        return this.deckCount;
    }
   
    
    public void addDeckCount(int count) {
        this.deckCount += count;
    }
    
    /*
     * Manages existing decks
     */
    public void manageDecks(TradingCardInventorySystem tcis) {
        boolean inDeckMenu = true;

        while (inDeckMenu) {
            System.out.println("\n=== Manage Decks ===");
            System.out.println("1. Create a new Deck");
            System.out.println("2. Delete a Deck");
            System.out.println("3. Add Card to Deck");
            System.out.println("4. Remove Card from Deck");
            System.out.println("5. View Deck");
            System.out.println("0. Go back to Main Menu");

            int choice = scanner.getIntInput("Enter your choice: ");

            if (choice == 1) {
                createDeck();
            } else if (choice == 2) {
                deleteDeck(tcis);
            } else if (choice == 3) {
                addCardToDeck(tcis);
            } else if (choice == 4) {
                removeCardFromDeck(tcis);
            } else if (choice == 5) {
                viewDeck(tcis);
            } else if (choice == 0) {
                inDeckMenu = false;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Creates a new deck
     */
    public void createDeck() {
        System.out.println("\n=== Create New Deck ===");

        String name = scanner.ask("Enter deck name");

        if (name.isEmpty()) {
            System.out.println("Deck name cannot be empty.");
            scanner.hitEnter();
            return;
        }

        // Check if deck name already exists
        for (int i = 0; i < deckCount; i++) {
            if (decks[i].getName().equals(name)) {
                System.out.println("Deck with this name already exists.");
                scanner.hitEnter();
                return;
            }
        }

        decks[deckCount] = new Deck(name);
        addDeckCount(1);
        System.out.println("Deck created successfully!");
    }

    /**
     * Deletes a deck and returns cards to collection
     */
    private void deleteDeck(TradingCardInventorySystem tcis) {
        System.out.println("\n=== Delete Deck ===");
 
        if (deckCount == 0) {
            System.out.println("No decks to delete.");
            scanner.hitEnter();
            return;
        }

        displayDecks();

        String name = scanner.ask("Enter deck name to delete: ");

        for (int i = 0; i < deckCount; i++) {
            if (decks[i].getName().equals(name)) {
                Card[] deckCards = decks[i].getAllCards();
                for (int j = 0; j < deckCards.length; j++) {
                    if (deckCards[j] != null) {
                        tcis.getCollection().addCard(deckCards[j]);
                    }
                }

                for (int j = i; j < deckCount - 1; j++) {
                    decks[j] = decks[j + 1];
                }
                decks[deckCount - 1] = null;
                addDeckCount(-1);

                System.out.println("Deck deleted successfully! All cards returned to collection.");
                scanner.hitEnter();
                return;
            }
        }

        System.out.println("Deck not found.");
        scanner.hitEnter();
    }

    /**
     * Displays all decks
     */
    private void displayDecks() {
        System.out.println("Available Decks:");
        for (int i = 0; i < deckCount; i++) {
            System.out.println("- " + decks[i].getName());
        }
    }

    /**
     * Adds a card to a deck
     */
    public void addCardToDeck(TradingCardInventorySystem tcis) {
        System.out.println("\n=== Add Card to Deck ===");

        if (deckCount == 0) {
            System.out.println("No decks available.");
            scanner.hitEnter();
            return;
        }

        if (!tcis.getCollection().hasAvailableCards()) {
            System.out.println("No available cards in collection.");
            scanner.hitEnter();
            return;
        }

        displayDecks();

        String deckName = scanner.ask("Enter deck name: ");

        Deck selectedDeck = getDeck(deckName);

        if (selectedDeck == null) {
            System.out.println("Deck not found.");
            scanner.hitEnter();
            return;
        }

        if (selectedDeck.isFull()) {
            System.out.println("Deck is full. Cannot add more cards.");
            scanner.hitEnter();
            return;
        }

        tcis.getCollection().displayAvailableCards();

        String cardName = scanner.ask("Enter card name to add: ");

        Card card = tcis.getCollection().getAvailableCard(cardName);
        if (card != null) {
            if (selectedDeck.getCard(cardName) != null) {
                System.out.println("Card with this name already exists in deck. Cannot add duplicate.");
                scanner.hitEnter();
                return;
            }

            selectedDeck.addCard(card);
            tcis.getCollection().decreaseCardCount(cardName);
            System.out.println("Card added to deck successfully!");
            scanner.hitEnter();
        } else {
            System.out.println("Card not available in collection.");
            scanner.hitEnter();
        }
    }

    /**
     * Removes a card from a deck
     */
    public void removeCardFromDeck(TradingCardInventorySystem tcis) {
        System.out.println("\n=== Remove Card from Deck ===");

        if (deckCount == 0) {
            System.out.println("No decks available.");
            scanner.hitEnter();
            return;
        }

        displayDecks();

        String deckName = scanner.ask("Enter deck name: ");

        Deck selectedDeck = getDeck(deckName);

        if (selectedDeck == null) {
            System.out.println("Deck not found.");
            scanner.hitEnter();
            return;
        }

        if (selectedDeck.isEmpty()) {
            System.out.println("Deck is empty.");
            scanner.hitEnter();
            return;
        }

        selectedDeck.displayCards();

        String cardName = scanner.ask("Enter card name to remove: ");

        Card card = selectedDeck.removeCard(cardName);
        if (card != null) {
            tcis.getCollection().addCard(card);
            System.out.println("Card removed from deck and returned to collection!");
            scanner.hitEnter();
        } else {
            System.out.println("Card not found in deck.");
            scanner.hitEnter();
        }
    }

    /**
     * Views contents of a deck with option to view card details
     */
    public void viewDeck(TradingCardInventorySystem tcis) {
        System.out.println("\n=== View Deck ===");

        if (deckCount == 0) {
            System.out.println("No decks available.");
            scanner.hitEnter();
            return;
        }

        displayDecks();

        String deckName = scanner.ask("Enter deck name: ");

        Deck selectedDeck = getDeck(deckName);

        if (selectedDeck == null) {
            System.out.println("Deck not found.");
            scanner.hitEnter();
            return;
        }

        selectedDeck.displayCards();

        if (!selectedDeck.isEmpty()) {
            String response = scanner.askLowerTrimmed("Do you want to view details of a specific card? (y/n): ");

            if (response.equals("y") || response.equals("yes")) {
            	String cardName = scanner.ask("Enter card name: ");

                Card card = selectedDeck.getCard(cardName);
                if (card != null) {
                    System.out.println(card.getDetailedInfo());
                } else {
                    System.out.println("Card not found in deck.");
                }
            }
        }
    }

}

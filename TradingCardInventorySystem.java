package com.tciss;


/**
 * Main class for the Trading Card Inventory System (TCIS)
 * Handles the command line interface and main program flow
 *
 * @author Theodore Garcia
 * @version 1.0
 */
public class TradingCardInventorySystem {
    private Collection collection;
    private DeckManager deckManager;
    private BinderManager binderManager;
    private Input scanner;

    /**
     * Constructor for TradingCardInventorySystem
     * Initializes the collection, arrays for binders and decks, and scanner
     */
    public TradingCardInventorySystem(Input input) {
        this.scanner = input;
        this.collection = new Collection(input);
        this.binderManager = new BinderManager(input);
        this.deckManager = new DeckManager(input);
    }

    /**
     * Displays the main menu with available options
     */
    public void displayMainMenu() {
        System.out.println("\n=== Trading Card Inventory System ===");
        System.out.println("1. Add a Card");

        if (collection.hasCards()) {
            System.out.println("2. Increase/Decrease Card Count");
            System.out.println("3. Display a Card");
            System.out.println("4. Display Collection");
        }

        if (binderManager.getBinderCount() == 0) {
            System.out.println("5. Create a new Binder");
        } else {
            System.out.println("5. Manage Binders");
        }

        if (deckManager.getDeckCount() == 0) {
            System.out.println("6. Create a new Deck");
        } else {
            System.out.println("6. Manage Decks");
        }

        System.out.println("0. Exit");
    }

    public boolean hasCardsInCollection() {
        return collection.hasCards();
    }
    /**
     * Adds a new card to the collection via manual input
     */
    public Card addCard(char ver) {
        String name = scanner.ask("Enter card name: ");

        if (name.isEmpty()) {
            System.out.println("Card name cannot be empty.");
            return null;
        }

        // for normal adding of cards
        if (collection.cardExists(name) && ver == 'C') {
            System.out.println("Card '" + name + "' already exists in the collection.");
            String response = scanner.askLowerTrimmed("Do you want to increase the count instead? (y/n): ");

            if (response.equals("y") || response.equals("yes")) {
                collection.increaseCardCount(name);
                System.out.println("Card count increased successfully!");
                scanner.hitEnter();
            }
            return null;
            // for trade adding of cards
        } else if (collection.cardExists(name) && ver == 'T') {
            System.out.println("Card '" + name + "' already exists in the collection.");
            return collection.getAvailableCard(name);
        }

        String rarity;
        System.out.println("Select rarity:");
        System.out.println("1. Common");
        System.out.println("2. Uncommon");
        System.out.println("3. Rare");
        System.out.println("4. Legendary");

        int choiceRar = scanner.getIntInput("Enter choice (1-4): ");

        if (choiceRar == 1) {
            rarity = Card.Rarity.COMMON;
        } else if (choiceRar == 2) {
            rarity = Card.Rarity.UNCOMMON;
        } else if (choiceRar == 3) {
            rarity = Card.Rarity.RARE;
        } else if (choiceRar == 4) {
            rarity = Card.Rarity.LEGENDARY;
        } else {
            rarity = null;
        }

        if (rarity == null) {
            System.out.println("Invalid rarity. Card not added.");
            return null;
        }

        String variant;
        if (rarity.equals(Card.Rarity.COMMON) || rarity.equals(Card.Rarity.UNCOMMON)) {
            variant = Card.Variant.NORMAL;
        } else {
            System.out.println("Select variant:");
            System.out.println("1. Normal");
            System.out.println("2. Extended-art");
            System.out.println("3. Full-art");
            System.out.println("4. Alt-art");

            int choiceVar = scanner.getIntInput("Enter choice (1-4): ");

            if (choiceVar == 1) {
                variant = Card.Variant.NORMAL;
            } else if (choiceVar == 2) {
                variant = Card.Variant.EXTENDED_ART;
            } else if (choiceVar == 3) {
                variant = Card.Variant.FULL_ART;
            } else if (choiceVar == 4) {
                variant = Card.Variant.ALT_ART;
            } else {
                variant = null;
            }

            if (variant == null) {
                System.out.println("Invalid variant. Card not added.");
                return null;
            }
        }

        double value = scanner.askDouble("Enter card value: $");

        if (value < 0) {
            System.out.println("Card value cannot be negative. Card not added.");
            return null;
        }

        Card card = new Card(name, rarity, variant, value);

        return card;
    }

    /**
     * Manages increasing or decreasing card count
     */
    public void manageCardCount() {
        System.out.println("\n=== Manage Card Count ===");

        collection.displayCollection();

        String name = scanner.ask("Enter card name: ");

        if (!collection.cardExists(name)) {
            System.out.println("Card not found in collection.");
            return;
        }

        System.out.println("1. Increase count");
        System.out.println("2. Decrease count");
        int choice = scanner.getIntInput("Enter choice (1-2): ");

        if (choice == 1) {
            collection.increaseCardCount(name);
            System.out.println("Card count increased successfully!");
            scanner.hitEnter();
        } else if (choice == 2) {
            collection.decreaseCardCount(name);
            System.out.println("Card count decreased successfully!");
            scanner.hitEnter();
        } else {
            System.out.println("Invalid choice.");
            scanner.hitEnter();
        }

    }
    
    // Getters
    public Collection getCollection() {
        return this.collection;
    }
    
    public DeckManager getDeckManager() {
    	return this.deckManager;
    }
    
    public BinderManager getBinderManager() {
    	return this.binderManager;
    }

}

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
        this.binderManager = new BinderManager(50);
        
        this.deckManager = new DeckManager(50);
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

        if (binderManager.getContainerCount() == 0) {
            System.out.println("5. Create a new Binder");
        } else {
            System.out.println("5. Manage Binders");
        }

        if (deckManager.getContainerCount() == 0) {
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
        boolean exists = collection.cardExists(name);
        if (exists && ver == 'C') {
            System.out.println("Card '" + name + "' already exists in the collection.");
            String response = scanner.askLowerTrimmed("Do you want to increase the count instead? (y/n): ");

            if (response.equals("y") || response.equals("yes")) {
                collection.increaseCardCount(name);
                System.out.println("Card count increased successfully!");
                scanner.hitEnter();
            }
            return null;
            // for trade adding of cards	
        } else if (exists && ver == 'T') {
            System.out.println("Card '" + name + "' already exists in the collection.");
            return collection.getAvailableCard(name);
        }

       return createCard(name);
    }
    
    private Card createCard(String name) {
        String rarity = promptRarity();
        if (rarity == null) {
            System.out.println("Invalid rarity. Card not added.");
            return null;
        }

        String variant;
        if (rarity.equals(Card.Rarity.COMMON) || rarity.equals(Card.Rarity.UNCOMMON)) {
            variant = Card.Variant.NORMAL;
        } else {
            variant = promptVariant();
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

        return new Card(name, rarity, variant, value);
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
    
    private String promptRarity() {
    	 System.out.println("Select rarity:");
         System.out.println("1. Common");
         System.out.println("2. Uncommon");
         System.out.println("3. Rare");
         System.out.println("4. Legendary");
         int choiceRar = scanner.getIntInput("Enter choice (1-4): ");
         return switch(choiceRar) {
         	case 1 -> Card.Variant.NORMAL;
         	case 2 ->  Card.Rarity.UNCOMMON;
         	case 3 -> Card.Rarity.RARE;
         	case 4 -> Card.Rarity.LEGENDARY;
         	default -> null;
         };
    }
    
    private String promptVariant() {
    	System.out.println("Select variant:");
        System.out.println("1. Normal");
        System.out.println("2. Extended-art");
        System.out.println("3. Full-art");
        System.out.println("4. Alt-art");
        int choiceVar = scanner.getIntInput("Enter choice (1-4): ");
        return switch(choiceVar) {
        	case 1 -> Card.Variant.NORMAL;
        	case 2 -> Card.Variant.EXTENDED_ART;
        	case 3 -> Card.Variant.ALT_ART;
        	case 4 -> Card.Variant.FULL_ART;
        	default -> null;
        };
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

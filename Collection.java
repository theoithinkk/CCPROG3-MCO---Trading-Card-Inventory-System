package com.tciss;



/**
 * Represents a collection of trading cards
 * Manages cards and their counts
 *
 * @author Student Names
 * @version 1.0
 */

public class Collection {
    private Card[] cards;
    private int[] counts;
    private int cardCount;
    private Input scanner;

    /**
     * Constructor for Collection
     * Initializes arrays with capacity for 1000 unique cards
     */
    public Collection(Input scanner) {
        this.cards = new Card[200]; // Maximum 1000 unique cards
        this.counts = new int[200];
        this.cardCount = 0;
        this.scanner = scanner;
    }

    /**
     * Adds a card to the collection or increases its count if it already exists
     *
     * @param card the card to add
     */
    public void addCard(Card card) {
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].equals(card)) {
                counts[i]++;
                return;
            }
        }

        cards[cardCount] = card;
        counts[cardCount] = 1;
        cardCount++;
    }

    /**
     * Checks if a card exists in the collection by name
     *
     * @param name the name of the card to check
     * @return true if card exists, false otherwise
     */
    public boolean cardExists(String name) {
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an available card (count > 0) by name
     *
     * @param name the name of the card
     * @return the card if available, null otherwise
     */
    public Card getAvailableCard(String name) {
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getName().equals(name) && counts[i] > 0) {
                return cards[i];
            }
        }
        return null;
    }

    /**
     * Increases the count of a card by name
     *
     * @param name the name of the card
     */
    public void increaseCardCount(String name) {
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getName().equals(name)) {
                counts[i]++;
                return;
            }
        }
    }

    /**
     * Decreases the count of a card by name
     *
     * @param name the name of the card
     */
    public void decreaseCardCount(String name) {
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getName().equals(name) && counts[i] > 0) {
                counts[i]--;
                return;
            }
        }
    }

    /**
     * Checks if the collection has any cards
     *
     * @return true if collection has cards, false otherwise
     */
    public boolean hasCards() {
        return cardCount > 0;
    }

    /**
     * Checks if the collection has any available cards (count > 0)
     *
     * @return true if collection has available cards, false otherwise
     */
    public boolean hasAvailableCards() {
        for (int i = 0; i < cardCount; i++) {
            if (counts[i] > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Displays the entire collection sorted alphabetically by card name
     */
    public void displayCollection() {
        if (cardCount == 0) {
            System.out.println("Collection is empty.");
            return;
        }

        String[] names = new String[cardCount];
        int[] sortedCounts = new int[cardCount];

        for (int i = 0; i < cardCount; i++) {
            names[i] = cards[i].getName();
            sortedCounts[i] = counts[i];
        }

        // Bubble sort in alphabetical order
        for (int i = 0; i < cardCount - 1; i++) {
            for (int j = 0; j < cardCount - 1 - i; j++) {
                if (names[j].compareToIgnoreCase(names[j + 1]) > 0) {
                    String tempName = names[j];
                    names[j] = names[j + 1];
                    names[j + 1] = tempName;

                    int tempCount = sortedCounts[j];
                    sortedCounts[j] = sortedCounts[j + 1];
                    sortedCounts[j + 1] = tempCount;
                }
            }
        }

        System.out.println("Cards in collection:");
        for (int i = 0; i < cardCount; i++) {
            System.out.println("- " + names[i] + " (Count: " + sortedCounts[i] + ")");
        }

    }

    /**
     * Displays only available cards (count > 0) sorted alphabetically
     */
    public void displayAvailableCards() {
        boolean hasAvailable = false;

        // Create arrays for available cards
        String[] availableNames = new String[cardCount];
        int[] availableCounts = new int[cardCount];
        int availableCount = 0;

        // Collect available cards
        for (int i = 0; i < cardCount; i++) {
            if (counts[i] >= 0) {
                availableNames[availableCount] = cards[i].getName();
                availableCounts[availableCount] = counts[i];
                availableCount++;
                hasAvailable = true;
            }
        }

        if (!hasAvailable) {
            System.out.println("No available cards in collection.");
            return;
        }

        // Sort available cards
        for (int i = 0; i < availableCount - 1; i++) {
            for (int j = 0; j < availableCount - 1 - i; j++) {
                if (availableNames[j].compareToIgnoreCase(availableNames[j + 1]) > 0) {
                    String tempName = availableNames[j];
                    availableNames[j] = availableNames[j + 1];
                    availableNames[j + 1] = tempName;

                    int tempCount = availableCounts[j];
                    availableCounts[j] = availableCounts[j + 1];
                    availableCounts[j + 1] = tempCount;
                }
            }
        }

        System.out.println("Available cards in collection:");
        for (int i = 0; i < availableCount; i++) {
            System.out.println("- " + availableNames[i] + " (Available: " + availableCounts[i] + ")");
        }
    }

    /**
     * Displays detailed information about a specific card in the collection
     */
    public void displayCard() {
        System.out.println("\n=== Display Card ===");
        displayAvailableCards();
        String name = scanner.ask("Enter card name: ");

        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getName().equalsIgnoreCase(name)) {
                System.out.println(cards[i].getDetailedInfo());
                System.out.println("Copies in collection: " + counts[i]);
                scanner.hitEnter();
                return;
            }
        }

        System.out.println("Card not found in collection.");
        scanner.hitEnter();
    }
}

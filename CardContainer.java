package com.tciss;

import java.util.Arrays;

public abstract class CardContainer{
	protected String name;
	protected Card[] cards;
	protected int cardCount;
	protected final int MAX_CAPACITY;
	
	public CardContainer(String name, int maxCapacity) {
		this.name = name;
		this.MAX_CAPACITY = maxCapacity;
		this.cards = new Card[MAX_CAPACITY];
		this.cardCount = 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected abstract String getContainerTypeName();
	
	/**
     * Gets the current number of cards 
     *
     * @return the number of cards
     */
    public int getCardCount() {
        return cardCount;
    }
    
    public boolean addCard(Card card) {
    	if(cardCount >= MAX_CAPACITY) {
    		return false;
    	}
    	cards[cardCount++] = card;
    	return true;
    }
    
    public Card removeCard(String cardName) {
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getName().equalsIgnoreCase(cardName)) {
                System.arraycopy(cards, i + 1, cards, i, cardCount - i - 1);
                cards[--cardCount] = null;
                return cards[i];
            }
        }
        return null;
    }
    
    public void removeCard(Card car) {
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].equals(car)) {
                System.arraycopy(cards, i + 1, cards, i, cardCount - i - 1);
                cards[--cardCount] = null;
                return;
            }
        }
        System.out.println("Card not in " + getContainerTypeName());
    } 
    
    /**
     * Gets all cards in the deck
    *
    * @return array of all cards in the deck
    */
   public Card[] getAllCards() {
	   return Arrays.copyOf(cards, cardCount);
   }
   
   
   public void displayCards() {

       String[] names = new String[cardCount];
       for (int i = 0; i < cardCount; i++) {
           names[i] = cards[i].getName();
       }
       Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);

       System.out.println("Cards in " + name + " (" + cardCount + "/" + MAX_CAPACITY + "):");
       for (String n : names) {
           System.out.println("- " + n);
       }
   }
   
   /**
    * Checks if the binder is full
    *
    * @return true if binder is full, false otherwise
    */
   public boolean isFull() {
       return cardCount >= MAX_CAPACITY;
   }

   /**
    * Checks if the binder is empty
    *
    * @return true if binder is empty, false otherwise
    */
   public boolean isEmpty() {
       return cardCount == 0;
   }
   
   /**
    * Gets a card from the deck by name
    *
    * @param name the name of the card
    * @return the card if found, null otherwise
    */
   public Card getCard(String name) {
       for (int i = 0; i < cardCount; i++) {
           if (cards[i].getName().equals(name)) {
               return cards[i];
           }
       }
       return null;
   }
   
}

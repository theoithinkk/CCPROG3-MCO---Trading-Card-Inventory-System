package com.tciss;
/**
 * Represents a deck that can hold up to 10 unique cards
 * No duplicate card names are allowed in a deck
 *
 * @author Student Names
 * @version 1.0
 */
public class Deck extends CardContainer{
    private static final int MAX_CAPACITY = 10;

    /**
     * Constructor for Deck
     *
     * @param name the name of the deck
     */
    public Deck(String name) {
    	super(name, MAX_CAPACITY);
    }
    protected String getContainerTypeName() {
        return "Deck";
    }
}

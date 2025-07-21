package com.tciss;

/**
 * Represents a binder that can hold up to 20 cards for trading
 *
 * @author Student Names
 * @version 1.0
 */
public class Binder extends CardContainer{
    private static final int MAX_CAPACITY = 20;

    /**
     * Constructor for Binder
     *
     * @param name the name of the binder
     */
    public Binder(String name) {
        super(name,MAX_CAPACITY);
    }
    protected String getContainerTypeName() {
        return "Binder";
    }
}

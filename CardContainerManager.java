package com.tciss;

public abstract class CardContainerManager <T extends CardContainer>{ // CardContainer is either Deck or Binder
	protected T[] containers;
    protected int containerCount = 0;
    protected final int MAX_CONTAINERS;

    public CardContainerManager(int maxContainers) {
        this.MAX_CONTAINERS = maxContainers;
        this.containers = createContainerArray();
    }
	
	protected abstract T createNewContainer(String name);
	protected abstract T[] createContainerArray();
	protected abstract String getContainerTypeName();
	
	private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
	
	public int getContainerCount() {
		return this.containerCount;
	}
	
	public void addToCollection(String name) { // adds binders/decks to their respective inventory
        if (containerCount >= MAX_CONTAINERS) {
            System.out.println("Maximum number of " + getContainerTypeName() + "s reached.");
            return;
        }
        
        if(getContainerByName(name) != null) {
        	System.out.println("Duplicate " + getContainerTypeName() + "s are not allowed");
        	return;
        }
        
        if(name.isEmpty()) {
        	System.out.println("Name cannot be blank."); 
        	return;
        }

        containers[containerCount++] = createNewContainer(name);
        System.out.println(capitalize(getContainerTypeName()) + " added successfully!");
    }
	
	protected T getContainerByName(String name) {  // retrieves a binder/deck using their name
        for (int i = 0; i < containerCount; i++) {
            if (containers[i].getName().equalsIgnoreCase(name)) {
                return containers[i];
            }
        }
        return null;
    }
	
	protected void removeFromCollection(String name) {  // removes a binder/deck from their inventory
        for (int i = 0; i < containerCount; i++) {
            if (containers[i].getName().equalsIgnoreCase(name)) {
                // Shift left
                System.arraycopy(containers, i + 1, containers, i, containerCount - i - 1);
                containers[--containerCount] = null;
                System.out.println(capitalize(getContainerTypeName()) + " deleted successfully.");
                return;
            }
        }
        System.out.println(capitalize(getContainerTypeName()) + " not found.");
    }
	
	protected void displayContainers() {  // displays all binders/decks
		if(containerCount == 0) {
			System.out.println("No " + getContainerTypeName() + "s available.");
			return;
		}
		System.out.println("=== " + capitalize(getContainerTypeName()) + " List ===");
        for (int i = 0; i < containerCount; i++) {
            System.out.println((i + 1) + ". " + containers[i].getName());
        }
	}
	
}

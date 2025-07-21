package com.tciss;

public abstract class ContainerMenu<T extends CardContainer>{ // CardContainer is either Deck or Binder
	protected CardContainerManager<T> manager; // Either binderManager or DeckManager
	protected TradingCardInventorySystem tcis;
	protected Input sc;

	public ContainerMenu(CardContainerManager<T> manager, TradingCardInventorySystem tcis, Input sc) {
		this.manager = manager;
		this.tcis = tcis;
		this.sc = sc;
	}
	
	public abstract void showMenu();  // each implements their own menu
	
	public void addNewContainer() {  // adds new binders/decks
		manager.displayContainers();  
		System.out.println("\n=== Create New " + getMenuTitle() + " ===");
        String name = sc.ask("Enter name of new "+ getContainerTypeName()+ ":");
        manager.addToCollection(name);
        sc.hitEnter();
	}
	
	protected void removeContainer() { // removes binders/decks
		manager.displayContainers();
		System.out.println("\n=== Delete " + getMenuTitle() + " ===");
		 manager.displayContainers();
	     String name = sc.ask("Enter name of " + getContainerTypeName()+ " to delete: ");
	     manager.removeFromCollection(name);
	     sc.hitEnter();
	}
	
	protected void addCard() {  // adds cards to either binders or decks
		System.out.println("\n=== Add Card to " + getMenuTitle() + " ===");
		if(manager.getContainerCount() == 0) {
			System.out.println("No " + getContainerTypeName() + " available.");
            sc.hitEnter();
            return;
		}
		
		manager.displayContainers();
        String name = sc.ask("Enter name of " + getContainerTypeName() + ": ");
        T container = manager.getContainerByName(name);
        if (container == null) {
            System.out.println(getContainerTypeName() + " not found.");
            return;
        }
        
        tcis.getCollection().displayCollection();
        String cardName = sc.ask("Enter name of card to add: ");
        
        if (!tcis.getCollection().decreaseCardCount(cardName)) {
            System.out.println("Card not found in collection.");
            sc.hitEnter();
        } else {
        	Card card = tcis.getCollection().getAvailableCard(cardName);
            container.addCard(card);
            System.out.println("Card added to " + getContainerTypeName() + ".");
            sc.hitEnter();
        }
        
	}
	
	protected void removeCard() {  // removes cards from binders/decks
		System.out.println("\n=== Remove Card from " + getMenuTitle() + " ===");
		if(manager.getContainerCount() == 0) {
			System.out.println("No " + getContainerTypeName() + " available.");
            sc.hitEnter();
            return;
		}
		
		manager.displayContainers();
		
		String name = sc.ask("Enter name of " + getContainerTypeName() + ": ");
        T container = manager.getContainerByName(name);
        if (container == null) {
            System.out.println(getContainerTypeName() + " not found.");
            return;
        }
        if (container.getCardCount() == 0) {
            System.out.println(getContainerTypeName() + " \"" + name + "\" is empty. No cards to remove.");
            sc.hitEnter();
            return;
        }
      
        container.displayCards();
        
        String cardName = sc.ask("Enter name of card to remove: ");
        Card removed = container.removeCard(cardName);
         
        if (removed != null) {
        	tcis.getCollection().addCard(removed);
            System.out.println("Card removed from " + getContainerTypeName() + " and returned to collection!");
            sc.hitEnter();
        } else {
            System.out.println("Card not found in "+ getContainerTypeName() + ".");
            sc.hitEnter();
        }
	}
	
	protected void viewSpecificContainer() {   // Views specific decks/binders
		System.out.println("\n=== View " + getMenuTitle() + " ===");
		if(manager.getContainerCount() == 0) {
			System.out.println("No " + getContainerTypeName() + " available.");
            sc.hitEnter();
            return;
		}
		
		manager.displayContainers();
		
		String name = sc.ask("Enter " + getContainerTypeName() + " name:");
		T container = manager.getContainerByName(name);
		
		if(container == null) {
        	System.out.println(getContainerTypeName() +" not found.");
        	sc.hitEnter();
        	return;
        
		}
		if (container.getCardCount() == 0) {
            System.out.println(getContainerTypeName() + " \"" + name + "\" is empty. No cards to show.");
            sc.hitEnter();
            return;
        }
		container.displayCards();
		
		
	}
	
	protected abstract String getMenuTitle();
    protected abstract String getContainerTypeName();
}

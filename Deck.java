package com.tciss;

public class DeckMenu extends ContainerMenu<Deck>{

	public DeckMenu(DeckManager DeckManager, TradingCardInventorySystem tcis , Input scanner) {
		super(DeckManager, tcis, scanner); // calls the superclass's (ContainerMenu) constructor along with its methods 
	}
	
	 public void showMenu() {
	        int choice;
	       

	        do{
	            System.out.println("\n=== Manage Decks ===");
	            System.out.println("[1] Create a new Deck");
	            System.out.println("[2] Delete a Deck");
	            System.out.println("[3] Add Card to Deck");
	            System.out.println("[4] Remove Card from Deck");
	            System.out.println("[5] View Deck");
	            System.out.println("[0] Go back to Main Menu");

	            choice = sc.getIntInput("Enter your choice: ");

	            switch (choice) {
	                case 1 -> addNewContainer();
	                case 2 -> removeContainer();
	                case 3 -> addCard();
	                case 4 -> removeCard();
	                case 5 -> viewSpecificContainer();
	                case 0 -> System.out.println("Returning to main menu...");
	                default -> System.out.println("Invalid choice. Please try again.");
	            }
	        }while(choice != 0);
	 }

	 
	 protected String getMenuTitle() {
		return "Deck";
	 }

	 protected String getContainerTypeName() {
		 return "Deck";
	 }
	 
	 @Override
	 protected void viewSpecificContainer() {
	     System.out.println("\n=== View " + getMenuTitle() + " ===");
	     if (manager.getContainerCount() == 0) {
	         System.out.println("No " + getContainerTypeName() + " available.");
	         sc.hitEnter();
	         return;
	     }

	     manager.displayContainers();
	     String name = sc.ask("Enter " + getContainerTypeName() + " name:");
	     Deck container = manager.getContainerByName(name);

	     if (container == null) {
	         System.out.println(getContainerTypeName() + " not found.");
	         sc.hitEnter();
	         return;
	     }

	     if (container.getCardCount() == 0) {
	         System.out.println(getContainerTypeName() + " \"" + name + "\" is empty. No cards to show.");
	         sc.hitEnter();
	     } else {
	         container.displayCards();
	     }

	     // Additional functionality
	     String response = sc.askLowerTrimmed("Do you want to view details of a specific card? (y/n): ");
	     if (response.equals("y") || response.equals("yes")) {
	         String cardName = sc.ask("Enter card name: ");
	         Card card = container.getCard(cardName);
	         if (card != null) {
	             System.out.println(card.getDetailedInfo());
	         } else {
	             System.out.println("Card not found in " + getContainerTypeName() + ".");
	         }
	     }
	 }

	 
}

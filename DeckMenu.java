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
	 
	 
}

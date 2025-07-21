package com.tciss;
public class BinderMenu extends ContainerMenu<Binder>{

    public BinderMenu(BinderManager binderManager, TradingCardInventorySystem tcis , Input scanner) {
        super(binderManager, tcis, scanner); // calls the superclass's (ContainerMenu) constructor along with its methods 
    }
    
    protected String getMenuTitle() {
        return "Binder";
    }

    
    protected String getContainerTypeName() {
        return "Binder";
    }


    public void showMenu() {
        int choice;
        do {
            System.out.println("\n=== Binder Menu ===");
            System.out.println("[1] Add New Binder");
            System.out.println("[2] Delete Binder");
            System.out.println("[3] Add Card to Binder");
            System.out.println("[4] Remove Card from Binder");
            System.out.println("[5] View Binder");
            System.out.println("[6] Trade Card");
            System.out.println("0. Return to Main Menu");

            choice = sc.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addNewContainer();
                case 2 -> removeContainer();
                case 3 -> addCard();
                case 4 -> removeCard();
                case 5 -> viewSpecificContainer();
                case 6 -> tradeCard();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }
    
    private void tradeCard() {
    	System.out.println("\n=== Trade Card ===");
    	
    	manager.displayContainers();
    	if(manager.getContainerCount() == 0) {
			System.out.println("No " + getContainerTypeName() + " available.");
            sc.hitEnter();
            return;
		}
    	
    	String name = sc.ask("Enter " + getContainerTypeName() + " name:");
    	Binder binder = manager.getContainerByName(name);
    	if (binder == null) {
            System.out.println("Binder not found.");
            sc.hitEnter();
            return;
        }

        if (binder.isEmpty()) {
            System.out.println("Binder is empty. No cards to trade.");
            sc.hitEnter();
            return;
        }
        
        if (binder.getCardCount() == 0) {
            System.out.println(getContainerTypeName() + " \"" + name + "\" is empty. No available cards to trade.");
            sc.hitEnter();
            return;
        }
        
        binder.displayCards();
        
        // Select outgoing card
        String outgoingCardName = sc.ask("Enter name of card to trade away: ");
        Card outgoingCard = binder.getCard(outgoingCardName);

        if (outgoingCard == null) {
            System.out.println("Card not found in binder.");
            sc.hitEnter();
            return;
        }

        // Create incoming card
        System.out.println("Enter details of the card you want to receive:");
        Card incomingCard;
        do{
            incomingCard = tcis.addCard('T');
        } while (incomingCard == null);
        
        if (!confirmTrade(outgoingCard, incomingCard)) {
            System.out.println("Trade cancelled.");
            sc.hitEnter();
            return;
        }

        executeTrade(binder, outgoingCard, incomingCard, tcis);
    	
    }
    
    private boolean confirmTrade(Card outgoing, Card incoming) {
    	  double valueDifference = Math.abs(incoming.getTotalValue() - outgoing.getTotalValue());

          if (valueDifference >= 1.0) {
              System.out.printf("Warning: Value difference is $%.2f\n", valueDifference);
              String response = sc.askLowerTrimmed("Do you want to proceed with the trade? (y/n): ");

              if (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                  return false;
              }
          }
    	return true;
    }
    
    private void executeTrade(Binder binder, Card outgoing, Card incoming, TradingCardInventorySystem tcis) {
    	// If everything successful, execute trade
        binder.removeCard(outgoing);
        tcis.getCollection().addCard(incoming);
        System.out.println("Trade completed successfully!");
        sc.hitEnter();
    }

}

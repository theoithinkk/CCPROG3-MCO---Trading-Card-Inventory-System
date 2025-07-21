package com.tciss;

public class DeckManager extends CardContainerManager<Deck>{

    public DeckManager(int maxnb){  // inherits the constructors and methods from its superclass, CardContainerManager
        super(maxnb);
    }
    
	protected String getContainerTypeName() {
		return "Deck";
	}
	
	protected Deck createNewContainer(String name) {
		return new Deck(name);
	}
	
	protected Deck[] createContainerArray() {
		return new Deck[MAX_CONTAINERS];
	}

 

}

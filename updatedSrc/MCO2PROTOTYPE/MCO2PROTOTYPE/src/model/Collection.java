package model;

public class Collection extends CardContainer {
    public Collection() {
        super("Main Collection", Integer.MAX_VALUE);
    }

    @Override
    public boolean canAddCard(Card card) {
        return getTotalCards() < getCapacity();
    }

	@Override
	protected boolean isSellable() {
		return true;
	}

	@Override
	protected double getSellingValue() {
		return getTotalValue();
	}
    
    
    
}

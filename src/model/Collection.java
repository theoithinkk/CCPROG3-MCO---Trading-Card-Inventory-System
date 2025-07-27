package model;

public class Collection extends CardContainer {
    public Collection() {
        super("Main Collection", Integer.MAX_VALUE);
    }
    
    public Card findMatchingCard(Card template) {
        for (Card c : cards.keySet()) {
            if (c.getName().equals(template.getName())
              && c.getRarity() == template.getRarity()
              && c.getVariant() == template.getVariant()) {
                return c;
            }
        }
        return null;
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

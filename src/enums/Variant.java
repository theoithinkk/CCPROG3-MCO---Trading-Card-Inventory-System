package enums;

public enum Variant {
    NORMAL(1.0), 
    EXTENDED_ART(1.5), 
    FULL_ART(2.0), 
    ALT_ART(3.0);
    
    private final double multiplier;
    
    Variant(double multiplier) {
        this.multiplier = multiplier;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
}
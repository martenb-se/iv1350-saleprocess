package se.martenb.iv1350.project.saleprocess.util;

/**
 * Represents some quantity containing a numerical value and a unit.
 * All instances are immutable.
 */
public class Quantity {
    private final double numericalValue;
    private final Unit unitType;
    
    /**
     * Creates a new instance, representing a quantity along with its unit.
     * 
     * @param numericalValue The final price of the purchase.
     * @param unitType The amount paid for the purchase.
     * 
    */
    public Quantity(double numericalValue, Unit unitType) {
        this.numericalValue = numericalValue;
        this.unitType = unitType;
    }
    
    /**
     * Get the numerical value for this Quantity object.
     * 
     * @return The numerical value.
     */
    public double getNumericalValue() {
        return numericalValue;
    }
    
    /**
     * Get the type of unit for this Quantity object.
     * 
     * @return The unit type.
     */
    public Unit getUnitType() {
        return unitType;
    }
    
}


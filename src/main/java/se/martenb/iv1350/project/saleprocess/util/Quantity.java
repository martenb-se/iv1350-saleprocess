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
     * Uses the standard unit of representing the item in pieces.
     * 
     * @param numericalValue The final price of the purchase.
     * 
    */
    public Quantity(double numericalValue) {
        this.numericalValue = numericalValue;
        unitType = Unit.PIECE;
    }
    
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
     * Get the numerical value for this {@link Quantity} object.
     * 
     * @return The numerical value.
     */
    public double getNumericalValue() {
        return numericalValue;
    }
    
    /**
     * Get the type of unit for this {@link Quantity} object.
     * 
     * @return The unit type.
     */
    public Unit getUnitType() {
        return unitType;
    }
    
    /**
     * Get the <code>String</code> representation of the {@link Unit} for 
     * this {@link Quantity} object.
     * 
     * @return The {@link Unit} type <code>String</code>.
     */
    public String getUnitString() {
        String unitTypeString = "";
        if(unitType == Unit.PIECE)
            unitTypeString = "pc";
        if(unitType == Unit.KILOGRAM)
            unitTypeString = "kg";
        if(unitType == Unit.LITRE)
            unitTypeString = "l";
        return unitTypeString;
    }

    /**
     * Creates a <code>String</code> of the quantity containing the numerical 
     * value and the type of quantity.
     * 
     * @return The <code>String</code> representing the quantity.
     */
    @Override
    public String toString() {
        StringBuilder quantityString = new StringBuilder();
        if (unitType == Unit.PIECE)
            quantityString.append((int) numericalValue);
        else
            quantityString.append(numericalValue);
        
        quantityString.append(" ");
        quantityString.append(getUnitString());
        return quantityString.toString();
    }
    
}


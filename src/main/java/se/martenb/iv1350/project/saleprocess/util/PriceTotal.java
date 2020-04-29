package se.martenb.iv1350.project.saleprocess.util;

/**
 * Represents a total price including combined taxes of many added prices.
*/
public class PriceTotal {
    private final Amount totalPriceAfterTaxes;
    private final Amount totalPriceBeforeTaxes;
    private final Amount totalTaxes;
    
    /**
     * Creates a new instance, representing a total price before and after 
     * added taxes along with the total of taxes.
     * 
     * @param totalPriceAfterTaxes The total price after taxes
     * @param totalPriceBeforeTaxes The total price after taxes
     * @param totalTaxes The total amount taxes
     */
    public PriceTotal(Amount totalPriceAfterTaxes, 
            Amount totalPriceBeforeTaxes, Amount totalTaxes) {
        this.totalPriceAfterTaxes = totalPriceAfterTaxes;
        this.totalPriceBeforeTaxes = totalPriceBeforeTaxes;
        this.totalTaxes = totalTaxes;
        
    }
    
    /**
     * Get the total price after taxes are applied.
     * 
     * @return The total price after taxes.
     */
    public Amount getTotalPriceAfterTaxes() {
        return totalPriceAfterTaxes;
    }
    
    /**
     * Get the total price before taxes are applied.
     * 
     * @return The total price before taxes.
     */
    public Amount getTotalPriceBeforeTaxes() {
        return totalPriceBeforeTaxes;
    }
    
    /**
     * Get the amount of total taxes.
     * 
     * @return The total amount of taxes.
     */
    public Amount getTotalTaxes() {
        return totalTaxes;
    }
    
}

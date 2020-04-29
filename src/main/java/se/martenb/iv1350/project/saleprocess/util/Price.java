package se.martenb.iv1350.project.saleprocess.util;

/**
 * Represents a price. All instances are immutable.
 */
public class Price {
    private static final double PERCENT_MULTIPLIER = 0.01;
    private final Amount priceBeforeTaxes;
    private final Amount priceAfterTaxes;
    private final Amount taxesAmount;
    private final double vatTaxRate;
    
    /**
     * Creates a new instance, representing the specified price.
     * 
     * @param priceBeforeTaxes The price before any added tax.
     * @param vatTaxRate The VAT tax rate in percent applied to the price.
     * 
    */
    public Price(Amount priceBeforeTaxes, double vatTaxRate) {
        this.priceBeforeTaxes = priceBeforeTaxes;
        this.vatTaxRate = vatTaxRate;
        this.taxesAmount = calculateAmountOfTaxes();
        this.priceAfterTaxes = priceBeforeTaxes.plus(this.taxesAmount);
    }
    
    /**
     * Calculate the amount of taxes for the specified price.
     * 
     * @return The amount of taxes.
     */
    private Amount calculateAmountOfTaxes() {
        double taxRatePercentMultiplier = PERCENT_MULTIPLIER * vatTaxRate;
        Amount amountOfTaxes = 
                priceBeforeTaxes.multiply(taxRatePercentMultiplier);
        return amountOfTaxes;
    }
    
    /**
     * Get the price before taxes for this object.
     * 
     * @return The price before taxes.
     */
    public Amount getPriceBeforeTax() {
        return this.priceBeforeTaxes;
    }
    
    /**
     * Get the price after taxes for this object.
     * 
     * @return The price after taxes.
     */
    public Amount getPriceAfterTax() {
        return this.priceAfterTaxes;
    }
    
    /**
     * Get the taxes for this object.
     * 
     * @return The taxes.
     */
    public Amount getTaxesAmount() {
        return this.taxesAmount;
    }
    
    /**
     * Get the VAT tax rate in percent for this object.
     * 
     * @return The VAT tax rate in percent.
     */
    public double getVATTaxRate() {
        return this.vatTaxRate;
    }
    
}

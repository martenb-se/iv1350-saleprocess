package se.martenb.iv1350.project.saleprocess.integration.dto;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.util.PriceTotal;

/**
 * Represents a purchase containing the final price, amount paid and the amount
 * of change. 
 * 
 */
public class PurchaseDTO {
    private final PriceTotal finalPrice;
    private final Amount amountPaid;
    private final Amount amountChange;
    
    /**
     * Creates a new instance, representing the purchase.
     * 
     * @param finalPrice The final price of the purchase.
     * @param amountPaid The amount paid for the purchase.
     * @param amountChange The amount of change.
     * 
    */
    public PurchaseDTO(PriceTotal finalPrice, Amount amountPaid, 
            Amount amountChange) {
        this.finalPrice = finalPrice;
        this.amountPaid = amountPaid;
        this.amountChange = amountChange;
    }
    
    /**
     * Get the final price for the purchase.
     * 
     * @return The final price in the purchase.
     */
    public PriceTotal getFinalPrice() {
        return finalPrice;
    }
    
    /**
     * Get the amount paid for the purchase.
     * 
     * @return The amount paid.
     */
    public Amount getAmountPaid() {
        return amountPaid;
    }
    
    /**
     * Get the amount of change after the purchase.
     * 
     * @return The amount of change.
     */
    public Amount getAmountChange() {
        return amountChange;
    }
}

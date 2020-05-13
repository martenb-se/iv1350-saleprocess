package se.martenb.iv1350.project.saleprocess.integration.dto;

import se.martenb.iv1350.project.saleprocess.util.Amount;

/**
 * Represents discount information after applying several discount rules.
*/
public class DiscountDTO {
    private final SaleDTO saleBeforeDiscount;
    private final SaleDTO saleAfterDiscount;
    private final Amount discountAmount;
    private final double discountPercent;
    
    /**
     * Creates a new instance, representing the specified item.
     * 
     * @param saleBeforeDiscount Full sale before applied discount.
     * @param saleAfterDiscount Full sale after applied discount.
     * @param discountAmount Amount of discount.
     * @param discountPercent Discount percent.
     */
    public DiscountDTO(SaleDTO saleBeforeDiscount, 
            SaleDTO saleAfterDiscount, 
            Amount discountAmount, 
            double discountPercent) {
        this.saleBeforeDiscount = saleBeforeDiscount;
        this.saleAfterDiscount = saleAfterDiscount;
        this.discountAmount = discountAmount;
        this.discountPercent = discountPercent;
    }

    /**
     * Get the total price before the applied discount.
     * 
     * @return The total price before the discount.
     */
    public SaleDTO getSaleBeforeDiscount() {
        return saleBeforeDiscount;
    }
    
    /**
     * Get the total price after the applied discount.
     * 
     * @return The total price after the discount.
     */
    public SaleDTO getSaleAfterDiscount() {
        return saleAfterDiscount;
    }

    /**
     * Get amount of discount.
     * 
     * @return discount amount.
     */
    public Amount getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Get discount percent.
     * 
     * @return The discount percent-
     */
    public double getDiscountPercent() {
        return discountPercent;
    }
    
    
    
}

package se.martenb.iv1350.project.saleprocess.model;

import se.martenb.iv1350.project.saleprocess.integration.discount.DiscountRule;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.DiscountDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Amount;

/**
 * Represents handling discounts for an ended saleState.
 */
public class Discount {
    private SaleDTO saleState;
    private final DiscountRule compositeDiscountRule;
    
    /**
     * Creates a new discount handler for the specified saleState.
     * 
     * @param compositeDiscountRule The discount rule to try to apply.
     */
    public Discount(DiscountRule compositeDiscountRule) {
        this.compositeDiscountRule = compositeDiscountRule;
    }
    
    /**
     * Start the discount process and set the sale to try discounts on.
     * 
     * @param saleState The sale to handle discounts for.
     */
    public void startDiscount(SaleDTO saleState) {
        this.saleState = saleState;
    }
    
    /**
     * Try discounts on the entire sale. 
     * 
     * @param customerInformaton The customer to try discounts with.
     * @return Sale state after discounts have been tried.
     */
    private SaleDTO tryAvailableDiscountsOnSale(
            CustomerDTO customerInformaton) {
        SaleDTO saleAfterDiscount = saleState;
        boolean isDiscountRuleApplicable = 
                compositeDiscountRule.isRuleApplicable(
                        saleAfterDiscount, customerInformaton);
        if (isDiscountRuleApplicable) {
            saleAfterDiscount = compositeDiscountRule.applyRule();
        }
        return saleAfterDiscount;
    }
    
    /**
     * Calculate total amount of discounts on the entire sale.
     * 
     * @param saleAfterDiscount Sale state after applied discounts.
     * @return The total amount of discounts.
     */
    private Amount calculateDiscountAmount(SaleDTO saleAfterDiscount) {
        Amount runningTotalBeforeDiscount = saleState.getRunningTotal();
        Amount runningTotalAfterDiscount = saleAfterDiscount.getRunningTotal();
        Amount discountAmount = 
                runningTotalBeforeDiscount.minus(runningTotalAfterDiscount);
        return discountAmount;
    }
    
    /**
     * Calculate total percentage of discounts on the entire saleState.
     * 
     * @param discountAmount Total amount of discounts.
     * @return The percentage of discounts.
     */
    private double calculateDiscountPercent(Amount discountAmount) {
        Amount runningTotalBeforeDiscount = saleState.getRunningTotal();
        double totalBeforeDiscount = 
                runningTotalBeforeDiscount.getAmount().doubleValue();
        double totalDiscount = 
                discountAmount.getAmount().doubleValue();
        double totalsQuotient = totalDiscount / totalBeforeDiscount;
        double discountPercent = totalsQuotient * 100;
        return discountPercent;
    }
    
    /**
     * Try discounts on current sale using the specified customer information.
     * 
     * @param customerInformaton The customer to try discounts with.
     * @return The discount information.
     */
    public DiscountDTO tryDiscountOnSale(CustomerDTO customerInformaton) {
        if (saleState == null)
            throw new IllegalStateException("startDiscount(SaleDTO " + 
                    "saleState) must be called before calling " + 
                    "tryDiscountOnSale(CustomerDTO customerInformaton).");
        SaleDTO saleAfterDiscount = 
                tryAvailableDiscountsOnSale(customerInformaton);
        Amount discountAmount = 
                calculateDiscountAmount(saleAfterDiscount);
        double discountPercent = 
                calculateDiscountPercent(discountAmount);
        DiscountDTO discountInformation = 
                new DiscountDTO(
                        saleState, 
                        saleAfterDiscount, 
                        discountAmount, 
                        discountPercent);
        return discountInformation;
        
    }
    
}

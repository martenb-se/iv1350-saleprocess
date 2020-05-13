package se.martenb.iv1350.project.saleprocess.integration.discount;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;

/**
 * Singleton with rules for applying senior specific discounts.
 */
public class SeniorDiscount implements DiscountRule {
    private static final SeniorDiscount SENIOR_DISCOUNT = new SeniorDiscount();
    private final String name = "Senior Discount!";
    private int customerAge;
    private Date bornBefore;
    private double fullSaleDiscountPercent;
    private DiscountApplier discountApplier;

    /**
     * Creates the only instance of the senior discount rules.
     */
    private SeniorDiscount() {
        initializeDiscountRules();
    }
    
    /**
     * Get the only instance of the senior discount rules.
     * @return The only instance of the senior discount rules.
     */
    public static SeniorDiscount getSeniorDiscount() {
        return SENIOR_DISCOUNT;
    }
    
    /**
     * Initializes all stored (hard coded) discount rules, when rules apply and
     * for which items.
     */
    private void initializeDiscountRules() {
        customerAge = 65;
        fullSaleDiscountPercent = 10;
        GregorianCalendar seniorDate = new GregorianCalendar();
        seniorDate.add(Calendar.YEAR, customerAge * -1);
        bornBefore = seniorDate.getTime();
    }

    /**
     * Check if senior discounts are applicable for the current sale and for
     * the current specified customer.
     * 
     * @param saleInfo The sale on which to check if discounts are applicable.
     * @param customerInfo The customer that might be eligible for discounts.
     * @return Whether discounts are applicable.
     */
    @Override
    public boolean isRuleApplicable(
            SaleDTO saleInfo, CustomerDTO customerInfo) {
        discountApplier = 
                new DiscountApplier(saleInfo, customerInfo);
        boolean isCustomerOldEnough = 
                discountApplier.isCustomerOldEnough(bornBefore);
        boolean isRuleApplicable = isCustomerOldEnough;
        return isRuleApplicable;
    }

    /**
     * Apply senior discounts to specified sale after it has been verified
     * that rules can be applied. {@link SeniorDiscount isRuleApplicable} 
     * must be called before applying.
     * 
     * @return The sale after rules have been applied.
     */
    @Override
    public SaleDTO applyRule() {
        if(discountApplier == null)
            throw new IllegalStateException("isRuleApplicable(SaleDTO " + 
                    "saleInfo, CustomerDTO customerInfo) must be called " + 
                    "before applyRule()");
        discountApplier.applyFullSaleDiscount(fullSaleDiscountPercent);
        SaleDTO saleAfterDiscounts = discountApplier.getSaleInfo();
        return saleAfterDiscounts;
    }

    /**
     * String describing the senior rule.
     * 
     * @return The string describing the discount.
     */
    @Override
    public String toString() {
        StringBuilder stringRule = new StringBuilder();
        stringRule.append(name);
        stringRule.append("\n");
        stringRule.append("║ For customers aged ");
        stringRule.append(customerAge);
        stringRule.append(" and over.\n");
        stringRule.append("║ - ");
        stringRule.append(fullSaleDiscountPercent);
        stringRule.append("% off whole sale!");
        return stringRule.toString();
    }
    
}

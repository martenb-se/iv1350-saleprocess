package se.martenb.iv1350.project.saleprocess.integration.discount;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;

/**
 * Singleton with rules for applying summer specific discounts.
 */
public class SummerDiscount implements DiscountRule {
    private static final SummerDiscount SUMMER_DISCOUNT = new SummerDiscount();
    private final String name = "Summer Discount!";
    private Date startDate;
    private Date endDate;
    private Map<String,Double> itemNameContains = new HashMap<>();
    private DiscountApplier discountApplier;

    /**
     * Creates the only instance of the summer discount rules.
     */
    private SummerDiscount() {
        initializeDiscountRules();
    }
    
    /**
     * Get the only instance of the summer discount rules.
     * @return The only instance of the summer discount rules.
     */
    public static SummerDiscount getSummerDiscount() {
        return SUMMER_DISCOUNT;
    }
    
    /**
     * Initializes all stored (hard coded) discount rules, when rules apply and
     * for which items.
     */
    private void initializeDiscountRules() {
        startDate = new GregorianCalendar(
                Calendar.getInstance().get(Calendar.YEAR), Calendar.MAY, 1).
                getTime();
        endDate = new GregorianCalendar(
                Calendar.getInstance().get(Calendar.YEAR), Calendar.AUGUST, 31).
                getTime();
        String stringItemNameContainsDrink = "drink";
        int discountPercentItemNameContainsDrink = 10;
        String stringItemNameContainsStrawberry = "strawberry";
        int discountPercentItemNameContainsStrawberry = 5;
        initializeItemNameContainsDiscount(
                stringItemNameContainsDrink, 
                discountPercentItemNameContainsDrink);
        initializeItemNameContainsDiscount(
                stringItemNameContainsStrawberry, 
                discountPercentItemNameContainsStrawberry);
    }
    
    /**
     * Initialize discount rules for items containing specified text 
     * (in lowercase) in its name and the discount percentage.
     * 
     * @param nameContains The text to find within the item name.
     * @param discountPercent The percentage of the discount.
     */
    private void initializeItemNameContainsDiscount(
            String nameContains, double discountPercent) {
        String lowerCaseNameContains = nameContains.toLowerCase();
        itemNameContains.put(lowerCaseNameContains, discountPercent);
    }

    /**
     * Check if summer discounts are applicable for the current sale and for
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
        discountApplier.setItemNameContains(itemNameContains);
        boolean isItTheRightPeriod = 
                discountApplier.isItTheRightPeriod(startDate, endDate);
        boolean isApplicableItemInSale = 
                discountApplier.isApplicableItemInSale();
        boolean isRuleApplicable = 
                isItTheRightPeriod && isApplicableItemInSale;
        return isRuleApplicable;
    }

    /**
     * Apply summer discounts to specified sale after it has been verified
     * that rules can be applied. {@link SummerDiscount isRuleApplicable} 
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
        discountApplier.applyPerItemRules();
        SaleDTO saleAfterDiscounts = discountApplier.getSaleInfo();
        return saleAfterDiscounts;
    }

    /**
     * String describing the summer rule.
     * 
     * @return The string describing the discount.
     */
    @Override
    public String toString() {
        StringBuilder stringRule = new StringBuilder();
        stringRule.append(name);
        stringRule.append("\n");
        stringRule.append("║ From ");
        stringRule.append(startDate.toString());
        stringRule.append(" to ");
        stringRule.append(endDate.toString());
        stringRule.append(":\n");
        for(Map.Entry<String,Double> itemNameContainsDiscount : 
                itemNameContains.entrySet()) {
            stringRule.append("║ - ");
            stringRule.append(itemNameContainsDiscount.getValue());
            stringRule.append("% off all ");
            stringRule.append(itemNameContainsDiscount.getKey());
            stringRule.append(" products!\n");
        }
        return stringRule.toString();
    }
    
}

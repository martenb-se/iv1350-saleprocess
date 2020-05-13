package se.martenb.iv1350.project.saleprocess.integration.discount;

import java.util.ArrayList;
import java.util.List;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;

/**
 * A {@link DiscountRule} which tries and applies multiple rules all at once.
 */
public class CompositeDiscountRule implements DiscountRule {
    private List<DiscountRule> discountRuleList = new ArrayList<>();
    private List<DiscountRule> applicableRulesList = new ArrayList<>();
    private SaleDTO saleInfo;
    private CustomerDTO customerInfo;
    
    /**
     * Check if any added discounts are applicable for the current sale and 
     * for the current specified customer.
     * 
     * @param saleInfo The sale on which to check if discounts are applicable.
     * @param customerInfo The customer that might be eligible for discounts.
     * @return Whether discounts are applicable.
     */
    @Override
    public boolean isRuleApplicable(
            SaleDTO saleInfo, CustomerDTO customerInfo) {
        if(discountRuleList.isEmpty())
            throw new IllegalStateException("addRule(DiscountRule " + 
                    "discountRule) must be called before isRuleApplicable()");
        this.saleInfo = saleInfo;
        this.customerInfo = customerInfo;
        boolean isAnyRuleApplicable = false;
        for(DiscountRule discountRule : discountRuleList) {
            boolean isDiscountRuleApplicable = 
                    discountRule.isRuleApplicable(saleInfo, customerInfo);
            if (isDiscountRuleApplicable) {
                isAnyRuleApplicable = true;
                applicableRulesList.add(discountRule);
            }
        }
        return isAnyRuleApplicable;
    }

    /**
     * Apply any applicable discounts to specified sale after it has been 
     * verified any rule can be applied. 
     * {@link CompositeDiscountRule isRuleApplicable} must be called 
     * before applying.
     * 
     * @return The sale after rules have been applied.
     */
    @Override
    public SaleDTO applyRule() {
        if(applicableRulesList.isEmpty())
            throw new IllegalStateException("isRuleApplicable(SaleDTO " + 
                    "saleInfo, CustomerDTO customerInfo) must be called " + 
                    "before applyRule()");
        for(DiscountRule discountRule : applicableRulesList) {
            discountRule.isRuleApplicable(saleInfo, customerInfo);
            saleInfo = discountRule.applyRule();
        }
        return saleInfo;
    }
    
    /**
     * Adds a rule to be applied when this composite is trying and applying
     * discounts.
     * 
     * @param discountRule The discount rule to add.
     */
    void addRule(DiscountRule discountRule) {
        discountRuleList.add(discountRule);
    }
    
    /**
     * String describing all rules in this composite object.
     * 
     * @return The string describing the discount(s).
     */
    @Override
    public String toString() {
        StringBuilder stringRule = new StringBuilder();
        for(DiscountRule discountRule : discountRuleList) {
            stringRule.append(discountRule.toString());
        }
        return stringRule.toString();
    }
    
}
package se.martenb.iv1350.project.saleprocess.integration.discount;

import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;

/**
 * Defines the ability to apply discounts onto a sale.
 */
public interface DiscountRule {
    /**
     * See if a rule is applicable with the current sale.
     * 
     * @param saleInfo The sale to see if rule can be applied to.
     * @param customerInfo The customer to see if eligible.
     * @return The discount rule that is possible to apply.
     */
    public boolean isRuleApplicable(SaleDTO saleInfo, CustomerDTO customerInfo);
    
    /**
     * Apply the verified discount rule to the sale. 
     * {@link DiscountRule isRuleApplicable} must be called before applying.
     * 
     * @return The sale after the applied rule.
     */
    public SaleDTO applyRule();
    
    /**
     * Return a <code>String</code> representation of the discount rule.
     * 
     * @return The <code>String</code> representation of the discount rule.
     */
    @Override
    public String toString();
}

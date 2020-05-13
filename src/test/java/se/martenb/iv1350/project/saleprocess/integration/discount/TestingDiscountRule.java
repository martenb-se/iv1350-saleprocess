package se.martenb.iv1350.project.saleprocess.integration.discount;

import java.util.HashMap;
import java.util.Map;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;

public class TestingDiscountRule implements DiscountRule {
    private static final TestingDiscountRule TESTING_DISCOUNT_RULE = 
            new TestingDiscountRule();
    private final String name = "Testing Discount!";
    private Map<String,Double> itemNameContains = new HashMap<>();
    private DiscountApplier discountApplier;
    
    private TestingDiscountRule() {
        initializeDiscountRules();
    }
    
    public static TestingDiscountRule getTestingDiscountRule() {
        return TESTING_DISCOUNT_RULE;
    }
    
    private void initializeDiscountRules() {
        String stringItemNameContainsTwenty = "item #02";
        int discountPercentItemNameTwenty = 10;
        String stringItemNameContainsNumberTwentyFive = "#025";
        int discountPercentItemNameContainsNumberTwentyFive = 50;
        initializeItemNameContainsDiscount(
                stringItemNameContainsTwenty, 
                discountPercentItemNameTwenty);
        initializeItemNameContainsDiscount(
                stringItemNameContainsNumberTwentyFive, 
                discountPercentItemNameContainsNumberTwentyFive);
    }
    
    private void initializeItemNameContainsDiscount(
            String nameContains, double discountPercent) {
        itemNameContains.put(nameContains, discountPercent);
    }
    
    @Override
    public boolean isRuleApplicable(SaleDTO saleInfo, CustomerDTO customerInfo) {
        discountApplier = 
                new DiscountApplier(saleInfo, customerInfo);
        discountApplier.setItemNameContains(itemNameContains);
        boolean isApplicableItemInSale = 
                discountApplier.isApplicableItemInSale();
        boolean isRuleApplicable = isApplicableItemInSale;
        return isRuleApplicable;
    }

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
    
    @Override
    public String toString() {
        StringBuilder stringRule = new StringBuilder();
        stringRule.append(name);
        stringRule.append("\n");
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

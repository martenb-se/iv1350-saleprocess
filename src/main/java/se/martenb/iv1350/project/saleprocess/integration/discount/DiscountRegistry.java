package se.martenb.iv1350.project.saleprocess.integration.discount;

/**
 * The {@link DiscountRegistry} singleton handles communications with an 
 * external item discount system.
 */
public class DiscountRegistry {
    private static final DiscountRegistry DISCOUNT_REGISTRY = 
            new DiscountRegistry();
    
    /**
     * Create the only instance of the registry communicator. 
     */
    private DiscountRegistry() {}
    
    /**
     * Get the only instance of the {@link DiscountRegistry} .
     * @return the only instance of the {@link DiscountRegistry}.
     */
    public static DiscountRegistry getDiscountRegistry() {
        return DISCOUNT_REGISTRY;
    }
    
    /**
     * Get a composite {@link DiscountRule}
     * 
     * @return The composite {@link DiscountRule} representing all available 
     * rules.
     */
    public DiscountRule getCompositeDiscountRule() {
        CompositeDiscountRule allRulesComposite = new CompositeDiscountRule();
        allRulesComposite.addRule(SummerDiscount.getSummerDiscount());
        allRulesComposite.addRule(SeniorDiscount.getSeniorDiscount());
        return allRulesComposite;
    } 
    
}

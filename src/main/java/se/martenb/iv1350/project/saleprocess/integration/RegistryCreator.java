package se.martenb.iv1350.project.saleprocess.integration;

/**
 * {@link RegistryCreator} handles initiation of external registries such as
 * {@link ItemRegistry}, {@link AccountingRegistry} and {@link StoreRegistry}.
 * 
 * TODO Add implementation of DiscountRegistry
 */
public class RegistryCreator {
    StoreRegistry storeRegistry;
    AccountingRegistry accountingRegistry;
    ItemRegistry itemRegistry;
    
    /**
     * Initiates the {@link RegistryCreator} which initiates all 
     * required external registries.
     */
    public RegistryCreator() {
        this.storeRegistry = new StoreRegistry();
        this.accountingRegistry = new AccountingRegistry();
        this.itemRegistry = new ItemRegistry();
    }

    /**
     * Get the {@link StoreRegistry} object.
     * @return The {@link StoreRegistry} object.
     */
    public StoreRegistry getStoreRegistry() {
        return this.storeRegistry;
    }

    /**
     * Get the {@link AccountingRegistry} object.
     * @return The {@link AccountingRegistry} object.
     */
    public AccountingRegistry getAccountingRegistry() {
        return this.accountingRegistry;
    }

    /**
     * Get the {@link ItemRegistry} object.
     * @return The {@link ItemRegistry} object.
     */
    public ItemRegistry getItemRegistry() {
        return this.itemRegistry;
    }
    
}

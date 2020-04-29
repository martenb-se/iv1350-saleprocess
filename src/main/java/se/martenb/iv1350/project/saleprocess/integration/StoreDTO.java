package se.martenb.iv1350.project.saleprocess.integration;

/**
 * Represents store information such as a stores' name or address.
 */
public class StoreDTO {
    private final String storeName;
    private final AddressDTO storeAddress;
    
    /**
     * Creates a new instance, representing store information.
     * 
     * @param storeName The name of the store.
     * @param storeAddress The address of the store.
     */
    public StoreDTO(String storeName, AddressDTO storeAddress) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }
    
    /**
     * Get the store name for the current {@link StoreDTO} object.
     * 
     * @return The store name
     */
    public String getStoreName() {
        return this.storeName;
    }
    
    /**
     * Get the store address for the current {@link StoreDTO} object.
     * 
     * @return The store address
     */
    public AddressDTO getStoreAddress() {
        return this.storeAddress;
    }
    
}

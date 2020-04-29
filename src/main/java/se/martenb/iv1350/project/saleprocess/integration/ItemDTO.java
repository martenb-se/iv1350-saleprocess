package se.martenb.iv1350.project.saleprocess.integration;

import se.martenb.iv1350.project.saleprocess.util.Price;

/**
 * Represents an item in the system.
 * 
 */
public class ItemDTO {
    private final int itemID;
    private final String itemName;
    private final Price itemPrice;
    
    /**
     * Creates a new instance, representing the specified item.
     * 
     * @param itemID The unique numerical identifier for the item.
     * @param itemName The name for the item.
     * @param itemPrice The price for the item.
     * 
    */
    public ItemDTO(int itemID, String itemName, Price itemPrice) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    /**
     * Get the unique item identifier for this object.
     * 
     * @return The unique item identifier.
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * Get the item name for this object.
     * 
     * @return The item name.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Get the item price for this object.
     * 
     * @return The item price.
     */
    public Price getItemPrice() {
        return itemPrice;
    }
    
}

package se.martenb.iv1350.project.saleprocess.integration;

import se.martenb.iv1350.project.saleprocess.util.Quantity;

/**
 * Represents an item in a ongoing or finished sale. Containing information
 * about the item and in what quantity it exists.
 */
public class ItemInSaleDTO {
    private final ItemDTO itemInfo;
    private final Quantity itemQuantity;
    
    /**
     * Creates a new instance, representing an item in a sale.
     * 
     * @param itemInfo The item information.
     * @param itemQuantity The quantity of the item.
     * 
    */
    public ItemInSaleDTO(ItemDTO itemInfo, Quantity itemQuantity) {
        this.itemInfo = itemInfo;
        this.itemQuantity = itemQuantity;
    }
    
    /**
     * Get the item info for the sale item in the current object.
     * 
     * @return The item info.
     */
    public ItemDTO getItemInfo() {
        return itemInfo;
    }
    
    /**
     * Get the item quantity of the sale item in the current object.
     * 
     * @return The item quantity.
     */
    public Quantity getItemQuantity() {
        return itemQuantity;
    }
    
}

package se.martenb.iv1350.project.saleprocess.integration.dto;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.util.Price;
import se.martenb.iv1350.project.saleprocess.util.Quantity;

/**
 * Represents an item in a ongoing or finished sale. Containing information
 * about the item and in what quantity it exists.
 */
public class ItemInSaleDTO {
    private final ItemDTO itemInfo;
    private final Quantity itemQuantity;
    private static final double PERCENT_MULTIPLIER = 0.01;
    
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
    
    /**
     * Apply a discount percentage and return a new {@link ItemInSaleDTO} with
     * the updated price.
     * @param dicountPercent The percent to discount the item in sale with.
     * @return The item after the applied discount.
     */
    public ItemInSaleDTO applyDiscount(double dicountPercent) {
        int originalID = itemInfo.getItemID();
        String originalName = itemInfo.getItemName();
        Price originalPrice = itemInfo.getItemPrice();
        Amount originalPriceBeforeTaxes = originalPrice.getPriceBeforeTax();
        double originalVATTaxRate = originalPrice.getVATTaxRate();
        Amount discountAmount = originalPriceBeforeTaxes.
                multiply(PERCENT_MULTIPLIER * dicountPercent);
        Amount discountedPriceBeforeTaxes = originalPriceBeforeTaxes.
                minus(discountAmount);
        Price discountedPrice = 
                new Price(discountedPriceBeforeTaxes, originalVATTaxRate);
        ItemDTO discountedItemInfo = 
                new ItemDTO(originalID, originalName, discountedPrice);
        
        return new ItemInSaleDTO(discountedItemInfo, itemQuantity);
    }
    
}

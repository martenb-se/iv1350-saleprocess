package se.martenb.iv1350.project.saleprocess.model;

import se.martenb.iv1350.project.saleprocess.integration.dto.ItemDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Quantity;

/**
 * Represents an ongoing sale. 
 */
public class Sale {
    private SaleInformation saleInformation;
    
    /**
     * Creates a new instance of an ongoing sale and sets the sale time to
     * the current date/time.
     */
    public Sale() {
        this.saleInformation = new SaleInformation();
        updateSaleTime();
    }
    
    /**
     * Updates the sale time to the current time.
     */
    private void updateSaleTime() {
        this.saleInformation.updateSaleTime();
    }
    
    /**
     * Add an item to the sale. 
     * 
     * @param itemInfo What item to add.
     * @param quantity How many of the items to add.
     * 
     * @return The state of the updated sale in a {@link SaleDTO} object.
     * @throws IllegalItemQuantityException if the quantity was invalid.
     */
    public SaleDTO addItemToSale(ItemDTO itemInfo, Quantity quantity) 
            throws IllegalItemQuantityException {
        saleInformation.addItemToSale(itemInfo, quantity);
        SaleDTO saleState = saleInformation.getSaleDTO();
        return saleState;
    }
    
}

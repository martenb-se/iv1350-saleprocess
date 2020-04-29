package se.martenb.iv1350.project.saleprocess.integration;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an ongoing or completed sale, containing information about
 * date/time, running total, items in sale and amount of items.
 */
public class SaleDTO {
    private final LocalDateTime saleDateTime;
    private final Amount runningTotal;
    private final List<ItemInSaleDTO> itemsInSale;
    private final int totalItems;
    
    /**
     * Creates a new instance of a sale object. 
     * 
     * @param saleDateTime Date and time of the sale.
     * @param runningTotal Running total of the sale.
     * @param itemsInSaleList List of items in the sale.
     * @param totalItems Total number of items in the sale.
     */
    public SaleDTO(LocalDateTime saleDateTime, Amount runningTotal, 
            List<ItemInSaleDTO> itemsInSaleList, int totalItems) {
        this.saleDateTime = saleDateTime;
        this.runningTotal = runningTotal;
        this.itemsInSale = itemsInSaleList;
        this.totalItems = totalItems;
    }

    /**
     * Get the date/time for the sale.
     * 
     * @return The date/time.
     */
    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    /**
     * Get the running total for the sale.
     * 
     * @return The date/time.
     */
    public Amount getRunningTotal() {
        return runningTotal;
    }

    /**
     * Get the items in the current sale.
     * 
     * @return The list containing the items in the sale.
     */
    public List<ItemInSaleDTO> getItemsInSale() {
        return itemsInSale;
    }

    /**
     * Get the date/time for the sale.
     * 
     * @return The date/time.
     */
    public int getTotalItems() {
        return totalItems;
    }
    
}

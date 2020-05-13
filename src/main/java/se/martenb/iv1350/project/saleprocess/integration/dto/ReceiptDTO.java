package se.martenb.iv1350.project.saleprocess.integration.dto;

import se.martenb.iv1350.project.saleprocess.util.Amount;

/**
 * Represents a receipt containing information about a purchase. Information
 * about the store, information about the purchase and total amount 
 * of discounts.
 */
public class ReceiptDTO {
    private final StoreDTO storeInfo;
    private final PurchaseDTO purchaseInfo;
    private final Amount totalDiscount;
    private final SaleDTO saleInfo;
    
    /**
     * Creates a new instance, representing the receipt.
     * 
     * @param storeInfo The store information.
     * @param purchaseInfo The purchase information.
     * @param totalDiscount The applied total discount.
     * @param saleInfo The state of the sale.
    */
    public ReceiptDTO(StoreDTO storeInfo, PurchaseDTO purchaseInfo, 
            Amount totalDiscount, SaleDTO saleInfo) {
        this.storeInfo = storeInfo;
        this.purchaseInfo = purchaseInfo;
        this.totalDiscount = totalDiscount;
        this.saleInfo = saleInfo;
    }

    /**
     * Get the store info from the receipt.
     * 
     * @return The store info.
     */
    public StoreDTO getStoreInfo() {
        return storeInfo;
    }

    /**
     * Get purchase info from the receipt.
     * 
     * @return The store info.
     */
    public PurchaseDTO getPurchaseInfo() {
        return purchaseInfo;
    }

    /**
     * Get the total discount from the receipt.
     * 
     * @return The total discount.
     */
    public Amount getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * Get the sale info from the receipt.
     * 
     * @return The sale info.
     */
    public SaleDTO getSaleInfo() {
        return saleInfo;
    }
    
}

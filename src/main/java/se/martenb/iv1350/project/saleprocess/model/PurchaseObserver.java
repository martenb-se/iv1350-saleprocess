package se.martenb.iv1350.project.saleprocess.model;

import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;

/**
 * A listener interface for receiving notifications about successful purchases.
 */
public interface PurchaseObserver {
    /**
     * Called when a new purchase has been made.
     * 
     * @param purchaseInfo Info about the purchase.
     */
    public void newRegisteredPurchase(PurchaseDTO purchaseInfo);
}

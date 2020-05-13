package se.martenb.iv1350.project.saleprocess.testing;

import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.model.PurchaseObserver;

public class TestingPurchaseObserver implements PurchaseObserver {

    @Override
    public void newRegisteredPurchase(PurchaseDTO purchaseInfo) {
        System.out.println(purchaseInfo.
                getFinalPrice().
                getTotalPriceAfterTaxes().
                toString());
    }
    
}

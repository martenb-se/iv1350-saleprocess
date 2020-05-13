package se.martenb.iv1350.project.saleprocess.model;

import java.util.ArrayList;
import java.util.List;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.util.PriceTotal;
import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ReceiptDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.StoreDTO;
import se.martenb.iv1350.project.saleprocess.integration.StoreRegistry;

/**
 * Represents handling payment for an ended sale.
 */
public class Payment {
    private final StoreRegistry storeRegistry;
    private final Register register;
    private final Amount totalDiscount;
    private List<PurchaseObserver> purchaseObserverList = new ArrayList<>();
    private SaleDTO saleState;
    
    /**
     * Creates a new instance of a payment handler. Discounts are not yet 
     * implemented and defaulted to 0. 
     * 
     * @param storeRegistry Registry containing store information.
     * @param register The register to put the paid amount into.
     */
    public Payment(StoreRegistry storeRegistry, Register register) {
        this.storeRegistry = storeRegistry;
        this.register = register;
        this.totalDiscount = new Amount(0);
    }
    
    /**
     * Start payment for the specified sale.
     * 
     * @param saleState Sale to handle payment for.
     */
    public void startPayment(SaleDTO saleState) {
        this.saleState = saleState;
    }
    
    /**
     * Calculate final price with or without VAT and total amount of 
     * VAT for the entire sale.
     * 
     * @return The final price 
     */
    private PriceTotal calculateFinalPrice() {
        Amount totalPriceBeforeTaxes = new Amount(0);
        Amount totalPriceAfterTaxes = new Amount(0);
        Amount totalTaxes = new Amount(0);
        for (ItemInSaleDTO itemInSale : 
                saleState.getItemsInSale()) {
            double itemMultiplier = 
                    itemInSale.getItemQuantity().getNumericalValue();
            Amount totalItemPriceBeforeTax = itemInSale.
                    getItemInfo().
                    getItemPrice().
                    getPriceBeforeTax().
                    multiply(itemMultiplier);
            Amount totalItemPriceAfterTaxes = itemInSale.
                    getItemInfo().
                    getItemPrice().
                    getPriceAfterTax().
                    multiply(itemMultiplier);
            Amount totalItemTaxes = itemInSale.
                    getItemInfo().
                    getItemPrice().
                    getTaxesAmount().
                    multiply(itemMultiplier);
            totalPriceBeforeTaxes = totalPriceBeforeTaxes.plus(
                    totalItemPriceBeforeTax);
            totalPriceAfterTaxes = totalPriceAfterTaxes.plus(
                    totalItemPriceAfterTaxes);
            totalTaxes = totalTaxes.plus(
                    totalItemTaxes);
        }
        PriceTotal finalPrice = new PriceTotal(totalPriceAfterTaxes, 
                totalPriceBeforeTaxes, totalTaxes);
        return finalPrice;
    }
    
    /**
     * Make {@link PurchaseDTO} containing information about a purchase.
     * 
     * @param amountPaid The paid amount.
     * @param amountChange Amount of change.'
     * 
     * @return The {@link PurchaseDTO}.
     */
    private PurchaseDTO makePurchaseInfo(Amount amountPaid, 
            Amount amountChange) {
        PriceTotal finalPrice = calculateFinalPrice();
        PurchaseDTO purchaseInfo = new PurchaseDTO(
                finalPrice, amountPaid, amountChange);
        return purchaseInfo;
    }

    /**
     * Calculate change to to return to the customer.
     * 
     * @param runningTotal Running total for the sale.
     * @param payment Amount paid.
     * 
     * @return The amount of change.
     */
    private Amount calculateChange(Amount runningTotal, Amount amountPaid) {
        Amount changeAfterPayment = amountPaid.minus(runningTotal);
        return changeAfterPayment;
    }
    
    /**
     * Make receipt from purchase information.
     * 
     * @param purchaseInfo Information about the purchase.
     * @return The receipt.
     */
    private ReceiptDTO makeReceipt(PurchaseDTO purchaseInfo) {
        StoreDTO storeInfo = storeRegistry.getStoreInfo();
        ReceiptDTO receiptDTO = new ReceiptDTO(storeInfo, purchaseInfo, 
                totalDiscount, saleState);
        return receiptDTO;
    }
    
    /**
     * Pay for the sale and complete the purchase. 
     * Updates the amount available in the register.
     * Will notify any {@link PurchaseObserver} listening for when new 
     * purchases are made.
     * 
     * @param amountPaid The paid amount.
     * @return The receipt for the purchase.
     */
    public ReceiptDTO pay(Amount amountPaid) {
        Amount runningTotal = saleState.getRunningTotal();
        Amount amountChange = calculateChange(runningTotal, amountPaid);
        PurchaseDTO purchaseInfo = makePurchaseInfo(amountPaid, amountChange);
        ReceiptDTO receipt = makeReceipt(purchaseInfo);
        register.addAmount(
                receipt.getPurchaseInfo().
                        getFinalPrice().
                        getTotalPriceAfterTaxes());
        notifyObserversNewPurchase(purchaseInfo);
        
        return receipt;
    }
    
    /**
     * Add a {@link PurchaseObserver} to the ongoing sale.
     * 
     * @param purchaseObserver The observer to add.
     */
    public void addPurchaseObserver(PurchaseObserver purchaseObserver) {
        purchaseObserverList.add(purchaseObserver);
    }
    
    /**
     * Notifies purchase observers about a finished purchase, giving them
     * information about the purchase after the payment has been registered.
     * 
     * @param purchaseInfo Information about the purchase.
     */
    private void notifyObserversNewPurchase(PurchaseDTO purchaseInfo) {
        for(PurchaseObserver purchaseObserver : purchaseObserverList) {
            purchaseObserver.newRegisteredPurchase(purchaseInfo);
        }
    }
}

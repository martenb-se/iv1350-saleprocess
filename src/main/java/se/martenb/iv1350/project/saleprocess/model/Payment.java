package se.martenb.iv1350.project.saleprocess.model;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.util.PriceTotal;
import se.martenb.iv1350.project.saleprocess.integration.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.ReceiptDTO;
import se.martenb.iv1350.project.saleprocess.integration.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.StoreDTO;
import se.martenb.iv1350.project.saleprocess.integration.StoreRegistry;

/**
 * Represents handling payment for a ended sale.
 */
public class Payment {
    private final SaleDTO saleState;
    private final StoreRegistry storeRegistry;
    private final Register register;
    private final Amount totalDiscount;
    
    /**
     * Creates a new instance of an ongoing sale and sets the sale time to
     * the current date/time. Discounts are not yet implemented and 
     * defaulted to 0. 
     * 
     * @param saleState Sale to handle payment for.
     * @param storeRegistry Registry containing store information.
     * @param register The register to put the paid amount into.
     */
    public Payment(
            SaleDTO saleState, StoreRegistry storeRegistry, Register register) {
        this.saleState = saleState;
        this.storeRegistry = storeRegistry;
        this.register = register;
        this.totalDiscount = new Amount(0);
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
        
        return receipt;
    }
}

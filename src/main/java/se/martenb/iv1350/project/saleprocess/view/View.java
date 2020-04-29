package se.martenb.iv1350.project.saleprocess.view;

import java.util.List;
import se.martenb.iv1350.project.saleprocess.controller.Controller;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Price;
import se.martenb.iv1350.project.saleprocess.integration.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.SaleDTO;

/**
 * Example of a View-class. Only for testing.
*/
public class View {
    Controller contr;
    
    private static final int ITEM_BASE_QUANTITY = 1;
     
    /**
     * Creates a new instance of a {@link View}.
     * 
     * @param contr The controller responsible for communications 
     * with the model.
     */
    public View(Controller contr) {
        this.contr = contr;
    }
    
    /**
     * Ends the sale and outputs the total price (incl. VAT).
     * 
     * @param stateOfSale State of the sale.
     */
    private void fakeExecutionEndSale(SaleDTO stateOfSale) {
        StringBuilder stringOutput = new StringBuilder();
        stringOutput.append(">> Total cost: ");
        stringOutput.append(stateOfSale.getRunningTotal());
        System.out.println(stringOutput);
    }
    
    /**
     * Get item information from the current sale about the specified item,
     * a <code>null</code> value is returned if item is not in the sale.
     * 
     * @param itemInSaleList List with items in the sale.
     * @param itemID The item ID to look for.
     * @return The found item or <code>null</code>.
     */
    private ItemInSaleDTO getItemInSale(List<ItemInSaleDTO> itemInSaleList, 
            int itemID) {
        ItemInSaleDTO foundItemInSale = null;
        for (ItemInSaleDTO itemInSale : itemInSaleList)
            if (itemInSale.getItemInfo().getItemID() == itemID)
                foundItemInSale = itemInSale;
        return foundItemInSale;
    }
    
    /**
     * Output after registering an item during fake execution.
     * 
     * @param stateOfSale The sale state.
     * @param itemID The item ID.
     * @param quantity The quantity of the item.
     */
    private void fakeExecutionItemRegistrationOutput(SaleDTO stateOfSale, 
            int itemID, int quantity) {
        ItemInSaleDTO registeredItemInSale = 
                getItemInSale(stateOfSale.getItemsInSale(), itemID);
        String itemDesciption = 
                registeredItemInSale.getItemInfo().getItemName();
        Price itemPrice = registeredItemInSale.getItemInfo().getItemPrice();
        double itemPriceTaxRate = itemPrice.getVATTaxRate();
        Amount itemPriceAfterTax = itemPrice.getPriceAfterTax();
        Amount runningTotal = stateOfSale.getRunningTotal();
        System.out.println(">> Added\n>> ║" + quantity + " pc\n>> ║" + 
                itemDesciption + "\n>> ║" + 
                itemPriceAfterTax + "/pc (VAT " + 
                itemPriceTaxRate + "%)\n>>");
        System.out.println(">> Running total: " + 
                runningTotal + "\n>>");
        
    }
    
    /**
     * Register an item during a fake execution.
     * 
     * @param itemID The item to register.
     * @return State of sale after item registration
     */
    private SaleDTO fakeExecutionItemRegistration(int itemID) {
        System.out.println("<< Entered item with ID: " + itemID);
        SaleDTO stateOfSale = this.contr.registerItem(
                itemID, ITEM_BASE_QUANTITY);
        fakeExecutionItemRegistrationOutput(stateOfSale, itemID, 
                ITEM_BASE_QUANTITY);
        return stateOfSale;
    }
    
    /**
     * Register a specified amount of an item during a fake execution.
     * 
     * @param itemID The item to register.
     * @param quantity The quantity of the item.
     * @return State of sale after item registration
     */
    private SaleDTO fakeExecutionItemRegistration(int itemID, int quantity) {
        System.out.println("<< Entered item with ID: " + itemID);
        System.out.println("<< Entered quantity: " + quantity);
        SaleDTO stateOfSale = this.contr.registerItem(
                itemID, quantity);
        fakeExecutionItemRegistrationOutput(stateOfSale, itemID, 
                quantity);
        return stateOfSale;
    }
    
    /**
     * Pay for and complete the sale.
     * 
     * @param saleState Sale to pay for.
     * @param payment Payment amount.
     * @return Information about the made purchase.
     */
    private PurchaseDTO fakeExecutionPayForSale(
            SaleDTO saleState, double payment) {
        System.out.println("<< Register paid amount: " + payment);
        Amount amountPaid = new Amount(payment);
        System.out.println("\n\n[PRINTING RECEIPT]\n\n");
        PurchaseDTO purchaseInfo = contr.registerPayment(saleState, amountPaid);
        System.out.println("\n\n[PRINTING HAS FINISHED]\n\n");
        Amount amountChange = purchaseInfo.getAmountChange();
        System.out.println(">> Change to return to customer: " + 
                amountChange);
        
        return purchaseInfo;
    }
    
    /**
     * Run a fake execution of the application, calling all implemented 
     * public operations.
     */
    public void runFakeExecution() {
        System.setProperty("console.encoding", "UTF-8");
        SaleDTO stateOfSale = null;
        int itemIDForAmazingFruitCereal = 53;
        int itemIDForLuxuryFruitDrink = 22;
        int quantityLuxuryFruitDrink = 5;
        int itemIDInvalid = -1;
        double fakePayment = 2000;
        System.out.println("<< Starting a new sale..");
        contr.startSale();
        System.out.println(">> A new sale has started");
        fakeExecutionItemRegistration(itemIDForAmazingFruitCereal);
        fakeExecutionItemRegistration(
                itemIDForLuxuryFruitDrink, quantityLuxuryFruitDrink);
        try {
            fakeExecutionItemRegistration(itemIDInvalid);
        } catch (IllegalArgumentException expectedException) {
            System.out.println(
                    ">> Invalid item identifier! Try again..\n>>");
        }
        stateOfSale = fakeExecutionItemRegistration(itemIDForAmazingFruitCereal);
        System.out.println("<< End sale..");
        fakeExecutionEndSale(stateOfSale);
        fakeExecutionPayForSale(stateOfSale, fakePayment);
        
    }
}

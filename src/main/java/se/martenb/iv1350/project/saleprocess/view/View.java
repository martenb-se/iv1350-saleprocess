package se.martenb.iv1350.project.saleprocess.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import se.martenb.iv1350.project.saleprocess.controller.Controller;
import se.martenb.iv1350.project.saleprocess.controller.OperationFailedException;
import se.martenb.iv1350.project.saleprocess.model.ItemRegistrationException;
import se.martenb.iv1350.project.saleprocess.model.IllegalItemQuantityException;
import se.martenb.iv1350.project.saleprocess.model.UnknownItemException;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.DiscountDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Quantity;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.util.ErrorLogger;
import se.martenb.iv1350.project.saleprocess.util.Price;

/**
 * Example of a View-class. Only for testing.
*/
public class View {
    Controller contr;
    ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();
    ErrorLogger errorLogger;
    
    private static final Quantity ITEM_BASE_QUANTITY = new Quantity(1);
     
    /**
     * Creates a new instance of a {@link View}.
     * 
     * @param contr The controller responsible for communications 
     * with the model.
     */
    public View(Controller contr) {
        this.contr = contr;
        errorLogger = new ErrorLogger();
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
     * @param itemID The item ID.Quantity
     * @param quantity The quantity of the item.
     */
    private void fakeExecutionItemRegistrationOutput(SaleDTO stateOfSale, 
            int itemID, Quantity quantity) {
        ItemInSaleDTO registeredItemInSale = 
                getItemInSale(stateOfSale.getItemsInSale(), itemID);
        String itemDesciption = 
                registeredItemInSale.getItemInfo().getItemName();
        Price itemPrice = registeredItemInSale.getItemInfo().getItemPrice();
        double itemPriceTaxRate = itemPrice.getVATTaxRate();
        Amount itemPriceAfterTax = itemPrice.getPriceAfterTax();
        Amount runningTotal = stateOfSale.getRunningTotal();
        System.out.println(">> Added\n>> ║" + quantity + "\n>> ║" + 
                itemDesciption + "\n>> ║" + 
                itemPriceAfterTax + "/" + quantity.getUnitString() + " (VAT " + 
                itemPriceTaxRate + "%)\n>>");
        System.out.println(">> Running total: " + 
                runningTotal + "\n>>");
        
    }
    
    /**
     * Register an item during a fake execution.
     * 
     * @param itemID The item to register.
     * @return State of sale after item registration.
     */
    private SaleDTO fakeExecutionItemRegistration(
            SaleDTO stateOfSale, int itemID) {
        return fakeExecutionItemRegistration(
                stateOfSale, itemID, ITEM_BASE_QUANTITY);
    }
    
    /**
     * Register a specified amount of an item during a fake execution.
     * 
     * @param itemID The item to register.
     * @param quantity The quantity of the item.
     * @return State of sale after item registration.
     */
    private SaleDTO fakeExecutionItemRegistration(
            SaleDTO stateOfSale, int itemID, Quantity quantity) {
        double quantityValue = quantity.getNumericalValue();
        StringBuilder registerString = new StringBuilder();
        registerString.append("<< Register item: ").append(itemID);
        if (quantityValue != ITEM_BASE_QUANTITY.getNumericalValue())
            registerString.append("\n<< Set quantity: ").append(quantity);
        System.out.println(registerString);
        try {
            SaleDTO stateOfSaleUpdated = this.contr.registerItem(
                    itemID, quantity);
            fakeExecutionItemRegistrationOutput(
                    stateOfSaleUpdated, itemID, quantity);
            return stateOfSaleUpdated;
        } catch (ItemRegistrationException exc) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Unable to register item: ");
            errorMessage.append(exc.getItemThatCannotBeRegistered());
            if(exc instanceof UnknownItemException) {
                errorMessage.append(". Item is not found in item registry.");
            } else if (exc instanceof IllegalItemQuantityException) {
                IllegalItemQuantityException excQuantity = 
                        (IllegalItemQuantityException) exc;
                errorMessage.append(". Invalid item quanitity: ");
                errorMessage.append(excQuantity.getIllegalQuantity());
                errorMessage.append(".");
            }
            errorMessageHandler.showErrorMessage(errorMessage.toString());
        } catch (OperationFailedException exc) {
            errorMessageHandler.showErrorMessage("Unable to register item, " + 
                    "please try again.");
        }
        return stateOfSale;
    }
    
    /**
     * Try discount on the sale.
     * 
     * @param saleState Sale to try discount on.
     * @param CustomerDTO Customer to try discount with.
     * @return Sale after the possible discount was applied.
     */
    private SaleDTO fakeExecutionTryDiscountOnSale(
            SaleDTO saleState, CustomerDTO customerInfo) {
        DateFormat customerIdFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("<< Try discount using customer ID: " + 
                customerIdFormat.format(customerInfo.getBirthDate()));
        DiscountDTO discountInformation = 
                contr.tryDiscountOnSale(saleState, customerInfo);
        StringBuilder discountOutput = new StringBuilder();
        SaleDTO saleStateAfterDiscountTrial = saleState;
        if(discountInformation.getDiscountPercent() == 0) {
            discountOutput.append(">> No applicable discount was found.");
        } else {
            discountOutput.append(">> Sale was discounted by: ");
            discountOutput.append(discountInformation.getDiscountAmount());
            saleStateAfterDiscountTrial = 
                    discountInformation.getSaleAfterDiscount();
        }
        
        System.out.println(discountOutput);
        fakeExecutionEndSale(saleStateAfterDiscountTrial);
        return saleStateAfterDiscountTrial;
    }
    
    /**
     * Pay for and complete the sale.
     * 
     * @param saleState Sale to pay for.
     * @param payment Payment amount.
     * @return Information about the made purchase.
     */
    private PurchaseDTO fakeExecutionPayForSale(
            SaleDTO saleState, Amount payment) {
        System.out.println("<< Register paid amount: " + payment);
        System.out.println("\n\n[PRINTING RECEIPT]");
        System.out.println("[NOTIFYING PURCHASE OBSERVERS]\n\n");
        PurchaseDTO purchaseInfo = contr.registerPayment(saleState, payment);
        System.out.println("\n\n[PURCHASE OBSERVERS HAVE BEEN NOTIFIED]");
        System.out.println("[PRINTING HAS FINISHED]\n\n");
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
        try {
            System.setProperty("console.encoding", "UTF-8");
            Date customerBirthDate = 
                    new GregorianCalendar(1950, Calendar.MAY, 15).getTime();
            CustomerDTO customerInfo = new CustomerDTO(customerBirthDate);
            TotalRevenueView totalRevenueView = 
                    TotalRevenueView.getTotalRevenueView();
            contr.addPurchaseObserver(totalRevenueView);
            SaleDTO stateOfSale = null;
            int itemIDForAmazingFruitCereal = 53;
            int itemIDForLuxuryFruitDrink = 22;
            int itemIDForHealthyChocolateSnack = 84;
            Quantity quantityLuxuryFruitDrink = new Quantity(5);
            Quantity quantityHealthyChocolateSnack = new Quantity(7);
            int itemIDInvalid = -1;
            Quantity quantityInvalid = new Quantity(0);
            int itemIDError = 999999999;
            Quantity quantityOne = new Quantity(1);
            Amount fakePayment = new Amount(2000);
            Amount fakePaymentSecondSale = new Amount(888);
            System.out.println("<< Starting a new sale..");
            contr.startSale();
            System.out.println(">> A new sale has started");
            stateOfSale = fakeExecutionItemRegistration(
                    stateOfSale, 
                    itemIDError,
                    quantityOne);
            stateOfSale = fakeExecutionItemRegistration(
                    stateOfSale, 
                    itemIDForAmazingFruitCereal);
            stateOfSale = fakeExecutionItemRegistration(
                    stateOfSale, 
                    itemIDForLuxuryFruitDrink, 
                    quantityLuxuryFruitDrink);
            stateOfSale = fakeExecutionItemRegistration(
                    stateOfSale, 
                    itemIDInvalid);
            stateOfSale = fakeExecutionItemRegistration(
                    stateOfSale, 
                    itemIDForAmazingFruitCereal);
            stateOfSale = fakeExecutionItemRegistration(
                    stateOfSale, 
                    itemIDForAmazingFruitCereal,
                    quantityInvalid);
            System.out.println("<< End sale..");
            fakeExecutionEndSale(stateOfSale);
            stateOfSale = fakeExecutionTryDiscountOnSale(stateOfSale, 
                    customerInfo);
            fakeExecutionPayForSale(stateOfSale, fakePayment);
            SaleDTO stateOfSecondSale = null;
            System.out.println("<< Starting a new sale..");
            contr.startSale();
            System.out.println(">> A new sale has started");
            stateOfSecondSale = fakeExecutionItemRegistration(
                    stateOfSecondSale, 
                    itemIDForHealthyChocolateSnack,
                    quantityHealthyChocolateSnack);
            System.out.println("<< End sale..");
            fakeExecutionEndSale(stateOfSecondSale);
            fakeExecutionPayForSale(stateOfSecondSale, fakePaymentSecondSale);
        } catch (Exception exc) {
            StringBuilder errorMessage = new StringBuilder();
            errorLogger.logException(exc);
            errorMessage.append("An unexpected error occured. "+
                    "Please restart the program.");
            errorMessageHandler.showErrorMessage(errorMessage.toString());
        }
    }
}

package se.martenb.iv1350.project.saleprocess.controller;

import se.martenb.iv1350.project.saleprocess.model.UnknownItemException;
import se.martenb.iv1350.project.saleprocess.model.ItemRegistrationException;
import se.martenb.iv1350.project.saleprocess.util.ErrorLogger;
import se.martenb.iv1350.project.saleprocess.integration.AccountingRegistry;
import se.martenb.iv1350.project.saleprocess.integration.discount.DiscountRegistry;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemDTO;
import se.martenb.iv1350.project.saleprocess.integration.ItemRegistry;
import se.martenb.iv1350.project.saleprocess.integration.ItemRegistryException;
import se.martenb.iv1350.project.saleprocess.integration.Printer;
import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ReceiptDTO;
import se.martenb.iv1350.project.saleprocess.integration.RegistryCreator;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.StoreRegistry;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.DiscountDTO;
import se.martenb.iv1350.project.saleprocess.model.Discount;
import se.martenb.iv1350.project.saleprocess.model.Payment;
import se.martenb.iv1350.project.saleprocess.model.Register;
import se.martenb.iv1350.project.saleprocess.model.Sale;
import se.martenb.iv1350.project.saleprocess.model.PurchaseObserver;
import se.martenb.iv1350.project.saleprocess.util.Quantity;

/**
 * Controller responsible for communications between the user and the model and
 * integration.
 */
public class Controller {
    private Printer printer;
    private Register register;
    private RegistryCreator registryCreator;
    private ItemRegistry itemRegistry;
    private AccountingRegistry accountingRegistry;
    private StoreRegistry storeRegistry;
    private DiscountRegistry discountRegistry;
    private Discount discount;
    private Payment payment;
    private Sale sale;
    ErrorLogger errorLogger;
    
    /**
     * Creates a new instance of a {@link Controller}. 
     * 
     * @param printer The printer later responsible for printing receipts.
     */
    public Controller(Printer printer) {
        this.printer = printer;
        register = new Register();
        registryCreator = new RegistryCreator();
        importExternalRegistries();
        discount = new Discount(discountRegistry.getCompositeDiscountRule());
        payment = new Payment(storeRegistry, register);
        errorLogger = new ErrorLogger();
    }
    
    /**
     * Import external registries needed by the application.
     */
    private void importExternalRegistries() {
        itemRegistry = registryCreator.getItemRegistry();
        accountingRegistry = registryCreator.getAccountingRegistry();
        storeRegistry = registryCreator.getStoreRegistry();
        discountRegistry = DiscountRegistry.getDiscountRegistry();
    }
    
    /**
     * Start a new sale and create a new {@link Sale} instance.
     */
    public void startSale() {
        sale = new Sale();
    }
    
    /**
     * Register an item to the ongoing sale.
     * 
     * @param itemID Item to add onto the sale.
     * @param quantity How many of the item to add onto the sale.
     * @return The state of the sale information after adding the item(s).
     * @throws ItemRegistrationException If the item could not be registered 
     *                                   because of a user error.
     * @throws OperationFailedException If the item could not be registered 
     *                                  because of an error not caused by the 
     *                                  user.
     */
    public SaleDTO registerItem(int itemID, Quantity quantity) 
            throws ItemRegistrationException, OperationFailedException {
        SaleDTO stateOfSale = null;
        try {
            boolean isItemInRegistry = itemRegistry.isItemInDB(itemID);
            if (!isItemInRegistry)
                throw new UnknownItemException(itemID);
            ItemDTO itemInfo = itemRegistry.getItemInfo(itemID);
            stateOfSale = sale.addItemToSale(itemInfo, quantity);
        } catch (ItemRegistryException exc) {
            errorLogger.logException(exc);
            throw new OperationFailedException("Failed to register the " + 
                    "specified item.");
        }
        return stateOfSale;
    }
    
    /**
     * Try discounts on sale using the specified customer information.
     * 
     * @param saleState Sale to try discounts on.
     * @param customerInformaton The customer to try discounts with.
     * @return Information about the possible discount.
     */
    public DiscountDTO tryDiscountOnSale(
            SaleDTO saleState, CustomerDTO customerInformaton) {
        discount.startDiscount(saleState);
        DiscountDTO discountInformation = 
                discount.tryDiscountOnSale(customerInformaton);
        return discountInformation;
    }
    
    /**
     * Register payment and end the sale.
     * 
     * @param saleState Sale to register payment to.
     * @param amountPaid The paid amount.
     * @return Information about the purchase
     */
    public PurchaseDTO registerPayment(SaleDTO saleState, Amount amountPaid) {
        payment.startPayment(saleState);
        ReceiptDTO receipt = payment.pay(amountPaid);
        accountingRegistry.bookkeep(receipt.getSaleInfo());
        itemRegistry.stocktacke(receipt.getSaleInfo());
        printer.print(receipt);
        
        return receipt.getPurchaseInfo();
    }
    
   /**
     * Add a {@link PurchaseObserver} to a payment handler. 
     * 
     * @param purchaseObserver The observer to add.
     */
    public void addPurchaseObserver(PurchaseObserver purchaseObserver) {
        payment.addPurchaseObserver(purchaseObserver);
    }
}
package se.martenb.iv1350.project.saleprocess.controller;

import se.martenb.iv1350.project.saleprocess.integration.AccountingRegistry;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.ItemDTO;
import se.martenb.iv1350.project.saleprocess.integration.ItemRegistry;
import se.martenb.iv1350.project.saleprocess.integration.Printer;
import se.martenb.iv1350.project.saleprocess.integration.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.ReceiptDTO;
import se.martenb.iv1350.project.saleprocess.integration.RegistryCreator;
import se.martenb.iv1350.project.saleprocess.integration.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.StoreRegistry;
import se.martenb.iv1350.project.saleprocess.model.Payment;
import se.martenb.iv1350.project.saleprocess.model.Register;
import se.martenb.iv1350.project.saleprocess.model.Sale;

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
    private Sale sale;
    
    /**
     * Creates a new instance of a <code>Controller</code>. 
     * 
     * @param printer The printer later responsible for printing receipts.
     */
    public Controller(Printer printer) {
        this.printer = printer;
        this.register = new Register();
        this.registryCreator = new RegistryCreator();
        importExternalRegistries();
    }
    
    /**
     * Import external registries needed by the application.
     */
    private void importExternalRegistries() {
        this.itemRegistry = this.registryCreator.getItemRegistry();
        this.accountingRegistry = this.registryCreator.getAccountingRegistry();
        this.storeRegistry = this.registryCreator.getStoreRegistry();
    }
    
    /**
     * Start a new sale and create a new <code>Sale</code> instance.
     */
    public void startSale() {
        this.sale = new Sale();
    }
    
    /**
     * Register an item to the ongoing sale, if the item is unknown an exception
     * of <code>IllegalArgumentException</code> will be thrown.
     * 
     * @param itemID Item to add onto the sale.
     * @param quantity How many of the item to add onto the sale.
     * @return The state of the sale information after adding the item(s).
     */
    public SaleDTO registerItem(int itemID, int quantity) 
            throws IllegalArgumentException {
        ItemDTO itemInfo = itemRegistry.getItemInfo(itemID);
        if(itemInfo == null)
            throw new IllegalArgumentException("Unknown item");
        SaleDTO stateOfSaleAfterAdding = 
                sale.addItemToSale(itemInfo, quantity);
        return stateOfSaleAfterAdding;
    }
    
    /**
     * Register payment and end the sale.
     * @param saleState Sale to register payment to.
     * @param amountPaid The paid amount.
     * @return Information about the purchase
     */
    public PurchaseDTO registerPayment(SaleDTO saleState, Amount amountPaid) {
        Payment payment = new Payment(saleState, storeRegistry, register);
        ReceiptDTO receipt = payment.pay(amountPaid);
        accountingRegistry.bookkeep(receipt.getSaleInfo());
        itemRegistry.stocktacke(receipt.getSaleInfo());
        printer.print(receipt);
        
        return receipt.getPurchaseInfo();
    }
}
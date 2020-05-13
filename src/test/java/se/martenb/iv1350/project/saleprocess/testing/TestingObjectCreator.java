package se.martenb.iv1350.project.saleprocess.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import se.martenb.iv1350.project.saleprocess.integration.dto.AddressDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Price;
import se.martenb.iv1350.project.saleprocess.util.PriceTotal;
import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.util.Quantity;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.StoreDTO;
import se.martenb.iv1350.project.saleprocess.util.Unit;

/**
 * An object creator for tests, helping with the creation of objects used
 * in various tests. 
 */
public class TestingObjectCreator {
    private final String streetName = "Testing Street";
    private final int streetNumber = 99;
    private final int postalCode = 12345;
    private final String postTown = "Experiment City";
    private final String storeName = "Trial Store";
    private LocalDateTime saleDateTime;
    private Amount totalPriceBeforeTaxes = new Amount(0);
    private Amount totalPriceAfterTaxes = new Amount(0);
    private Amount totalTaxes = new Amount(0);
    private int lastTotalItems = 0;
    private PriceTotal lastFinalPrice = null;
    
    public Amount makeAmount(double amount) {
        return new Amount(amount);
    }
    
    public Price makePrice(double priceBeforeTaxes, double vatTaxRate) {
        Amount amountBeforeTaxes = makeAmount(priceBeforeTaxes);
        return new Price(amountBeforeTaxes, vatTaxRate);
    }
    
    public ItemDTO makeItemDTO(int itemID, String itemName, Price itemPrice) {
        return new ItemDTO(itemID, itemName, itemPrice);
    }
    
    public ItemDTO makeItemDTOSimple(int itemID, String itemName, 
            double priceBeforeTaxes, double vatTaxRate) {
        Price itemPrice = makePrice(priceBeforeTaxes, 
                vatTaxRate);
        return makeItemDTO(itemID, itemName, itemPrice);
    }
    
    public CustomerDTO makeCustomerDTOSimple(int bithYear, int bithMonth, 
            int birthDay) {
        Date birthDate = 
                new GregorianCalendar(bithYear, bithMonth, birthDay).getTime();
        CustomerDTO customerInfo = new CustomerDTO(birthDate);
        return customerInfo;
    }
    
    public ItemInSaleDTO findItemInSale(List<ItemInSaleDTO> itemsInSale,
            int itemID) {
        ItemInSaleDTO foundItemInSale = null;
        for (ItemInSaleDTO itemInSale : itemsInSale)
            if (itemInSale.getItemInfo().getItemID() == itemID)
                foundItemInSale = itemInSale;
        return foundItemInSale;
    }
    
    public boolean wasItemAddedToSale(SaleDTO saleDTO, int itemID) {
        List<ItemInSaleDTO> itemsInSaleState = saleDTO.getItemsInSale();
        ItemInSaleDTO foundItemInSale = findItemInSale(itemsInSaleState, 
                itemID);
        return foundItemInSale != null;
    }
    
    public double quantityOfItemAddedToSale(SaleDTO saleDTO, int itemID) {
        List<ItemInSaleDTO> itemsInSaleState = saleDTO.getItemsInSale();
        ItemInSaleDTO foundItemInSale = findItemInSale(
                itemsInSaleState, itemID);
        if (foundItemInSale != null)
            return foundItemInSale.getItemQuantity().getNumericalValue();
        else
            return 0;
    }
    
    public int getTotalItemsOfSale(SaleDTO saleDTO) {
        return saleDTO.getTotalItems();
    }
    
    public AddressDTO generateAddressDTO() {
        return new AddressDTO(streetName, streetNumber, postalCode, postTown);
    }
    
    public StoreDTO getStoreDTO() {
        AddressDTO storeAddress = generateAddressDTO();
        return new StoreDTO(storeName, storeAddress);
    }
    
    public Price generatePrice(int itemID) {
        double startBasePrice = 10.00;
        double startVATTaxRateOfItem = 15.00;
        double generatedBasePrice = startBasePrice + itemID * 5;
        double generatedVATTaxRateOfItem = 
                startVATTaxRateOfItem - itemID * 0.2;
        Amount amountBasePrice = new Amount(generatedBasePrice);
        return new Price(amountBasePrice, generatedVATTaxRateOfItem);
    }
    
    public ItemDTO generateItemDTO(int itemID) {
        String itemName = "Test Item #" + String.format("%03d" , itemID);
        Price itemPrice = generatePrice(itemID);
        return new ItemDTO(itemID, itemName, itemPrice);
    }
    
    public ItemInSaleDTO generatePieceOfItemInSaleDTO(int itemID) {
        int numericalQuantity = 1 + itemID % 5;
        Quantity itemQuantity = new Quantity(numericalQuantity, Unit.PIECE);
        ItemDTO itemInfo = generateItemDTO(itemID);
        lastTotalItems += numericalQuantity;
        Amount itemTotalPriceBeforeTax = 
                itemInfo.getItemPrice().
                        getPriceBeforeTax().
                        multiply(numericalQuantity);
        Amount itemTotalPriceAfterTax = 
                itemInfo.getItemPrice().
                        getPriceAfterTax().
                        multiply(numericalQuantity);
        Amount itemTotalTaxes = 
                itemInfo.getItemPrice().
                        getTaxesAmount().
                        multiply(numericalQuantity);
        totalPriceBeforeTaxes =
                totalPriceBeforeTaxes.plus(itemTotalPriceBeforeTax);
        totalPriceAfterTaxes = 
                totalPriceAfterTaxes.plus(itemTotalPriceAfterTax);
        totalTaxes = totalTaxes.plus(itemTotalTaxes);
        return new ItemInSaleDTO(itemInfo, itemQuantity);
    }
    
    public List<ItemInSaleDTO> generateItemInSaleList(
            int numberOfItemsToGenerate) {
        List<ItemInSaleDTO> itemInSaleList = new ArrayList<>();
        for (int i = 1; i <= numberOfItemsToGenerate; i++) {
            itemInSaleList.add(generatePieceOfItemInSaleDTO(i));
        }
        return itemInSaleList;
    }
    
    public SaleDTO generateSaleDTOAndFinalPrice(int numberOfEntriesInSale) {
        lastTotalItems = 0;
        totalPriceBeforeTaxes = new Amount(0);
        totalPriceAfterTaxes = new Amount(0);
        totalTaxes = new Amount(0);
        saleDateTime = LocalDateTime.now();
        List<ItemInSaleDTO> itemInSaleList = 
                generateItemInSaleList(numberOfEntriesInSale);
        SaleDTO generatedSaleDTO = new SaleDTO(saleDateTime, totalPriceAfterTaxes, 
            itemInSaleList, lastTotalItems);
        lastFinalPrice = new PriceTotal(totalPriceAfterTaxes, 
            totalPriceBeforeTaxes, totalTaxes);
        return generatedSaleDTO;
    }
    
    public PriceTotal getLastFinalPrice() {
        return lastFinalPrice;
    }
    
    public PurchaseDTO generateLastPurchaseDTO(PriceTotal finalPrice, 
            Amount amountPaid) {
        Amount amountChange = 
                amountPaid.minus(finalPrice.getTotalPriceAfterTaxes());
        return new PurchaseDTO(finalPrice, amountPaid, amountChange);
    }
}

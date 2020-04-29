package se.martenb.iv1350.project.saleprocess.integration;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.util.Price;
import java.util.ArrayList;
import java.util.List;

/**
 * ItemRegistry handles communications with an external item 
 * inventory/system.
 */
public class ItemRegistry {
    private List<ItemDTO> dummyItemDB = new ArrayList<>();
    
    /**
     * Initializes the {@link ItemRegistry} instance for communications 
     * with the external system. Adds 100 dummy items to a fake database for 
     * testing.
     */
    public ItemRegistry() {
        dummyMakeAndAdd100ItemsToDB();
    }
    
    /**
     * NOT FOR PRODUCTION
     * 
     * Dummy generation for the {@link Price} of an item.
     * 
     * @param priceOfItem Price before taxes on the dummy item
     * @param vatTaxRateOfItem VAT tax rate of the dummy item
     * @return Price information for the dummy item.
     */
    private Price dummyGeneratePriceForItem(double priceOfItem, 
            double vatTaxRateOfItem) {
        Amount priceBeforeTaxes = new Amount(priceOfItem);
        return new Price(priceBeforeTaxes, vatTaxRateOfItem);
    }
    
    /**
     * NOT FOR PRODUCTION
     * 
     * Dummy generation of {@link ItemDTO} representing an item in 
     * the database.
     * 
     * @param itemID The ID of the item.
     * @param itemName The name of the item.
     * @param priceOfItem The price of the item before taxes.
     * @param vatTaxRateOfItem The tax rate for the price of the item.
     * 
     * @return The dummy item as an <code>ItemDTO</code>.
     */
    private ItemDTO dummyCreateItem(int itemID, String itemName, 
            double priceOfItem, double vatTaxRateOfItem) {
        Price itemPrice = dummyGeneratePriceForItem(priceOfItem, 
                vatTaxRateOfItem);
        return new ItemDTO(itemID, itemName, itemPrice);
    }
    
    /**
     * NOT FOR PRODUCTION
     * 
     * Add a dummy item to the fake database.
     * 
     * @param itemToAdd The dummy item to be added.
     */
    private void dummyAddItemToDB(ItemDTO itemToAdd) {
        dummyItemDB.add(itemToAdd);
    }
    
    /**
     * NOT FOR PRODUCTION
     * 
     * Create and add 100 dummy items to the fake database, all items have 
     * different IDs, names, prices and tax rates.
     */
    private void dummyMakeAndAdd100ItemsToDB() {
        double dummyStartBasePrice = 1.07;
        double dummyStartVATTaxRateOfItem = 12.00;
        String[] dummyItemNamesPart1Array = new String[] {"Tasty","Luxury",
            "Amazing","Organic","Healthy"};
        String[] dummyItemNamesPart2Array = new String[] {"Chocolate","Fruit",
            "Banana","Strawberry","Blueberry"};
        String[] dummyItemNamesPart3Array = new String[] {"Cereal","Drink",
            "Cookies","Snack"};
        for (int i = 0; i < 100; i++) {
            ItemDTO dummyItem = dummyCreateItem(i+1, 
                    dummyItemNamesPart1Array[i/20] + " " + 
                            dummyItemNamesPart2Array[(i/4)%4] + " " +
                            dummyItemNamesPart3Array[i%4], 
                    dummyStartBasePrice * (i+1), 
                    dummyStartVATTaxRateOfItem - i*0.03);
            dummyAddItemToDB(dummyItem);
        }
    }
    
    /**
     * NOT FOR PRODUCTION
     * 
     * Dummy for matching an item id to an item row in a database. 
     * 
     * @param itemID Item ID to match.
     * @param itemDTO ItemDTO to match item ID with.
     * @return If the item id matched the ItemDTO or not.
     */
    private boolean dummyDoesItemIDMatchItemInDB(int itemID, ItemDTO itemDTO) {
        int idOfItemDTO = itemDTO.getItemID();
        boolean doesItemIDMatchItemDTO = 
                itemID == idOfItemDTO;
        return doesItemIDMatchItemDTO;
    }
    
    /**
     * NOT FOR PRODUCTION
     * 
     * Dummy search in item database looking through all items until an item
     * matches the specified ID. If a match is found, row data will be returned
     * in an {@link ItemDTO} object. If there's no match, a null value is
     * returned.
     * 
     * @param itemID The item id to search for.
     * @return {@link ItemDTO} object containing information about found
     * item or null if no item was found.
     */
    private ItemDTO dummySequentialIDSearchInDB(int itemID) {
        ItemDTO foundItem = null;
        for (ItemDTO itemInDB : dummyItemDB)
            if(dummyDoesItemIDMatchItemInDB(itemID, itemInDB))
                foundItem = itemInDB;
        return foundItem; 
    }
    
    /**
     * Get information about the item from the database specified by the
     * item id. If no item is found with the ID, a <code>null</code> value 
     * is returned.
     * 
     * @param itemID Item ID to look for.
     * @return The item containing the item ID or null if no item was found.
     */
    public ItemDTO getItemInfo(int itemID) {
        ItemDTO foundItem = dummySequentialIDSearchInDB(itemID);
        return foundItem;
    } 
    
    /**
     * Dirty dummy for sending sale information to the external 
     * item inventory/registry system.
     * 
     * @param saleInformation Information to send to the external system.
     */
    public void stocktacke(SaleDTO saleInformation) {
        /*
        Code does nothing for now.
        */
    }
}
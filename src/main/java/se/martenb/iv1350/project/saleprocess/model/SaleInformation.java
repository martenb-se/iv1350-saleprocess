package se.martenb.iv1350.project.saleprocess.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.ItemDTO;
import se.martenb.iv1350.project.saleprocess.integration.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Quantity;
import se.martenb.iv1350.project.saleprocess.integration.SaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Unit;

/**
 * Represents sale information for ongoing sale.
 */
public class SaleInformation {
    private static final Unit UNIT_PIECE = Unit.PIECE;
    
    private LocalDateTime saleTime;
    private Amount runningTotal;
    private List<Item> itemList = new ArrayList<>();
    private int totalItems = 0;

    /**
     * Creates a new instance of a sale information object that goes along
     * with an ongoing sale.
     */
    public SaleInformation() {
        runningTotal = new Amount(0);
    }
    
    /**
     * Updates the sale time to the current time.
     */
    void updateSaleTime() {
        this.saleTime = LocalDateTime.now();
    }
    
    /**
     * Get the time from the sale information.
     * 
     * @return Sale time ({@link LocalDateTime}) for current sale.
     */
    public LocalDateTime getSaleTime() {
        return this.saleTime;
    }
    
    /**
     * See if an {@link Item} object is uniquely identified by a
     * specified {@link ItemDTO}. 
     * 
     * @param itemCompare Item to compare against an ItemDTO.
     * @param itemDTOMatch ItemDTO to match the specified Item.
     * @return If the Item was identified by the specified ItemDTO.
     */
    private boolean doesItemAndItemDTOMatch(Item itemCompare, 
            ItemDTO itemDTOMatch) {
        int idOfItemCompare = 
                itemCompare.getItemInSale().getItemInfo().getItemID();
        int idOfItemDTOMatch = itemDTOMatch.getItemID();
        boolean doesBothItemsHaveTheSameID = 
                idOfItemCompare == idOfItemDTOMatch;
        return doesBothItemsHaveTheSameID;
    }
    
    /**
     * See if item is in current sale, returns <code>true</code> if the item
     * with its unique ID is found among the items in the current sale, returns
     * <code>false</code> no match among all the items in sale was found.
     * 
     * @param itemInfo The item to search for.
     * @return If the item was in the current sale or not.
     */
    private boolean isItemInSale(ItemDTO itemInfo) {
        for (Item itemInSale : itemList)
            if (doesItemAndItemDTOMatch(itemInSale, itemInfo))
                return true;
        return false;
    }
    
    /**
     * Get the {@link Item} in sale that matches the specified 
     * {@link ItemDTO}. If there's no match, a <code>null</code> value 
     * will be returned.
     * 
     * @param itemInfo The item to look for.
     * @return The found {@link Item} or <code>null</code>.
     */
    private Item getItemInSale(ItemDTO itemInfo) {
        Item foundItemInSale = null;
        for (Item itemInSale : itemList)
            if (doesItemAndItemDTOMatch(itemInSale, itemInfo))
                foundItemInSale = itemInSale;
        
        return foundItemInSale;
    }
    
    /**
     * Increases the quantity amount of an item in the current sale.
     * 
     * @param itemInfo The item to increase the quantity of.
     * @param quantityValue How much to increase the quantity value with.
     */
    private void increaseItemQuantity(ItemDTO itemInfo, double quantityValue) {
        Item itemToIncreaseQuantityOf = getItemInSale(itemInfo);
        if (itemToIncreaseQuantityOf != null)
            itemToIncreaseQuantityOf.addQuantity(quantityValue);
    }
    
    /**
     * Create a new piece of an {@link Item}.
     * 
     * @param itemInfo What item to create.
     * @param quantity How many of the item to create.
     * @return 
     */
    private Item createPieceOfItemToPutInSale(ItemDTO itemInfo, int quantity) {
        Quantity quantiyOfItem = new Quantity(quantity, UNIT_PIECE);
        ItemInSaleDTO itemInformation = new ItemInSaleDTO(itemInfo, 
                quantiyOfItem);
        return new Item(itemInformation);
    }
    
    /**
     * Put a new piece of an {@link Item} in the current sale defined by an 
     * {@link ItemDTO} with the specified amount.
     * 
     * @param itemInfo What item to put into the sale.
     * @param quantityValue How many to put into the sale.
     */
    private void putNewPieceOfItemInSale(ItemDTO itemInfo, int quantity) {
        Item newItemToPutInSale = 
                createPieceOfItemToPutInSale(itemInfo, quantity);
        itemList.add(newItemToPutInSale);
    }
    
    /**
     * Increases the count of total pieces of items in the current sale.
     * Including duplicates of the same item and different items.
     * 
     * @param quantity How much to increase the number of items with.
     */
    private void increaseTotalPiecesOfItems(int quantity) {
        totalItems += quantity;
    }
    
    /**
     * Update the running total {@link Amount} with the total price of the 
     * item(s) in specified quantity.
     * 
     * @param itemInfo Item to add the cost of.
     * @param quantity How many of the items to add the cost of.
     */
    private void updateRunningTotal(ItemDTO itemInfo, int quantity) {
        Amount amountCostOfAddedItems = 
                itemInfo.getItemPrice().getPriceAfterTax().multiply(quantity);
        runningTotal = runningTotal.plus(amountCostOfAddedItems);
    }
    
    /**
     * Add specified item to the current sale.
     * 
     * @param itemInfo Item to add to the sale.
     * @param quantity How many to add to the sale.
     */
    void addItemToSale(ItemDTO itemInfo, int quantity) 
            throws IllegalArgumentException {
        if (quantity <= 0)
            throw new IllegalArgumentException(
                    "Quantity must be positive and greater than 0");
        if (isItemInSale(itemInfo))
            increaseItemQuantity(itemInfo, quantity);
        else
            putNewPieceOfItemInSale(itemInfo, quantity);
        increaseTotalPiecesOfItems(quantity);
        updateRunningTotal(itemInfo, quantity);
    }
    
    /**
     * Get a list with the currents sales' items 
     * {@link ItemInSaleDTO} objects.
     * @return 
     */
    private List<ItemInSaleDTO> getListForItemInSaleDTO() {
        List<ItemInSaleDTO> listItemInSaleDTO = new ArrayList<>(); 
        for (Item itemInSale : itemList)
            listItemInSaleDTO.add(itemInSale.getItemInSale());
        return listItemInSaleDTO;
    }
    
    /**
     * Get {@link SaleDTO} object of current sale state.
     * 
     * @return A SaleDTO object with information about current sale.
     */
    SaleDTO getSaleDTO() {
        List<ItemInSaleDTO> itemInSaleDTOList = getListForItemInSaleDTO();
        SaleDTO saleDTO = new SaleDTO(saleTime, runningTotal, 
                itemInSaleDTOList, totalItems);
        return saleDTO;
    }
    
    /**
     * Get running total for the sale.
     * 
     * @return {@link Amount} of running total.
     */
    Amount getRunningTotal() {
        return runningTotal;
    }
    
}

package se.martenb.iv1350.project.saleprocess.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Quantity;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Unit;

/**
 * Represents sale information for ongoing sale.
 */
class SaleInformation {
    private LocalDateTime saleTime;
    private Amount runningTotal;
    private List<Item> itemList = new ArrayList<>();
    private int totalItems = 0;

    /**
     * Creates a new instance of a sale information object that goes along
     * with an ongoing sale.
     */
    SaleInformation() {
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
    LocalDateTime getSaleTime() {
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
     * @param itemInfo The item to increase the {@link Quantity} of.
     * @param quantity How much to increase the {@link Quantity} with.
     */
    private void increaseItemQuantity(ItemDTO itemInfo, Quantity quantity) {
        double quantityValue = quantity.getNumericalValue();
        Item itemToIncreaseQuantityOf = getItemInSale(itemInfo);
        if (itemToIncreaseQuantityOf != null)
            itemToIncreaseQuantityOf.addQuantity(quantityValue);
        if (quantity.getUnitType() == Unit.PIECE)
            updateTotalPiecesOfItems(quantity);
    }
    
    /**
     * Create a new {@link Item}.
     * 
     * @param itemInfo What item to create.
     * @param quantity The {@link Quantity} of the item.
     * @return 
     */
    private Item createItemToPutInSale(ItemDTO itemInfo, Quantity quantity) {
        ItemInSaleDTO itemInformation = new ItemInSaleDTO(itemInfo, quantity);
        return new Item(itemInformation);
    }
    
    /**
     * Put a new {@link Item} in the current sale defined by an {@link ItemDTO} 
     * with the specified {@link Quantity}. Updates the total number of items
     * added to the sale.
     * 
     * @param itemInfo What item to put into the sale.
     * @param quantity How many to put into the sale.
     */
    private void putNewItemInSale(ItemDTO itemInfo, Quantity quantity) {
        Item newItemToPutInSale = createItemToPutInSale(itemInfo, quantity);
        itemList.add(newItemToPutInSale);
        updateTotalPiecesOfItems(quantity);
    }
    
    /**
     * Increases the count of total pieces of items in the current sale.
     * Including duplicates of the same item and different items. Items that 
     * are weighed or measured by volume will count as one piece.
     * 
     * @param quantity How much to increase the number of items with.
     */
    private void updateTotalPiecesOfItems(Quantity quantity) {
        int quantityValue = (int) quantity.getNumericalValue();
        if(quantity.getUnitType() == Unit.PIECE)
            totalItems += quantityValue;
        else
            totalItems += 1;
    }
    
    /**
     * Update the running total {@link Amount} with the total price of the 
     * item(s) in specified {@link Quantity}.
     * 
     * @param itemInfo Item to add the cost of.
     * @param quantity The {@link Quantity} of items to add the cost of.
     */
    private void updateRunningTotal(ItemDTO itemInfo, Quantity quantity) {
        double quantityValue = quantity.getNumericalValue();
        Amount amountCostOfAddedItems = 
                itemInfo.
                        getItemPrice().
                        getPriceAfterTax().
                        multiply(quantityValue);
        runningTotal = runningTotal.plus(amountCostOfAddedItems);
    }
    
    /**
     * Add specified item to the current sale.
     * 
     * @param itemInfo Item to add to the sale.
     * @param quantity The quantity of the item to add to the sale.
     */
    void addItemToSale(ItemDTO itemInfo, Quantity quantity) 
            throws IllegalItemQuantityException {
        double numericalQuantity = quantity.getNumericalValue();
        if (numericalQuantity <= 0) {
            int itemID = itemInfo.getItemID();
            throw new IllegalItemQuantityException(itemID, quantity);
        }
        if (isItemInSale(itemInfo))
            increaseItemQuantity(itemInfo, quantity);
        else
            putNewItemInSale(itemInfo, quantity);
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

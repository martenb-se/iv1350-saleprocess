package se.martenb.iv1350.project.saleprocess.integration.discount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Amount;

/**
 * Handler for verifying and applying discount onto a sale.
 */
class DiscountApplier {
    private SaleDTO saleInfo;
    private final CustomerDTO customerInfo;
    private Map<String,Double> itemNameContains = new HashMap<>();

    /**
     * Creates a new discount applier for the specified sale.
     * 
     * @param saleInfo The sale to apply discounts to.
     * @param The customer that might be eligible for discounts.
     */
    DiscountApplier(SaleDTO saleInfo, CustomerDTO customerInfo) {
        this.saleInfo = saleInfo;
        this.customerInfo = customerInfo;
    }
    
    /**
     * Set rules for when item name contains specific text.
     * 
     * @param itemNameContains The "item name contains" rules.
     * @param The rules for when item name contains specific text.
     */
    void setItemNameContains(Map<String,Double> itemNameContains) {
        this.itemNameContains = new HashMap<>(itemNameContains);
    }
    
    /**
     * Get the sale info with any applied discount.
     * 
     * @return The sale info.
     */
    SaleDTO getSaleInfo() {
        return saleInfo;
    }
    
    /**
     * See if today's date is within the right period.
     * 
     * @param startDate The start of the period.
     * @param endDate The end of the period.
     * @return True if today's date is within the right period.
     */
    boolean isItTheRightPeriod(Date startDate, Date endDate) {
        Date todayDate = new Date();
        boolean isTodayWithinPeriod = 
                !(todayDate.before(startDate) || todayDate.after(endDate));
        return isTodayWithinPeriod;
    }
    
    /**
     * See if customer is old enough.
     * 
     * @return True if today's date is within the right period.
     */
    boolean isCustomerOldEnough(Date requiredDate) {
        Date customerBirhDate = customerInfo.getBirthDate();
        boolean isCustomerOldEnough = requiredDate.after(customerBirhDate);
        return isCustomerOldEnough;
    }
    
    /**
     * Go through all items within the sale and see if an item approves
     * summer discounts.
     */
    boolean isApplicableItemInSale() {
        boolean foundItemThatApprovesDiscount = false;
        for(ItemInSaleDTO itemInSale : saleInfo.getItemsInSale()) {
            if (doesItemHaveApplicableNameContainsDiscount(itemInSale)) {
                foundItemThatApprovesDiscount = true;
                break;
            }
        }
        return foundItemThatApprovesDiscount;
    }
    
    /**
     * Calculate running total for items in sale.
     * 
     * @param itemsInSale The item list to calculate running total for.
     */
    private Amount calculateRunningTotal(List<ItemInSaleDTO> itemsInSale) {
        Amount runningTotal = new Amount(0);
        for(ItemInSaleDTO itemInSale : itemsInSale) {
            double numericalQuantity = 
                    itemInSale.
                            getItemQuantity().
                            getNumericalValue();
            Amount itemTotal = 
                    itemInSale.
                            getItemInfo().
                            getItemPrice().
                            getPriceAfterTax().
                            multiply(numericalQuantity);
            runningTotal = runningTotal.plus(itemTotal);
        }
        return runningTotal;
    }
    
    /**
     * Apply discounts that are specified for individual items and update
     * sale with the latest item information and running total.
     */
    void applyPerItemRules() {
        LocalDateTime saleDateTime = saleInfo.getSaleDateTime();
        int saleTotalItems = saleInfo.getTotalItems();
        List<ItemInSaleDTO> itemsInSaleWithDiscounted = 
                makeItemInSaleListAfterDiscounts();
        Amount saleRunningTotalUpdated = 
                calculateRunningTotal(itemsInSaleWithDiscounted);
        SaleDTO saleInfoUpdated = new SaleDTO(
                saleDateTime, 
                saleRunningTotalUpdated, 
                itemsInSaleWithDiscounted, 
                saleTotalItems);
        
        saleInfo = saleInfoUpdated;
    }
    
    /**
     * Apply a full sale discount and update sale with the latest 
     * running total.
     * 
     * @param The percent of the full sale discount.
     */
    void applyFullSaleDiscount(double fullSaleDiscountPercent) {
        LocalDateTime saleDateTime = saleInfo.getSaleDateTime();
        int saleTotalItems = saleInfo.getTotalItems();
        List<ItemInSaleDTO> itemsInSaleWithDiscounted = 
            makeItemInSaleListAfterFullSaleDiscount(fullSaleDiscountPercent);
        Amount saleRunningTotalUpdated = 
                calculateRunningTotal(
                        itemsInSaleWithDiscounted);
        SaleDTO saleInfoUpdated = new SaleDTO(
                saleDateTime, 
                saleRunningTotalUpdated, 
                itemsInSaleWithDiscounted, 
                saleTotalItems);
        
        saleInfo = saleInfoUpdated;
    }
    
    /**
     * Create a new item in sale list after applying discounts to
     * applicable items for the specified sale.
     */
    private List<ItemInSaleDTO> makeItemInSaleListAfterDiscounts() {
        List<ItemInSaleDTO> itemsInSaleWithDiscounted = new ArrayList<>();
        for(ItemInSaleDTO itemInSale : saleInfo.getItemsInSale()) {
            if (doesItemHaveApplicableNameContainsDiscount(itemInSale)) {
                double percentDiscount = 
                        getItemNameContainsDiscount(itemInSale);
                ItemInSaleDTO itemInSaleDiscounted = 
                        itemInSale.applyDiscount(percentDiscount);
                itemsInSaleWithDiscounted.add(itemInSaleDiscounted);
            } else {
                itemsInSaleWithDiscounted.add(itemInSale);
            }
        }
        return itemsInSaleWithDiscounted;
    }
    
    /**
     * Create a new item in sale list after applying full sale discount to
     * all items within the sale.
     */
    private List<ItemInSaleDTO> makeItemInSaleListAfterFullSaleDiscount(
            double fullSaleDiscountPercent) {
        List<ItemInSaleDTO> itemsInSaleWithDiscounted = new ArrayList<>();
        for(ItemInSaleDTO itemInSale : saleInfo.getItemsInSale()) {
            ItemInSaleDTO itemInSaleDiscounted = 
                    itemInSale.applyDiscount(fullSaleDiscountPercent);
            itemsInSaleWithDiscounted.add(itemInSaleDiscounted);
        }
        return itemsInSaleWithDiscounted;
    }
    
    /**
     * Get possible "name contains" discount rule 
     * <code>Entry&lt;String,Double&gt;</code> if the specified item name 
     * is applicable. Will return the best discount if more than one is 
     * applicable to the item.
     * 
     * @param The item name.
     * @return The best possible  "name contains" discount rule 
     * <code>Entry&lt;String,Double&gt;</code>.
     */
    private Entry<String,Double> getBestPossibleNameContainsDiscountEntry(
            String itemName) {
        Entry<String,Double> bestBossibleEntry = null;
        String lowerCaseItemName = itemName.toLowerCase();
        for(Entry<String,Double> itemNameContainsEntry : 
                itemNameContains.entrySet()) {
            boolean entryContainsText = 
                    lowerCaseItemName.contains(itemNameContainsEntry.getKey());
            if (entryContainsText) {
                boolean foundNoPossibleEntryYet = bestBossibleEntry == null;
                boolean foundBetterPossibleEntry = foundNoPossibleEntryYet ||
                        itemNameContainsEntry.getValue() > 
                        bestBossibleEntry.getValue();
                if(foundBetterPossibleEntry) {
                    bestBossibleEntry = itemNameContainsEntry;
                }
            }
        }
        return bestBossibleEntry;
    }
    
    /**
     * See if specified item in a sale is applicable for a 
     * "name contains" discount.
     * 
     * @param itemInSale The specified item to see if discount exists for.
     * @return Whether a discount exists or not.
     */
    private boolean doesItemHaveApplicableNameContainsDiscount(
            ItemInSaleDTO itemInSale) {
        String itemName = itemInSale.getItemInfo().getItemName();
        boolean doesItemHaveApplicableDiscount = 
                getBestPossibleNameContainsDiscountEntry(itemName) != null;
        
        return doesItemHaveApplicableDiscount;
    }
    
    /**
     * Get the discount percentage for the specified item.
     * 
     * @param itemInSale The item to get the discount for.
     * @return The discount percentage for the item.
     */
    private double getItemNameContainsDiscount(ItemInSaleDTO itemInSale) {
        String itemName = itemInSale.getItemInfo().getItemName();
        Entry<String,Double> possibleNameContainsDiscountEntry = 
                getBestPossibleNameContainsDiscountEntry(itemName);
        double itemDiscountPercent = 
                possibleNameContainsDiscountEntry.getValue();
        return itemDiscountPercent;
    }
}

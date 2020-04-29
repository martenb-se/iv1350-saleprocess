package se.martenb.iv1350.project.saleprocess.model;

import se.martenb.iv1350.project.saleprocess.util.Unit;
import se.martenb.iv1350.project.saleprocess.util.Quantity;
import se.martenb.iv1350.project.saleprocess.integration.ItemInSaleDTO;

/**
 * Represents an item in an ongoing sale.
 */
public class Item {
    private ItemInSaleDTO itemInSale;
    
    /**
     * Creates a new instance of an item in an ongoing sale.
     * 
     * @param itemInSale Item in sale information.
     */
    public Item(ItemInSaleDTO itemInSale) {
        this.itemInSale = itemInSale;
    }

    /**
     * Get all information as a {@link ItemInSaleDTO} for the current item 
     * in sale.
     * 
     * @return <code>ItemInSaleDTO</code> containing information about the item.
     */
    public ItemInSaleDTO getItemInSale() {
        return itemInSale;
    }
    
    /**
     * Get the numerical value of the quantity of the current item in a sale.
     * 
     * @return The numerical value of the quantity of the item.
     */
    private double getNumericalQuantityValue() {
        return itemInSale.getItemQuantity().getNumericalValue();
    }
    
    /**
     * Get the type of {@link Unit} for the current item in sale.
     * 
     * @return Unit type of item in sale.
     */
    private Unit getUnitType() {
        return itemInSale.getItemQuantity().getUnitType();
    }
    
    /**
     * Updates the numerical value quantity and return a new Quantity object
     * with updated info.
     * 
     * @param numericalQuantity Value to set the new quantity to.
     * 
     * @return Quantity object with updated quantity value.
     */
    private Quantity updateQuantityAmount(double numericalQuantity) {
        return new Quantity(numericalQuantity, getUnitType());
    }
    
    /**
     * Set the quantity the current item to the specified numerical value.
     * 
     * @param numericalQuantityValue Quantity value to set the new item to.
     */
    void setQuantity(double numericalQuantityValue) {
        Quantity updatedQuantity = updateQuantityAmount(numericalQuantityValue);
        itemInSale = new ItemInSaleDTO(itemInSale.getItemInfo(), 
                updatedQuantity);
    }
    
    /**
     * Increase the current item's quantity with the specified numerical value.
     * 
     * @param addedNumericalQuantityValue How many numerical values to increase 
     * the item's quantity with.
     */
    void addQuantity(double addedNumericalQuantityValue) {
        double sumNumericalValueQuantityOfItem = 
                getNumericalQuantityValue() + addedNumericalQuantityValue;
        setQuantity(sumNumericalValueQuantityOfItem);
    }
    
}

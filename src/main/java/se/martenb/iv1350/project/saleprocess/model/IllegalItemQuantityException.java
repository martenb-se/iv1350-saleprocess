package se.martenb.iv1350.project.saleprocess.model;

import se.martenb.iv1350.project.saleprocess.util.Quantity;

/**
 * Thrown when an illegal quantity is used when registering an item.
 */
public class IllegalItemQuantityException 
        extends ItemRegistrationException {
    private final Quantity illegalQuantity;
    
    /**
     * Creates a new instance specifying which item with an illegal quantity 
     * was used.
     * 
     * @param itemID Id of item that could not be registered.
     * @param illegalQuantity Quantity of item that was illegal.
     */
    public IllegalItemQuantityException(int itemID, Quantity illegalQuantity) {
        super(itemID);
        this.illegalQuantity = illegalQuantity;
    }
    
    /**
     * Get the illegal quantity that was not allowed to be used.
     * 
     * @return The quantity that was illegal
     */
    public Quantity getIllegalQuantity() {
        return illegalQuantity;
    }
}

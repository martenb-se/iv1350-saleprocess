package se.martenb.iv1350.project.saleprocess.model;

/**
 * Thrown when unable to register an item onto a sale.
 */
public class ItemRegistrationException extends Exception {
    private final int itemThatCannotBeRegistered;
    
    /**
     * Creates a new instance specifying for which item the registration
     * failed. 
     * 
     * @param itemThatCannotBeRegistered The item that cannot be registered.
     */
    public ItemRegistrationException(int itemThatCannotBeRegistered) {
        super("Unable to register item " + itemThatCannotBeRegistered + ".");
        this.itemThatCannotBeRegistered = itemThatCannotBeRegistered;
    }
    
    /**
     * Get the item that cannot be registered.
     * @return The item that cannot be registered.
     */
    public int getItemThatCannotBeRegistered() {
        return itemThatCannotBeRegistered;
    }
}

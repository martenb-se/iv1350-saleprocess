package se.martenb.iv1350.project.saleprocess.model;

/**
 * Thrown when a registered item is unknown to the system.
 */
public class UnknownItemException
        extends ItemRegistrationException {
    /**
     * Creates a new instance specifying for which item was unknown.
     * 
     * @param unknownItemID The item that cannot be registered.
     */
    public UnknownItemException(int unknownItemID) {
        super(unknownItemID);
    }
    
}

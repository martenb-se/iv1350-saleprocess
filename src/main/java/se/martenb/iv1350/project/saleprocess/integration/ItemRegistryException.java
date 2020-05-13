package se.martenb.iv1350.project.saleprocess.integration;

/**
 * Thrown when something goes wrong performing an operation in
 * the {@link ItemRegistry}.
 */
public class ItemRegistryException extends RuntimeException {
    /**
     * Creates a new instance representing the condition described in
     * the message.
     * 
     * @param exceptionMessage A message to describe what went wrong.
     */
    public ItemRegistryException(String exceptionMessage) {
        super(exceptionMessage);
    }
}

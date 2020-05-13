package se.martenb.iv1350.project.saleprocess.controller;

/**
 * Thrown when something else than breaking business rules went wrong
 * during a user operation. 
 */
public class OperationFailedException extends Exception {
    /**
     * Creates a new instance representing the condition described in
     * the message.
     * 
     * @param exceptionMessage A message to describe what went wrong.
     */
    public OperationFailedException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
}

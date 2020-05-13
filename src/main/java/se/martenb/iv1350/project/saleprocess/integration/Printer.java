package se.martenb.iv1350.project.saleprocess.integration;

import se.martenb.iv1350.project.saleprocess.integration.dto.ReceiptDTO;

/**
 * Representing communications with a real printer.
 */
public class Printer {
    PrinterReceiptHandler receiptHandler = null;
    
    /**
     * Creates a new instance of a {@link Printer}
     */
    public Printer() {
        
    }
    
    /**
     * Prints the specified receipt. This dummy implementation prints to 
     * <code>System.out</code> instead of a printer.
     * 
     * @param receiptDTO The receipt to print.
     */
    public void print(ReceiptDTO receiptDTO) {
        receiptHandler = new PrinterReceiptHandler(receiptDTO);
        System.out.println(receiptHandler.createReceiptString());
    }
    
}

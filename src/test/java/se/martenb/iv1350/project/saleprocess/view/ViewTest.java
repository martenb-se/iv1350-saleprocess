package se.martenb.iv1350.project.saleprocess.view;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.controller.Controller;
import se.martenb.iv1350.project.saleprocess.integration.Printer;

public class ViewTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    
    @BeforeEach
    public void setUp() {
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }
    
    @AfterEach
    public void tearDown() {
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testRunFakeExecution() {
        Printer printer = new Printer();
        Controller contr = new Controller(printer);
        View view = new View(contr);
        view.runFakeExecution();
        String printout = printoutBuffer.toString();
        String responseNewSale = "A new sale has started";
        String responseAddedItem = "Added";
        String responseRunningTotal = "Running total";
        String responseTotalCost = "Total cost";
        String responseChange = "Change to return to customer";
        String actionPrintingReceiptStart = "[PRINTING RECEIPT]";
        String actionPrintingReceiptEnd = "[PRINTING HAS FINISHED]";
        Boolean foundResponseNewSale = 
                printout.contains(responseNewSale);
        Boolean foundResponseAddedItem = 
                printout.contains(responseAddedItem);
        Boolean foundResponseRunningTotal = 
                printout.contains(responseRunningTotal);
        Boolean foundResponseTotalCost = 
                printout.contains(responseTotalCost);
        Boolean foundResponseChange = 
                printout.contains(responseChange);
        Boolean foundActionPrintingReceiptStart = 
                printout.contains(actionPrintingReceiptStart);
        Boolean foundActionPrintingReceiptEnd = 
                printout.contains(actionPrintingReceiptEnd);
        Boolean foundAllNecessaryInformation = foundResponseNewSale && 
                foundResponseAddedItem && foundResponseRunningTotal && 
                foundResponseTotalCost && foundResponseChange &&
                foundActionPrintingReceiptStart && 
                foundActionPrintingReceiptEnd;
        assertTrue(foundAllNecessaryInformation, "Output is missing");
    }
    
}

package se.martenb.iv1350.project.saleprocess.controller;

import se.martenb.iv1350.project.saleprocess.model.UnknownItemException;
import se.martenb.iv1350.project.saleprocess.model.ItemRegistrationException;
import se.martenb.iv1350.project.saleprocess.model.IllegalItemQuantityException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.Printer;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingEnvironment;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.testing.TestingPurchaseObserver;
import se.martenb.iv1350.project.saleprocess.util.Quantity;


public class ControllerTest {
    TestingObjectCreator testObjCr;
    Printer printerInitial;
    Controller controllerInitial;

    private static final int VALID_ITEM_ID_A = 1;
    private static final int VALID_ITEM_ID_B = 2;
    private static final Quantity VALID_ITEM_QUANT = new Quantity(1);
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        printerInitial = new Printer();
        controllerInitial = new Controller(printerInitial);
    }
    
    @AfterEach
    public void tearDown() {
        testObjCr = null;
        printerInitial = null;
        controllerInitial = null;
    }

    @Test
    public void testStartSale() {
        int mustBeValidItemIDinDB = 1;
        Quantity mustBeValidItemQuantity = VALID_ITEM_QUANT;
        try {
            SaleDTO stateSaleDTO = controllerInitial.registerItem(
                mustBeValidItemIDinDB, mustBeValidItemQuantity);
            fail("Illegal state reached, registered item onto uninitialized " +
                    "Sale");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof NullPointerException;
            if (correctExceptionThrown) {
                controllerInitial.startSale();
                try {
                    SaleDTO stateSaleDTO = controllerInitial.registerItem(
                            mustBeValidItemIDinDB, mustBeValidItemQuantity);
                } catch (Exception unwantedException) {
                    fail("Starting sale failed.");
                }
            } else {
                fail("Test failed unexpectedly.");
            }
        }
    }
    
    private SaleDTO startSaleAndRegisterItem(int itemID, Quantity itemQuantity)
            throws ItemRegistrationException, OperationFailedException {
        controllerInitial.startSale();
        return registerItemToSale(itemID, itemQuantity);
    }
    
    private SaleDTO registerItemToSale(int itemID, Quantity itemQuantity) 
            throws ItemRegistrationException, OperationFailedException {
        return controllerInitial.registerItem(itemID, itemQuantity);
    }
    
    @Test
    public void testAddItemToSale() 
            throws OperationFailedException, ItemRegistrationException {
        SaleDTO saleDTO = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
                VALID_ITEM_QUANT);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(saleDTO, 
                VALID_ITEM_ID_A);
        assertTrue(isItemInSale,"Item is not added to sale.");
    }
    
    @Test
    public void testAddItemToSaleTwice() 
            throws OperationFailedException, ItemRegistrationException {
        SaleDTO saleDTO = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
        VALID_ITEM_QUANT);
        saleDTO = registerItemToSale(VALID_ITEM_ID_A, VALID_ITEM_QUANT);
        double expResult = VALID_ITEM_QUANT.getNumericalValue() * 2;
        double result = testObjCr.quantityOfItemAddedToSale(saleDTO, 
                VALID_ITEM_ID_A);
        assertEquals(expResult, result, "Item not added twice to sale.");
    }
    
    @Test
    public void testAddTwoDifferentItemsToSale() 
            throws ItemRegistrationException, OperationFailedException {
        SaleDTO saleDTO = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
        VALID_ITEM_QUANT);
        saleDTO = registerItemToSale(VALID_ITEM_ID_B, 
        VALID_ITEM_QUANT);
        boolean foundItemInSaleFirst = testObjCr.wasItemAddedToSale(
                saleDTO, VALID_ITEM_ID_A);
        boolean foundItemInSaleSecond = testObjCr.wasItemAddedToSale(
                saleDTO, VALID_ITEM_ID_B);
        boolean areAllItemsInSale = foundItemInSaleFirst && 
                foundItemInSaleSecond;
        assertTrue(areAllItemsInSale, "Items were not added to sale.");
    }
    
    @Test
    public void testAddIntMaxQuantityOfItemToSale() 
            throws OperationFailedException, ItemRegistrationException {
        Quantity mustBeValidItemQuantity = new Quantity(Integer.MAX_VALUE);
        SaleDTO saleDTO = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
        mustBeValidItemQuantity);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(saleDTO, 
                VALID_ITEM_ID_A);
        assertTrue(isItemInSale,"Item is not added to sale.");
    }
    
    @Test
    public void testAddUnknownItemIDToSaleException() 
            throws ItemRegistrationException, OperationFailedException  {
        int mustBeInvalidItemIDinDB = 9999;
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    mustBeInvalidItemIDinDB, VALID_ITEM_QUANT);
            fail("Added invalid item to sale.");
        } catch (UnknownItemException exc) {
            assertEquals(
                    mustBeInvalidItemIDinDB, 
                    exc.getItemThatCannotBeRegistered(), 
                    "Wrong exception message, does not contain specified id:" +
                            exc.getMessage());
        }
    }

    
    @Test
    public void testAddZeroItemIDToSaleException() 
            throws ItemRegistrationException, OperationFailedException {
        int mustBeInvalidItemIDinDB = 0;
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    mustBeInvalidItemIDinDB, VALID_ITEM_QUANT);
            fail("Added invalid item to sale.");
        } catch (ItemRegistrationException exc) {
            assertEquals(
                    mustBeInvalidItemIDinDB, 
                    exc.getItemThatCannotBeRegistered(), 
                    "Wrong exception message, does not contain specified id:" +
                            exc.getMessage());
        }
    }
    
    @Test
    public void testAddNegativeItemIDToSaleException() 
            throws ItemRegistrationException, OperationFailedException {
        int mustBeInvalidItemIDinDB = -1;
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    mustBeInvalidItemIDinDB, VALID_ITEM_QUANT);
            fail("Added invalid item to sale.");
        } catch (ItemRegistrationException exc) {
            assertEquals(
                    mustBeInvalidItemIDinDB, 
                    exc.getItemThatCannotBeRegistered(), 
                    "Wrong exception message, does not contain specified id:" +
                            exc.getMessage());
        }
    }
    
    @Test
    public void testAddZeroQuantityItemToSaleException() 
            throws ItemRegistrationException, OperationFailedException {
        Quantity mustBeInvalidItemQuantity = new Quantity(0);
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    VALID_ITEM_ID_A, mustBeInvalidItemQuantity);
            fail("Added item with invalid quantity to sale.");
        } catch (IllegalItemQuantityException exc) {
            assertEquals(
                    VALID_ITEM_ID_A, 
                    exc.getItemThatCannotBeRegistered(), 
                    "Wrong exception message, does not contain " + 
                            " specified item id:" +
                            exc.getMessage());
            assertEquals(
                    mustBeInvalidItemQuantity, 
                    exc.getIllegalQuantity(), 
                    "Wrong exception message, does not contain " + 
                            " specified quantity:" +
                            exc.getMessage());
        }
    }
    
    @Test
    public void testAddNegativeQuantityItemToSaleException() 
            throws ItemRegistrationException, OperationFailedException {
        Quantity mustBeInvalidItemQuantity = new Quantity(-1);
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    VALID_ITEM_ID_A, mustBeInvalidItemQuantity);
            fail("Added item with invalid quantity to sale.");
        } catch (IllegalItemQuantityException exc) {
            assertEquals(
                    VALID_ITEM_ID_A, 
                    exc.getItemThatCannotBeRegistered(), 
                    "Wrong exception message, does not contain " + 
                            " specified item id:" +
                            exc.getMessage());
            assertEquals(
                    mustBeInvalidItemQuantity, 
                    exc.getIllegalQuantity(), 
                    "Wrong exception message, does not contain " + 
                            " specified quantity:" +
                            exc.getMessage());
        }
    }
    
    @Test
    public void testIntMinQuantityOfItemToSaleExceptionException() 
            throws ItemRegistrationException, OperationFailedException {
        Quantity mustBeInvalidItemQuantity = new Quantity(Integer.MIN_VALUE);
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    VALID_ITEM_ID_A, mustBeInvalidItemQuantity);
            fail("Added item with invalid quantity to sale.");
        } catch (IllegalItemQuantityException exc) {
            assertEquals(
                    mustBeInvalidItemQuantity, 
                    exc.getIllegalQuantity(), 
                    "Wrong exception message, does not contain " + 
                            " specified quantity:" +
                            exc.getMessage());
        }
    }

    @Test
    public void testRegisterPayment() {
        ByteArrayOutputStream printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        PrintStream originalSysOut = System.out;
        System.setOut(inMemSysOut);
        int entriesInSale = 10;
        Amount paidAmount = new Amount(5000);
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        PurchaseDTO purchaseInformation = 
                controllerInitial.registerPayment(basicTestSaleDTO, paidAmount);
        System.setOut(originalSysOut);
        assertEquals(paidAmount, purchaseInformation.getAmountPaid(), 
                "Wrong amount in register after payment.");
    }

    @Test
    public void testRegisterNegativePayment() {
        ByteArrayOutputStream printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        PrintStream originalSysOut = System.out;
        System.setOut(inMemSysOut);
        int entriesInSale = 10;
        Amount paidAmount = new Amount(-5000);
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        PurchaseDTO purchaseInformation = 
                controllerInitial.registerPayment(basicTestSaleDTO, paidAmount);
        System.setOut(originalSysOut);
        assertEquals(paidAmount, purchaseInformation.getAmountPaid(), 
                "Wrong amount in register after payment.");
    }

    @Test
    public void testRegisterIntMaxPayment() {
        ByteArrayOutputStream printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        PrintStream originalSysOut = System.out;
        System.setOut(inMemSysOut);
        int entriesInSale = 10;
        Amount paidAmount = new Amount(Integer.MAX_VALUE);
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        PurchaseDTO purchaseInformation = 
                controllerInitial.registerPayment(basicTestSaleDTO, paidAmount);
        System.setOut(originalSysOut);
        assertEquals(paidAmount, purchaseInformation.getAmountPaid(), 
                "Wrong amount in register after payment.");
    }

    @Test
    public void testRegisterIntMinPayment() {
        ByteArrayOutputStream printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        PrintStream originalSysOut = System.out;
        System.setOut(inMemSysOut);
        int entriesInSale = 10;
        Amount paidAmount = new Amount(Integer.MIN_VALUE);
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        PurchaseDTO purchaseInformation = 
                controllerInitial.registerPayment(basicTestSaleDTO, paidAmount);
        System.setOut(originalSysOut);
        assertEquals(paidAmount, purchaseInformation.getAmountPaid(), 
                "Wrong amount in register after payment.");
    }

    @Test
    public void testPurchaseObserver() 
            throws ItemRegistrationException, OperationFailedException {
        TestingEnvironment testEnv = new TestingEnvironment();
        testEnv.redirectSystemOut();
        Amount paidAmount = new Amount(5000);
        SaleDTO saleState = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
                VALID_ITEM_QUANT);
        String outputBeforeActiveTestingPurchaseObserver = 
                testEnv.getRedirectedSystemOut();
        controllerInitial.addPurchaseObserver(new TestingPurchaseObserver());
        PurchaseDTO purchaseInfo = controllerInitial.registerPayment(
                saleState, paidAmount);
        String outputAfterActiveTestingPurchaseObserver = 
                testEnv.getRedirectedSystemOut();
        testEnv.restoreSystemOut();
        StringBuilder expectedObserverOutput = new StringBuilder();
        expectedObserverOutput.
                append(purchaseInfo.getFinalPrice().getTotalPriceAfterTaxes()).
                append("\n");
        assertNotEquals(outputBeforeActiveTestingPurchaseObserver, 
                outputAfterActiveTestingPurchaseObserver,
                "TestingPurchaseObserver was not notified.");
        assertTrue(outputAfterActiveTestingPurchaseObserver.
                startsWith(expectedObserverOutput.toString()), 
                "TestingPurchaseObserver did not get correct data.");
    }

    @Test
    public void testPurchaseObserverMultiple() 
            throws ItemRegistrationException, OperationFailedException {
        TestingEnvironment testEnv = new TestingEnvironment();
        testEnv.redirectSystemOut();
        Amount paidAmount = new Amount(5000);
        SaleDTO saleState = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
                VALID_ITEM_QUANT);
        String outputBeforeActiveTestingPurchaseObserver = 
                testEnv.getRedirectedSystemOut();
        controllerInitial.addPurchaseObserver(new TestingPurchaseObserver());
        controllerInitial.addPurchaseObserver(new TestingPurchaseObserver());
        controllerInitial.addPurchaseObserver(new TestingPurchaseObserver());
        controllerInitial.addPurchaseObserver(new TestingPurchaseObserver());
        controllerInitial.addPurchaseObserver(new TestingPurchaseObserver());
        PurchaseDTO purchaseInfo = controllerInitial.registerPayment(
                saleState, paidAmount);
        String outputAfterActiveTestingPurchaseObserver = 
                testEnv.getRedirectedSystemOut();
        testEnv.restoreSystemOut();
        StringBuilder expectedObserverOutput = new StringBuilder();
        expectedObserverOutput.
                append(purchaseInfo.getFinalPrice().getTotalPriceAfterTaxes()).
                append("\n").
                append(purchaseInfo.getFinalPrice().getTotalPriceAfterTaxes()).
                append("\n").
                append(purchaseInfo.getFinalPrice().getTotalPriceAfterTaxes()).
                append("\n").
                append(purchaseInfo.getFinalPrice().getTotalPriceAfterTaxes()).
                append("\n").
                append(purchaseInfo.getFinalPrice().getTotalPriceAfterTaxes()).
                append("\n");
        
        assertTrue(outputAfterActiveTestingPurchaseObserver.
                startsWith(expectedObserverOutput.toString()), 
                "TestingPurchaseObserver did not get correct data.");
    }

    @Test
    public void testTryDiscountOnSale() {
        int itemsInSale = 50;
        SaleDTO saleInfo = testObjCr.generateSaleDTOAndFinalPrice(itemsInSale);
        CustomerDTO customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        try {
            controllerInitial.tryDiscountOnSale(saleInfo, customerInfo);
        } catch (IllegalStateException exc) {
            fail("Discount was not tried on sale correctly.");
        }
    }
    
}

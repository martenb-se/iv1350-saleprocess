package se.martenb.iv1350.project.saleprocess.controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.Printer;
import se.martenb.iv1350.project.saleprocess.integration.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;


public class ControllerTest {
    TestingObjectCreator testObjCr;
    Printer printerInitial;
    Controller controllerInitial;

    private static final int VALID_ITEM_ID_A = 1;
    private static final int VALID_ITEM_ID_B = 2;
    private static final int VALID_ITEM_QUANT = 1;
    
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
        int mustBeValidItemQuantity = 1;
        try {
            SaleDTO stateSaleDTO = controllerInitial.registerItem(
                mustBeValidItemIDinDB, mustBeValidItemQuantity);
            fail("Illegal state reached, registered item onto ininitialized " +
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
    
    private SaleDTO startSaleAndRegisterItem(int itemID, int itemQuantity) {
        controllerInitial.startSale();
        return registerItemToSale(itemID, itemQuantity);
    }
    
    private SaleDTO registerItemToSale(int itemID, int itemQuantity) {
        return controllerInitial.registerItem(itemID, itemQuantity);
    }
    
    @Test
    public void testAddItemToSale() {
        SaleDTO saleDTO = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
                VALID_ITEM_QUANT);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(saleDTO, 
                VALID_ITEM_ID_A);
        assertTrue(isItemInSale,"Item is not added to sale.");
    }
    
    @Test
    public void testAddItemToSaleTwice() {
        SaleDTO saleDTO = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
        VALID_ITEM_QUANT);
        saleDTO = registerItemToSale(VALID_ITEM_ID_A, VALID_ITEM_QUANT);
        double expResult = VALID_ITEM_QUANT * 2;
        double result = testObjCr.quantityOfItemAddedToSale(saleDTO, 
                VALID_ITEM_ID_A);
        assertEquals(expResult, result, "Item not added twice to sale.");
    }
    
    @Test
    public void testAddTwoDifferentItemsToSale() {
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
    public void testAddIntMaxQuantityOfItemToSale() {
        int mustBeValidItemQuantity = Integer.MAX_VALUE;
        SaleDTO saleDTO = startSaleAndRegisterItem(VALID_ITEM_ID_A, 
        mustBeValidItemQuantity);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(saleDTO, 
                VALID_ITEM_ID_A);
        assertTrue(isItemInSale,"Item is not added to sale.");
    }
    
    @Test
    public void testAddUnknownItemIDToSaleException() {
        int mustBeInvalidItemIDinDB = 9999;
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    mustBeInvalidItemIDinDB, VALID_ITEM_QUANT);
            fail("Illegal state reached.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalArgumentException;
            if (!correctExceptionThrown)
                fail("Invalid item allowed to be added to sale");
        }
    }
    
    @Test
    public void testAddZeroItemIDToSaleException() {
        int mustBeInvalidItemIDinDB = 0;
        controllerInitial.startSale();
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    mustBeInvalidItemIDinDB, VALID_ITEM_QUANT);
            fail("Illegal state reached.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalArgumentException;
            if (!correctExceptionThrown)
                fail("Invalid item allowed to be added to sale");
        }
    }
    
    @Test
    public void testAddNegativeItemIDToSaleException() {
        int mustBeInvalidItemIDinDB = -1;
        controllerInitial.startSale();
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    mustBeInvalidItemIDinDB, VALID_ITEM_QUANT);
            fail("Illegal state reached.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalArgumentException;
            if (!correctExceptionThrown)
                fail("Invalid item allowed to be added to sale");
        }
    }
    
    @Test
    public void testAddZeroQuantityItemToSaleException() {
        int mustBeInvalidItemQuantity = 0;
        controllerInitial.startSale();
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    VALID_ITEM_ID_A, mustBeInvalidItemQuantity);
            fail("Illegal state reached.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalArgumentException;
            if (!correctExceptionThrown)
                fail("Invalid item quantity allowed to be added to sale");
        }
    }
    
    @Test
    public void testAddNegativeQuantityItemToSaleException() {
        int mustBeInvalidItemQuantity = -1;
        controllerInitial.startSale();
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    VALID_ITEM_ID_A, mustBeInvalidItemQuantity);
            fail("Illegal state reached.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalArgumentException;
            if (!correctExceptionThrown)
                fail("Invalid item quantity allowed to be added to sale");
        }
    }
    
    @Test
    public void testIntMinQuantityOfItemToSaleExceptionException() {
        int mustBeInvalidItemQuantity = Integer.MIN_VALUE;
        controllerInitial.startSale();
        try {
            SaleDTO saleDTO = startSaleAndRegisterItem(
                    VALID_ITEM_ID_A, mustBeInvalidItemQuantity);
            fail("Illegal state reached.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalArgumentException;
            if (!correctExceptionThrown)
                fail("Invalid item quantity allowed to be added to sale");
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
    
}

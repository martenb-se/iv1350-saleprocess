package se.martenb.iv1350.project.saleprocess.model;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.ItemDTO;
import se.martenb.iv1350.project.saleprocess.util.Price;
import se.martenb.iv1350.project.saleprocess.integration.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;

public class SaleInformationTest {
    SaleInformation saleInformationInitial;
    TestingObjectCreator testObjCr;
    
    private static final int ITEM_ID_A = 1;
    private static final int ITEM_ID_B = 2;
    private static final String ITEM_NAME_A = "Test Item 1";
    private static final String ITEM_NAME_B = "Test Item 2";
    private static final double ITEM_PRICE_A = 99.99;
    private static final double ITEM_PRICE_B = 200.50;
    private static final double ITEM_VAT_A = 12.0;
    private static final double ITEM_VAT_B = 10.5;
    private static final int ITEM_QUANT_A = 1;
    private static final int ITEM_QUANT_B = 5;
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        saleInformationInitial = new SaleInformation();
    }
    
    @AfterEach
    public void tearDown() {
        testObjCr = null;
        saleInformationInitial = null;
    }
    
    private LocalDateTime updateSaleTimeAndThenGet() {
        saleInformationInitial.updateSaleTime();
        return saleInformationInitial.getSaleTime();
    }

    @Test
    public void testUpdateSaleTimeFromUninitialized() {
        LocalDateTime initialSaleTime = saleInformationInitial.getSaleTime();
        LocalDateTime updatedSaleTime = updateSaleTimeAndThenGet();

        assertNotEquals(initialSaleTime, updatedSaleTime, 
                "Sale time did not update.");
    }

    @Test
    public void testUpdateSaleTimeFromPreviousTime() {
        LocalDateTime initialSaleTime = updateSaleTimeAndThenGet();
        LocalDateTime updatedSaleTime = updateSaleTimeAndThenGet();

        assertNotEquals(initialSaleTime, updatedSaleTime, 
                "Sale time did not update.");
    }
    
    private SaleDTO makeItemAndAddToSale(int itemID, String itemName, 
            double priceBeforeTaxes, double vatTaxRate, int itemQuantity) {
        ItemDTO itemToAdd = testObjCr.makeItemDTOSimple(
                itemID, itemName, priceBeforeTaxes, vatTaxRate);
        saleInformationInitial.addItemToSale(itemToAdd, itemQuantity);
        return saleInformationInitial.getSaleDTO();
    }
    
    @Test
    public void testAddItemToEmptySale() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, ITEM_ID_A);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddItemTotalItems() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        double expResult = ITEM_QUANT_A;
        double result = testObjCr.getTotalItemsOfSale(saleDTO);
        
        assertEquals(expResult, result, "Total number of items incorrect.");
    }
    
    @Test
    public void testAddItemRunningTotal() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        Price itemPrice = testObjCr.makePrice(ITEM_PRICE_A, ITEM_VAT_A);
        Amount totalPrice = itemPrice.getPriceAfterTax().multiply(ITEM_QUANT_A);
        Amount expResult = totalPrice;
        Amount result = saleDTO.getRunningTotal();
        assertEquals(expResult, result, "Running total is incorrect.");
    }
    
    @Test
    public void testAddItemToSaleZeroID() {
        int itemID = 0;
        SaleDTO saleDTO = makeItemAndAddToSale(itemID, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, itemID);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddItemToSaleNegativeID() {
        int itemID = -1;
        SaleDTO saleDTO = makeItemAndAddToSale(itemID, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, itemID);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddItemToSaleMaxIntID() {
        int itemID = Integer.MAX_VALUE;
        SaleDTO saleDTO = makeItemAndAddToSale(itemID, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, itemID);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddItemToSaleMinIntID() {
        int itemID = Integer.MIN_VALUE;
        SaleDTO saleDTO = makeItemAndAddToSale(itemID, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, itemID);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddTwoItemsToSale() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = makeItemAndAddToSale(ITEM_ID_B, ITEM_NAME_B, ITEM_PRICE_B, 
                ITEM_VAT_B, ITEM_QUANT_B);
        boolean foundItemInSaleFirst = testObjCr.wasItemAddedToSale(
                saleDTO, ITEM_ID_A);
        boolean foundItemInSaleSecond = testObjCr.wasItemAddedToSale(
                saleDTO, ITEM_ID_B);
        boolean areAllItemsInSale = foundItemInSaleFirst && 
                foundItemInSaleSecond;
        assertTrue(areAllItemsInSale, "Items were not added to sale.");
    }
    
    @Test
    public void testAddTwoItemsToSaleTotalItems() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = makeItemAndAddToSale(ITEM_ID_B, ITEM_NAME_B, ITEM_PRICE_B, 
                ITEM_VAT_B, ITEM_QUANT_B);
        
        double expResult = ITEM_QUANT_A + ITEM_QUANT_B;
        double result = testObjCr.getTotalItemsOfSale(saleDTO);
        
        assertEquals(expResult, result, "Total number of items incorrect.");
    }
    
    @Test
    public void testAddTwoItemsToSaleRunningTotal() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = makeItemAndAddToSale(ITEM_ID_B, ITEM_NAME_B, ITEM_PRICE_B, 
                ITEM_VAT_B, ITEM_QUANT_B);
        Price itemPriceA = testObjCr.makePrice(ITEM_PRICE_A, ITEM_VAT_A);
        Price itemPriceB = testObjCr.makePrice(ITEM_PRICE_B, ITEM_VAT_B);
        Amount totalPriceA = 
                itemPriceA.getPriceAfterTax().multiply(ITEM_QUANT_A);
        Amount totalPriceB = 
                itemPriceB.getPriceAfterTax().multiply(ITEM_QUANT_B);
        Amount combinedTotal = totalPriceA.plus(totalPriceB);
        
        Amount expResult = combinedTotal;
        Amount result = saleDTO.getRunningTotal();
        
        assertEquals(expResult, result, "Running total is incorrect.");
    }
    
    @Test
    public void testAddItemToSaleTwice() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        double expResult = ITEM_QUANT_A * 2;
        double result = testObjCr.quantityOfItemAddedToSale(saleDTO, 
                ITEM_ID_A);
        
        assertEquals(expResult,result,"Item not added twice to sale.");
    }
    
    @Test
    public void testAddItemToSaleTwiceTotalItems() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        double expResult = ITEM_QUANT_A * 2;
        double result = testObjCr.getTotalItemsOfSale(saleDTO);
        
        assertEquals(expResult, result, "Total number of items incorrect.");
    }
    
    @Test
    public void testAddItemToSaleTwiceRunningTotal() {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        Price itemPrice = testObjCr.makePrice(ITEM_PRICE_A, ITEM_VAT_A);
        Amount totalPrice = 
                itemPrice.getPriceAfterTax().multiply(ITEM_QUANT_A * 2);
        Amount expResult = totalPrice;
        Amount result = saleDTO.getRunningTotal();
        
        assertEquals(expResult, result, "Running total is incorrect.");
    }
    
    @Test
    public void testAddZeroItemToSaleException() {
        int itemQuantity = 0;
        ItemDTO itemToAdd = testObjCr.makeItemDTOSimple(
                ITEM_ID_A, ITEM_NAME_A, ITEM_PRICE_A, ITEM_VAT_A);
        try {
            saleInformationInitial.addItemToSale(itemToAdd, itemQuantity);
            fail("Added illegal quantity of item to sale.");
        } catch(Exception e) {
            boolean correctExceptionThrown = 
                    e instanceof IllegalArgumentException;
            assertTrue(correctExceptionThrown, 
                    "Wrong exception thrown when adding illegal quantity.");
        }
        
    }
    
    @Test
    public void testAddNegativeItemToSaleException() {
        int itemQuantity = -10;
        ItemDTO itemToAdd = testObjCr.makeItemDTOSimple(
                ITEM_ID_A, ITEM_NAME_A, ITEM_PRICE_A, ITEM_VAT_A);
        try {
            saleInformationInitial.addItemToSale(itemToAdd, itemQuantity);
            fail("Added illegal quantity of item to sale.");
        } catch(Exception e) {
            boolean correctExceptionThrown = 
                    e instanceof IllegalArgumentException;
            assertTrue(correctExceptionThrown, 
                    "Wrong exception thrown when adding illegal quantity.");
        }
        
    }
    
}

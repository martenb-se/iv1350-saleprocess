package se.martenb.iv1350.project.saleprocess.model;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemDTO;
import se.martenb.iv1350.project.saleprocess.util.Price;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.util.Quantity;

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
    private static final Quantity ITEM_QUANT_A = new Quantity(1);
    private static final Quantity ITEM_QUANT_B = new Quantity(1);
    
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
            double priceBeforeTaxes, double vatTaxRate, Quantity itemQuantity) 
            throws IllegalItemQuantityException {
        ItemDTO itemToAdd = testObjCr.makeItemDTOSimple(
                itemID, itemName, priceBeforeTaxes, vatTaxRate);
        saleInformationInitial.addItemToSale(itemToAdd, itemQuantity);
        return saleInformationInitial.getSaleDTO();
    }
    
    @Test
    public void testAddItemToEmptySale() 
            throws IllegalItemQuantityException {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, ITEM_ID_A);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddItemTotalItems() 
            throws IllegalItemQuantityException {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        double expResult = ITEM_QUANT_A.getNumericalValue();
        double result = testObjCr.getTotalItemsOfSale(saleDTO);
        
        assertEquals(expResult, result, "Total number of items incorrect.");
    }
    
    @Test
    public void testAddItemRunningTotal() 
            throws IllegalItemQuantityException {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        Price itemPrice = testObjCr.makePrice(ITEM_PRICE_A, ITEM_VAT_A);
        Amount totalPrice = itemPrice.
                getPriceAfterTax().
                multiply(ITEM_QUANT_A.getNumericalValue());
        Amount expResult = totalPrice;
        Amount result = saleDTO.getRunningTotal();
        assertEquals(expResult, result, "Running total is incorrect.");
    }
    
    @Test
    public void testAddItemToSaleZeroID() 
            throws IllegalItemQuantityException {
        int itemID = 0;
        SaleDTO saleDTO = makeItemAndAddToSale(itemID, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, itemID);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddItemToSaleNegativeID() 
            throws IllegalItemQuantityException {
        int itemID = -1;
        SaleDTO saleDTO = makeItemAndAddToSale(itemID, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, itemID);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddItemToSaleMaxIntID() 
            throws IllegalItemQuantityException {
        int itemID = Integer.MAX_VALUE;
        SaleDTO saleDTO = makeItemAndAddToSale(itemID, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, itemID);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddItemToSaleMinIntID() 
            throws IllegalItemQuantityException {
        int itemID = Integer.MIN_VALUE;
        SaleDTO saleDTO = makeItemAndAddToSale(itemID, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        boolean isItemInSale = testObjCr.wasItemAddedToSale(
                saleDTO, itemID);
        
        assertTrue(isItemInSale,"Item not added to sale.");
    }
    
    @Test
    public void testAddTwoItemsToSale() 
            throws IllegalItemQuantityException {
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
    public void testAddTwoItemsToSaleTotalItems() 
            throws IllegalItemQuantityException {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = makeItemAndAddToSale(ITEM_ID_B, ITEM_NAME_B, ITEM_PRICE_B, 
                ITEM_VAT_B, ITEM_QUANT_B);
        
        double expResult = ITEM_QUANT_A.getNumericalValue() + 
                ITEM_QUANT_B.getNumericalValue();
        double result = testObjCr.getTotalItemsOfSale(saleDTO);
        
        assertEquals(expResult, result, "Total number of items incorrect.");
    }
    
    @Test
    public void testAddTwoItemsToSaleRunningTotal() 
            throws IllegalItemQuantityException {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = makeItemAndAddToSale(ITEM_ID_B, ITEM_NAME_B, ITEM_PRICE_B, 
                ITEM_VAT_B, ITEM_QUANT_B);
        Price itemPriceA = testObjCr.makePrice(ITEM_PRICE_A, ITEM_VAT_A);
        Price itemPriceB = testObjCr.makePrice(ITEM_PRICE_B, ITEM_VAT_B);
        Amount totalPriceA = 
                itemPriceA.
                        getPriceAfterTax().
                        multiply(ITEM_QUANT_A.getNumericalValue());
        Amount totalPriceB = 
                itemPriceB.
                        getPriceAfterTax().
                        multiply(ITEM_QUANT_B.getNumericalValue());
        Amount combinedTotal = totalPriceA.plus(totalPriceB);
        
        Amount expResult = combinedTotal;
        Amount result = saleDTO.getRunningTotal();
        
        assertEquals(expResult, result, "Running total is incorrect.");
    }
    
    @Test
    public void testAddItemToSaleTwice() 
            throws IllegalItemQuantityException {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        double expResult = ITEM_QUANT_A.getNumericalValue() * 2;
        double result = testObjCr.quantityOfItemAddedToSale(saleDTO, 
                ITEM_ID_A);
        
        assertEquals(expResult,result,"Item not added twice to sale.");
    }
    
    @Test
    public void testAddItemToSaleTwiceTotalItems() 
            throws IllegalItemQuantityException {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        double expResult = ITEM_QUANT_A.getNumericalValue() * 2;
        double result = testObjCr.getTotalItemsOfSale(saleDTO);
        
        assertEquals(expResult, result, "Total number of items incorrect.");
    }
    
    @Test
    public void testAddItemToSaleTwiceRunningTotal() 
            throws IllegalItemQuantityException {
        SaleDTO saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        saleDTO = saleDTO = makeItemAndAddToSale(ITEM_ID_A, ITEM_NAME_A, 
                ITEM_PRICE_A, ITEM_VAT_A, ITEM_QUANT_A);
        Price itemPrice = testObjCr.makePrice(ITEM_PRICE_A, ITEM_VAT_A);
        Amount totalPrice = 
                itemPrice.
                        getPriceAfterTax().
                        multiply(ITEM_QUANT_A.getNumericalValue() * 2);
        Amount expResult = totalPrice;
        Amount result = saleDTO.getRunningTotal();
        
        assertEquals(expResult, result, "Running total is incorrect.");
    }
    
    @Test
    public void testAddZeroItemToSaleException() {
        Quantity itemQuantity = new Quantity(0);
        ItemDTO itemToAdd = testObjCr.makeItemDTOSimple(
                ITEM_ID_A, ITEM_NAME_A, ITEM_PRICE_A, ITEM_VAT_A);
        try {
            saleInformationInitial.addItemToSale(itemToAdd, itemQuantity);
            fail("Added item with invalid quantity to sale.");
        } catch (IllegalItemQuantityException exc) {
            assertEquals(
                    itemQuantity.getNumericalValue(), 
                    exc.getIllegalQuantity().getNumericalValue(), 
                    "Wrong exception message, does not contain " + 
                            " specified quantity:" +
                            exc.getMessage());
        }
    }
    
    @Test
    public void testAddNegativeItemToSaleException() {
        Quantity itemQuantity = new Quantity(-10);
        ItemDTO itemToAdd = testObjCr.makeItemDTOSimple(
                ITEM_ID_A, ITEM_NAME_A, ITEM_PRICE_A, ITEM_VAT_A);
        try {
            saleInformationInitial.addItemToSale(itemToAdd, itemQuantity);
            fail("Added item with invalid quantity to sale.");
        } catch (IllegalItemQuantityException exc) {
            assertEquals(
                    itemQuantity.getNumericalValue(), 
                    exc.getIllegalQuantity().getNumericalValue(), 
                    "Wrong exception message, does not contain " + 
                            " specified quantity:" +
                            exc.getMessage());
        }
        
    }
    
}

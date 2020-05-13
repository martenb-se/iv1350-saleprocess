package se.martenb.iv1350.project.saleprocess.integration.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.util.Amount;

public class ItemInSaleDTOTest {
    private TestingObjectCreator testObjCr;
    private static final double PERCENT_MULTIPLIER = 0.01;
    
    public ItemInSaleDTOTest() {
    }
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
    }
    
    @AfterEach
    public void tearDown() {
        testObjCr = null;
    }

    @Test
    public void testApplyDiscount() {
        int itemID = 50;
        double discountPercent = 10;
        ItemInSaleDTO itemInSale = testObjCr.generatePieceOfItemInSaleDTO(itemID);
        ItemInSaleDTO itemInSaleDiscounted = itemInSale.applyDiscount(discountPercent);
        Amount itemInSalePriceBeforeTaxes = 
                itemInSale.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount discountAmount = itemInSalePriceBeforeTaxes.
                multiply(PERCENT_MULTIPLIER * discountPercent);
        Amount discountedPriceBeforeTaxes = itemInSalePriceBeforeTaxes.
                minus(discountAmount);
        Amount itemInSaleDiscountedPriceBeforeTaxes = 
                itemInSaleDiscounted.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount expectedResult = discountedPriceBeforeTaxes;
        Amount result = itemInSaleDiscountedPriceBeforeTaxes;
        assertEquals(expectedResult, result, "Wrong price after discount.");
    }

    @Test
    public void testApplyDiscountNegative() {
        int itemID = 50;
        double discountPercent = -10;
        ItemInSaleDTO itemInSale = testObjCr.generatePieceOfItemInSaleDTO(itemID);
        ItemInSaleDTO itemInSaleDiscounted = itemInSale.applyDiscount(discountPercent);
        Amount itemInSalePriceBeforeTaxes = 
                itemInSale.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount discountAmount = itemInSalePriceBeforeTaxes.
                multiply(PERCENT_MULTIPLIER * discountPercent);
        Amount discountedPriceBeforeTaxes = itemInSalePriceBeforeTaxes.
                minus(discountAmount);
        Amount itemInSaleDiscountedPriceBeforeTaxes = 
                itemInSaleDiscounted.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount expectedResult = discountedPriceBeforeTaxes;
        Amount result = itemInSaleDiscountedPriceBeforeTaxes;
        assertEquals(expectedResult, result, "Wrong price after discount.");
    }

    @Test
    public void testApplyDiscountZero() {
        int itemID = 50;
        double discountPercent = 0;
        ItemInSaleDTO itemInSale = testObjCr.generatePieceOfItemInSaleDTO(itemID);
        ItemInSaleDTO itemInSaleDiscounted = itemInSale.applyDiscount(discountPercent);
        Amount itemInSalePriceBeforeTaxes = 
                itemInSale.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount discountAmount = itemInSalePriceBeforeTaxes.
                multiply(PERCENT_MULTIPLIER * discountPercent);
        Amount discountedPriceBeforeTaxes = itemInSalePriceBeforeTaxes.
                minus(discountAmount);
        Amount itemInSaleDiscountedPriceBeforeTaxes = 
                itemInSaleDiscounted.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount expectedResult = discountedPriceBeforeTaxes;
        Amount result = itemInSaleDiscountedPriceBeforeTaxes;
        assertEquals(expectedResult, result, "Wrong price after discount.");
    }

    @Test
    public void testApplyDiscountDoubleMax() {
        int itemID = 50;
        double discountPercent = Double.MAX_VALUE;
        ItemInSaleDTO itemInSale = testObjCr.generatePieceOfItemInSaleDTO(itemID);
        ItemInSaleDTO itemInSaleDiscounted = itemInSale.applyDiscount(discountPercent);
        Amount itemInSalePriceBeforeTaxes = 
                itemInSale.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount discountAmount = itemInSalePriceBeforeTaxes.
                multiply(PERCENT_MULTIPLIER * discountPercent);
        Amount discountedPriceBeforeTaxes = itemInSalePriceBeforeTaxes.
                minus(discountAmount);
        Amount itemInSaleDiscountedPriceBeforeTaxes = 
                itemInSaleDiscounted.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount expectedResult = discountedPriceBeforeTaxes;
        Amount result = itemInSaleDiscountedPriceBeforeTaxes;
        assertEquals(expectedResult, result, "Wrong price after discount.");
    }

    @Test
    public void testApplyDiscountDoubleMin() {
        int itemID = 50;
        double discountPercent = Double.MIN_VALUE;
        ItemInSaleDTO itemInSale = testObjCr.generatePieceOfItemInSaleDTO(itemID);
        ItemInSaleDTO itemInSaleDiscounted = itemInSale.applyDiscount(discountPercent);
        Amount itemInSalePriceBeforeTaxes = 
                itemInSale.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount discountAmount = itemInSalePriceBeforeTaxes.
                multiply(PERCENT_MULTIPLIER * discountPercent);
        Amount discountedPriceBeforeTaxes = itemInSalePriceBeforeTaxes.
                minus(discountAmount);
        Amount itemInSaleDiscountedPriceBeforeTaxes = 
                itemInSaleDiscounted.
                        getItemInfo().
                        getItemPrice().
                        getPriceBeforeTax();
        Amount expectedResult = discountedPriceBeforeTaxes;
        Amount result = itemInSaleDiscountedPriceBeforeTaxes;
        assertEquals(expectedResult, result, "Wrong price after discount.");
    }
    
}

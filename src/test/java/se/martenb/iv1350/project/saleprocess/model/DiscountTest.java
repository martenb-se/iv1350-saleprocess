package se.martenb.iv1350.project.saleprocess.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.integration.discount.TestingDiscountRule;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.DiscountDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;

public class DiscountTest {
    private TestingObjectCreator testObjCr;
    TestingDiscountRule testingDiscountRule;
    Discount discount = null;
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        testingDiscountRule = TestingDiscountRule.getTestingDiscountRule();
        discount = new Discount(testingDiscountRule);
    }
    
    @AfterEach
    public void tearDown() {
        discount = null;
        testingDiscountRule = null;
        testObjCr = null;
    }

    @Test
    public void testStartDiscount() {
        int itemsInSale = 50;
        SaleDTO saleInfo = testObjCr.generateSaleDTOAndFinalPrice(itemsInSale);
        CustomerDTO customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        try {
            discount.tryDiscountOnSale(customerInfo);
            fail("Illegal state reached, allowed to try discounts on " + 
                    "nonexistent sale.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalStateException;
            if (correctExceptionThrown) {
                discount.startDiscount(saleInfo);
                try {
                    DiscountDTO discountDTO = 
                            discount.tryDiscountOnSale(customerInfo);
                } catch (Exception unwantedException) {
                    fail("Failed specify sale used for trying discounts on.");
                }
            } else {
                fail("Test failed unexpectedly.");
            }
        }
    }

    @Test
    public void testTryDiscountOnSaleApplicableItems() {
        int itemsInSale = 50;
        SaleDTO saleInfo = testObjCr.generateSaleDTOAndFinalPrice(itemsInSale);
        CustomerDTO customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        discount.startDiscount(saleInfo);
        DiscountDTO discountInfo = discount.tryDiscountOnSale(customerInfo);
        boolean expectedResult = discountInfo.getDiscountPercent() > 0;
        assertTrue(expectedResult, "Discount was not applied when it " + 
                "should have been.");
    }

    @Test
    public void testTryDiscountOnSaleNotApplicableItems() {
        int itemsInSale = 10;
        SaleDTO saleInfo = testObjCr.generateSaleDTOAndFinalPrice(itemsInSale);
        CustomerDTO customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        discount.startDiscount(saleInfo);
        DiscountDTO discountInfo = discount.tryDiscountOnSale(customerInfo);
        boolean expectedResult = discountInfo.getDiscountPercent() > 0;
        assertFalse(expectedResult, "Discount was applied when it should" + 
                "not have been.");
    }
    
}

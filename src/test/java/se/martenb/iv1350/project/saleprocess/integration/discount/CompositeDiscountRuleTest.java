package se.martenb.iv1350.project.saleprocess.integration.discount;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.util.Amount;

public class CompositeDiscountRuleTest {
    private TestingObjectCreator testObjCr;
    private CompositeDiscountRule compositeDiscountRule;
    private TestingDiscountRule testingDiscountRule;
    SaleDTO saleInfoWithoutApplicapleItems;
    SaleDTO saleInfoWithApplicapleItems;
    CustomerDTO customerInfo;
    
    public CompositeDiscountRuleTest() {
    }
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        int itemsInSaleWithoutApplicapleItems = 10;
        int itemsInSaleWithApplicapleItems = 50;
        saleInfoWithoutApplicapleItems = 
                testObjCr.generateSaleDTOAndFinalPrice(itemsInSaleWithoutApplicapleItems);
        saleInfoWithApplicapleItems = 
                testObjCr.generateSaleDTOAndFinalPrice(itemsInSaleWithApplicapleItems);
        customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        testingDiscountRule = TestingDiscountRule.getTestingDiscountRule();
        compositeDiscountRule = new CompositeDiscountRule();
    }
    
    @AfterEach
    public void tearDown() {
        compositeDiscountRule = null;
        testingDiscountRule = null;
        customerInfo = null;
        saleInfoWithoutApplicapleItems = null;
        saleInfoWithApplicapleItems = null;
        testObjCr = null;
    }

    //@Test
    public void testAddRule() {
        try {
            compositeDiscountRule.isRuleApplicable(
                    saleInfoWithApplicapleItems, customerInfo);
            fail("Illegal state reached, tested if nonexistent discount " + 
                    "rules are applicable.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalStateException;
            if (correctExceptionThrown) {
                compositeDiscountRule.addRule(testingDiscountRule);
                try {
                    compositeDiscountRule.isRuleApplicable(
                            saleInfoWithApplicapleItems, customerInfo);
                } catch (Exception unwantedException) {
                    fail("Adding discount rule dailed.");
                }
            } else {
                fail("Test failed unexpectedly.");
            }
        }
    }

    @Test
    public void testIsRuleApplicable() {
        compositeDiscountRule.addRule(testingDiscountRule);
        boolean result = 
                compositeDiscountRule.isRuleApplicable(
                        saleInfoWithApplicapleItems, customerInfo);
        assertTrue(result, "Wrong result for if a customer is eligible for a " + 
                "discount.");
    }

    @Test
    public void testRuleNotApplicable() {
        compositeDiscountRule.addRule(testingDiscountRule);
        boolean result = 
                compositeDiscountRule.isRuleApplicable(
                        saleInfoWithoutApplicapleItems, customerInfo);
        assertFalse(result, "Wrong result for if a customer is eligible for a " + 
                "discount.");
    }

    @Test
    public void testApplyRule() {
        compositeDiscountRule.addRule(testingDiscountRule);
        compositeDiscountRule.isRuleApplicable(
                saleInfoWithApplicapleItems, customerInfo);
        SaleDTO updatedSale = compositeDiscountRule.applyRule();
        Amount originalTotal = saleInfoWithApplicapleItems.getRunningTotal();
        Amount discountedTotal = updatedSale.getRunningTotal();
        boolean expectedResult = originalTotal.getAmount().
                compareTo(discountedTotal.getAmount()) > 0;
        assertTrue(expectedResult, "Discount was not applied when " + 
                "it should have been.");
    }

    @Test
    public void testApplyNoApplicableRules() {
        try {
            compositeDiscountRule.addRule(testingDiscountRule);
            compositeDiscountRule.isRuleApplicable(
                    saleInfoWithoutApplicapleItems, customerInfo);
            compositeDiscountRule.applyRule();
            fail("Allowed to apply rules that are not applicable.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof IllegalStateException;
            if (!correctExceptionThrown) {
                fail("Wrong exception thrown");
            }
        }
    }

    @Test
    public void testToStringEmpty() {
        String expectedResult = "";
        assertEquals(compositeDiscountRule.toString(), expectedResult, 
                "Wrong string representation for the composite discount.");
    }

    @Test
    public void testToString() {
        compositeDiscountRule.addRule(testingDiscountRule);
        StringBuilder stringRule = new StringBuilder();
        stringRule.append(testingDiscountRule.toString());
        assertEquals(stringRule.toString(), compositeDiscountRule.toString(), 
                "Wrong string representation for the composite discount.");
    }
    
}

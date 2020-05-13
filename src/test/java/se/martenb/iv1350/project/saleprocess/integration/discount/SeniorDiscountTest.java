package se.martenb.iv1350.project.saleprocess.integration.discount;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.util.Amount;

public class SeniorDiscountTest {
    private TestingObjectCreator testObjCr;
    private DiscountRule discountRule;
    SaleDTO saleInfo;
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        discountRule = SeniorDiscount.getSeniorDiscount();
        int itemsInSale = 50;
        saleInfo = testObjCr.generateSaleDTOAndFinalPrice(itemsInSale);
    }
    
    @AfterEach
    public void tearDown() {
        saleInfo = null;
        discountRule = null;
        testObjCr = null;
    }

    @Test
    public void testIsRuleApplicable() {
        int ageOldEnoughCustomer = 80;
        GregorianCalendar birthDate = new GregorianCalendar();
        birthDate.add(Calendar.YEAR, ageOldEnoughCustomer * -1);
        CustomerDTO customerInfo = new CustomerDTO(birthDate.getTime());
        boolean result = discountRule.isRuleApplicable(saleInfo, customerInfo);
        assertTrue(result, "Wrong result for if a customer is eligible for a " + 
                "discount.");
    }

    @Test
    public void testIsRuleApplicableTooYoung() {
        int ageNotOldEnoughCustomer = 50;
        GregorianCalendar birthDate = new GregorianCalendar();
        birthDate.add(Calendar.YEAR, ageNotOldEnoughCustomer * -1);
        CustomerDTO customerInfo = new CustomerDTO(birthDate.getTime());
        boolean result = discountRule.isRuleApplicable(saleInfo, customerInfo);
        assertFalse(result, "Wrong result for if a customer is eligible for a " + 
                "discount.");
    }

    @Test
    public void testApplyRule() {
        int ageOldEnoughCustomer = 80;
        GregorianCalendar birthDate = new GregorianCalendar();
        birthDate.add(Calendar.YEAR, ageOldEnoughCustomer * -1);
        CustomerDTO customerInfo = new CustomerDTO(birthDate.getTime());
        discountRule.isRuleApplicable(saleInfo, customerInfo);
        SaleDTO updatedSale = discountRule.applyRule();
        Amount originalTotal = saleInfo.getRunningTotal();
        Amount discountedTotal = updatedSale.getRunningTotal();
        boolean expectedResult = originalTotal.getAmount().
                compareTo(discountedTotal.getAmount()) > 0;
        assertTrue(expectedResult, "Discount was not applied when " + 
                "it should have been.");
    }

    @Test
    public void testApplyRuleTooYoung() {
        int ageNotOldEnoughCustomer = 50;
        GregorianCalendar birthDate = new GregorianCalendar();
        birthDate.add(Calendar.YEAR, ageNotOldEnoughCustomer * -1);
        CustomerDTO customerInfo = new CustomerDTO(birthDate.getTime());
        discountRule.isRuleApplicable(saleInfo, customerInfo);
        SaleDTO updatedSale = discountRule.applyRule();
        Amount originalTotal = saleInfo.getRunningTotal();
        Amount discountedTotal = updatedSale.getRunningTotal();
        boolean expectedResult = originalTotal.getAmount().
                compareTo(discountedTotal.getAmount()) > 0;
        assertTrue(expectedResult, "Discount was applied when it  " + 
                "should not have been.");
    }

    @Test
    public void testToString() {
        int customerAge = 65;
        double fullSaleDiscountPercent = 10;
        StringBuilder stringRule = new StringBuilder();
        stringRule.append("Senior Discount!");
        stringRule.append("\n");
        stringRule.append("║ For customers aged ");
        stringRule.append(customerAge);
        stringRule.append(" and over.\n");
        stringRule.append("║ - ");
        stringRule.append(fullSaleDiscountPercent);
        stringRule.append("% off whole sale!");
        
        assertEquals(stringRule.toString(), discountRule.toString(), 
                "Wrong string representation for the discount.");
    }
    
}

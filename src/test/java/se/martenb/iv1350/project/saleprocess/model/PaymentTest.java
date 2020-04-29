package se.martenb.iv1350.project.saleprocess.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.StoreRegistry;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;


public class PaymentTest {
    TestingObjectCreator testObjCr;
    
    public PaymentTest() {
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
    public void testPay() {
        StoreRegistry storeRegistry = new StoreRegistry();
        Register register = new Register();
        int entriesInSale = 10;
        Amount paidAmount = new Amount(5000);
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        Payment payment = new Payment(basicTestSaleDTO, storeRegistry, register);
        Amount amountInRegisterBeforePayment = register.getAmount();
        Amount amountToPutInRegister = basicTestSaleDTO.getRunningTotal();
        payment.pay(paidAmount);
        Amount expResult = 
                amountInRegisterBeforePayment.plus(amountToPutInRegister);
        Amount result = register.getAmount();
        
        assertEquals(expResult, result, 
                "Wrong amount in register after payment.");
    }
    
}

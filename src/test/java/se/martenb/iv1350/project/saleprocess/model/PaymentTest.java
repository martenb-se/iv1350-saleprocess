package se.martenb.iv1350.project.saleprocess.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.StoreRegistry;
import se.martenb.iv1350.project.saleprocess.testing.TestingEnvironment;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.testing.TestingPurchaseObserver;
import se.martenb.iv1350.project.saleprocess.util.PriceTotal;


public class PaymentTest {
    TestingObjectCreator testObjCr;
    StoreRegistry storeRegistry;
    Register register;
    Payment payment;
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        storeRegistry = new StoreRegistry();
        register = new Register();
        payment = new Payment(storeRegistry, register);
    }
    
    @AfterEach
    public void tearDown() {
        payment = null;
        register = null;
        storeRegistry = null;
        testObjCr = null;
    }

    @Test
    public void testPay() {
        int entriesInSale = 10;
        Amount paidAmount = new Amount(5000);
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        payment.startPayment(basicTestSaleDTO);
        Amount amountInRegisterBeforePayment = register.getAmount();
        Amount amountToPutInRegister = basicTestSaleDTO.getRunningTotal();
        payment.pay(paidAmount);
        Amount expResult = 
                amountInRegisterBeforePayment.plus(amountToPutInRegister);
        Amount result = register.getAmount();
        
        assertEquals(expResult, result, 
                "Wrong amount in register after payment.");
    }

    @Test
    public void testPurchaseObserver() {
        int entriesInSale = 10;
        Amount paidAmount = new Amount(5000);
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        PriceTotal finalPrice = testObjCr.getLastFinalPrice();
        PurchaseDTO purchaseInfo = 
                testObjCr.generateLastPurchaseDTO(finalPrice, paidAmount);
        payment.startPayment(basicTestSaleDTO);
        TestingEnvironment testEnv = new TestingEnvironment();
        testEnv.redirectSystemOut();
        String outputBeforeActiveTestingPurchaseObserver = 
                testEnv.getRedirectedSystemOut();
        payment.addPurchaseObserver(new TestingPurchaseObserver());
        payment.pay(paidAmount);
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
        assertEquals(expectedObserverOutput.toString(), 
                outputAfterActiveTestingPurchaseObserver,
                "TestingPurchaseObserver did not get correct data.");
    }

    @Test
    public void testPurchaseObserverMultiple() {
        int entriesInSale = 10;
        Amount paidAmount = new Amount(5000);
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        PriceTotal finalPrice = testObjCr.getLastFinalPrice();
        PurchaseDTO purchaseInfo = 
                testObjCr.generateLastPurchaseDTO(finalPrice, paidAmount);
        payment.startPayment(basicTestSaleDTO);
        TestingEnvironment testEnv = new TestingEnvironment();
        testEnv.redirectSystemOut();
        payment.addPurchaseObserver(new TestingPurchaseObserver());
        payment.addPurchaseObserver(new TestingPurchaseObserver());
        payment.addPurchaseObserver(new TestingPurchaseObserver());
        payment.addPurchaseObserver(new TestingPurchaseObserver());
        payment.addPurchaseObserver(new TestingPurchaseObserver());
        payment.pay(paidAmount);
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
        
        assertEquals(expectedObserverOutput.toString(), 
                outputAfterActiveTestingPurchaseObserver,
                "TestingPurchaseObservers did not all get notified.");
    }
    
}

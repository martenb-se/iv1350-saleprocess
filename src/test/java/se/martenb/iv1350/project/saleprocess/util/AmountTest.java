package se.martenb.iv1350.project.saleprocess.util;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AmountTest {
    @Test
    public void testNotEquals() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 9;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = new Amount(amountOfSecondOperand);
        assertNotEquals(firstOperand, secondOperand, "Amount instances with " + 
                "different states are equal.");
    }
    
    @Test
    public void testNotEqualsNull() {
        double amountOfFirstOperand = 10;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = null;
        assertNotEquals(firstOperand, secondOperand, "Initated Amount and  " + 
                "null Amount are equal.");
    }
    
    @Test
    public void testNotEqualsJavaLangObject() {
        double amountOfFirstOperand = 10;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Object secondOperand = new Object();
        assertNotEquals(firstOperand, secondOperand, "Amount and " + 
                "java.Lang.Object are equal.");
    }

    @Test
    public void testEquals() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 10;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = new Amount(amountOfSecondOperand);
        assertEquals(firstOperand, secondOperand, "Amount instances with same" +
                "states are not equal.");
    }

    @Test
    public void testPlus() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = new Amount(amountOfSecondOperand);
        Amount expResult = new Amount(amountOfFirstOperand + 
                amountOfSecondOperand);
        Amount result = firstOperand.plus(secondOperand);
        assertEquals(expResult, result, "Wrong addition result.");
    }

    @Test
    public void testPlus_double() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount expResult = new Amount(amountOfFirstOperand + 
                amountOfSecondOperand);
        Amount result = firstOperand.plus(amountOfSecondOperand);
        assertEquals(expResult, result, "Wrong addition result.");
    }
    
    @Test
    public void testMinus() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = new Amount(amountOfSecondOperand);
        Amount expResult = new Amount(amountOfFirstOperand - 
                amountOfSecondOperand);
        Amount result = firstOperand.minus(secondOperand);
        assertEquals(expResult, result, "Wrong subtraciton result.");
    }
    
    @Test
    public void testMinus_double() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount expResult = new Amount(amountOfFirstOperand - 
                amountOfSecondOperand);
        Amount result = firstOperand.minus(amountOfSecondOperand);
        assertEquals(expResult, result, "Wrong subtraciton result.");
    }

    @Test
    public void testMinusNegResult() {
        double amountOfFirstOperand = 5;
        double amountOfSecondOperand = 10;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = new Amount(amountOfSecondOperand);
        Amount expResult = new Amount(amountOfFirstOperand - 
                amountOfSecondOperand);
        Amount result = firstOperand.minus(secondOperand);
        assertEquals(expResult, result, "Wrong subtraciton result.");
    }

    @Test
    public void testMinusZeroResultNegOperand() {
        double amountOfFirstOperand = -5;
        double amountOfSecondOperand = -5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = new Amount(amountOfSecondOperand);
        Amount expResult = new Amount(amountOfFirstOperand - 
                amountOfSecondOperand);
        Amount result = firstOperand.minus(secondOperand);
        assertEquals(expResult, result, "Wrong subtraciton result.");
    }
    
    @Test
    public void testMultiply() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = new Amount(amountOfSecondOperand);
        Amount expResult = new Amount(amountOfFirstOperand * 
                amountOfSecondOperand);
        Amount result = firstOperand.multiply(secondOperand);
        assertEquals(expResult, result, "Wrong multiplication result.");
    }
    
    @Test
    public void testMultiply_double() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount expResult = new Amount(amountOfFirstOperand * 
                amountOfSecondOperand);
        Amount result = firstOperand.multiply(amountOfSecondOperand);
        assertEquals(expResult, result, "Wrong multiplication result.");
    }
    
    @Test
    public void testDivide() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount secondOperand = new Amount(amountOfSecondOperand);
        Amount expResult = new Amount(amountOfFirstOperand / 
                amountOfSecondOperand);
        Amount result = firstOperand.divide(secondOperand);
        assertEquals(expResult, result, "Wrong division result.");
    }
    
    @Test
    public void testDivide_double() {
        double amountOfFirstOperand = 10;
        double amountOfSecondOperand = 5;
        Amount firstOperand = new Amount(amountOfFirstOperand);
        Amount expResult = new Amount(amountOfFirstOperand / 
                amountOfSecondOperand);
        Amount result = firstOperand.divide(amountOfSecondOperand);
        assertEquals(expResult, result, "Wrong division result.");
    }

    @Test
    public void testToString() {
        double amountToTestNumerical = 10;
        Amount amountToTest = new Amount(amountToTestNumerical);
        String expectedResult = 
                amountToTest.getAmount().toString() + 
                " " + 
                amountToTest.getCurrency();
        String result = amountToTest.toString();
        assertTrue(expectedResult.equals(result));
    }
    
}

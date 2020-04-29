package se.martenb.iv1350.project.saleprocess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.util.Amount;

public class RegisterTest {

    @Test
    public void testAddAmount() {
        Register register = new Register();
        Amount initialAmountInRegister = register.getAmount();
        double amountToPutInRegisterNumerical = 1000;
        Amount amountToPutInRegister = 
                new Amount(amountToPutInRegisterNumerical);
        register.addAmount(amountToPutInRegister);
        Amount expResult = 
                initialAmountInRegister.plus(amountToPutInRegister);
        Amount result = register.getAmount();
        
        assertEquals(expResult, result, "Wrong addition result.");
    }
    
}

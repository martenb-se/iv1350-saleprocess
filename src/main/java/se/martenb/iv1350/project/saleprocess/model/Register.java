package se.martenb.iv1350.project.saleprocess.model;

import se.martenb.iv1350.project.saleprocess.util.Amount;

/**
 * Representing a register in a store and its content.
 */
public class Register {
    private Amount amount;
    
    /**
     * Creates a new instance of a {@link Register}, setting the register 
     * amount to a dummy value.
     */
    public Register() {
        double dummyRegisterAmount = 10000;
        amount = new Amount(10000);
    }
    
    /**
     * Add an {@link Amount} to the register
     */
    void addAmount(Amount amountToAdd) {
        amount = amount.plus(amountToAdd);
    }
    
    /**
     * Get the {@link Amount} for this object.
     * 
     * @return The amount.
     */
    Amount getAmount() {
        return amount;
    }
}

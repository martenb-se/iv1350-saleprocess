package se.martenb.iv1350.project.saleprocess.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents an amount of money. Instances are immutable.
 */
public class Amount {
    private final int DECIMAL_PRECISION = 2;
    private final String STANDARD_CURRENCY = "SEK";
    private final RoundingMode STANDARD_ROUNDING = RoundingMode.DOWN;
    private final BigDecimal amount;
    private final String currency;
    
    /**
     * Creates a new instance, representing the specified amount.
     * Sets the currency to the standard currency and sets the decimal 
     * precision to the specified precision.
     * 
     * @param amount The amount represented by the newly created instance.
     * 
    */
    public Amount(double amount) {
        this.amount = new BigDecimal(amount).
                setScale(DECIMAL_PRECISION, STANDARD_ROUNDING);
        this.currency = STANDARD_CURRENCY;
    }
    
    /**
     * Creates a new instance, representing the specified amount.
     * Sets the currency to the standard currency and sets the decimal 
     * precision to the specified precision.
     * 
     * @param amount The amount represented by the newly created instance.
     * 
    */
    public Amount(BigDecimal amount) {
        this.amount = amount.setScale(DECIMAL_PRECISION, STANDARD_ROUNDING);
        this.currency = STANDARD_CURRENCY;
    }

    /**
     * Get the amount for this object.
     * 
     * @return The amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Get the currency for this object.
     * 
     * @return The currency.
     */
    public String getCurrency() {
        return currency;
    }
    
    /**
     * Adds the specified <code>double</code> value to this object and returns 
     * an {@link Amount} instance with the result.
     * 
     * @param amountToAdd Amount to add
     * @return The sum of the addition.
     */
    public Amount plus(double amountToAdd) {
        BigDecimal amountToAddBigDecimal = new BigDecimal(amountToAdd);
        return new Amount(amount.add(amountToAddBigDecimal));
    }
    
    /**
     * Adds the specified {@link Amount} to this object and returns 
     * an {@link Amount} instance with the result.
     * 
     * @param amountToAdd Amount to add
     * @return The sum of the addition.
     */
    public Amount plus(Amount amountToAdd) {
        return new Amount(amount.add(amountToAdd.amount));
    }
    
    /**
     * Subtracts the specified <code>double</code> from this object and 
     * returns an {@link Amount} instance with the result.
     * 
     * @param amountToSubtract The {@link Amount} to subtract.
     * @return The difference of the subtraction.
     */
    public Amount minus(double amountToSubtract) {
        BigDecimal amountToSubtractBigDecimal = 
                new BigDecimal(amountToSubtract);
        return new Amount(amount.subtract(amountToSubtractBigDecimal));
    }
    
    /**
     * Subtracts the specified {@link Amount} from this object and returns an 
     * {@link Amount} instance with the result.
     * 
     * @param amountToSubtract The {@link Amount} to subtract.
     * @return The difference of the subtraction.
     */
    public Amount minus(Amount amountToSubtract) {
        return new Amount(amount.subtract(amountToSubtract.amount));
    }
    
    /**
     * Multiplies the specified <code>double</code> with this object and 
     * returns an {@link Amount} instance with the result.
     * 
     * @param amountToMultiplyWith The {@link Amount} multiplicand.
     * @return The product of the multiplication.
     */
    public Amount multiply(double amountToMultiplyWith) {
        BigDecimal amountToMultiplyWithBigDecimal = 
                new BigDecimal(amountToMultiplyWith);
        return new Amount(amount.multiply(amountToMultiplyWithBigDecimal));
    }
    
    /**
     * Multiplies the specified {@link Amount} with this object and returns 
     * an {@link Amount} instance with the result.
     * 
     * @param amountToMultiplyWith The {@link Amount} multiplicand.
     * @return The product of the multiplication.
     */
    public Amount multiply(Amount amountToMultiplyWith) {
        return new Amount(amount.multiply(amountToMultiplyWith.amount));
    }
    
    /**
     * Divides this object with the specified <code>double</code> and 
     * returns an {@link Amount} instance with the quotient.
     * 
     * @param amountToDivideWith The {@link Amount} divisor.
     * @return The quotient of the division.
     */
    public Amount divide(double amountToDivideWith) {
        BigDecimal amountToDivideWithBigDecimal = 
                new BigDecimal(amountToDivideWith);
        return new Amount(amount.divide(amountToDivideWithBigDecimal));
    }
    
    /**
     * Divides this object with the specified {@link Amount} and 
     * returns an {@link Amount} instance with the quotient.
     * 
     * @param amountToDivideWith The {@link Amount} divisor.
     * @return The quotient of the division.
     */
    public Amount divide(Amount amountToDivideWith) {
        return new Amount(amount.divide(amountToDivideWith.amount));
    }
    
    /**
     * Compares the specified <code>Object</code> to this object and 
     * returns <code>true</code> if they are the same object of if both 
     * instances of {@link Amount} and contain the same numerical value and 
     * the same currency.
     *
     * @param objectToCompare The object to compare this object with.
     * @return The result of the comparison, <code>true</code> or 
     * <code>false</code>.
     */
    @Override
    public boolean equals(Object objectToCompare) {
        boolean isSameObject = objectToCompare == this;
        boolean isNullObject = objectToCompare == null;
        boolean isInstanceOfAmount = objectToCompare instanceof Amount;
        if (isSameObject)
            return true;
        if (isNullObject)
            return false;
        if (!isInstanceOfAmount)
            return false;
        Amount amountToCompare = (Amount) objectToCompare;
        return amount.equals(amountToCompare.amount) && 
                currency.equals(amountToCompare.currency);
        
    }

    /**
     * Creates a string of the amount containing the numerical value and
     * the currency.
     * 
     * @return The string representing the amount.
     */
    @Override
    public String toString() {
        return amount.toString() + " " + currency;
    }
    
}

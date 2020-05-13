package se.martenb.iv1350.project.saleprocess.integration.dto;
import java.util.Date;

/**
 * Represents the customer during a sale.
 */
public class CustomerDTO {
    private final Date birthDate;
    
    /**
     * Creates a new instance of a customer object.
     * 
     * @param birthDate The birth date of the customer
     */
    public CustomerDTO(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Get the customer's birthday.
     * 
     * @return The birthday for the customer.
     */
    public Date getBirthDate() {
        return birthDate;
    }
    
}

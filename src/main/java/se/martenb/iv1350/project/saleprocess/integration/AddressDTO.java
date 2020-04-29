package se.martenb.iv1350.project.saleprocess.integration;

/**
 * Represents an address. 
 */
public class AddressDTO {
    private final String streetName;
    private final int streetNumber;
    private final int postalCode;
    private final String postTown;
    
    /**
     * Creates a new instance, representing the specified address.
     * 
     * @param streetName The street name for the address.
     * @param streetNumber The street number for the address.
     * @param postalCode The postal code for the address.
     * @param postTown The postal town for the address.
     */
    public AddressDTO(String streetName, int streetNumber, int postalCode,
    String postTown) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.postTown = postTown;
    }

    /**
     * Get the street name for the current address.
     * 
     * @return The street name.
     */
    public String getStreetName() {
        return streetName;
    }
    
    /**
     * Get the street number for the current address.
     * 
     * @return The street number.
     */
    public int getStreetNumber() {
        return streetNumber;
    }

    /**
     * Get the postal code for the current address.
     * 
     * @return The postal code.
     */
    public int getPostalCode() {
        return postalCode;
    }

    /**
     * Get the post town for the current address.
     * 
     * @return The post town.
     */
    public String getPostTown() {
        return postTown;
    }

    /**
     * Generates a <code>String</code> of the full address in this object.
     *
     * @return The address as a <code>String</code>.
     */
    @Override
    public String toString() {
        return streetName + " " + streetNumber + ", " + postalCode + " " + 
                postTown;
    }
    
}

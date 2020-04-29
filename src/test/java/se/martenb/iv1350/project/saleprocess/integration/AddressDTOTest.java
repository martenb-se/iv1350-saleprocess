package se.martenb.iv1350.project.saleprocess.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AddressDTOTest {
    private final String streetName = "Testing Street";
    private final int streetNumber = 99;
    private final int postalCode = 12345;
    private final String postTown = "Experiment City";
    private AddressDTO addrInitial;
    
    @BeforeEach
    public void setUp() {
        this.addrInitial = 
                new AddressDTO(streetName, streetNumber, postalCode, postTown);
    }
    
    @AfterEach
    public void tearDown() {
        this.addrInitial = null;
    }

    @Test
    public void testToString() {
        String expResult = streetName + " " + streetNumber + ", " + 
                postalCode + " " + postTown;
        String result = addrInitial.toString();
        assertEquals(expResult, result, "Wrong string result.");
    }

}

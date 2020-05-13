package se.martenb.iv1350.project.saleprocess.integration;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.martenb.iv1350.project.saleprocess.testing.TestingEnvironment;

public class ItemRegistryTest {
    private ItemRegistry itemRegistry;
    private TestingEnvironment testEnv;
    
    @BeforeEach
    public void setUp() {
        itemRegistry = new ItemRegistry();
        testEnv = new TestingEnvironment();
    }
    
    @AfterEach
    public void tearDown() {
        testEnv = null;
        itemRegistry = null;
    }

    @Test
    public void testGetItemInfoInvalidIDException() {
        int mustBeInvalidID = 0;
        try {
            itemRegistry.getItemInfo(mustBeInvalidID);
            fail("Fetched info for an unknown item.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof ItemRegistryException;
            assertTrue(correctExceptionThrown, "Wrong exception thrown.");
        }
    }

    @Test
    public void testHardcodedDBConnectionFailureDuringIsItemInDBException() 
            throws IOException {
        int mustBeHardcodedErrorID = 999999999;
        try {
            itemRegistry.isItemInDB(mustBeHardcodedErrorID);
            fail("Searched in database without a connection.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof ItemRegistryException;
            assertTrue(correctExceptionThrown, "Wrong exception thrown.");
        }
    }

    @Test
    public void testHardcodedDBConnectionFailureDuringGetItemInfoException() 
            throws IOException {
        int mustBeHardcodedErrorID = 999999999;
        try {
            itemRegistry.getItemInfo(mustBeHardcodedErrorID);
            fail("Searched in database without a connection.");
        } catch (Exception awaitedException) {
            boolean correctExceptionThrown = 
                    awaitedException instanceof ItemRegistryException;
            assertTrue(correctExceptionThrown, "Wrong exception thrown.");
        }
    }
    
}

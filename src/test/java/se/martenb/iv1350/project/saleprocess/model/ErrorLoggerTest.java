package se.martenb.iv1350.project.saleprocess.model;

import se.martenb.iv1350.project.saleprocess.util.ErrorLogger;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.testing.TestingEnvironment;

public class ErrorLoggerTest {
    private TestingEnvironment testEnv;
    
    @BeforeEach
    public void setUp() throws IOException {
        testEnv = new TestingEnvironment();
        testEnv.backupErrorLog();
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        testEnv.restoreErrorLog();
        testEnv = null;
    }

    @Test
    public void testLogExceptionRuntimeException() throws IOException {
        ErrorLogger errorLogger = new ErrorLogger();
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("testLogExceptionIOException ");
        errorMessage.append("RuntimeException ");
        errorMessage.append(currentTimeMillis);
        try {
            throw new RuntimeException(errorMessage.toString());
        } catch (RuntimeException exc) {
            errorLogger.logException(exc);
            boolean foundErrorMessage = 
                    testEnv.findTextInErrorLog(errorMessage.toString());
            assertTrue(foundErrorMessage, "Exception was not logged.");
        }
    }

    @Test
    public void testLogExceptionIOException() throws IOException {
        ErrorLogger errorLogger = new ErrorLogger();
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("testLogExceptionIOException ");
        errorMessage.append("IOException ");
        errorMessage.append(currentTimeMillis);
        try {
            throw new IOException(errorMessage.toString());
        } catch (IOException exc) {
            errorLogger.logException(exc);
            boolean foundErrorMessage = 
                    testEnv.findTextInErrorLog(errorMessage.toString());
            assertTrue(foundErrorMessage, "Exception was not logged.");
        }
    }

    @Test
    public void testLogException() throws IOException {
        ErrorLogger errorLogger = new ErrorLogger();
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("testLogExceptionIOException ");
        errorMessage.append("Exception ");
        errorMessage.append(currentTimeMillis);
        try {
            throw new Exception(errorMessage.toString());
        } catch (Exception exc) {
            errorLogger.logException(exc);
            boolean foundErrorMessage = 
                    testEnv.findTextInErrorLog(errorMessage.toString());
            assertTrue(foundErrorMessage, "Exception was not logged.");
        }
    }
    
}

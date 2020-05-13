package se.martenb.iv1350.project.saleprocess.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.testing.TestingEnvironment;

/**
 *
 * @author Mårten Björkman <marten.bjorkman@apelkado.com>
 */
public class ErrorMessageHandlerTest {
    private TestingEnvironment testEnv;
    
    @BeforeEach
    public void setUp() {
        testEnv = new TestingEnvironment();
        testEnv.redirectSystemOut();
    }
    
    @AfterEach
    public void tearDown() {
        testEnv.restoreSystemOut();
        testEnv = null;
    }

    @Test
    public void testShowErrorMessage() {
        String errorMessage = "Testing Error";
        String expextedResult = "!!\n!! Error! " + errorMessage + "\n!!\n";
        ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();
        errorMessageHandler.showErrorMessage(errorMessage);
        String result = testEnv.getRedirectedSystemOut();
        assertEquals(expextedResult, result, "Wrong error message.");
    }

    @Test
    public void testShowErrorMessageEmpty() {
        String errorMessage = "";
        String expextedResult = "!!\n!! Error! " + errorMessage + "\n!!\n";
        ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();
        errorMessageHandler.showErrorMessage(errorMessage);
        String result = testEnv.getRedirectedSystemOut();
        assertEquals(expextedResult, result, "Wrong error message.");
    }

    @Test
    public void testShowErrorMessageVeryLong() {
        String errorMessage = "1234567890".repeat(1000);
        String expextedResult = "!!\n!! Error! " + errorMessage + "\n!!\n";
        ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();
        errorMessageHandler.showErrorMessage(errorMessage);
        String result = testEnv.getRedirectedSystemOut();
        assertEquals(expextedResult, result, "Wrong error message.");
    }
    
}

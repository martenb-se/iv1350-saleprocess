package se.martenb.iv1350.project.saleprocess.startup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.testing.TestingEnvironment;

public class MainTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private TestingEnvironment testEnv;

    @BeforeEach
    public void setUp() throws IOException {
        testEnv = new TestingEnvironment();
        testEnv.backupErrorLog();
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        printoutBuffer = null;
        System.setOut(originalSysOut);
        testEnv.restoreErrorLog();
        testEnv = null;
    }

    @Test
    public void testMain() {
        String[] args = null;
        Main.main(args);
        String printout = printoutBuffer.toString();
        String responseFromFakeExecutionFirst = "A new sale has started";
        String responseFromFakeExecutionFinal = "Change to return to customer";
        Boolean foundResponseFromFakeExecutionFirst = 
                printout.contains(responseFromFakeExecutionFirst);
        Boolean foundResponseFromFakeExecutionFinal = 
                printout.contains(responseFromFakeExecutionFinal);
        Boolean fountStartAndEndOfProgram = 
                foundResponseFromFakeExecutionFirst && 
                foundResponseFromFakeExecutionFinal;
        assertTrue(fountStartAndEndOfProgram, 
                "Program did not start and end correctly.");
    }
    
}

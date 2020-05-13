package se.martenb.iv1350.project.saleprocess.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger for various types of errors.
 */
public class ErrorLogger {
    private static final String LOG_FILE_NAME = "error_log.txt";
    private static final boolean MODE_APPEND_TO_FILE = true;
    private PrintWriter logFile;
    
    /**
     * Creates a new instance of the exception logger and opens the log
     * file for writing.
     * 
     */
    public ErrorLogger() {}
    
    /**
     * Logs the thrown error to the log file.
     *
     * @param exception The exception that shall be logged.
    */
    public void logException(Exception exception) {
        try {
            logFile = new PrintWriter(
                    new FileWriter(LOG_FILE_NAME, MODE_APPEND_TO_FILE));
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("[");
            errorMessage.append(getCurrentTime());
            errorMessage.append("] Exception was thrown: ");
            errorMessage.append(exception.getMessage());
            errorMessage.append("\nprintStackTrace:");
            logFile.println(errorMessage);
            exception.printStackTrace(logFile);
            logFile.println("");
            logFile.close();
        } catch (IOException ex) {
            Logger.getLogger(ErrorLogger.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get current date and time.
     * 
     * @return The current time.
     */
    private String getCurrentTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter saleTimeFormat = 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSSS");
        String dateTimeString = dateTime.format(saleTimeFormat);
        return dateTimeString;
    }

}

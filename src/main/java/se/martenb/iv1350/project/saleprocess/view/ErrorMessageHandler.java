package se.martenb.iv1350.project.saleprocess.view;

/**
 * Handler for user friendly error logs.
 */
public class ErrorMessageHandler {
    /**
     * Send user friendly log to output.
     * 
     * @param logString The error message.
     */
    public void showErrorMessage(String logString) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("!!\n!! Error! ");
        errorMessage.append(logString);
        errorMessage.append("\n!!");
        System.out.println(errorMessage);
    }
    
}

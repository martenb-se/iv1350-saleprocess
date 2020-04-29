package se.martenb.iv1350.project.saleprocess.startup;

import se.martenb.iv1350.project.saleprocess.controller.Controller;
import se.martenb.iv1350.project.saleprocess.integration.Printer;
import se.martenb.iv1350.project.saleprocess.view.View;

/**
 * Starts the entire application, contains the main method used to start the application.
 */
public class Main {
    /**
     * The main method used to start the entire application.
     *
     * @param args The application does not take any command line parameters.
     */
    public static void main(String[] args) {
        Printer printer = new Printer();
        Controller contr = new Controller(printer);
        View view = new View(contr);
        view.runFakeExecution();
    }
}

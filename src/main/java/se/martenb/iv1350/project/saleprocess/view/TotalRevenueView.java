package se.martenb.iv1350.project.saleprocess.view;

import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.model.PurchaseObserver;

/**
 * A singleton that represents a physical revenue display in the store. 
 * The display shows the total amount of revenue for all sales.
 */
public class TotalRevenueView implements PurchaseObserver {
    
    private static final TotalRevenueView TOTAL_REVENUE_VIEW = 
            new TotalRevenueView();
    
    private Amount totalRevenue = new Amount(0);
    
    private static final int DISPLAY_SIZE_WIDTH = 30;
    private static final int DISPLAY_MARGIN_TOP = 1;
    private static final int DISPLAY_MARGIN_BOTTOM = 1;
    private static final int DISPLAY_MARGIN_LEFT = 3;
    private static final int DISPLAY_MARGIN_RIGHT = 3;
    private static final int DISPLAY_BORDER_SIZE = 1;
    private static final int DISPLAY_INNER_AREA_WIDTH = 
            DISPLAY_SIZE_WIDTH - DISPLAY_BORDER_SIZE * 2;
    private static final int DISPLAY_TEXT_AREA_WIDTH = 
            DISPLAY_INNER_AREA_WIDTH - DISPLAY_MARGIN_LEFT - 
            DISPLAY_MARGIN_RIGHT;
    private static final String DISPLAY_BORDER_TOP_CHAR = "═";
    private static final String DISPLAY_BORDER_BOTTOM_CHAR = "═";
    private static final String DISPLAY_BORDER_LEFT_CHAR = "║";
    private static final String DISPLAY_BORDER_RIGHT_CHAR = "║";
    private static final String DISPLAY_BORDER_TOP_LEFT_CORNER_CHAR = "╔";
    private static final String DISPLAY_BORDER_TOP_RIGHT_CORNER_CHAR = "╗";
    private static final String DISPLAY_BORDER_BOTTOM_LEFT_CORNER_CHAR = "╚";
    private static final String DISPLAY_BORDER_BOTTOM_RIGHT_CORNER_CHAR = "╝";
    
    private TotalRevenueView() {}
    
    /**
     * Get the only instance of {@link TotalRevenueView}.
     * @return The only instance of this object.
     */
    public static TotalRevenueView getTotalRevenueView() {
        return TOTAL_REVENUE_VIEW;
    }
    
    /**
     * Makes vertical borders with the specified size width, and with the 
     * specified border characters.
     * 
     * @param borderLength Length of the border without its corners.
     * @param leftCorner Character for the left corner.
     * @param rightCorner Character for the right corner.
     * @param middle Character for the border.
     */
    private void printBorderVertical(
            int borderLength, 
            String leftCorner, 
            String rightCorner, 
            String middle) {
        StringBuilder borderString = new StringBuilder();
        borderString.append(leftCorner);
        borderString.append(middle.repeat(borderLength));
        borderString.append(rightCorner);
        System.out.println(borderString);
    }
    
    /**
     * Prints the top border of the display.
     */
    private void printBorderTop() {
        printBorderVertical(
                DISPLAY_INNER_AREA_WIDTH,
                DISPLAY_BORDER_TOP_LEFT_CORNER_CHAR, 
                DISPLAY_BORDER_TOP_RIGHT_CORNER_CHAR,
                DISPLAY_BORDER_BOTTOM_CHAR);
    }
    
    
    /**
     * Prints the bottom border of the display.
     */
    private void printBorderBottom() {
        printBorderVertical(
                DISPLAY_INNER_AREA_WIDTH,
                DISPLAY_BORDER_BOTTOM_LEFT_CORNER_CHAR, 
                DISPLAY_BORDER_BOTTOM_RIGHT_CORNER_CHAR,
                DISPLAY_BORDER_TOP_CHAR);
    }
    
    /**
     * Prints vertical margins with the specified size width and height, and
     * with the specified borders.
     * 
     * @param borderLength Length of the border without its corners.
     * @param marginHeight Height of the margin.
     * @param leftBorder Character for the left border.
     * @param rightBorder Character for the right border.
     */
    private void printMarginVertical(
            int borderLength, 
            int marginHeight, 
            String leftBorder, 
            String rightBorder) {
        StringBuilder marginString = new StringBuilder();
        for(int i = 0; i < marginHeight; i++) {
            marginString.append(leftBorder);
            marginString.append(" ".repeat(borderLength));
            marginString.append(rightBorder);
            marginString.append("\n");
        }
        System.out.print(marginString);
    }
    
    /**
     * Prints the top margins onto the display.
     */
    private void printTopMargins() {
        printMarginVertical(DISPLAY_INNER_AREA_WIDTH,
                DISPLAY_MARGIN_TOP, 
                DISPLAY_BORDER_LEFT_CHAR,
                DISPLAY_BORDER_RIGHT_CHAR);
    }
    
    /**
     * Prints the bottom margins onto the display.
     */
    private void printBottomMargins() {
        printMarginVertical(DISPLAY_INNER_AREA_WIDTH,
                DISPLAY_MARGIN_BOTTOM, 
                DISPLAY_BORDER_LEFT_CHAR,
                DISPLAY_BORDER_RIGHT_CHAR);
    }
    
    /**
     * String formatter for aligning text to the right on the display.
     * @return The formatter <code>String</code>.
     */
    private String formatAlignRight() {
        return "%1$" + DISPLAY_TEXT_AREA_WIDTH + "s";
    }
    
    /**
     * String formatter for aligning text to the left on the display.
     * @return The formatter <code>String</code>.
     */
    private String formatAlignLeft() {
        return "%1$-" + DISPLAY_TEXT_AREA_WIDTH + "s";
    }
    
    /**
     * Print the specified information onto the display, align text according 
     * to alignment format.
     * 
     * @param printInformation The information to print.
     * @param alignmentFormat The format for aligning the printed text.
     */
    private void printInformation(
            String printInformation, String alignmentFormat) {
        String alignedInformation = 
                String.format(alignmentFormat, printInformation);
        StringBuilder informationString = new StringBuilder();
        informationString.append(DISPLAY_BORDER_LEFT_CHAR);
        informationString.append(" ".repeat(DISPLAY_MARGIN_LEFT));
        informationString.append(alignedInformation);
        informationString.append(" ".repeat(DISPLAY_MARGIN_RIGHT));
        informationString.append(DISPLAY_BORDER_RIGHT_CHAR);
        System.out.println(informationString);
    }
    
    /**
     * Print title onto the display.
     * @param titleText The title to print.
     */
    private void printTitle(String titleText) {
        String titleAlignment = formatAlignLeft();
        printInformation(titleText, titleAlignment);
    }
    
    /**
     * Print total revenue onto the display.
     * @param totalRevenue The total revenue to print.
     */
    private void printTotalRevenue(String totalRevenue) {
        String revenueAlignment = formatAlignRight();
        printInformation(totalRevenue, revenueAlignment);
    }
    
    /**
     * Update display with the latest total revenue.
     * 
     * @param saleInfo Sale state to get the running total from.
     */
    private void updateDisplayWithTotalRevenue() {
        String titleString = "Total revenue: ";
        String revenueTotalString = totalRevenue.toString();
        printBorderTop();
        printTopMargins();
        printTitle(titleString);
        printTotalRevenue(revenueTotalString);
        printBottomMargins();
        printBorderBottom();
    }
    
    /**
     * Increase the total logged revenue since program have started and 
     * update the revenue display with it.
     * 
     * @param purchaseInfo Info about the purchase.
     */
    @Override
    public void newRegisteredPurchase(PurchaseDTO purchaseInfo) {
        Amount amountRevenue = 
                purchaseInfo.getFinalPrice().getTotalPriceAfterTaxes();
        totalRevenue = totalRevenue.plus(amountRevenue);
        updateDisplayWithTotalRevenue();
    }
    
}

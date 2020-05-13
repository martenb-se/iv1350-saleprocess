package se.martenb.iv1350.project.saleprocess.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.model.Sale;
import se.martenb.iv1350.project.saleprocess.testing.TestingEnvironment;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.util.PriceTotal;

public class TotalRevenueViewTest {
    private TestingEnvironment testEnv;
    private TestingObjectCreator testObjCr;
    Sale saleInitial;
    TotalRevenueView totalRevenueView = TotalRevenueView.getTotalRevenueView();
    
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
    
    @BeforeEach
    public void setUp() {
        testEnv = new TestingEnvironment();
        testObjCr = new TestingObjectCreator();
        saleInitial = new Sale();
        testEnv.redirectSystemOut();
    }
    
    @AfterEach
    public void tearDown() {
        testEnv.restoreSystemOut();
        saleInitial = null;
        testObjCr = null;
        testEnv = null;
    }

    @Test
    public void testNewRegisteredItem() {
        int itemsInSale = 10;
        double numericalPaid = 10000;
        SaleDTO saleState = testObjCr.generateSaleDTOAndFinalPrice(itemsInSale);
        PriceTotal finalPrice = testObjCr.getLastFinalPrice();
        Amount amountPaid = new Amount(numericalPaid);
        PurchaseDTO purchaseInfo = testObjCr.
                generateLastPurchaseDTO(finalPrice, amountPaid);
        totalRevenueView.newRegisteredPurchase(purchaseInfo);
        String alignLeft = "%1$-" + DISPLAY_TEXT_AREA_WIDTH + "s";
        String alignRight = "%1$" + DISPLAY_TEXT_AREA_WIDTH + "s";
        
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(DISPLAY_BORDER_TOP_LEFT_CORNER_CHAR).
                append(DISPLAY_BORDER_TOP_CHAR.
                        repeat(DISPLAY_INNER_AREA_WIDTH)).
                append(DISPLAY_BORDER_TOP_RIGHT_CORNER_CHAR).
                append("\n");
        for(int i = 0; i < DISPLAY_MARGIN_TOP; i++) {
            expectedOutput.append(DISPLAY_BORDER_LEFT_CHAR).
                    append(" ".repeat(DISPLAY_INNER_AREA_WIDTH)).
                    append(DISPLAY_BORDER_RIGHT_CHAR).
                    append("\n");
        }
        expectedOutput.append(DISPLAY_BORDER_LEFT_CHAR).
                append(" ".repeat(DISPLAY_MARGIN_LEFT)).
                append(String.format(alignLeft, "Total revenue: ")).
                append(" ".repeat(DISPLAY_MARGIN_RIGHT)).
                append(DISPLAY_BORDER_RIGHT_CHAR).
                append("\n").
                append(DISPLAY_BORDER_LEFT_CHAR).
                append(" ".repeat(DISPLAY_MARGIN_LEFT)).
                append(String.format(
                        alignRight, 
                        purchaseInfo.
                                getFinalPrice().
                                getTotalPriceAfterTaxes().
                                toString())).
                append(" ".repeat(DISPLAY_MARGIN_RIGHT)).
                append(DISPLAY_BORDER_RIGHT_CHAR).
                append("\n");
        for(int i = 0; i < DISPLAY_MARGIN_BOTTOM; i++) {
            expectedOutput.append(DISPLAY_BORDER_LEFT_CHAR).
                    append(" ".repeat(DISPLAY_INNER_AREA_WIDTH)).
                    append(DISPLAY_BORDER_RIGHT_CHAR).
                    append("\n");
        }
        expectedOutput.append(DISPLAY_BORDER_BOTTOM_LEFT_CORNER_CHAR).
                append(DISPLAY_BORDER_BOTTOM_CHAR.
                        repeat(DISPLAY_INNER_AREA_WIDTH)).
                append(DISPLAY_BORDER_BOTTOM_RIGHT_CORNER_CHAR).
                append("\n");
        
        String result = testEnv.getRedirectedSystemOut();
        
        assertEquals(expectedOutput.toString(), result, 
                "Display output is wrong");
    }
    
}

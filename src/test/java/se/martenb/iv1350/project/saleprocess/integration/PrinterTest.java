package se.martenb.iv1350.project.saleprocess.integration;

import se.martenb.iv1350.project.saleprocess.integration.dto.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.PurchaseDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ReceiptDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.StoreDTO;
import se.martenb.iv1350.project.saleprocess.util.PriceTotal;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;


public class PrinterTest {
    TestingObjectCreator testObjCr;
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    
    private static final String COLUMN_SPACER_CHAR = " ";
    private static final int COLUMN_SPACER_WIDTH = 1;
    private static final int COLUMNS_TOTAL = 4;
    private static final int RECEIPT_NAME_WIDTH = 20;
    private static final int RECEIPT_QUANTITY_WIDTH = 10;
    private static final int RECEIPT_UNIT_PRICE_WIDTH = 15;
    private static final int RECEIPT_PRICE_WIDTH = 15;
    private static final int RECEIPT_COLUMN_SPACES_WIDTH = 
            (COLUMNS_TOTAL - 1) * COLUMN_SPACER_WIDTH;
    private static final int RECEIPT_TOTAL_WIDTH = RECEIPT_NAME_WIDTH +
            RECEIPT_QUANTITY_WIDTH + RECEIPT_UNIT_PRICE_WIDTH + 
            RECEIPT_PRICE_WIDTH + RECEIPT_COLUMN_SPACES_WIDTH;
    private static final int RECEIPT_LISTING_NAME_WIDTH = RECEIPT_NAME_WIDTH + 
            RECEIPT_QUANTITY_WIDTH;
    private static final int RECEIPT_LISTING_CONTENT_WIDTH = 
            RECEIPT_TOTAL_WIDTH - RECEIPT_LISTING_NAME_WIDTH;
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }
    
    @AfterEach
    public void tearDown() {
        testObjCr = null;
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }
    
    /**
     * String formatter for the name column of an item table.
     */
    private String itemTableFormatNameColumn() {
        return "%1$-" + RECEIPT_NAME_WIDTH + "s";
    }
    
    /**
     * String formatter for the quantity column of an item table.
     */
    private String itemTableFormatQuantityColumn() {
        return "%1$" + RECEIPT_QUANTITY_WIDTH + "s";
    }
    
    /**
     * String formatter for the price column of an item table.
     */
    private String itemTableFormatUnitPriceColumn() {
        return "%1$" + RECEIPT_UNIT_PRICE_WIDTH + "s";
    }
    
    /**
     * String formatter for the price column of an item table.
     */
    private String itemTableFormatPriceColumn() {
        return "%1$" + RECEIPT_PRICE_WIDTH + "s";
    }
    
    /**
     * String formatter for a listing name
     */
    private String listingNameColumn() {
        return "%1$-" + RECEIPT_LISTING_NAME_WIDTH + "s";
    }
    
    /**
     * String formatter for a listing's content
     */
    private String listingContentColumn() {
        return "%1$" + RECEIPT_LISTING_CONTENT_WIDTH + "s";
    }

    @Test
    public void testPrint() {
        int entriesInSale = 10;
        Amount paidAmount = new Amount(5000);
        Amount totalDiscount = new Amount(0);
        StoreDTO basicTestStoreDTO = testObjCr.getStoreDTO();
        SaleDTO basicTestSaleDTO = 
                testObjCr.generateSaleDTOAndFinalPrice(entriesInSale);
        PriceTotal finalPrice = testObjCr.getLastFinalPrice();
        PurchaseDTO purchaseInfo = testObjCr.generateLastPurchaseDTO(
                finalPrice, paidAmount);
        ReceiptDTO receiptDTO = new ReceiptDTO(basicTestStoreDTO, purchaseInfo, 
                totalDiscount, basicTestSaleDTO);
        Printer printer = new Printer();
        printer.print(receiptDTO);
        String printout = printoutBuffer.toString();
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(StringUtils.center(
                receiptDTO.
                        getStoreInfo().
                        getStoreName(),
                RECEIPT_TOTAL_WIDTH)).
                append("\n\n").
                append(StringUtils.center(
                        receiptDTO.
                                getStoreInfo().
                                getStoreAddress().
                                toString(),
                        RECEIPT_TOTAL_WIDTH)).
                append("\n\n").
                append(String.format(itemTableFormatNameColumn(), 
                        "Item name")).
                append(COLUMN_SPACER_CHAR.repeat(COLUMN_SPACER_WIDTH)).
                append(String.format(itemTableFormatQuantityColumn(), 
                        "Quantity")).
                append(COLUMN_SPACER_CHAR.repeat(COLUMN_SPACER_WIDTH)).
                append(String.format(itemTableFormatUnitPriceColumn(), 
                        "Unit price")).
                append(COLUMN_SPACER_CHAR.repeat(COLUMN_SPACER_WIDTH)).
                append(String.format(itemTableFormatPriceColumn(), 
                        "Total")).
                append("\n");
        
        for (ItemInSaleDTO itemInSale : 
                receiptDTO.getSaleInfo().getItemsInSale()) {
            expectedOutput.append(String.format(itemTableFormatNameColumn(), 
                        itemInSale.getItemInfo().getItemName())).
                    append(COLUMN_SPACER_CHAR.repeat(COLUMN_SPACER_WIDTH)).
                    append(String.format(itemTableFormatQuantityColumn(), 
                            itemInSale.
                                getItemQuantity())).
                    append(COLUMN_SPACER_CHAR.repeat(COLUMN_SPACER_WIDTH)).
                    append(String.format(itemTableFormatUnitPriceColumn(), 
                            itemInSale.
                                    getItemInfo().
                                    getItemPrice().
                                    getPriceAfterTax() + 
                            "/" + 
                            itemInSale.
                                    getItemQuantity().
                                    getUnitString())).
                    append(COLUMN_SPACER_CHAR.repeat(COLUMN_SPACER_WIDTH)).
                    append(String.format(itemTableFormatPriceColumn(), 
                            itemInSale.
                                    getItemInfo().
                                    getItemPrice().
                                    getPriceAfterTax().
                                    multiply(itemInSale.
                                            getItemQuantity().
                                            getNumericalValue()))).
                    append("\n");
        }
        
        expectedOutput.
                append(" ".repeat(RECEIPT_LISTING_NAME_WIDTH)).
                append("-".repeat(RECEIPT_LISTING_CONTENT_WIDTH)).
                append("\n").
                append(String.format(listingNameColumn(), "Total amount")).
                append(String.format(listingContentColumn(), 
                        receiptDTO.
                                getPurchaseInfo().
                                getFinalPrice().
                                getTotalPriceAfterTaxes())).
                append("\n").
                append(String.format(listingNameColumn(), "(Included taxes)")).
                append(String.format(listingContentColumn(), 
                        receiptDTO.
                                getPurchaseInfo().
                                getFinalPrice().
                                getTotalTaxes())).
                append("\n\n").
                append(String.format(listingNameColumn(), "Paid amount")).
                append(String.format(listingContentColumn(), 
                        receiptDTO.
                                getPurchaseInfo().
                                getAmountPaid())).
                append("\n").
                append(String.format(listingNameColumn(), "Change returned")).
                append(String.format(listingContentColumn(), 
                        receiptDTO.
                                getPurchaseInfo().
                                getAmountChange())).
                append("\n\n");
        
        DateTimeFormatter saleTimeFormat = 
                DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm:ss");
        
        expectedOutput.
                append(StringUtils.center(
                        receiptDTO.
                                getSaleInfo().
                                getSaleDateTime().
                                format(saleTimeFormat), 
                        RECEIPT_TOTAL_WIDTH)).
                append("\n");
        
        assertTrue(printout.equals(expectedOutput.toString()), 
                "Wrong receipt printed.");
    }
    
}

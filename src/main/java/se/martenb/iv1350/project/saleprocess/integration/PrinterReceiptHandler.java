package se.martenb.iv1350.project.saleprocess.integration;

import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.util.PriceTotal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Handler for converting {@link ReceiptDTO} data to printable data.
 */
public class PrinterReceiptHandler {
    ReceiptDTO receipt = null;
    
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
    
    /**
     * Creates a new handler for {@link ReceiptDTO}
     * @param receipt The {@link ReceiptDTO} to handle.
     */
    public PrinterReceiptHandler(ReceiptDTO receipt) {
        this.receipt = receipt;
    }
    
    /**
     * String formatter for the name column of an item table.
     * @return The formatter {@link String}.
     */
    private String itemTableFormatNameColumn() {
        return "%1$-" + RECEIPT_NAME_WIDTH + "s";
    }
    
    /**
     * String formatter for the quantity column of an item table.
     * @return The formatter {@link String}.
     */
    private String itemTableFormatQuantityColumn() {
        return "%1$" + RECEIPT_QUANTITY_WIDTH + "s";
    }
    
    /**
     * String formatter for the price column of an item table.
     * @return The formatter {@link String}.
     */
    private String itemTableFormatUnitPriceColumn() {
        return "%1$" + RECEIPT_UNIT_PRICE_WIDTH + "s";
    }
    
    /**
     * String formatter for the price column of an item table.
     * @return The formatter {@link String}.
     */
    private String itemTableFormatPriceColumn() {
        return "%1$" + RECEIPT_PRICE_WIDTH + "s";
    }
    
    /**
     * String formatter for a listing name.
     * @return The formatter {@link String}.
     */
    private String listingNameColumn() {
        return "%1$-" + RECEIPT_LISTING_NAME_WIDTH + "s";
    }
    
    /**
     * String formatter for a listing's content.
     * @return The formatter {@link String}.
     */
    private String listingContentColumn() {
        return "%1$" + RECEIPT_LISTING_CONTENT_WIDTH + "s";
    }
    
    /**
     * Add column spacer between columns to a 
     * {@link StringBuilder} object containing a table.
     * 
     * @param builder The {@link StringBuilder} to add onto.
     */
    private void builderAddColumnSpacer(StringBuilder builder) {
        builder.append(COLUMN_SPACER_CHAR.repeat(COLUMN_SPACER_WIDTH));
    }
    
    /**
     * Add a formatted item table header to a {@link StringBuilder} object.
     * 
     * @param builder The {@link StringBuilder} to add onto.
     */
    private void builderItemTableAddHeaderRow(StringBuilder builder) {
        String headerName = "Item name";
        String headerQuantity = "Quantity";
        String headerUnitPrice = "Unit price";
        String headerTotalPrice = "Total";
        
        builder.append(String.format(
                itemTableFormatNameColumn(), headerName));
        builderAddColumnSpacer(builder);
        builder.append(String.format(
                itemTableFormatQuantityColumn(), headerQuantity));
        builderAddColumnSpacer(builder);
        builder.append(String.format(
                itemTableFormatUnitPriceColumn(), headerUnitPrice));
        builderAddColumnSpacer(builder);
        builder.append(String.format(
                itemTableFormatPriceColumn(), headerTotalPrice));
        builder.append("\n");
    }
    
    /**
     * Add a formatted item table row containing the name, piece quantity and 
     * price of the item to a {@link StringBuilder} object.
     * 
     * @param builder The {@link StringBuilder} to add onto.
     */
    private void builderItemTableAddRowItemUnitPiece(StringBuilder builder, 
            ItemInSaleDTO itemInSaleInfo) {
        ItemDTO itemInfo = itemInSaleInfo.getItemInfo();
        String itemName = StringUtils.abbreviate(itemInfo.getItemName(), 
                RECEIPT_NAME_WIDTH);
        int itemQuantity = 
                (int) itemInSaleInfo.getItemQuantity().getNumericalValue();
        Amount itemPriceAfterTax = 
                itemInfo.getItemPrice().getPriceAfterTax();
        Amount totalItemPriceAfterTax = 
                itemPriceAfterTax.multiply(itemQuantity);
        builder.append(String.format(itemTableFormatNameColumn(), 
                itemName));
        builderAddColumnSpacer(builder);
        builder.append(String.format(itemTableFormatQuantityColumn(), 
                itemQuantity + " pc"));
        builderAddColumnSpacer(builder);
        builder.append(String.format(itemTableFormatUnitPriceColumn(), 
                itemPriceAfterTax + "/pc"));
        builderAddColumnSpacer(builder);
        builder.append(String.format(itemTableFormatPriceColumn(), 
                totalItemPriceAfterTax));
        builder.append("\n");

    }
    
    /**
     * Add all specified pieces of items to a formatted item table in a 
     * {@link StringBuilder} object.
     * 
     * @param builder The {@link StringBuilder} to add onto.
     */
    private void builderItemTableAddItemPiecesList(StringBuilder builder, 
            List<ItemInSaleDTO> itemsToAdd) {
        for (ItemInSaleDTO itemInSale : itemsToAdd)
            builderItemTableAddRowItemUnitPiece(builder, itemInSale);
    }
    
    /**
     * Add specified store information to a {@link StringBuilder} object.
     * 
     * @param builder The {@link StringBuilder} to add onto.
     * @param storeInfo The store information.
     */
    private void builderAddStoreInformation(StringBuilder builder, 
            StoreDTO storeInfo) {
        String storeName = storeInfo.getStoreName();
        AddressDTO storeAddress = storeInfo.getStoreAddress();
        String addressStreetName = storeAddress.getStreetName();
        int addressStreetNumber = storeAddress.getStreetNumber();
        int addressPostalCode = storeAddress.getPostalCode();
        String addressPostTown = storeAddress.getPostTown();
        String storeNameCentered = StringUtils.center(
                storeName, RECEIPT_TOTAL_WIDTH);
        String addressCentered = StringUtils.center(
                storeAddress.toString(), 
                RECEIPT_TOTAL_WIDTH);
        builder.append(storeNameCentered);
        builder.append("\n\n");
        builder.append(addressCentered);
        builder.append("\n\n");
    }
    
    /**
     * Add total price and VAT to a {@link StringBuilder} object.
     * 
     * @param builder The {@link StringBuilder} to add onto.
     * @param storeInfo The store information.
     */
    private void builderAddTotalPriceAndVAT(StringBuilder builder, 
            PriceTotal finalPrice) {
        Amount totalPriceAmount = finalPrice.getTotalPriceAfterTaxes();
        Amount totalTaxesAmount = finalPrice.getTotalTaxes();
        String listingNameTotal = 
                String.format(listingNameColumn(), "Total amount");
        String listingContentTotal = 
                String.format(listingContentColumn(), totalPriceAmount);        
        String listingNameTaxes = 
                String.format(listingNameColumn(), "(Included taxes)");        
        String listingContentTaxes = 
                String.format(listingContentColumn(), totalTaxesAmount);
        String delimiterLine = " ".repeat(RECEIPT_LISTING_NAME_WIDTH) + 
                "-".repeat(RECEIPT_LISTING_CONTENT_WIDTH);
        builder.append(delimiterLine);
        builder.append("\n");
        builder.append(listingNameTotal);
        builder.append(listingContentTotal);
        builder.append("\n");
        builder.append(listingNameTaxes);
        builder.append(listingContentTaxes);
        builder.append("\n\n");
    }
    
    /**
     * Add specified sale date and time to a {@link StringBuilder} object.
     * 
     * @param builder The {@link StringBuilder} to add onto.
     * @param storeInfo The store information.
     */
    private void builderAddPaymentAndChange(StringBuilder builder, 
            PurchaseDTO purchaseInfo) {
        Amount paymentAmount = purchaseInfo.getAmountPaid();
        Amount changeAmount = purchaseInfo.getAmountChange();
        String listingNamePaid = 
                String.format(listingNameColumn(), "Paid amount");        
        String listingContentPaid = 
                String.format(listingContentColumn(), paymentAmount);        
        String listingNameChange = 
                String.format(listingNameColumn(), "Change returned");
        String listingContentChange = 
                String.format(listingContentColumn(), changeAmount);
        builder.append(listingNamePaid);
        builder.append(listingContentPaid);
        builder.append("\n");
        builder.append(listingNameChange);
        builder.append(listingContentChange);
        builder.append("\n\n");
    }
    
    /**
     * Add specified sale date and time to a {@link StringBuilder} object.
     * 
     * @param builder The {@link StringBuilder} to add onto.
     * @param storeInfo The store information.
     */
    private void builderAddDateAndTime(StringBuilder builder, 
            LocalDateTime saleDateTime) {
        DateTimeFormatter saleTimeFormat = 
                DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' hh:mm:ss");
        String saleDateAndTimeCentered = StringUtils.center(
                saleDateTime.format(saleTimeFormat), RECEIPT_TOTAL_WIDTH);
        
        builder.append(saleDateAndTimeCentered);
    }
    
    /**
     * Creates a formatted receipt string of the data in the 
     * specified {@link ReceiptDTO}.
     * 
     * @param receiptDTO The receipt data to create a receipt string from.
     * @return The formatted receipt as a {@link String}.
     */
    String createReceiptString() {
        StringBuilder stringReceipt = new StringBuilder();
        builderAddStoreInformation(stringReceipt, receipt.getStoreInfo());
        builderItemTableAddHeaderRow(stringReceipt);
        List<ItemInSaleDTO> itemsInSaleList = 
                receipt.getSaleInfo().getItemsInSale();
        builderItemTableAddItemPiecesList(stringReceipt, itemsInSaleList);
        builderAddTotalPriceAndVAT(stringReceipt, 
                receipt.getPurchaseInfo().getFinalPrice());
        builderAddPaymentAndChange(stringReceipt, receipt.getPurchaseInfo());
        builderAddDateAndTime(stringReceipt, 
                receipt.getSaleInfo().getSaleDateTime());
        return stringReceipt.toString();
    }
    
}

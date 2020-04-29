package se.martenb.iv1350.project.saleprocess.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.integration.ItemDTO;
import se.martenb.iv1350.project.saleprocess.integration.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.util.Quantity;
import se.martenb.iv1350.project.saleprocess.util.Unit;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;

public class ItemTest {
    private TestingObjectCreator testObjCr;
    private final int itemIDInitial = 12345;
    private final String itemNameInitial = "Testing Item";
    private final double amountBeforeTaxesInitial = 550.00;
    private final double itemVATTaxRateInitial = 12.5;
    private final double itemNumericalValueInitial = 10;
    private final Unit itemUnitTypeInitial = Unit.PIECE;
    private Item itemInitial;

    private Item makeItem(int itemID, String itemName, 
            double amountBeforeTaxes, double itemVATTaxRate, 
            double itemNumericalValue, Unit itemUnitType) {
        
        ItemDTO itemInfo = testObjCr.makeItemDTOSimple(
                itemID, itemName, amountBeforeTaxes, itemVATTaxRate);
        Quantity itemQuantity = new Quantity(itemNumericalValue, itemUnitType);
        ItemInSaleDTO itemInSale = new ItemInSaleDTO(itemInfo, itemQuantity);
        return new Item(itemInSale);
    }
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        itemInitial = makeItem(itemIDInitial, itemNameInitial, 
                amountBeforeTaxesInitial, itemVATTaxRateInitial, 
                itemNumericalValueInitial, itemUnitTypeInitial);
    }
    
    @AfterEach
    public void tearDown() {
        itemInitial = null;
        testObjCr = null;
    }

    @Test
    public void testSetQuantity() {
        double newQuantity = 10;
        itemInitial.setQuantity(newQuantity);
        double expResult = newQuantity;
        double result = itemInitial.getItemInSale().getItemQuantity().
                getNumericalValue();
        assertEquals(expResult, result, "Wrong set quantity result.");
    }

    @Test
    public void testSetNegativeQuantity() {
        double newQuantity = -10;
        itemInitial.setQuantity(newQuantity);
        double expResult = newQuantity;
        double result = itemInitial.getItemInSale().getItemQuantity().
                getNumericalValue();
        assertEquals(expResult, result, "Wrong set quantity result.");
    }

    @Test
    public void testAddQuantity() {
        double addedQuantity = 10;
        itemInitial.addQuantity(addedQuantity);
        double expResult = this.itemNumericalValueInitial + addedQuantity;
        double result = itemInitial.getItemInSale().getItemQuantity().
                getNumericalValue();
        assertEquals(expResult, result, "Wrong add quantity result.");
    }

    @Test
    public void testAddNegativeQuantity() {
        double addedQuantity = -10;
        itemInitial.addQuantity(addedQuantity);
        double expResult = this.itemNumericalValueInitial + addedQuantity;
        double result = itemInitial.getItemInSale().getItemQuantity().
                getNumericalValue();
        assertEquals(expResult, result, "Wrong add quantity result.");
    }

    @Test
    public void testAddNegativeQuantities() {
        double itemNumericalValueInitialNegative = -10;
        double addedQuantity = -10;
        this.itemInitial = makeItem(itemIDInitial, itemNameInitial, 
                amountBeforeTaxesInitial, itemVATTaxRateInitial, 
                itemNumericalValueInitialNegative, itemUnitTypeInitial);
        itemInitial.addQuantity(addedQuantity);
        double expResult = itemNumericalValueInitialNegative + addedQuantity;
        double result = itemInitial.getItemInSale().getItemQuantity().
                getNumericalValue();
        assertEquals(expResult, result, "Wrong add quantity result.");
    }
    
}

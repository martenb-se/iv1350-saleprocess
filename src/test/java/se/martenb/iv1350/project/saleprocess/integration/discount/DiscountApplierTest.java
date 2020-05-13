package se.martenb.iv1350.project.saleprocess.integration.discount;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.util.Amount;

public class DiscountApplierTest {
    private TestingObjectCreator testObjCr;
    DiscountApplier discountApplier;
    SaleDTO saleInfo;
    CustomerDTO customerInfo;
    
    public DiscountApplierTest() {
    }
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        int itemsInSale = 50;
        saleInfo = testObjCr.generateSaleDTOAndFinalPrice(itemsInSale);
        customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        discountApplier = new DiscountApplier(saleInfo, customerInfo);
    }
    
    @AfterEach
    public void tearDown() {
        discountApplier = null;
        customerInfo = null;
        saleInfo = null;
        testObjCr = null;
    }

    @Test
    public void testIsItTheRightPeriod() {
        GregorianCalendar yesterday = new GregorianCalendar();
        GregorianCalendar tomorrow = new GregorianCalendar();
        yesterday.add(Calendar.DATE, -1);
        tomorrow.add(Calendar.DATE, 1);
        Date dateYesterday = yesterday.getTime();
        Date dateTomorrow = tomorrow.getTime();
        boolean result = discountApplier.isItTheRightPeriod(
                dateYesterday, dateTomorrow);
        assertTrue(result, "Wrong result for if the period is right");
    }

    @Test
    public void testIsItTheRightPeriodFuture() {
        GregorianCalendar tomorrow = new GregorianCalendar();
        GregorianCalendar dayAfterTomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, 1);
        dayAfterTomorrow.add(Calendar.DATE, 2);
        Date dateTomorrow = tomorrow.getTime();
        Date dateDayAfterTomorrow = dayAfterTomorrow.getTime();
        boolean result = discountApplier.isItTheRightPeriod(
                dateTomorrow, dateDayAfterTomorrow);
        assertFalse(result, "Wrong result for if the period is right");
    }

    @Test
    public void testIsItTheRightPeriodPast() {
        GregorianCalendar dayBeforeYesterday = new GregorianCalendar();
        GregorianCalendar yesterday = new GregorianCalendar();
        dayBeforeYesterday.add(Calendar.DATE, -2);
        yesterday.add(Calendar.DATE, -1);
        Date dateDayBeforeYesterday = dayBeforeYesterday.getTime();
        Date dateYesterday = yesterday.getTime();
        boolean result = discountApplier.isItTheRightPeriod(
                dateDayBeforeYesterday, dateYesterday);
        assertFalse(result, "Wrong result for if the period is right");
    }
    
    private Map<String,Double> makeItemNameContainsFirstNineTenPercentMap() {
        Map<String,Double> itemNameContains = new HashMap<>();
        String stringItemNameContainsFirstNine = "item #00";
        double discountPercentItemNameContainsFirstNine = 10;
        itemNameContains.put(
                stringItemNameContainsFirstNine, 
                discountPercentItemNameContainsFirstNine);
        return itemNameContains;
    }
    
    private Map<String,Double> 
        makeItemNameContainsFirstNineTenPercentWrongOrderBestMap() {
        Map<String,Double> itemNameContains = new HashMap<>();
        String stringItemNameContainsFirstNine = "item #00";
        double discountPercentItemNameContainsFirstNine = 1;
        String stringItemNameContainsFirstNineBetter = "item #0";
        double discountPercentItemNameContainsFirstNineBetter = 99;
        itemNameContains.put(
                stringItemNameContainsFirstNine, 
                discountPercentItemNameContainsFirstNine);
        itemNameContains.put(
                stringItemNameContainsFirstNineBetter, 
                discountPercentItemNameContainsFirstNineBetter);
        return itemNameContains;
    }
    
    private Map<String,Double> makeItemNameContainsImpossibleMap() {
        Map<String,Double> itemNameContains = new HashMap<>();
        String stringItemNameContainsImpossible = "ImPoSsIbLe";
        double discountPercentItemNameContainsImpossible = 10;
        itemNameContains.put(
                stringItemNameContainsImpossible, 
                discountPercentItemNameContainsImpossible);
        return itemNameContains;
    }

    @Test
    public void testIsApplicableItemInSaleUsingItemNameContains() {
        Map<String,Double> itemNameContainsFirstNineTenPercent = 
                makeItemNameContainsFirstNineTenPercentMap();
        discountApplier.setItemNameContains(itemNameContainsFirstNineTenPercent);
        boolean result = discountApplier.isApplicableItemInSale();
        assertTrue(result, "Wrong result for if an item is applicable for a " + 
                "discount.");
    }

    @Test
    public void testIsApplicableItemInSaleUsingItemNameContainsAndFail() {
        Map<String,Double> itemNameContainsImpossible = 
                makeItemNameContainsImpossibleMap();
        discountApplier.setItemNameContains(itemNameContainsImpossible);
        boolean result = discountApplier.isApplicableItemInSale();
        assertFalse(result, "Wrong result for if an item is applicable for a " + 
                "discount.");
    }

    @Test
    public void testApplyPerItemRulesUsingItemNameContains() {
        Map<String,Double> itemNameContainsFirstNineTenPercent = 
                makeItemNameContainsFirstNineTenPercentMap();
        discountApplier.setItemNameContains(
                itemNameContainsFirstNineTenPercent);
        discountApplier.applyPerItemRules();
        SaleDTO updatedSaleState = discountApplier.getSaleInfo();
        
        Amount expectedTotal = new Amount(0);
        
        for(Entry<String,Double> itemNameContainsEntry : 
                itemNameContainsFirstNineTenPercent.entrySet()) {
            for(ItemInSaleDTO itemInSale : saleInfo.getItemsInSale()) {
                String lowerCaseItemName = 
                        itemInSale.getItemInfo().getItemName().toLowerCase();
                boolean entryContainsText = 
                    lowerCaseItemName.contains(itemNameContainsEntry.getKey());
                double numericalQuantity = 
                        itemInSale.getItemQuantity().getNumericalValue();
                Amount itemPrice = null;
                if (entryContainsText) {
                    ItemInSaleDTO itemInSaleDiscount = itemInSale.applyDiscount(
                            itemNameContainsEntry.getValue());
                    itemPrice = itemInSaleDiscount.
                            getItemInfo().
                            getItemPrice().
                            getPriceAfterTax();
                } else {
                    itemPrice = itemInSale.
                            getItemInfo().
                            getItemPrice().
                            getPriceAfterTax();
                }
                Amount itemTotal = itemPrice.multiply(numericalQuantity);
                expectedTotal = expectedTotal.plus(itemTotal);
            }
        }
        Amount expectedResult = expectedTotal;
        Amount result = updatedSaleState.getRunningTotal();
        assertEquals(expectedResult, result, "Wrong discount result");
    }

    @Test
    public void testApplyPerItemRulesUsingItemNameContainsAndNoChange() {
        Map<String,Double> itemNameContainsImpossible = 
                makeItemNameContainsImpossibleMap();
        discountApplier.setItemNameContains(
                itemNameContainsImpossible);
        discountApplier.applyPerItemRules();
        SaleDTO updatedSaleState = discountApplier.getSaleInfo();
        
        Amount expectedTotal = new Amount(0);
        
        for(Entry<String,Double> itemNameContainsEntry : 
                itemNameContainsImpossible.entrySet()) {
            for(ItemInSaleDTO itemInSale : saleInfo.getItemsInSale()) {
                String lowerCaseItemName = 
                        itemInSale.getItemInfo().getItemName().toLowerCase();
                boolean entryContainsText = 
                    lowerCaseItemName.contains(itemNameContainsEntry.getKey());
                double numericalQuantity = 
                        itemInSale.getItemQuantity().getNumericalValue();
                Amount itemPrice = null;
                if (entryContainsText) {
                    ItemInSaleDTO itemInSaleDiscount = itemInSale.applyDiscount(
                            itemNameContainsEntry.getValue());
                    itemPrice = itemInSaleDiscount.
                            getItemInfo().
                            getItemPrice().
                            getPriceAfterTax();
                } else {
                    itemPrice = itemInSale.
                            getItemInfo().
                            getItemPrice().
                            getPriceAfterTax();
                }
                Amount itemTotal = itemPrice.multiply(numericalQuantity);
                expectedTotal = expectedTotal.plus(itemTotal);
            }
        }
        Amount expectedResult = expectedTotal;
        Amount result = updatedSaleState.getRunningTotal();
        assertEquals(expectedResult, result,  "Wrong discount result");
    }

    @Test
    public void testApplyBestPerItemRulesUsingItemNameContains() {
        Map<String,Double> itemNameContainsWrongOrderBest = 
                makeItemNameContainsFirstNineTenPercentWrongOrderBestMap();
        discountApplier.setItemNameContains(
                itemNameContainsWrongOrderBest);
        discountApplier.applyPerItemRules();
        SaleDTO updatedSaleState = discountApplier.getSaleInfo();
        
        Amount expectedTotal = new Amount(0);
        
        for(ItemInSaleDTO itemInSale : saleInfo.getItemsInSale()) {
            String lowerCaseItemName = 
                    itemInSale.getItemInfo().getItemName().toLowerCase();
            boolean entryContainsText = false;
            Entry<String,Double> bestItemNameContainsEntry = null;
            for(Entry<String,Double> itemNameContainsEntry : 
                    itemNameContainsWrongOrderBest.entrySet()) {
                entryContainsText = 
                    lowerCaseItemName.contains(itemNameContainsEntry.getKey());
                boolean foundNoBestItemNameContainsEntryYey = 
                        bestItemNameContainsEntry == null;
                boolean foundBetterItemNameContainsEntry = 
                        foundNoBestItemNameContainsEntryYey || 
                        itemNameContainsEntry.getValue() > 
                        bestItemNameContainsEntry.getValue();
                if (entryContainsText && foundBetterItemNameContainsEntry) {
                    bestItemNameContainsEntry = itemNameContainsEntry;
                }
            }
            double numericalQuantity = 
                    itemInSale.getItemQuantity().getNumericalValue();
            Amount itemPrice = null;
            if (entryContainsText) {
                ItemInSaleDTO itemInSaleDiscount = itemInSale.applyDiscount(
                        bestItemNameContainsEntry.getValue());
                itemPrice = itemInSaleDiscount.
                        getItemInfo().
                        getItemPrice().
                        getPriceAfterTax();
            } else {
                itemPrice = itemInSale.
                        getItemInfo().
                        getItemPrice().
                        getPriceAfterTax();
            }
            Amount itemTotal = itemPrice.multiply(numericalQuantity);
            expectedTotal = expectedTotal.plus(itemTotal);
        }
        Amount expectedResult = expectedTotal;
        Amount result = updatedSaleState.getRunningTotal();
        System.out.println(expectedResult);
        System.out.println(result);
        assertEquals(expectedResult, result, "Best discount was not used");
    }
}

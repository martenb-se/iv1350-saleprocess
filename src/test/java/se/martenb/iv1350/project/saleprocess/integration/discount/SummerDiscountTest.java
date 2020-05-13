package se.martenb.iv1350.project.saleprocess.integration.discount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.martenb.iv1350.project.saleprocess.integration.dto.CustomerDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.ItemInSaleDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.SaleDTO;
import se.martenb.iv1350.project.saleprocess.testing.TestingObjectCreator;
import se.martenb.iv1350.project.saleprocess.util.Amount;
import se.martenb.iv1350.project.saleprocess.util.Quantity;
import se.martenb.iv1350.project.saleprocess.util.Unit;

public class SummerDiscountTest {
    private TestingObjectCreator testObjCr;
    private DiscountRule discountRule;
    
    private final int ID_AMAZING_FRUIT_CEREAL = 
            1;
    private final int ID_HEALTHY_CHOCOLATE_CNACK = 
            2;
    private final int ID_ORGANIC_BANANA_COOKIES = 
            3;
    private final int ID_AMAZING_BERRY_DRINK = 
            4;
    private final int ID_HEALTHY_STRAWBERRY_CEREAL = 
            5;
    private final int ID_TASTY_STRAWBERRY_DRINK = 
            6;
    private final String NAME_AMAZING_FRUIT_CEREAL = 
            "Amazing Fruit Cereal";
    private final String NAME_HEALTHY_CHOCOLATE_CNACK = 
            "Healthy Chocolate Snack";
    private final String NAME_ORGANIC_BANANA_COOKIES = 
            "Organic Banana Cookies";
    private final String NAME_AMAZING_BERRY_DRINK = 
            "Amazing Berry Drink";
    private final String NAME_HEALTHY_STRAWBERRY_CEREAL = 
            "Healthy Strawberry Cereal";
    private final String NAME_TASTY_STRAWBERRY_DRINK = 
            "Tasty Strawberry Drink";
    private final Quantity QUANTITY_PIECE_ONE = new Quantity(1, Unit.PIECE);
    private final Quantity QUANTITY_PIECE_TWO = new Quantity(2, Unit.PIECE);
    private final Quantity QUANTITY_PIECE_THREE = new Quantity(3, Unit.PIECE);
    private final Quantity QUANTITY_PIECE_FOUR = new Quantity(4, Unit.PIECE);
    private final Quantity QUANTITY_PIECE_FIVE = new Quantity(5, Unit.PIECE);
    private final double TAX_RATE = 12;
    private final double PRICE_CHEAP = 8;
    private final double PRICE_MODERATE = 49;
    private final double PRICE_EXPENSIVE = 145;
    private Date startDate;
    private Date endDate;
    
    @BeforeEach
    public void setUp() {
        testObjCr = new TestingObjectCreator();
        startDate = new GregorianCalendar(
                Calendar.getInstance().get(Calendar.YEAR), Calendar.MAY, 1).
                getTime();
        endDate = new GregorianCalendar(
                Calendar.getInstance().get(Calendar.YEAR), Calendar.AUGUST, 31).
                getTime();
        discountRule = SummerDiscount.getSummerDiscount();
    }
    
    @AfterEach
    public void tearDown() {
        discountRule = null;
        endDate = null;
        startDate = null;
        testObjCr = null;
    }
    
    private int calculateTotalItems(List<ItemInSaleDTO> itemsInSale) {
        int totalItems = 0;
        for(ItemInSaleDTO itemInSale : itemsInSale) {
            double numericalQuantity = 
                    itemInSale.
                            getItemQuantity().
                            getNumericalValue();
            totalItems += numericalQuantity;
        }
        return totalItems;
    }
    
    private Amount calculateRunningTotal(List<ItemInSaleDTO> itemsInSale) {
        Amount runningTotal = new Amount(0);
        for(ItemInSaleDTO itemInSale : itemsInSale) {
            double numericalQuantity = 
                    itemInSale.
                            getItemQuantity().
                            getNumericalValue();
            Amount itemTotal = 
                    itemInSale.
                            getItemInfo().
                            getItemPrice().
                            getPriceAfterTax().
                            multiply(numericalQuantity);
            runningTotal = runningTotal.plus(itemTotal);
        }
        return runningTotal;
    }
    
    private List<ItemInSaleDTO> itemInSaleListApplicableItems() {
        List<ItemInSaleDTO> itemInSaleList = new ArrayList<>();
        itemInSaleList.add(new ItemInSaleDTO(
                testObjCr.makeItemDTOSimple(
                        ID_AMAZING_FRUIT_CEREAL,  
                        NAME_AMAZING_FRUIT_CEREAL, 
                        PRICE_CHEAP, 
                        TAX_RATE), 
                QUANTITY_PIECE_THREE));
        itemInSaleList.add(new ItemInSaleDTO(
                testObjCr.makeItemDTOSimple(
                        ID_AMAZING_BERRY_DRINK,  
                        NAME_AMAZING_BERRY_DRINK, 
                        PRICE_EXPENSIVE, 
                        TAX_RATE), 
                QUANTITY_PIECE_FIVE));
        itemInSaleList.add(new ItemInSaleDTO(
                testObjCr.makeItemDTOSimple(
                        ID_HEALTHY_STRAWBERRY_CEREAL,  
                        NAME_HEALTHY_STRAWBERRY_CEREAL, 
                        PRICE_MODERATE, 
                        TAX_RATE), 
                QUANTITY_PIECE_TWO));
        itemInSaleList.add(new ItemInSaleDTO(
                testObjCr.makeItemDTOSimple(
                        ID_TASTY_STRAWBERRY_DRINK,
                        NAME_TASTY_STRAWBERRY_DRINK,
                        PRICE_EXPENSIVE, 
                        TAX_RATE), 
                QUANTITY_PIECE_FOUR));
        return itemInSaleList;
    }
    
    private List<ItemInSaleDTO> itemInSaleListWithoutApplicableItems() {
        List<ItemInSaleDTO> itemInSaleList = new ArrayList<>();
        itemInSaleList.add(new ItemInSaleDTO(
                testObjCr.makeItemDTOSimple(
                        ID_AMAZING_FRUIT_CEREAL,  
                        NAME_AMAZING_FRUIT_CEREAL, 
                        PRICE_CHEAP, 
                        TAX_RATE), 
                QUANTITY_PIECE_THREE));
        itemInSaleList.add(new ItemInSaleDTO(
                testObjCr.makeItemDTOSimple(
                        ID_HEALTHY_CHOCOLATE_CNACK,  
                        NAME_HEALTHY_CHOCOLATE_CNACK, 
                        PRICE_EXPENSIVE, 
                        TAX_RATE), 
                QUANTITY_PIECE_ONE));
        itemInSaleList.add(new ItemInSaleDTO(
                testObjCr.makeItemDTOSimple(
                        ID_ORGANIC_BANANA_COOKIES,  
                        NAME_ORGANIC_BANANA_COOKIES, 
                        PRICE_MODERATE, 
                        TAX_RATE), 
                QUANTITY_PIECE_TWO));
        return itemInSaleList;
    }
    
    private SaleDTO saleWithApplicableItemsForDiscounts() {
        LocalDateTime saleDateTime = LocalDateTime.now();   
        List<ItemInSaleDTO> itemsInSaleWithDiscounted = 
                itemInSaleListApplicableItems();
        Amount runningTotalWithDiscounted = 
                calculateRunningTotal(itemsInSaleWithDiscounted);
        int totalItemsWithDiscounted = 
                calculateTotalItems(itemsInSaleWithDiscounted);
        SaleDTO saleInfo = new SaleDTO(
                saleDateTime, 
                runningTotalWithDiscounted, 
                itemsInSaleWithDiscounted, 
                totalItemsWithDiscounted);
        return saleInfo;
    }
    
    private SaleDTO saleWithoutApplicableItemsForDiscounts() {
        LocalDateTime saleDateTime = LocalDateTime.now();
        List<ItemInSaleDTO> itemsInSaleWithoutDiscounted = 
                itemInSaleListWithoutApplicableItems();
        Amount runningTotalWithoutDiscounted = 
                calculateRunningTotal(itemsInSaleWithoutDiscounted);
        int totalItemsWithoutDiscounted = 
                calculateTotalItems(itemsInSaleWithoutDiscounted);
        SaleDTO saleInfo = new SaleDTO(
                saleDateTime, 
                runningTotalWithoutDiscounted, 
                itemsInSaleWithoutDiscounted, 
                totalItemsWithoutDiscounted);
        return saleInfo;
    }
    
    private boolean isItTheRightPeriodForTestingSummerDiscounts() {
        Date todayDate = new Date();
        return !(todayDate.before(startDate) || todayDate.after(endDate));
    }

    @Test
    public void testIsRuleApplicableItems() {
        CustomerDTO customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        boolean result = discountRule.isRuleApplicable(
                saleWithApplicableItemsForDiscounts(), customerInfo);
        if(!isItTheRightPeriodForTestingSummerDiscounts())
            assertFalse(result, "Wrong result for if an item is applicable " + 
                    "for a discount.");
        assertTrue(result, "Wrong result for if an item is applicable for a " + 
                "discount.");
    }

    @Test
    public void testIsRuleNotApplicableItems() {
        CustomerDTO customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        boolean result = discountRule.isRuleApplicable(
                saleWithoutApplicableItemsForDiscounts(), customerInfo);
        assertFalse(result, "Wrong result for if an item is applicable for a " + 
                "discount.");
    }

    @Test
    public void testApplyRuleToApplicableItems() {
        CustomerDTO customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        SaleDTO originalSale = saleWithApplicableItemsForDiscounts();
        discountRule.isRuleApplicable(originalSale, customerInfo);
        SaleDTO updatedSale = discountRule.applyRule();
        Amount originalTotal = originalSale.getRunningTotal();
        Amount discountedTotal = updatedSale.getRunningTotal();
        boolean expectedResult = originalTotal.getAmount().
                compareTo(discountedTotal.getAmount()) > 0;
        if(!isItTheRightPeriodForTestingSummerDiscounts())
            assertFalse(expectedResult, "Discount was applied when it should not " + 
                    "have been.");
        assertTrue(expectedResult, "Discount was not applied when " + 
                "it should have been.");
    }
    
    @Test
    public void testApplyRuleToNotApplicableItems() {
        CustomerDTO customerInfo = testObjCr.makeCustomerDTOSimple(1950,1,1);
        SaleDTO originalSale = saleWithoutApplicableItemsForDiscounts();
        discountRule.isRuleApplicable(originalSale, customerInfo);
        SaleDTO updatedSale = discountRule.applyRule();
        Amount originalTotal = originalSale.getRunningTotal();
        Amount discountedTotal = updatedSale.getRunningTotal();
        boolean expectedResult = originalTotal.getAmount().
                compareTo(discountedTotal.getAmount()) == 0;
        assertTrue(expectedResult, "Discount was applied when it  " + 
                "should not have been.");
    }

    @Test
    public void testToString() {
        Map<String,Double> itemNameContains = new HashMap<>();
        itemNameContains.put("drink", 10.0);
        itemNameContains.put("strawberry", 5.0);
        StringBuilder stringRule = new StringBuilder();
        stringRule.append("Summer Discount!");
        stringRule.append("\n");
        stringRule.append("║ From ");
        stringRule.append(startDate.toString());
        stringRule.append(" to ");
        stringRule.append(endDate.toString());
        stringRule.append(":\n");
        for(Map.Entry<String,Double> itemNameContainsDiscount : 
                itemNameContains.entrySet()) {
            stringRule.append("║ - ");
            stringRule.append(itemNameContainsDiscount.getValue());
            stringRule.append("% off all ");
            stringRule.append(itemNameContainsDiscount.getKey());
            stringRule.append(" products!\n");
        }
        
        assertEquals(stringRule.toString(), discountRule.toString(), 
                "Wrong string representation for the discount.");
    }
    
}

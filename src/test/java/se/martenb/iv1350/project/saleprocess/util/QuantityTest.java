package se.martenb.iv1350.project.saleprocess.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {
    
    @Test
    public void testGetUnitStringStandard() {
        Quantity quantity = new Quantity(1);
        String result = quantity.getUnitString();
        String expectedResult = "pc";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testGetUnitStringPiece() {
        Quantity quantity = new Quantity(1, Unit.PIECE);
        String result = quantity.getUnitString();
        String expectedResult = "pc";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testGetUnitStringKilogram() {
        Quantity quantity = new Quantity(1, Unit.KILOGRAM);
        String result = quantity.getUnitString();
        String expectedResult = "kg";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testGetUnitStringLitre() {
        Quantity quantity = new Quantity(1, Unit.LITRE);
        String result = quantity.getUnitString();
        String expectedResult = "l";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testGetUnitStringUndefined() {
        Quantity quantity = new Quantity(1, null);
        String result = quantity.getUnitString();
        String expectedResult = "";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testToStringStandard() {
        Quantity quantity = new Quantity(1);
        String result = quantity.toString();
        String expectedResult = (int) quantity.getNumericalValue() + " pc";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testToStringPiece() {
        Quantity quantity = new Quantity(1, Unit.PIECE);
        String result = quantity.toString();
        String expectedResult = (int) quantity.getNumericalValue() + " pc";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testToStringKilogram() {
        Quantity quantity = new Quantity(1, Unit.KILOGRAM);
        String result = quantity.toString();
        String expectedResult = quantity.getNumericalValue() + " kg";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testToStringLitre() {
        Quantity quantity = new Quantity(1, Unit.LITRE);
        String result = quantity.toString();
        String expectedResult = quantity.getNumericalValue() + " l";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
    @Test
    public void testToStringUndefined() {
        Quantity quantity = new Quantity(1, null);
        String result = quantity.toString();
        String expectedResult = quantity.getNumericalValue() + " ";
        assertEquals(result, expectedResult, 
                "Wrong quantity type unit string.");
    }
    
}

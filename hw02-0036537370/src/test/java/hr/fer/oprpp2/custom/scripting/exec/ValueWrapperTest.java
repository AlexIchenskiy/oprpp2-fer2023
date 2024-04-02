package hr.fer.oprpp2.custom.scripting.exec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValueWrapperTest {

    /* Unhappy tests for exception throwing */

    @Test
    public void testInvalidStringValue() {
        assertThrows(RuntimeException.class, () -> new ValueWrapper("Hello world!").add(0));
    }

    @Test
    public void testDivisionByZero() {
        ValueWrapper wrapper = new ValueWrapper(5.0);

        assertThrows(ArithmeticException.class, () -> wrapper.divide(0));
    }

    /* Happy tests for testing arithmetic methods (almost all the possible permutations) */

    @Test
    public void testAddNullAndNull() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.add(null);
        assertEquals(0, wrapper.getValue());
    }

    @Test
    public void testAddNullAndInteger() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.add(5);
        assertEquals(5, wrapper.getValue());
    }

    @Test
    public void testAddNullAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.add(3.5);
        assertEquals(3.5, wrapper.getValue());
    }

    @Test
    public void testAddNullAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.add("10");
        assertEquals(10, wrapper.getValue());
    }

    @Test
    public void testAddNullAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.add("7.5");
        assertEquals(7.5, wrapper.getValue());
    }

    @Test
    public void testAddIntegerAndInteger() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.add(10);
        assertEquals(15, wrapper.getValue());
    }

    @Test
    public void testAddIntegerAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.add(3.5);
        assertEquals(8.5, wrapper.getValue());
    }

    @Test
    public void testAddIntegerAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.add("10");
        assertEquals(15, wrapper.getValue());
    }

    @Test
    public void testAddIntegerAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.add("7.5");
        assertEquals(12.5, wrapper.getValue());
    }

    @Test
    public void testAddDoubleAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.add(2.5);
        assertEquals(6.0, wrapper.getValue());
    }

    @Test
    public void testAddDoubleAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.add("2");
        assertEquals(5.5, wrapper.getValue());
    }

    @Test
    public void testAddDoubleAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.add("2.5");
        assertEquals(6.0, wrapper.getValue());
    }

    @Test
    public void testMultiplyNullAndNull() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.multiply(null);
        assertEquals(0, wrapper.getValue());
    }

    @Test
    public void testMultiplyNullAndInteger() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.multiply(5);
        assertEquals(0, wrapper.getValue());
    }

    @Test
    public void testMultiplyNullAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.multiply(3.5);
        assertEquals(0.0, wrapper.getValue());
    }

    @Test
    public void testMultiplyNullAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.multiply("10");
        assertEquals(0, wrapper.getValue());
    }

    @Test
    public void testMultiplyNullAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.multiply("7.5");
        assertEquals(0.0, wrapper.getValue());
    }

    @Test
    public void testMultiplyIntegerAndInteger() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.multiply(10);
        assertEquals(50, wrapper.getValue());
    }

    @Test
    public void testMultiplyIntegerAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.multiply(3.5);
        assertEquals(17.5, wrapper.getValue());
    }

    @Test
    public void testMultiplyIntegerAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.multiply("10");
        assertEquals(50, wrapper.getValue());
    }

    @Test
    public void testMultiplyIntegerAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.multiply("7.5");
        assertEquals(37.5, wrapper.getValue());
    }

    @Test
    public void testMultiplyDoubleAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.multiply(2.5);
        assertEquals(8.75, wrapper.getValue());
    }

    @Test
    public void testMultiplyDoubleAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.multiply("2");
        assertEquals(7.0, wrapper.getValue());
    }

    @Test
    public void testMultiplyDoubleAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.multiply("2.5");
        assertEquals(8.75, wrapper.getValue());
    }

    @Test
    public void testSubtractNullAndNull() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.subtract(null);
        assertEquals(0, wrapper.getValue());
    }

    @Test
    public void testSubtractNullAndInteger() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.subtract(5);
        assertEquals(-5, wrapper.getValue());
    }

    @Test
    public void testSubtractNullAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.subtract(3.5);
        assertEquals(-3.5, wrapper.getValue());
    }

    @Test
    public void testSubtractNullAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.subtract("10");
        assertEquals(-10, wrapper.getValue());
    }

    @Test
    public void testSubtractNullAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.subtract("7.5");
        assertEquals(-7.5, wrapper.getValue());
    }

    @Test
    public void testSubtractIntegerAndInteger() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.subtract(10);
        assertEquals(-5, wrapper.getValue());
    }

    @Test
    public void testSubtractIntegerAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.subtract(3.5);
        assertEquals(1.5, wrapper.getValue());
    }

    @Test
    public void testSubtractIntegerAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.subtract("10");
        assertEquals(-5, wrapper.getValue());
    }

    @Test
    public void testSubtractIntegerAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(5);

        wrapper.subtract("7.5");
        assertEquals(-2.5, wrapper.getValue());
    }

    @Test
    public void testSubtractDoubleAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.subtract(2.5);
        assertEquals(1.0, wrapper.getValue());
    }

    @Test
    public void testSubtractDoubleAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.subtract("2");
        assertEquals(1.5, wrapper.getValue());
    }

    @Test
    public void testSubtractDoubleAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.subtract("2.5");
        assertEquals(1.0, wrapper.getValue());
    }

    @Test
    public void testDivideNullAndNull() {
        ValueWrapper wrapper = new ValueWrapper(null);

        assertThrows(ArithmeticException.class, () -> wrapper.divide(null));
    }

    @Test
    public void testDivideNullAndInteger() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.divide(5);
        assertEquals(0, wrapper.getValue());
    }

    @Test
    public void testDivideNullAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.divide(3.5);
        assertEquals(0.0, wrapper.getValue());
    }

    @Test
    public void testDivideNullAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.divide("10");
        assertEquals(0, wrapper.getValue());
    }

    @Test
    public void testDivideNullAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(null);

        wrapper.divide("7.5E1");
        assertEquals(0.0, wrapper.getValue());
    }

    @Test
    public void testDivideIntegerAndInteger() {
        ValueWrapper wrapper = new ValueWrapper(10);

        wrapper.divide(2);
        assertEquals(5, wrapper.getValue());
    }

    @Test
    public void testDivideIntegerAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(10);

        wrapper.divide(3.0);
        assertEquals(3.3333333333333335, (Double) wrapper.getValue(), 0.000001);
    }

    @Test
    public void testDivideIntegerAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(10);

        wrapper.divide("2");
        assertEquals(5, wrapper.getValue());
    }

    @Test
    public void testDivideIntegerAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(10);

        wrapper.divide("3.0E0");
        assertEquals(3.3333333333333335, (Double) wrapper.getValue(), 0.000001);
    }

    @Test
    public void testDivideDoubleAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.divide(2.5);
        assertEquals(1.4, wrapper.getValue());
    }

    @Test
    public void testDivideDoubleAndIntegerAsString() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.divide("2");
        assertEquals(1.75, wrapper.getValue());
    }

    @Test
    public void testDivideDoubleAndDoubleAsString() {
        ValueWrapper wrapper = new ValueWrapper(3.5);

        wrapper.divide("2.5E0");
        assertEquals(1.4, wrapper.getValue());
    }

}

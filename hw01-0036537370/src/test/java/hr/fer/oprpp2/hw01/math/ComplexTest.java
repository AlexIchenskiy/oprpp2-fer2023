package hr.fer.oprpp2.hw01.math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexTest {

    Complex complex;
    Complex complexWithoutReal;
    Complex complexWithoutImaginary;

    @BeforeEach
    public void beforeEach() {
        complex = new Complex(1, Math.sqrt(3));
        complexWithoutReal = new Complex(0, 1);
        complexWithoutImaginary = new Complex(9, 0);
    }

    @Test
    public void testPower() {
        assertEquals(new Complex(64, 0), complex.power(6));
    }

    @Test
    public void testPowerWithoutReal() {
        assertEquals(new Complex(-1, 0), complexWithoutReal.power(2));
    }

    @Test
    public void testPowerWithoutImaginary() {
        assertEquals(new Complex(81, 0), complexWithoutImaginary.power(2));
    }

    @Test
    public void testDivide() {
        complex = new Complex(2, 4);

        assertEquals(new Complex(4, -2), complex.divide(complexWithoutReal));
    }

    @Test
    public void testMultiply() {
        assertEquals(new Complex(-3, 36), new Complex(6, 3).multiply(new Complex(2, 5)));
    }

}

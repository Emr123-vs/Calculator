package com.lab3.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    private final Calculator cal = new Calculator();

    @Test
    void testAddNormal(){
        assertEquals(15.0, cal.add(10,5));
    }

    @Test
    void testSubtractNormal(){
        assertEquals(6.0, cal.subtract(8,2));
    }

    @Test
    void testMultiplyNegative(){
        assertEquals(-12.0, cal.multiply(-3,4));
    }

    @Test
    void testDivideNormal(){
        assertEquals(5.0, cal.divide(20,4));
    }

    @Test
    void testDivideZeroNumerator(){
        assertEquals(0.0, cal.divide(0,5));
    }

    @Test
    void testDivideByZeroException(){
        assertThrows(ArithmeticException.class, () -> cal.divide(10,0));
    }
}

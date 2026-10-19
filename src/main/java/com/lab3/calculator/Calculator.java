package com.lab3.calculator;

public class Calculator {
    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Error: Divisor cannot be zero!");
        }
        return a / b;
    }

    public static void main(String[] args) {
        Calculator cal = new Calculator();
        System.out.println("Maven Calculator Application");
        System.out.println("10 + 5 = " + cal.add(10,5));
        System.out.println("10 - 5 = " + cal.subtract(10,5));
        System.out.println("10 * 5 = " + cal.multiply(10,5));
        try{
            System.out.println("10 / 0 = " + cal.divide(10,0));
        }catch (ArithmeticException e){
            System.out.println(e.getMessage());
        }
    }
}

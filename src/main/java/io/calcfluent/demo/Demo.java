package io.calcfluent.demo;

import io.calcfluent.Calculator;
import java.math.BigDecimal;

/**
 * Demo, tells you how to use Calculator & this is tested case
 * If you want to test more cases , you can see test cases in test folder
 * @author Bryan
 * @since 2025.11
 */
public class Demo {

    public static void main(String[] args) {
        System.out.println("Calculator demo \n");

        testBasicAddition();
        testBasicSubtraction();
        testBasicMultiplication();
        testMixedLeftToRight();
        testParenthesesMultiplicationFirst();
        testDivisionInParentheses();
        testNestedParentheses();
        testRoundInParentheses();
        testNegateInParentheses();
        testAbsInParentheses();
        testComplexExpression();

        System.out.println("over");
    }

    /**
     * 1. Basic Addition
     */
    static void testBasicAddition() {
        printTest("Test 1: Basic Addition 10 + 5");
        BigDecimal result = Calculator.startWith(new BigDecimal("10"))
            .add(5)
            .result();
        assertEquals(new BigDecimal("15"), result, "10 + 5 should equal 15");
    }

    /**
     *  2. Basic Subtraction
     */
    static void testBasicSubtraction() {
        printTest("Test 2: Basic Subtraction 20 - 3");
        BigDecimal result = Calculator.startWith(new BigDecimal("20"))
            .subtract(3)
            .result();
        assertEquals(new BigDecimal("17"), result, "20 - 3 should equal 17");
    }

    /**
     * 3. Basic Multiplication
     */
    static void testBasicMultiplication() {
        printTest("Test 3: Basic Multiplication 6 * 7");
        BigDecimal result = Calculator.startWith(new BigDecimal("6"))
            .multiply(7)
            .result();
        assertEquals(new BigDecimal("42"), result, "6 * 7 should equal 42");
    }

    /**
     * 4. No parentheses: left to right (10 + 2) * 3 = 36
     */
    static void testMixedLeftToRight() {
        printTest("Test 4: Mixed operations (no parentheses) (10 + 2) * 3 = 36");
        BigDecimal result = Calculator.startWith(new BigDecimal("10"))
            .add(2)
            .multiply(3)
            .result();
        assertEquals(new BigDecimal("36"), result, "(10 + 2) * 3 should equal 36");
    }

    /** 5. With parentheses: 10 + (2 * 3) = 16
     */
    static void testParenthesesMultiplicationFirst() {
        printTest("Test 5: Parentheses priority 10 + (2 * 3) = 16");
        BigDecimal result = Calculator.startWith(new BigDecimal("10"))
            .addParenthesisThen(new BigDecimal("2"))
            .multiply(3)
            .rightParenthesis()
            .result();
        assertEquals(new BigDecimal("16"), result, "10 + (2 * 3) should equal 16");
    }

    /**
     * 6. Division with precision: 100 + (50 / 3 keep 2 decimals) = 116.67
     */
    static void testDivisionInParentheses() {
        printTest("Test 6: Division in parentheses 100 + (50 / 3, 2 decimal places) = 116.67");
        BigDecimal result = Calculator.startWith(new BigDecimal("100"))
            .addParenthesisThen(new BigDecimal("50"))
            .divide(3, 2)
            .rightParenthesis()
            .result();
        assertEquals(new BigDecimal("116.67"), result, "100 + (50/3) ≈ 116.67");
    }

    /**
     * 7. Nested parentheses: 10 + ((2 + 3) * 4) = 30
     */
    static void testNestedParentheses() {
        printTest("Test 7: Nested parentheses 10 + ((2 + 3) * 4) = 30");
        BigDecimal result = Calculator.startWith(new BigDecimal("10"))
            .addParenthesisThen()
            .leftParenthesisThen(new BigDecimal("2"))
            .add(3)
            .rightParenthesis()
            .multiply(4)
            .rightParenthesis()
            .result();
        assertEquals(new BigDecimal("30"), result, "10 + ((2+3)*4) should equal 30");
    }

    /**
     * 8. Rounding: 1 + round((2.333 + 1.666), 2) * 3 = 1 + 4.00 * 3 = 13
     */
    static void testRoundInParentheses() {
        printTest("Test 8: Rounding in parentheses 1 + round(2.333+1.666,2)*3 = 13");
        BigDecimal result = Calculator.startWith(new BigDecimal("1"))
            .addParenthesisThen()
            .roundParenthesisThen(new BigDecimal("2.333"), 2)
            .add(new BigDecimal("1.666"))
            .multiply(3)
            .rightParenthesis()
            .rightParenthesis()
            .result();
        assertEquals(new BigDecimal("13.00"), result, "Result should be 13.00");
    }

    /**
     * 9. Negation: -(5 + 3) = -8
     */
    static void testNegateInParentheses() {
        printTest("Test 9: Parentheses negation -(5 + 3) = -8");
        BigDecimal result = Calculator.startWith()
            .negateParenthesisThen(new BigDecimal("5"))
            .add(3)
            .rightParenthesis()
            .result();
        assertEquals(new BigDecimal("-8"), result, "-(5+3) should equal -8");
    }

    /**
     * 10. Absolute value: |(5 - 8)| = 3
     */
    static void testAbsInParentheses() {
        printTest("Test 10: Parentheses absolute value |(5 - 8)| = 3");
        BigDecimal result = Calculator.startWith()
            .absParenthesisThen(new BigDecimal("5"))
            .subtract(8)
            .rightParenthesis()
            .result();
        assertEquals(new BigDecimal("3"), result, "|5-8| should equal 3");
    }

    /**
     * 11. Complex expression: 10 + (2 * (15 / (3 + 2))) = 16
     */
    static void testComplexExpression() {
        printTest("Test 11: Complex expression 10 + (2 * (15 / (3 + 2))) = 16");
        BigDecimal result = Calculator.startWith(new BigDecimal("10"))
            .addParenthesisThen(new BigDecimal("2"))
            .multiplyParenthesisThen(new BigDecimal("15"))
            .divideParenthesisThen(new BigDecimal("3"), 2)
            .add(2)
            .rightParenthesis()
            .rightParenthesis()
            .rightParenthesis()
            .result();
        assertEquals(new BigDecimal("16"), result, "Complex expression should equal 16");
    }

    private static void printTest(String title) {
        System.out.println(title);
    }

    private static void assertEquals(BigDecimal expected, BigDecimal actual, String msg) {
        if (!expected.stripTrailingZeros().equals(actual.stripTrailingZeros())) {
            System.err.println("   Failed: " + msg);
            System.err.println("   Expected: " + expected);
            System.err.println("   Actual: " + actual);
        } else {
            System.out.println("   Passed: " + msg);
        }
    }
}

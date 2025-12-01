package io.calcfluent;

import java.math.BigDecimal;

public class CalculatorFullTest {

    public static void main(String[] args) {
        System.out.println(" Starting comprehensive test of all Calculator methods...\n");

        // 1. Basic arithmetic operation tests
        testBasicAdd();
        testBasicSubtract();
        testBasicMultiply();
        testBasicDivide();

        // 2. abs / negate / round individual tests
        testAbs();
        testNegate();
        testRound();

        // 3. Chained operation tests
        testChainedOperations();

        // 4. Parentheses expression tests
        testAddParenthesisThen();
        testSubtractParenthesisThen();
        testMultiplyParenthesisThen();
        testDivideParenthesisThen();
        testRoundParenthesisThen();
        testNegateParenthesisThen();
        testAbsParenthesisThen();

        // 5. Multi-level nested parentheses
        testNestedParentheses();

        // 6. Mixed function nesting
        testMixedFunctionNesting();

        // 7. High precision division tests
        testHighPrecisionDivision();

        // 8. Edge case tests
        testEdgeCases();

        System.out.println("\n   All tests passed! Calculator full functionality verification completed.");
    }

    // Utility method: Print test title
    private static void printTest(String title) {
        System.out.println(title);
    }

    // Utility method: Assert results
    private static void assertEquals(BigDecimal expected, BigDecimal actual, String msg) {
        if (!expected.stripTrailingZeros().equals(actual.stripTrailingZeros())) {
            System.err.println("   Failed: " + msg);
            System.err.println("   Expected: " + expected);
            System.err.println("   Actual: " + actual);
        } else {
            System.out.println("   Passed: " + msg);
        }
    }

    // 1. Basic addition test
    static void testBasicAdd() {
        printTest("Test 1: Basic Addition");
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        BigDecimal result = Calculator.add(a, b);
        assertEquals(new BigDecimal("15"), result, "10 + 5 = 15");

        // Multi-parameter addition
        BigDecimal c = new BigDecimal("3");
        BigDecimal d = new BigDecimal("2");

        BigDecimal result2 = Calculator.add(a, b, 10);
        assertEquals(new BigDecimal("25"), result2, "10 + 5 + 10 = 25");

        BigDecimal result3 = Calculator.add(a, b, c, d);
        assertEquals(new BigDecimal("20"), result3, "10 + 5 + 3 + 2 = 20");

        BigDecimal result5 = Calculator.add(a, b, c, d, 10L, 10000000);
        assertEquals(new BigDecimal((10 + 5 + 3 + 2 + 10 + 10000000)), result5, "10 + 5 + 3 + 2 + 10 + 10000000 = " + (10 + 5 + 3 + 2 + 10 + 10000000));
    }

    // 2. Basic subtraction test
    static void testBasicSubtract() {
        printTest("Test 2: Basic Subtraction");
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("3");
        BigDecimal result = Calculator.subtract(a, b);
        assertEquals(new BigDecimal("7"), result, "10 - 3 = 7");
    }

    // 3. Basic multiplication test
    static void testBasicMultiply() {
        printTest("Test 3: Basic Multiplication");
        BigDecimal a = new BigDecimal("6");
        BigDecimal b = new BigDecimal("7");
        BigDecimal result = Calculator.multiply(a, b);
        assertEquals(new BigDecimal("42"), result, "6 * 7 = 42");

        // Multi-parameter multiplication
        BigDecimal c = new BigDecimal("2");
        BigDecimal result2 = Calculator.multiply(a, b, c);
        assertEquals(new BigDecimal("84"), result2, "6 * 7 * 2 = 84");

        BigDecimal result3 = Calculator.multiply(a, b, c, 12, new BigDecimal(12));
        assertEquals(new BigDecimal((6 * 7 * 2 * 12 * 12)), result3, "6 * 7 * 2 * 12 * 12 = " + (6 * 7 * 2 * 12 * 12));
    }

    // 4. Basic division test
    static void testBasicDivide() {
        printTest("Test 4: Basic Division (keep 2 decimal places)");
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("3");
        BigDecimal result = Calculator.divide(a, b, 2);
        assertEquals(new BigDecimal("3.33"), result, "10 / 3 ≈ 3.33 (keep 2 decimals)");
    }

    // 5. abs test
    static void testAbs() {
        printTest("Test 5: Absolute value abs");
        BigDecimal a = new BigDecimal("-8.5");
        BigDecimal result = Calculator.abs(a);
        assertEquals(new BigDecimal("8.5"), result, "abs(-8.5) = 8.5");
    }

    // 6. negate test
    static void testNegate() {
        printTest("Test 6: Negation negate");
        BigDecimal a = new BigDecimal("12.3");
        BigDecimal result = Calculator.negate(a);
        assertEquals(new BigDecimal("-12.3"), result, "negate(12.3) = -12.3");
    }

    // 7. round test
    static void testRound() {
        printTest("Test 7: Rounding round");
        BigDecimal a = new BigDecimal("3.14159");
        BigDecimal result = Calculator.round(a, 2);
        assertEquals(new BigDecimal("3.14"), result, "round(3.14159, 2) = 3.14");
    }

    // 8. Chained operation test
    static void testChainedOperations() {
        printTest("Test 8: Chained operations: 5 + 3 * 2 - 1");
        BigDecimal result = Calculator.startWith(5)
                .addParenthesisThen(3)
                .multiply(2)
                .rightParenthesis()
                .subtract(1)
                .result();
        assertEquals(new BigDecimal(5 + 3 * 2 - 1), result, "5 + 3 * 2 - 1 = " + (5 + 3 * 2 - 1));
    }

    // 9. Addition parentheses test
    static void testAddParenthesisThen() {
        printTest("Test 9: addParenthesisThen: 10 + (5 + 3)");
        BigDecimal result = Calculator.startWith(new BigDecimal("10"))
                .addParenthesisThen(new BigDecimal("5"))
                .add(3)
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("18"), result, "10 + (5 + 3) = 18");
    }

    // 10. Subtraction parentheses test
    static void testSubtractParenthesisThen() {
        printTest("Test 10: subtractParenthesisThen: 20 - (10 - 3)");
        BigDecimal result = Calculator.startWith(new BigDecimal("20"))
                .subtractParenthesisThen(new BigDecimal("10"))
                .subtract(3)
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("13"), result, "20 - (10 - 3) = 13");
    }

    // 11. Multiplication parentheses test
    static void testMultiplyParenthesisThen() {
        printTest("Test 11: multiplyParenthesisThen: 2 * (3 + 4)");
        BigDecimal result = Calculator.startWith(new BigDecimal("2"))
                .multiplyParenthesisThen(new BigDecimal("3"))
                .add(4)
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("14"), result, "2 * (3 + 4) = 14");
    }

    // 12. Division parentheses test
    static void testDivideParenthesisThen() {
        printTest("Test 12: divideParenthesisThen: 100 / (5 + 5), keep 2 decimal places");
        BigDecimal result = Calculator.startWith(new BigDecimal("100"))
                .divideParenthesisThen(new BigDecimal("5"), 2)
                .add(5)
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("10.00"), result, "100 / (5+5) = 10.00");
    }

    // 13. Round parentheses test
    static void testRoundParenthesisThen() {
        printTest("Test 13: roundParenthesisThen: round((2.333 + 1.666), 2)");
        BigDecimal result = Calculator.startWith()
                .roundParenthesisThen(new BigDecimal("2.333"), 2)
                .add(new BigDecimal("1.666"))
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("4"), result, "round(3.999, 2) = 4.00");
    }

    // 14. Negate parentheses test
    static void testNegateParenthesisThen() {
        printTest("Test 14: negateParenthesisThen: negate((5 - 10))");
        BigDecimal result = Calculator.startWith()
                .negateParenthesisThen(5)
                .subtract(10)
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("5"), result, "negate(-5) = 5");
    }

    // 15. Abs parentheses test (with parameters)
    static void testAbsParenthesisThen() {
        printTest("Test 15: absParenthesisThen: abs(( -8 + 3 ))");
        BigDecimal result = Calculator.startWith()
                .absParenthesisThen(new BigDecimal("-8"))
                .add(3)
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("5"), result, "abs(-5) = 5");
    }

    // 16. Multi-level nested parentheses test
    static void testNestedParentheses() {
        printTest("Test 16: Multi-level nesting: 1 + (2 * (3 + (4 * (5 + 6))))");
        BigDecimal result = Calculator.startWith(new BigDecimal("1"))
                .addParenthesisThen(new BigDecimal("2"))
                .multiplyParenthesisThen(new BigDecimal("3"))
                .addParenthesisThen(new BigDecimal("4"))
                .multiplyParenthesisThen(new BigDecimal("5"))
                .add(6)
                .rightParenthesis()
                .rightParenthesis()
                .rightParenthesis()
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("95"), result, "1 + (2*(3+(4*(5+6)))) = 95");
    }

    // 17. Mixed function nesting test
    static void testMixedFunctionNesting() {
        printTest("Test 17: Mixed nesting: 10 + abs(negate(round((5.6 - 10.4), 1)))");

        BigDecimal result = Calculator.startWith(new BigDecimal("10"))
                .addParenthesisThen()
                .absParenthesisThen()
                .negateParenthesisThen()
                .roundParenthesisThen(new BigDecimal("5.6"), 1)
                .subtract(new BigDecimal("10.4"))
                .rightParenthesis()
                .rightParenthesis()
                .rightParenthesis()
                .rightParenthesis()
                .result();
        assertEquals(new BigDecimal("14.8"), result, "10 + abs(negate(round(5.6-10.4,1))) = 14.8");
    }

    // 18. High precision division nesting
    static void testHighPrecisionDivision() {
        printTest("Test 18: High precision division nesting: (50 + (100 / 3, 4 digits)) * 2");
        // 100/3 ≈ 33.3333
        // 50 + 33.3333 = 83.3333
        // *2 = 166.6666
        BigDecimal result = Calculator.startWith()
                .leftParenthesisThen(50)
                .addParenthesisThen(100)
                .divide(3, 4)
                .rightParenthesis()
                .rightParenthesis()
                .multiply(2)
                .result();
        assertEquals(new BigDecimal("166.6666"), result, "Result should be 166.6666");
    }

    // 19. Edge case tests
    static void testEdgeCases() {
        printTest("Test 19: Edge case tests");

        // Negative numbers
        BigDecimal result1 = Calculator.startWith(new BigDecimal("-10")).add(20).result();
        assertEquals(new BigDecimal("10"), result1, "-10 + 20 = 10");

        // Division by zero
        assertThrows(ArithmeticException.class, () -> {
            Calculator.divide(new BigDecimal("1"), new BigDecimal("0"), 2);
        }, "Division by zero should throw exception");

        // Missing right parenthesis
        assertThrows(IllegalArgumentException.class, () -> {
            Calculator.startWith(new BigDecimal("1"))
                    .addParenthesisThen(new BigDecimal("2"))
                    .add(3)
                    .result(); // Missing rightParenthesis()
        }, "Missing right parenthesis should throw exception");

        // Extra right parenthesis
        assertThrows(IllegalArgumentException.class, () -> {
            Calculator.startWith(new BigDecimal("1"))
                    .rightParenthesis();
        }, "Extra right parenthesis should throw exception");
    }

    // Helper method: Assert exceptions
    private static void assertThrows(Class<? extends Throwable> expected, Runnable runnable, String message) {
        try {
            runnable.run();
            System.err.println("   Failed: " + message + " - Expected exception was not thrown");
        } catch (Throwable t) {
            if (expected.isInstance(t)) {
                System.out.println("   Passed: " + message);
            } else {
                System.err.println("   Failed: " + message);
                System.err.println("   Expected exception: " + expected.getSimpleName());
                System.err.println("   Actual exception: " + t.getClass().getSimpleName());
            }
        }
    }

    private static void assertDoesNotThrow(Runnable runnable, String message) {
        try {
            runnable.run();
            System.out.println("   Passed: " + message);
        } catch (Throwable t) {
            System.err.println("   Failed: " + message + " - Threw exception: " + t.getClass().getSimpleName());
        }
    }
}

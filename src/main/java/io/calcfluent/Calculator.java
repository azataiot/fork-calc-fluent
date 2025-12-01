package io.calcfluent;

import io.calcfluent.decimalpool.BigDecimalPool;
import io.calcfluent.decimalpool.DefaultBigDecimalPool;
import io.calcfluent.decimalpool.NoneBigDecimalPool;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：计算器工具类，提供基本的数学运算功能，包括加减乘除、取反、绝对值、四舍五入等操作。
 * <p>
 * 该类使用 {@link BigDecimalPool} 来优化 BigDecimal 对象的创建和复用，提高性能。
 * 部分计算结果都会通过 {@link BigDecimalPool} 进行缓存和复用。
 * </p>
 * <p>
 * 支持链式调用的计算方式，通过 {@link CalculatorHolder} 类实现。
 * </p>
 * <p>
 * Description: Calculator tool class, provides basic mathematical operations such as addition,
 * subtraction, multiplication, division, negation, absolute value, rounding, etc.
 * <p>
 * This class uses {@link BigDecimalPool} to optimize BigDecimal object creation and reuse, improving performance.
 * Certified results will be cached and reused through {@link BigDecimalPool}.
 * Support chain call, through {@link CalculatorHolder}.
 * </p>
 *
 * @author Bryan
 * @since 2025.09
 */
@SuppressWarnings("unused")
public final class Calculator {

    /**
     * 日志记录器，用户可自定义，调用 {@link #setLogger(Logger)} 方法进行设置
     * logger， user can customize, call {@link #setLogger(Logger)} method to set it
     */
    private static Logger logger = LoggerFactory.getLogger(Calculator.class);

    /**
     * BigDecimal对象池，用于缓存和复用BigDecimal对象，默认使用DefaultBigDecimalPool
     * 用户可自行实现，并调用 {@link #setBigDecimalPool(BigDecimalPool)} 方法进行设置
     * <p>
     * BigDecimal object pool to cache and reuse BigDecimal objects, default is DefaultBigDecimalPool
     * User can implement it and call {@link #setBigDecimalPool(BigDecimalPool)} method to set it.
     */
    private static BigDecimalPool BIG_DECIMAL_POOL = DefaultBigDecimalPool.getInstance();

    /**
     * 设置日志记录器，用户可自定义，调用该方法进行设置
     * Set the logger, user can customize, call this method to set it.
     * @param logger 日志记录器
     */
    public static void setLogger(Logger logger) {
        Calculator.logger = logger;
    }

    /**
     * 开启链式调用debug模式
     * <p>
     * enable chain call debug mode
     */
    public static void enableCalcDebug() {
        CalculatorHolder.setDebug(true);
    }

    /**
     * 关闭链式调用debug模式
     * <p>
     * disable chain call debug mode
     */
    public static void disableCaloDebug() {
        CalculatorHolder.setDebug(false);
    }

    /**
     * 设置BigDecimal对象池，用户可自行实现，并调用该方法进行设置，如果入参为空，则不进行缓存，使用{@link NoneBigDecimalPool}
     * <p>
     * Set the BigDecimal object pool, user can implement it and call this method to set it.
     * If the parameter is null, it will not be cached, using {@link NoneBigDecimalPool}
     * @param bigDecimalPool BigDecimal对象池 - BigDecimal object pool
     */
    public static void setBigDecimalPool(BigDecimalPool bigDecimalPool) {
        if (bigDecimalPool == null) {
            bigDecimalPool = new NoneBigDecimalPool();
        }
        BIG_DECIMAL_POOL = bigDecimalPool;
    }

    /**
     * 计算两个BigDecimal数的和
     * <p>
     * Calculate the sum of two BigDecimal numbers
     *
     * @param a 第一个加数 - 1st number
     * @param b 第二个加数 - 2nd number
     * @return a + b
     */
    public static BigDecimal add(Number a, Number b) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        BigDecimal bigDecimalB = coverNumberToBigDecimal(b);
        return BIG_DECIMAL_POOL.get(bigDecimalA.add(bigDecimalB));
    }

    /**
     * 计算三个BigDecimal数的和
     * <p>
     * Calculate the sum of three BigDecimal numbers
     * @param a 第一个加数 - 1st number
     * @param b 第二个加数 - 2nd number
     * @param c 第三个加数 - 3rd number
     * @return a + b + c
     */
    public static BigDecimal add(Number a, Number b, Number c) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        BigDecimal bigDecimalB = coverNumberToBigDecimal(b);
        BigDecimal bigDecimalC = coverNumberToBigDecimal(c);

        return BIG_DECIMAL_POOL.get(bigDecimalA.add(bigDecimalB).add(bigDecimalC));
    }

    /**
     * 计算多个BigDecimal数的和
     * <p>
     * Calculate the sum of multiple BigDecimal numbers
     * @param a 第一个加数 - 1st number
     * @param b 第二个加数 - 2nd number
     * @param c 第三个加数 - 3rd number
     * @param others 其他加数数组 - other numbers
     * @return a + b + c + Σ(others)
     */
    public static BigDecimal add(Number a, Number b, Number c, Number... others) {
        if (others == null || others.length == 0) {
            return add(a, b, c);
        }

        BigDecimal d = add(a, b, c);
        for (Number other : others) {
            d = d.add(coverNumberToBigDecimal(other));
        }
        return BIG_DECIMAL_POOL.get(d);
    }

    /**
     * 计算两个BigDecimal数的差
     * <p>
     * Calculate the difference between two BigDecimal numbers
     * @param a 被减数 - minuend number
     * @param b 减数 - subtrahend number
     * @return a - b
     */
    public static BigDecimal subtract(Number a, Number b) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        BigDecimal bigDecimalB = coverNumberToBigDecimal(b);
        return BIG_DECIMAL_POOL.get(bigDecimalA.subtract(bigDecimalB));
    }

    /**
     * 计算两个BigDecimal数的积
     * <p>
     * Calculate the product of two BigDecimal numbers
     * @param a 第一个乘数 - 1st number
     * @param b 第二个乘数 - 2nd number
     * @return a × b
     */
    public static BigDecimal multiply(Number a, Number b) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        BigDecimal bigDecimalB = coverNumberToBigDecimal(b);
        return BIG_DECIMAL_POOL.get(bigDecimalA.multiply(bigDecimalB));
    }

    /**
     * 计算三个BigDecimal数的积
     * <p>
     * Calculate the product of three BigDecimal numbers
     * @param a 第一个乘数 - 1st number
     * @param b 第二个乘数 - 2nd number
     * @param c 第三个乘数 - 3rd number
     * @return a × b × c
     */
    public static BigDecimal multiply(Number a, Number b, Number c) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        BigDecimal bigDecimalB = coverNumberToBigDecimal(b);
        BigDecimal bigDecimalC = coverNumberToBigDecimal(c);
        return BIG_DECIMAL_POOL.get(bigDecimalA.multiply(bigDecimalB).multiply(bigDecimalC));
    }

    /**
     * 计算多个BigDecimal数的积
     * <p>
     * Calculate the product of multiple BigDecimal numbers
     * @param a 第一个乘数 - 1st number
     * @param b 第二个乘数 - 2nd number
     * @param c 第三个乘数 - 3rd number
     * @param others 其他乘数数组 - other numbers
     * @return a × b × c × Π(others)
     */
    public static BigDecimal multiply(Number a, Number b, Number c, Number... others) {
        BigDecimal d = coverNumberToBigDecimal(a).multiply(coverNumberToBigDecimal(b)).multiply(coverNumberToBigDecimal(c));
        for (Number other : others) {
            d = d.multiply(coverNumberToBigDecimal(other));
        }
        return BIG_DECIMAL_POOL.get(d);
    }

    /**
     * 计算两个BigDecimal数的商
     * <p>
     * Calculate the quotient of two BigDecimal numbers
     * @param a 被除数 - dividend
     * @param b 除数 - divisor
     * @param scale 结果精度（小数位数） - result scale
     * @return a ÷ b，四舍五入到指定精度 - a ÷ b, rounded to the specified scale
     */
    public static BigDecimal divide(Number a, Number b, int scale) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        BigDecimal bigDecimalB = coverNumberToBigDecimal(b);
        return BIG_DECIMAL_POOL.get(bigDecimalA.divide(bigDecimalB, scale, RoundingMode.HALF_UP));
    }

    /**
     * 对BigDecimal数取反
     * <p>
     * Negates a BigDecimal number
     * @param a 需要取反的数 - number to be negated
     * @return -a
     */
    public static BigDecimal negate(Number a) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        return BIG_DECIMAL_POOL.get(bigDecimalA.negate());
    }

    /**
     * 计算BigDecimal数的绝对值
     * <p>
     * Absolute value of a BigDecimal number
     * @param a 需要计算绝对值的数 - number to calculate absolute value
     * @return |a| (if a >= 0 return a  else return -a)
     */
    public static BigDecimal abs(Number a) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        return BIG_DECIMAL_POOL.get(bigDecimalA.abs());
    }

    /**
     * 对BigDecimal数进行四舍五入
     * <p>
     * round a BigDecimal number
     * @param a 需要四舍五入的数 - number to be rounded
     * @param scale 精度（小数位数） - scale
     * @return 四舍五入后的结果，保留指定小数位数 - rounded result, with specified scale
     */
    public static BigDecimal round(Number a, int scale) {
        BigDecimal bigDecimalA = coverNumberToBigDecimal(a);
        return BIG_DECIMAL_POOL.get(bigDecimalA.setScale(scale, RoundingMode.HALF_UP));
    }

    /**
     * 开始链式计算，使用指定初始值
     * <p>
     * begin a chain calculation, using the specified initial value
     * @param a 初始值 - initial value
     * @return CalculatorHolder实例，用于链式计算 - CalculatorHolder instance for chain calculation
     */
    public static CalculatorHolder startWith(Number a) {
        return new CalculatorHolder(coverNumberToBigDecimal(a));
    }

    /**
     * 开始链式计算，无初始值
     * <p>
     * begin a chain calculation, no initial value
     * @return CalculatorHolder实例，用于链式计算 - CalculatorHolder instance for chain calculation
     */
    public static CalculatorHolder startWith() {
        return new CalculatorHolder(null);
    }

    /**
     * 描述：计算器持有者，用于支持链式计算操作
     * <p>
     * 通过该类可以进行连续的数学运算，避免重复调用静态方法。
     * 每次运算都会更新内部的BigDecimal值，并返回自身实例以支持链式调用。
     * 最终通过 {@link #result()} 方法获取计算结果。
     * </p>
     * <p>
     * 该类尽可能使得调用贴合正常运算的体验，但是正常只能做到从左到右的运算顺序
     * <li>比如 a + b * c的运算顺序应该为 a + (b * c)
     * <li>如果编码为： Calculator.startWith(a).add(b).multiply(c)，则会得到 (a + b) * c 的结果
     * <li>应当编码为： Calculator.startWith(a).addParenthesisThen(b).multiply(c).rightParenthesis()
     *
     * <p><p>
     * Description: Calculator holder, used to support chain calculation operations
     * <p>
     * Using this class, you can perform continuous mathematical operations, avoiding repeated calls to static methods.
     * Each operation will update the internal BigDecimal value and return the instance itself to support chain calls.
     * Finally, the calculation result can be obtained through the {@link #result()} method.
     * <p>
     * The class tries to make the call stick to the normal operation experience, but normally can only do left-to-right
     * <li>For example, the order of operations should be a + b * c.</li>
     * <li>If you code as: Calculator.startWith(a).add(b).multiply(c), you will get (a + b) * c.</li>
     * <li>You should code as: Calculator.startWith(a).addParenthesisThen(b).multiply(c).rightParenthesis()</li>
     */
    public static class CalculatorHolder {

        /* begin expression define */
        private static final int PRECOMPUTED_EXPRESSION_NONE = 0;
        private static final int PRECOMPUTED_EXPRESSION_ADD = 1;
        private static final int PRECOMPUTED_EXPRESSION_SUBTRACT = 2;
        private static final int PRECOMPUTED_EXPRESSION_MULTIPLY = 3;
        private static final int PRECOMPUTED_EXPRESSION_DIVIDE = 4;
        private static final int PRECOMPUTED_EXPRESSION_ROUND = 5;
        private static final int PRECOMPUTED_EXPRESSION_NEGATE = 6;
        private static final int PRECOMPUTED_EXPRESSION_ABS = 7;
        /* end expression define */

        /**
         * 是否开启调试模式，开启后，会打印计算过程
         * <p>
         * Is debug mode enabled, when enabled, the calculation process will be printed
         */
        private static boolean isDebug = false;

        /**
         * 当前节点的计算结果 - current node's calculation result
         */
        private BigDecimal bigDecimal;

        /**
         * 当前计算公式 - current calculation formula
         */
        private StringBuilder debugFormula;

        /**
         * 预计算表达式 - precomputed expression
         */
        private int precomputedExpression = PRECOMPUTED_EXPRESSION_NONE;

        /**
         * 预计算表达式的另一个参数，用于除法等运算
         * <p>
         * precomputed expression's another parameter, used for division, etc.
         */
        private int precomputedExpressionOtherParam = -1;

        /**
         * 上一个节点
         */
        private CalculatorHolder lastNode;

        /**
         * 下一个节点
         */
        private CalculatorHolder nextNode;

        /**
         * 设置是否开启调试模式
         * <p>
         * set debug mode
         * @param debug 是否开启调试模式 - whether to enable debug mode
         */
        private static void setDebug(boolean debug) {
            isDebug = debug;
        }

        /**
         * 构造函数
         * <p>
         * constructor
         * @param bigDecimal 初始值 - initial value
         */
        private CalculatorHolder(BigDecimal bigDecimal) {
            this(bigDecimal, null);
        }

        /**
         * 构造函数
         * <p>
         * constructor
         * @param bigDecimal 节点的计算结果 - node's calculation result
         * @param debugFormula 节点的计算公式 - node's calculation formula
         */
        private CalculatorHolder(BigDecimal bigDecimal, StringBuilder debugFormula) {

            this.bigDecimal = bigDecimal;
            this.debugFormula = debugFormula;
            if (isDebug && this.debugFormula == null) {
                this.debugFormula = new StringBuilder();
            }
            if (isDebug && bigDecimal != null) {
                this.debugFormula.append(bigDecimal);
            }
        }

        /**
         * 加法运算（Number参数）
         * <p>
         * plus calculation
         * @param b 要加的数值 - number to add
         * @return 当前CalculatorHolder实例，支持链式调用 - current CalculatorHolder instance, supports chain calls
         */
        public CalculatorHolder add(Number b) {
            Objects.requireNonNull(bigDecimal, "can't call this method , current value is null");
            bigDecimal = bigDecimal.add(coverNumberToBigDecimal(b));

            if (isDebug) {
                debugFormula.append(" + ").append(b);
            }
            return this;
        }

        /**
         * 无前置运算左括号，用于表达式起始的括号运算
         * <p>
         * no previous operation left parenthesis, used for expression starting parenthesis operation
         * @param b 括号内的第一个数值 - first number inside the parenthesis
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder leftParenthesisThen(Number b) {
            if (isDebug) {
                debugFormula.append("(");
            }
            nextNode = new CalculatorHolder(coverNumberToBigDecimal(b), debugFormula);
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 无前置运算左括号，用于表达式起始的括号运算
         * <p>
         * no previous operation left parenthesis, used for expression starting parenthesis operation
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder leftParenthesisThen() {
            if (isDebug) {
                debugFormula.append(" (");
            }
            nextNode = new CalculatorHolder(null, debugFormula);
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 加法运算（Number参数）
         * 调用 .addParenthesisThen(b) 相当于 + (b
         * 后续可以继续执行其他运算，该运算必须执行rightParenthesis()关闭括号
         * <p>
         * plus  calculation
         * call .addParenthesisThen(b) equivalent to + (b
         * after that, the operation must execute rightParenthesis() to close the parenthesis
         *
         * @param b 括号内的第一个数值 - first number inside the parenthesis
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder addParenthesisThen(Number b) {
            if (isDebug) {
                debugFormula.append(" + (");
            }
            nextNode = new CalculatorHolder(coverNumberToBigDecimal(b), debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_ADD;
            nextNode.lastNode = this;
            return nextNode;
        }

        /**
         * 加法运算（Number参数）
         * 调用 .addParenthesisThen() 相当于 + (
         * 后续可以继续嵌套括号，并继续执行其他运算，该运算必须执行rightParenthesis()关闭括号
         * <p>
         * 调用该方法后不能再调用 加、减、乘、除等需要左边参数的方法
         * <p>
         * plus calculation
         * call .addParenthesisThen() equivalent to + (
         * after that, you can nest parentheses and continue to execute other operations, which must execute rightParenthesis() to close the parenthesis
         * <p>
         * call this method after that, you cannot call methods that need left parameters
         *
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder addParenthesisThen() {
            if (isDebug) {
                debugFormula.append(" + (");
            }

            nextNode = new CalculatorHolder(null, debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_ADD;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 减法运算（Number参数）
         * <p>
         * subtraction calculation
         * @param b 要减的数值 - number to subtract
         * @return 当前CalculatorHolder实例，支持链式调用 - current CalculatorHolder instance, supports chain calls
         */
        public CalculatorHolder subtract(Number b) {
            bigDecimal = bigDecimal.subtract(coverNumberToBigDecimal(b));
            if (isDebug) {
                debugFormula.append(" - ").append(b);
            }
            return this;
        }

        /**
         * @see #addParenthesisThen(Number)
         * @param b 括号内的第一个数值 - first number inside the parenthesis
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder subtractParenthesisThen(Number b) {
            if (isDebug) {
                debugFormula.append(" - (");
            }
            nextNode = new CalculatorHolder(coverNumberToBigDecimal(b), debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_SUBTRACT;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder subtractParenthesisThen() {
            if (isDebug) {
                debugFormula.append(" - (");
            }
            nextNode = new CalculatorHolder(null, debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_SUBTRACT;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 乘法运算（Number参数）
         * multiplication calculation
         * @param b 要乘的数值 - number to multiply
         * @return 当前CalculatorHolder实例，支持链式调用 - current CalculatorHolder instance, supports chain calls
         */
        public CalculatorHolder multiply(Number b) {
            bigDecimal = bigDecimal.multiply(coverNumberToBigDecimal(b));
            if (isDebug) {
                debugFormula.append(" * ").append(b);
            }
            return this;
        }

        /**
         * @see #addParenthesisThen(Number)
         * @param b 括号内的第一个数值 - first number inside the parenthesis
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder multiplyParenthesisThen(Number b) {
            if (isDebug) {
                debugFormula.append(" * (");
            }
            nextNode = new CalculatorHolder(coverNumberToBigDecimal(b), debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_MULTIPLY;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder multiplyParenthesisThen() {
            if (isDebug) {
                debugFormula.append(" * (");
            }
            nextNode = new CalculatorHolder(null, debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_MULTIPLY;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 除法运算（Number参数）
         * division calculation
         * @param b 除数 -  divisor
         * @param scale 结果精度（小数位数） - result precision (decimal digits)
         * @return 当前CalculatorHolder实例，支持链式调用 - current CalculatorHolder instance, supports chain calls
         */
        public CalculatorHolder divide(Number b, int scale) {
            bigDecimal = bigDecimal.divide(coverNumberToBigDecimal(b), scale, RoundingMode.HALF_UP);
            if (isDebug) {
                debugFormula.append(" / ").append(b).append("[round precision to ").append(scale).append("]");
            }
            return this;
        }

        /**
         * @see #addParenthesisThen(Number)
         * @param b 括号内的第一个数值 - first number inside the parenthesis
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder divideParenthesisThen(Number b, int scale) {
            if (isDebug) {
                debugFormula.append(" / (");
            }
            nextNode = new CalculatorHolder(coverNumberToBigDecimal(b), debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_DIVIDE;
            this.precomputedExpressionOtherParam = scale;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder divideParenthesisThen(int scale) {
            if (isDebug) {
                debugFormula.append(" / (");
            }
            nextNode = new CalculatorHolder(null, debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_DIVIDE;
            this.precomputedExpressionOtherParam = scale;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 四舍五入到指定精度
         * round to specified precision
         * @param scale 精度（小数位数） - precision (decimal digits)
         * @return 当前CalculatorHolder实例，支持链式调用 - current CalculatorHolder instance, supports chain calls
         */
        public CalculatorHolder round(int scale) {
            bigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP);
            if (isDebug) {
                String s = debugFormula.toString();
                debugFormula.setLength(0);
                debugFormula.append("round(").append(s).append(", ").append(scale).append(")");
            }
            return this;
        }

        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder roundParenthesisThen(Number b, int scale) {
            if (isDebug) {
                debugFormula.append("round(");
            }
            nextNode = new CalculatorHolder(coverNumberToBigDecimal(b), debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_ROUND;
            this.precomputedExpressionOtherParam = scale;
            nextNode.lastNode = this;

            return nextNode;
        }
        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder roundParenthesisThen(int scale) {
            if (isDebug) {
                debugFormula.append("round(");
            }
            nextNode = new CalculatorHolder(null, debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_ROUND;
            this.precomputedExpressionOtherParam = scale;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 取反运算
         * negate  calculation
         * @return 当前CalculatorHolder实例，支持链式调用 - current CalculatorHolder instance, supports chain calls
         */
        public CalculatorHolder negate() {
            bigDecimal = bigDecimal.negate();
            if (isDebug) {
                String s = debugFormula.toString();
                debugFormula.setLength(0);
                debugFormula.append("-(").append(s).append(")");
            }
            return this;
        }

        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder negateParenthesisThen(Number b) {
            if (isDebug) {
                debugFormula.append("-(");
            }
            nextNode = new CalculatorHolder(coverNumberToBigDecimal(b), debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_NEGATE;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder negateParenthesisThen() {
            if (isDebug) {
                debugFormula.append("-(");
            }
            nextNode = new CalculatorHolder(null, debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_NEGATE;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 绝对值运算
         * abs calculation
         * @return 当前CalculatorHolder实例，支持链式调用 - current CalculatorHolder instance, supports chain calls
         */
        public CalculatorHolder abs() {
            bigDecimal = bigDecimal.abs();
            if (isDebug) {
                String s = debugFormula.toString();
                debugFormula.setLength(0);
                debugFormula.append("abs(").append(s).append(")");
            }
            return this;
        }

        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder absParenthesisThen() {
            if (isDebug) {
                debugFormula.append("abs(");
            }
            nextNode = new CalculatorHolder(null, debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_ABS;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * @see #addParenthesisThen()
         * @return 下一个节点（括号内的节点）直到 rightParenthesis() 返回this - next node (node inside the parenthesis) until rightParenthesis() returns this
         */
        public CalculatorHolder absParenthesisThen(Number b) {
            if (isDebug) {
                debugFormula.append("abs(");
            }
            nextNode = new CalculatorHolder(coverNumberToBigDecimal(b), debugFormula);
            this.precomputedExpression = PRECOMPUTED_EXPRESSION_ABS;
            nextNode.lastNode = this;

            return nextNode;
        }

        /**
         * 括号结束
         * right parenthesis
         * @return 上一个节点 - last node
         */
        public CalculatorHolder rightParenthesis() {
            if (lastNode == null) {
                throw new IllegalArgumentException("Missing left parenthesis, formula parsing error");
            }
            switch (lastNode.precomputedExpression) {
                case PRECOMPUTED_EXPRESSION_ADD:
                    lastNode.bigDecimal = lastNode.bigDecimal.add(bigDecimal);
                    break;
                case PRECOMPUTED_EXPRESSION_SUBTRACT:
                    lastNode.bigDecimal = lastNode.bigDecimal.subtract(bigDecimal);
                    break;
                case PRECOMPUTED_EXPRESSION_MULTIPLY:
                    lastNode.bigDecimal = lastNode.bigDecimal.multiply(bigDecimal);
                    break;
                case PRECOMPUTED_EXPRESSION_DIVIDE:
                    if (lastNode.precomputedExpressionOtherParam == -1) {
                        throw new IllegalArgumentException("Precision loss with parsing errors in formulas");
                    }
                    lastNode.bigDecimal = lastNode.bigDecimal.divide(bigDecimal, lastNode.precomputedExpressionOtherParam, RoundingMode.HALF_UP);
                    break;
                case PRECOMPUTED_EXPRESSION_NEGATE:
                    lastNode.bigDecimal = bigDecimal.negate();
                    break;
                case PRECOMPUTED_EXPRESSION_ABS:
                    lastNode.bigDecimal = bigDecimal.abs();
                    break;
                case PRECOMPUTED_EXPRESSION_ROUND:
                    if (lastNode.precomputedExpressionOtherParam == -1) {
                        throw new IllegalArgumentException("Precision loss with parsing errors in formulas");
                    }
                    lastNode.bigDecimal = bigDecimal.setScale(lastNode.precomputedExpressionOtherParam, RoundingMode.HALF_UP);
                    break;
                case PRECOMPUTED_EXPRESSION_NONE:
                    lastNode.bigDecimal = bigDecimal;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + lastNode.precomputedExpression);
            }

            if (isDebug) {
                switch (lastNode.precomputedExpression) {
                    case PRECOMPUTED_EXPRESSION_DIVIDE:
                        debugFormula.append(")[round to ").append(lastNode.precomputedExpressionOtherParam).append("digits]");
                        break;
                    case PRECOMPUTED_EXPRESSION_ROUND:
                        debugFormula.append(", ").append(lastNode.precomputedExpressionOtherParam).append(")");
                        break;
                    default:
                        debugFormula.append(")");
                }
            }

            lastNode.nextNode = null;
            lastNode.precomputedExpression = PRECOMPUTED_EXPRESSION_NONE;
            lastNode.precomputedExpressionOtherParam = -1;
            return lastNode;
        }

        /**
         * 获取最终计算结果
         * get result
         * @return 最终计算结果，通过BigDecimalPool获取 - final result, get from BigDecimalPool
         */
        public BigDecimal result() {
            if (nextNode != null || lastNode != null) {
                throw new IllegalArgumentException("Missing right parenthesis, formula parsing error");
            }
            if (isDebug) {
                debugFormula.append(" = ").append(bigDecimal).append(" ; No parentheses → strict left-to-right evaluation");
                logger.debug(debugFormula.toString());
            }
            return BIG_DECIMAL_POOL.get(bigDecimal);
        }
    }

    /**
     * 将入参Number 转换为BigDecimal, 不支持浮点数
     * convert Number to BigDecimal, not support float
     * @param number Number 类型对象 - Number type object
     * @return BigDecimal - 入参的BigDecimal对象 - BigDecimal object of the number
     */
    private static BigDecimal coverNumberToBigDecimal(Number number) {
        Objects.requireNonNull(number, "number must not be null");
        if (number instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        } else if (number instanceof Double || number instanceof Float) {
            throw new IllegalArgumentException("系统不支持浮点数运算");
        } else {
            return BigDecimal.valueOf(number.longValue());
        }
    }
}
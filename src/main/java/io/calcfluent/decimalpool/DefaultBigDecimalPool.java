package io.calcfluent.decimalpool;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BigDecimal对象池的默认实现，{@link BigDecimalPool}
 * 该池会缓存 -10000 到 10000 的所有实际精度小于等于3的数值
 * <p>
 * 例如：
 * 100 会缓存
 * -9999 会缓存
 * 1000.12 会缓存
 * 100.123 会缓存
 * 100.1230 会缓存，实际精度是3
 * 100.1234 不会缓存
 * 10001 不会缓存
 * <p>
 * BigDecimal Object Pool default implementation, {@link BigDecimalPool}
 * This pool will cache all actual precision less than or equal to 3 between -10000 and 10000
 * egg:
 * 100 will be cached
 * -9999 will be cached
 * 1000.12 will be cached
 * 100.123 will be cached
 * 100.1230 will be cached, actual precision is 3
 * 100.1234 does not cache
 * 10001 does not cache
 *
 * @author Bryan
 * @since 2025.09
 */
public final class DefaultBigDecimalPool implements BigDecimalPool {

    /**
     * 被缓存的最大精度
     */
    private static final int CACHED_MAX_PRECISION = 3;


    /**
     * 单例模式 - 提供唯一实例
     * Singleton pattern - Provides a single instance.
     */
    private static final DefaultBigDecimalPool INSTANCE = new DefaultBigDecimalPool();

    /**
     * 常量缓存的最大值
     * Constant cache maximum value
     */
    private final static BigDecimal DECIMAL_10000 = BigDecimal.valueOf(10_000);

    /**
     * 常量缓存的最小值
     * Constant cache minimum value
     */
    private final static BigDecimal DECIMAL_NEGATIVE_10000 = BigDecimal.valueOf(-10_000);

    /**
     * 获取BigDecimal对象池的单例
     * 获取BigDecimalPool的唯一实例
     * Get the singleton instance of BigDecimalPool.
     *
     * @return BigDecimalPool对象池实例
     */
    public static DefaultBigDecimalPool getInstance() {
        return INSTANCE;
    }

    /**
     * 私有构造函数，用于创建单例实例
     * Private constructor for creating a singleton instance.
     */
    private DefaultBigDecimalPool() {
    }

    /**
     * BigDecimal对象池，用于缓存和复用BigDecimal实例
     * BigDecimal object pool for caching and reusing BigDecimal instances.
     */
    private final Map<BigDecimal, BigDecimal> pool = new ConcurrentHashMap<>(10_000);

    /**
     * 获取指定BigDecimal对象：
     * <li>如果对象池中已存在，则直接返回缓存池中的对象
     * <li>否则， 检查是否满足缓存条件，如果满足条件{@link DefaultBigDecimalPool}， 则放入对象池，并返回 stripTrailingZeros 的结果
     * <li>否则， 返回对象本身stripTrailingZeros 的结果， 不缓存
     * <p>
     * Get the specified BigDecimal object:
     * <li>If the object pool already exists, return the cached object in the object pool
     * <li>Otherwise, check whether the condition is met, if the condition is met{@link DefaultBigDecimalPool}, put it into the object pool and return it stripTrailingZeros result
     * <li>Otherwise, return the value itself stripTrailingZeros result, not cached
     *
     * @param value 传入的BigDecimal数值 - input BigDecimal value
     * @return 缓存的BigDecimal对象 -  cached BigDecimal object
     */
    @Override
    public BigDecimal get(BigDecimal value) {
        if (value == null) {
            return null;
        }

        // 去除末尾的零
        // Remove trailing zeros
        value = value.stripTrailingZeros();

        // 如果scale大于3，直接返回
        // If the scale is greater than 3, return the value directly
        if (value.scale() > CACHED_MAX_PRECISION) {
            return value;
        }

        // 比较值与常量10000的关系
        // Compare the value with the constant 10000
        int compareResult = DECIMAL_10000.compareTo(value);
        switch (compareResult) {
            case 0:
                return DECIMAL_10000;
            case -1:
                return value;
            default:
                break;
        }

        // 比较值与常量-10000的关系
        // Compare the value with the constant -10000
        compareResult = DECIMAL_NEGATIVE_10000.compareTo(value);

        return switch (compareResult) {
            case 0 -> DECIMAL_NEGATIVE_10000;
            case 1 -> value;
            default ->

                // 如果以上条件都不满足，则放入对象池
                // If none of the above conditions are met, put it into the pool
                pool.computeIfAbsent(value, k -> k);
        };

    }

    /**
     * 清空对象池
     * Clear the object pool
     */
    @Override
    public void clear() {
        pool.clear();
    }
}
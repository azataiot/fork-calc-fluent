package io.calcfluent.decimalpool;

import java.math.BigDecimal;

/**
 * BigDecimal 对象池，意在减少BigDecimal对象的内存占用
 * 在很多场景下，BigDecimal的重复非常严重，推荐对常用对象进行缓存
 * 我们提供了一个默认的实现 {@link DefaultBigDecimalPool} ，如果你不希望进行池化管理，则可以使用{@link NoneBigDecimalPool}的实现
 * 如果你希望自定义自己的对象池，则可以继承此接口，并实现自己的对象池逻辑
 * <p>
 * BigDecimal object pool, which is intended to reduce the memory usage of BigDecimal objects
 * In many scenarios, the repetition of BigDecimal is very severe. It is recommended to cache commonly used objects
 * We provide a default implementation  {@link DefaultBigDecimalPool},
 * if you do not want to manage objects, you can use the implementation of {@link NoneBigDecimalPool}
 * @author Bryan
 * @since 2025.09
 */
public interface BigDecimalPool {

    /**
     * 获取缓存的BigDecimal对象
     * 入参.compareTo(出参)==0， 但是 入参 == 出参 的判定不一定为真， 且入参.equals(出参)不一定为真
     * <P>
     * get cached BigDecimal object
     * The parameter.compareTo(the returned value) == 0,
     * but the parameter == the returned value may not be true,
     * and parameter.equals(the returned value) may not be true
     *
     * @param value 输入的BigDecimal数值 - input BigDecimal value
     * @return 缓存的BigDecimal对象 -  cached BigDecimal object
     */
    default BigDecimal get(BigDecimal value) {
        return value;
    }

    /**
     * 清空对象池
     * Clear the object pool
     */
    default void clear() {
    }
}

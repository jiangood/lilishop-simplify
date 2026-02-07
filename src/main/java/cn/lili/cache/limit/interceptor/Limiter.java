package cn.lili.cache.limit.interceptor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.Objects;

/**
 * 使用Guava Cache实现的限流器，支持动态限流参数
 */
public class Limiter {
    // 缓存存储限流计数器，键为限流键，值为限流计数器
    private final Cache<String, LimiterCounter> cache;

    /**
     * 构造方法
     * @param window 默认时间窗口（保留但不直接使用）
     * @param unit 默认时间单位（保留但不直接使用）
     */
    public Limiter(long window, TimeUnit unit) {
        // 使用相对较长的过期时间，实际过期由LimiterCounter控制
        this.cache = CacheBuilder.newBuilder()
                .expireAfterAccess(window * 2, unit) // 使用access时间
                .build();
    }



    /**
     * 尝试获取许可
     * @param key 限流键
     * @param limitCount 限制次数
     * @param limitPeriod 限制周期
     * @param timeUnit 时间单位
     * @return 是否获取到许可
     */
    public boolean tryAcquire(String key, int limitCount, long limitPeriod, TimeUnit timeUnit) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(timeUnit, "TimeUnit cannot be null");

        if (limitCount <= 0) {
            throw new IllegalArgumentException("limitCount must be greater than 0");
        }

        if (limitPeriod <= 0) {
            throw new IllegalArgumentException("limitPeriod must be greater than 0");
        }

        long currentTime = System.currentTimeMillis();
        long periodMillis = timeUnit.toMillis(limitPeriod);

        // 获取或创建计数器
        LimiterCounter counter = cache.getIfPresent(key);

        // 如果计数器不存在或已过期，创建新的计数器
        if (counter == null || counter.isExpired(currentTime, periodMillis)) {
            counter = new LimiterCounter(limitCount, periodMillis, currentTime);
            cache.put(key, counter);
            // 新计数器总是可以获取第一次许可
            return counter.tryAcquire();
        }

        // 计数器存在且未过期，尝试获取许可
        return counter.tryAcquire();
    }

    /**
     * 内部类：限流计数器
     */
    private static class LimiterCounter {
        private final AtomicInteger counter;
        private final int limitCount;
        private final long periodMillis;
        private final long startTime;

        public LimiterCounter(int limitCount, long periodMillis, long startTime) {
            this.limitCount = limitCount;
            this.periodMillis = periodMillis;
            this.startTime = startTime;
            this.counter = new AtomicInteger(0);
        }

        /**
         * 检查是否过期
         */
        public boolean isExpired(long currentTime, long newPeriodMillis) {
            // 使用当前时间与起始时间比较
            return (currentTime - startTime) >= periodMillis;
        }

        /**
         * 尝试获取许可
         */
        public boolean tryAcquire() {
            while (true) {
                int current = counter.get();
                if (current >= limitCount) {
                    return false;
                }
                if (counter.compareAndSet(current, current + 1)) {
                    return true;
                }
            }
        }

        /**
         * 获取当前计数
         */
        public int getCurrentCount() {
            return counter.get();
        }
    }

    /**
     * 清理缓存（可选）
     */
    public void cleanUp() {
        cache.cleanUp();
    }

    /**
     * 获取缓存大小（用于监控）
     */
    public long size() {
        return cache.size();
    }
}
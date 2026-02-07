package cn.lili.cache.impl;

import cn.lili.cache.Cache;
import cn.lili.cache.TypedTuple;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Google Guava Cache实现
 */
@Component
public class GoogleCacheImpl<T> implements Cache<T> {
    
    private final com.google.common.cache.Cache<String, T> cache;
    private final Map<String, ConcurrentSkipListMap<Double, Set<String>>> zSetStore;
    private final Map<String, Map<String, Double>> zSetScores;
    private final Map<String, AtomicLong> counters;
    
    public GoogleCacheImpl() {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats()
                .build();
        
        this.zSetStore = new ConcurrentHashMap<>();
        this.zSetScores = new ConcurrentHashMap<>();
        this.counters = new ConcurrentHashMap<>();
    }
    
    @Override
    public T get(Object key) {
        return cache.getIfPresent(key.toString());
    }
    
    @Override
    public String getString(Object key) {
        T value = get(key);
        return value != null ? value.toString() : null;
    }
    
    @Override
    public List<T> multiGet(Collection keys) {
        return (List<T>) keys.stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    @Override
    public void multiDel(Collection keys) {
        keys.forEach(key -> cache.invalidate(key.toString()));
    }
    
    @Override
    public void put(Object key, T value) {
        cache.put(key.toString(), value);
    }
    
    @Override
    public void put(Object key, T value, Long exp) {
        put(key, value, exp, TimeUnit.SECONDS);
    }
    
    @Override
    public void put(Object key, T value, Long exp, TimeUnit timeUnit) {
        // Google Cache不支持为单个entry设置过期时间
        // 可以使用统一的过期策略
        cache.put(key.toString(), value);
    }
    
    @Override
    public Boolean remove(Object key) {
        cache.invalidate(key.toString());
        return true;
    }
    
    @Override
    public void vagueDel(Object key) {
        String pattern = key.toString();
        // 模糊删除需要自己维护key集合或遍历
        cache.asMap().keySet().stream()
                .filter(k -> k.contains(pattern.replace("*", "")))
                .forEach(cache::invalidate);
    }
    
    @Override
    public boolean hasKey(Object key) {
        return cache.getIfPresent(key.toString()) != null;
    }
    
    @Override
    public List<Object> keys(String pattern) {
        return cache.asMap().keySet().stream()
                .filter(k -> k.contains(pattern.replace("*", "")))
                .collect(Collectors.toList());
    }
    
    //----------------------------------------------- ZSet实现 --------------------------------------------
    
    @Override
    public boolean zAdd(String key, long score, String value) {
        zSetScores.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
                .put(value, (double) score);
        
        zSetStore.computeIfAbsent(key, k -> new ConcurrentSkipListMap<>())
                .computeIfAbsent((double) score, s -> ConcurrentHashMap.newKeySet())
                .add(value);
        
        return true;
    }
    
    @Override
    public void incrementScore(String sortedSetName, String keyword) {
        incrementScore(sortedSetName, keyword, 1);
    }
    
    @Override
    public void incrementScore(String sortedSetName, String keyword, Integer score) {
        Double currentScore = zScore(sortedSetName, keyword);
        double newScore = (currentScore != null ? currentScore : 0) + score;
        
        // 移除旧的分数位置
        if (currentScore != null) {
            Set<String> values = zSetStore.get(sortedSetName).get(currentScore);
            if (values != null) {
                values.remove(keyword);
                if (values.isEmpty()) {
                    zSetStore.get(sortedSetName).remove(currentScore);
                }
            }
        }
        
        // 添加到新的分数位置
        zSetStore.computeIfAbsent(sortedSetName, k -> new ConcurrentSkipListMap<>())
                .computeIfAbsent(newScore, s -> ConcurrentHashMap.newKeySet())
                .add(keyword);
        
        zSetScores.computeIfAbsent(sortedSetName, k -> new ConcurrentHashMap<>())
                .put(keyword, newScore);
    }
    
    @Override
    public Set<TypedTuple<T>> reverseRangeWithScores(String sortedSetName, Integer count) {
        ConcurrentSkipListMap<Double, Set<String>> sortedSet = zSetStore.get(sortedSetName);
        if (sortedSet == null) {
            return Collections.emptySet();
        }

        Set<TypedTuple<T>> result = new LinkedHashSet<>();
        int collected = 0;
        
        // 反向遍历（分数从高到低）
        for (Map.Entry<Double, Set<String>> entry : sortedSet.descendingMap().entrySet()) {
            for (String value : entry.getValue()) {
                if (collected >= count) {
                    return result;
                }
                result.add(new TypedTuple<>((T) value, entry.getKey()));
                collected++;
            }
        }
        
        return result;
    }

    @Override
    public Set<TypedTuple<T>> zRangeByScore(String key, int from, long to) {
        ConcurrentSkipListMap<Double, Set<String>> sortedSet = zSetStore.get(key);
        if (sortedSet == null) {
            return Collections.emptySet();
        }
        
        Set<TypedTuple<T>> result = new LinkedHashSet<>();
        
        // 获取分数范围内的entry
        sortedSet.subMap((double) from, true, (double) to, true)
                .forEach((score, values) -> {
                    for (String value : values) {
                        result.add(new TypedTuple<>((T) value, score));
                    }
                });
        
        return result;
    }
    
    @Override
    public Long zRemove(String key, String... values) {
        Map<String, Double> scores = zSetScores.get(key);
        if (scores == null) {
            return 0L;
        }
        
        long removed = 0;
        for (String value : values) {
            Double score = scores.remove(value);
            if (score != null) {
                Set<String> valueSet = zSetStore.get(key).get(score);
                if (valueSet != null) {
                    valueSet.remove(value);
                    if (valueSet.isEmpty()) {
                        zSetStore.get(key).remove(score);
                    }
                }
                removed++;
            }
        }
        
        return removed;
    }
    
    private Double zScore(String key, String value) {
        Map<String, Double> scores = zSetScores.get(key);
        return scores != null ? scores.get(value) : null;
    }
    
    //----------------------------------------------- 计数器实现 --------------------------------------------
    
    @Override
    public Long cumulative(Object key, Object value) {
        String cacheKey = "cumulative:" + key.toString();
        Set<Object> set = (Set<Object>) cache.getIfPresent(cacheKey);
        if (set == null) {
            set = ConcurrentHashMap.newKeySet();
        }
        set.add(value);
        cache.put(cacheKey, (T) set);
        return (long) set.size();
    }
    
    @Override
    public Long counter(Object key) {
        String counterKey = "counter:" + key.toString();
        AtomicLong counter = counters.get(counterKey);
        return counter != null ? counter.get() : 0L;
    }
    
    @Override
    public Long incr(String key) {
        return incr(key, 0);
    }
    
    @Override
    public Long incr(String key, long liveTime) {
        String counterKey = "counter:" + key;
        AtomicLong counter = counters.computeIfAbsent(counterKey, k -> new AtomicLong(-1));
        long result = counter.incrementAndGet();
        
        // 设置过期（定时清理）
        if (liveTime > 0) {
            scheduleExpiration(counterKey, liveTime);
        }
        
        return result;
    }

    @Override
    public Long incrBy(String key, long increment) {
        String counterKey = "counter:" + key;
        AtomicLong counter = counters.computeIfAbsent(counterKey, k -> new AtomicLong(-1));
        long result = counter.addAndGet(increment);


        return result;
    }

    private void scheduleExpiration(String key, long delaySeconds) {
        // 使用定时器或调度器实现过期逻辑
        // 这里简化处理

    }
}
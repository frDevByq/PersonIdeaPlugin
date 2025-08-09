package com.github.frdevbyq.personideaplugin.showcomment.performance;

import com.github.frdevbyq.personideaplugin.showcomment.cache.LineEndCacheUtils;
import com.github.frdevbyq.personideaplugin.showcomment.cache.TreeCacheUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PerformanceMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(PerformanceMonitor.class);
    
    private static final ConcurrentHashMap<String, AtomicLong> operationCounts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicLong> operationTimes = new ConcurrentHashMap<>();
    
    public static void recordOperation(@NotNull String operationName, long executionTimeMs) {
        operationCounts.computeIfAbsent(operationName, k -> new AtomicLong(0)).incrementAndGet();
        operationTimes.computeIfAbsent(operationName, k -> new AtomicLong(0)).addAndGet(executionTimeMs);
    }
    
    public static void logPerformanceStats() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("=== PersonIdeaPlugin Performance Stats ===");
            LOG.debug("LineEnd Cache Size: {}", LineEndCacheUtils.getCacheSize());
            LOG.debug("Tree Cache Projects: {}", TreeCacheUtils.cache.size());
            
            for (String operation : operationCounts.keySet()) {
                long count = operationCounts.get(operation).get();
                long totalTime = operationTimes.get(operation).get();
                long avgTime = count > 0 ? totalTime / count : 0;
                
                LOG.debug("{}: {} operations, {} ms total, {} ms avg", 
                    operation, count, totalTime, avgTime);
            }
            LOG.debug("=== End Performance Stats ===");
        }
    }
    
    public static void clearStats() {
        operationCounts.clear();
        operationTimes.clear();
    }
    
    public static long measureExecution(@NotNull String operationName, @NotNull Runnable operation) {
        long startTime = System.currentTimeMillis();
        try {
            operation.run();
            return System.currentTimeMillis() - startTime;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            recordOperation(operationName, executionTime);
        }
    }
    
    public static class Timer implements AutoCloseable {
        private final String operationName;
        private final long startTime;
        
        public Timer(@NotNull String operationName) {
            this.operationName = operationName;
            this.startTime = System.currentTimeMillis();
        }
        
        @Override
        public void close() {
            long executionTime = System.currentTimeMillis() - startTime;
            recordOperation(operationName, executionTime);
        }
    }
}
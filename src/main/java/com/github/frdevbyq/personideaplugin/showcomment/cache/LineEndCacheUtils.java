package com.github.frdevbyq.personideaplugin.showcomment.cache;

import com.intellij.openapi.editor.LineExtensionInfo;
import com.github.frdevbyq.personideaplugin.showcomment.bean.LineInfo;
import com.github.frdevbyq.personideaplugin.showcomment.LineEnd;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LineEndCacheUtils {
    
    private static final ConcurrentHashMap<String, CacheEntry> CACHE = new ConcurrentHashMap<>();
    private static final int MAX_CACHE_SIZE = 1000;
    private static final long CACHE_EXPIRY_TIME = 5 * 60 * 1000; // 5 minutes
    private static final AtomicLong lastCleanup = new AtomicLong(System.currentTimeMillis());
    
    private static class CacheEntry {
        final LineExtensionInfo info;
        final long timestamp;
        
        CacheEntry(LineExtensionInfo info) {
            this.info = info;
            this.timestamp = System.currentTimeMillis();
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_EXPIRY_TIME;
        }
    }
    
    public static @Nullable Collection<LineExtensionInfo> get(@NotNull LineInfo info) {
        cleanupIfNeeded();
        
        String key = getCacheKey(info);
        CacheEntry entry = CACHE.get(key);
        
        if (entry != null && !entry.isExpired()) {
            return Collections.singleton(entry.info);
        }
        
        // Remove expired entry
        if (entry != null && entry.isExpired()) {
            CACHE.remove(key);
        }
        
        // Compute and cache
        LineExtensionInfo lineExt = LineEnd.lineExt(info);
        if (lineExt != null) {
            // Check cache size limit
            if (CACHE.size() >= MAX_CACHE_SIZE) {
                performCleanup();
            }
            CACHE.put(key, new CacheEntry(lineExt));
            return Collections.singleton(lineExt);
        }
        
        return null;
    }
    
    public static void clear() {
        CACHE.clear();
    }
    
    private static void cleanupIfNeeded() {
        long now = System.currentTimeMillis();
        long last = lastCleanup.get();
        
        // Cleanup every 2 minutes
        if (now - last > 2 * 60 * 1000 && lastCleanup.compareAndSet(last, now)) {
            performCleanup();
        }
    }
    
    private static void performCleanup() {
        CACHE.entrySet().removeIf(entry -> entry.getValue().isExpired());
        
        // If still too large, remove oldest entries
        if (CACHE.size() > MAX_CACHE_SIZE * 0.8) {
            CACHE.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e1.getValue().timestamp, e2.getValue().timestamp))
                    .limit(CACHE.size() - (int)(MAX_CACHE_SIZE * 0.7))
                    .forEach(entry -> CACHE.remove(entry.getKey()));
        }
    }
    
    private static String getCacheKey(@NotNull LineInfo info) {
        return info.file.getPath() + ":" + info.lineNumber + ":" + info.text.hashCode();
    }
    
    public static int getCacheSize() {
        return CACHE.size();
    }
}
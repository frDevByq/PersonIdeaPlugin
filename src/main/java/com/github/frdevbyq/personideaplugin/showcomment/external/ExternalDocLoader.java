package com.github.frdevbyq.personideaplugin.showcomment.external;

import com.github.frdevbyq.personideaplugin.showcomment.settings.ShowBundle;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExternalDocLoader {
    
    private static final Logger LOG = Logger.getInstance(ExternalDocLoader.class);
    
    private final Map<String, String> externalDocs = new ConcurrentHashMap<>();
    private final Map<String, Long> fileTimestamps = new ConcurrentHashMap<>();
    
    public void loadExternalDocs(@NotNull Project project, @NotNull String configPath) {
        try {
            VirtualFile configFile = VirtualFileManager.getInstance().findFileByUrl("file://" + configPath);
            if (configFile == null || !configFile.exists()) {
                LOG.info("External doc config file not found: " + configPath);
                return;
            }
            
            long currentTimestamp = configFile.getTimeStamp();
            Long lastTimestamp = fileTimestamps.get(configPath);
            
            if (lastTimestamp != null && lastTimestamp == currentTimestamp) {
                return; // File hasn't changed
            }
            
            loadFromFile(configFile);
            fileTimestamps.put(configPath, currentTimestamp);
            
        } catch (Exception e) {
            LOG.warn("Failed to load external docs from: " + configPath, e);
        }
    }
    
    private void loadFromFile(@NotNull VirtualFile configFile) throws IOException {
        try (InputStream inputStream = configFile.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    externalDocs.put(key, value);
                }
            }
        }
    }
    
    @Nullable
    public String getExternalDoc(@NotNull String key) {
        return externalDocs.get(key);
    }
    
    @Nullable
    public String findMatchingDoc(@NotNull String className, @NotNull String methodName) {
        // Try exact match first
        String exactKey = className + "#" + methodName;
        String doc = externalDocs.get(exactKey);
        if (doc != null) {
            return doc;
        }
        
        // Try class-level match
        doc = externalDocs.get(className);
        if (doc != null) {
            return doc;
        }
        
        // Try pattern matching
        for (Map.Entry<String, String> entry : externalDocs.entrySet()) {
            String key = entry.getKey();
            if (key.contains("*") || key.contains("?")) {
                if (matchesPattern(exactKey, key)) {
                    return entry.getValue();
                }
            }
        }
        
        return null;
    }
    
    private boolean matchesPattern(@NotNull String text, @NotNull String pattern) {
        String regex = pattern.replace("*", ".*").replace("?", ".");
        return text.matches(regex);
    }
    
    public void clearCache() {
        externalDocs.clear();
        fileTimestamps.clear();
    }
    
    public int getDocCount() {
        return externalDocs.size();
    }
    
    @NotNull
    public Set<String> getAllKeys() {
        return new HashSet<>(externalDocs.keySet());
    }
}
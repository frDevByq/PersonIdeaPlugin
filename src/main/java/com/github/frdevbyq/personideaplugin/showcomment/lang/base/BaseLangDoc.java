package com.github.frdevbyq.personideaplugin.showcomment.lang.base;

import com.github.frdevbyq.personideaplugin.showcomment.bean.LineInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseLangDoc {
    
    public static @Nullable String langDoc(@NotNull LineInfo info) {
        // Basic implementation - can be extended for specific languages
        // For now, return null to indicate no documentation available
        return null;
    }
    
    protected static @Nullable String extractJavaDoc(@NotNull String text) {
        // Extract JavaDoc comments from text
        if (text.contains("/**") && text.contains("*/")) {
            int start = text.indexOf("/**") + 3;
            int end = text.indexOf("*/");
            if (start < end) {
                return text.substring(start, end).trim()
                    .replaceAll("\\s*\\*\\s*", " ")
                    .trim();
            }
        }
        return null;
    }
}
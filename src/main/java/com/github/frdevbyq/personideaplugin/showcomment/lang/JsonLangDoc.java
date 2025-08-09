package com.github.frdevbyq.personideaplugin.showcomment.lang;

import com.intellij.openapi.editor.EditorLinePainter;
import com.intellij.openapi.editor.LineExtensionInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.github.frdevbyq.personideaplugin.showcomment.LineEnd;
import com.github.frdevbyq.personideaplugin.showcomment.bean.LineInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class JsonLangDoc extends EditorLinePainter {

    @Override
    public @Nullable Collection<LineExtensionInfo> getLineExtensions(@NotNull Project project,
                                                                     @NotNull VirtualFile file, int lineNumber) {
        try {
            if (!file.getName().endsWith(".json") && !file.getName().endsWith(".json5")) {
                return null;
            }
            
            LineInfo info = LineInfo.of(file, project, lineNumber);
            if (info == null) {
                return null;
            }
            
            String doc = extractJsonFieldDoc(info);
            if (doc != null) {
                LineExtensionInfo lineExt = LineEnd.lineExtText(info, " // " + doc);
                return Collections.singleton(lineExt);
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private @Nullable String extractJsonFieldDoc(@NotNull LineInfo info) {
        try {
            // Simple JSON field documentation - could be enhanced with TSV integration
            String text = info.text.trim();
            
            // Look for JSON key-value patterns
            if (text.contains(":") && text.contains("\"")) {
                int keyStart = text.indexOf("\"");
                int keyEnd = text.indexOf("\"", keyStart + 1);
                if (keyStart >= 0 && keyEnd > keyStart) {
                    String key = text.substring(keyStart + 1, keyEnd);
                    return "JSON field: " + key;
                }
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
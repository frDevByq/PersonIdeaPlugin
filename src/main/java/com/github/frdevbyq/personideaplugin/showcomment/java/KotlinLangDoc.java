package com.github.frdevbyq.personideaplugin.showcomment.java;

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

public class KotlinLangDoc extends EditorLinePainter {

    @Override
    public @Nullable Collection<LineExtensionInfo> getLineExtensions(@NotNull Project project,
                                                                     @NotNull VirtualFile file, int lineNumber) {
        try {
            if (!file.getName().endsWith(".kt")) {
                return null;
            }
            
            LineInfo info = LineInfo.of(file, project, lineNumber);
            if (info == null) {
                return null;
            }
            
            String doc = extractKotlinDoc(info);
            if (doc != null) {
                LineExtensionInfo lineExt = LineEnd.lineExtText(info, " // " + doc);
                return Collections.singleton(lineExt);
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private @Nullable String extractKotlinDoc(@NotNull LineInfo info) {
        try {
            // Basic KDoc extraction - look for /** */ comments
            String text = info.text;
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
        } catch (Exception e) {
            return null;
        }
    }
}
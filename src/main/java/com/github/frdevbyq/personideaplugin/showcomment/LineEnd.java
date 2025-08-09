package com.github.frdevbyq.personideaplugin.showcomment;

import com.intellij.json.JsonFileType;
import com.intellij.json.json5.Json5FileType;
import com.intellij.openapi.editor.EditorLinePainter;
import com.intellij.openapi.editor.LineExtensionInfo;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.github.frdevbyq.personideaplugin.showcomment.bean.FileInfo;
import com.github.frdevbyq.personideaplugin.showcomment.bean.FuncEnum;
import com.github.frdevbyq.personideaplugin.showcomment.bean.LineInfo;
import com.github.frdevbyq.personideaplugin.showcomment.bean.SettingsInfo;
import com.github.frdevbyq.personideaplugin.showcomment.cache.LineEndCacheUtils;
import com.github.frdevbyq.personideaplugin.showcomment.ext.LineExt;
import com.github.frdevbyq.personideaplugin.showcomment.external.ExternalDocService;
import com.github.frdevbyq.personideaplugin.showcomment.lang.base.BaseLangDoc;
import com.github.frdevbyq.personideaplugin.showcomment.line.LineSelect;
import com.github.frdevbyq.personideaplugin.showcomment.performance.PerformanceMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class LineEnd extends EditorLinePainter {

    private static final Logger LOG = LoggerFactory.getLogger(LineEnd.class);

    @Override
    public @Nullable Collection<LineExtensionInfo> getLineExtensions(@NotNull Project project,
                                                                     @NotNull VirtualFile file, int lineNumber) {
        try {
            return getLineExtensionInfos(project, file, lineNumber);
        } catch (Throwable e) {
            LOG.info("LineEnd catch Throwable but log to record.", e);
            return null;
        }
    }

    @Nullable
    private static Collection<LineExtensionInfo> getLineExtensionInfos(@NotNull Project project,
                                                                       @NotNull VirtualFile file, int lineNumber) {
        try (PerformanceMonitor.Timer timer = new PerformanceMonitor.Timer("getLineExtensions")) {
            @NotNull SettingsInfo settingsInfo = SettingsInfo.of(project, FuncEnum.LINE);
            if (!settingsInfo.appSettings.showLineEndComment) {
                return null;
            }
            if (settingsInfo.globalSettings.onlySelectLine && LineSelect.notSelectLine(project, lineNumber)) {
                return null;
            }
            @Nullable LineInfo info = LineInfo.of(file, project, lineNumber);
            if (info == null) {
                return null;
            }
            if (settingsInfo.appSettings.lineEndCache) {
                return LineEndCacheUtils.get(info);
            } else {
                @Nullable LineExtensionInfo lineExt = lineExt(info);
                if (lineExt == null) {
                    return null;
                }
                return Collections.singleton(lineExt);
            }
        }
    }

    @Nullable
    public static LineExtensionInfo lineExt(@NotNull LineInfo info) {
        @Nullable String doc = lineDocSkipHave(info);
        if (doc == null) {
            return null;
        }
        return lineExtText(info, info.appSettings.lineEndPrefix + doc);
    }

    @NotNull
    public static LineExtensionInfo lineExtText(@NotNull LineInfo info, @NotNull String text) {
        @NotNull TextAttributes textAttr = info.file.getFileType().equals(JsonFileType.INSTANCE)
                || info.file.getFileType().equals(Json5FileType.INSTANCE)
                ? info.appSettings.lineEndJsonTextAttr
                : info.appSettings.lineEndTextAttr;
        return new LineExtensionInfo(text, textAttr);
    }

    public static void textWithDoc(@NotNull FileInfo fileInfo, int startLine, int endLine,
                                   @NotNull ProgressIndicator indicator, @NotNull Consumer<String> func) {
        boolean needFraction = indicator.getFraction() != 0;
        int size = endLine - startLine;
        @NotNull StringBuilder sb = new StringBuilder();
        for (int i = startLine; i <= endLine; i++) {
            @Nullable LineInfo info = LineInfo.of(fileInfo, i);
            if (info == null) {
                sb.append("\n");
                continue;
            }
            sb.append(info.text);
            @Nullable String doc = lineDocSkipHave(info);
            if (doc != null) {
                sb.append(info.appSettings.lineEndPrefix).append(doc);
            }
            sb.append("\n");
            indicator.setText2(i + " / " + size + " line");
            if (needFraction) {
                indicator.setFraction(1.0 * i / size);
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        func.accept(sb.toString());
    }

    private static @Nullable String lineDocSkipHave(@NotNull LineInfo info) {
        @Nullable String doc = LineExt.doc(info);
        if (doc == null) {
            doc = BaseLangDoc.langDoc(info);
        }
        if (doc == null) {
            // Try external doc service
            ExternalDocService externalDocService = ExternalDocService.getInstance(info.project);
            if (externalDocService != null) {
                String fileName = info.file.getName();
                String lineText = info.text.trim();
                doc = externalDocService.getExternalDoc(fileName, String.valueOf(info.lineNumber));
            }
        }
        if (doc == null) {
            return null;
        }
        @NotNull String trimDoc = doc.trim();
        if (info.text.trim().endsWith(trimDoc)) {
            return null;
        }
        return trimDoc;
    }
}
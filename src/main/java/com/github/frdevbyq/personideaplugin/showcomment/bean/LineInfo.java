package com.github.frdevbyq.personideaplugin.showcomment.bean;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.github.frdevbyq.personideaplugin.showcomment.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LineInfo {
    @NotNull public final VirtualFile file;
    @NotNull public final Project project;
    @NotNull public final String text;
    public final int lineNumber;
    @NotNull public final AppSettingsState appSettings;

    public LineInfo(@NotNull VirtualFile file,
                   @NotNull Project project,
                   @NotNull String text,
                   int lineNumber,
                   @NotNull AppSettingsState appSettings) {
        this.file = file;
        this.project = project;
        this.text = text;
        this.lineNumber = lineNumber;
        this.appSettings = appSettings;
    }

    public static @Nullable LineInfo of(@NotNull VirtualFile file, 
                                       @NotNull Project project, 
                                       int lineNumber) {
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null || lineNumber >= document.getLineCount() || lineNumber < 0) {
            return null;
        }
        
        int startOffset = document.getLineStartOffset(lineNumber);
        int endOffset = document.getLineEndOffset(lineNumber);
        String text = document.getText().substring(startOffset, endOffset);
        
        AppSettingsState appSettings = AppSettingsState.getInstance();
        
        return new LineInfo(file, project, text, lineNumber, appSettings);
    }

    public static @Nullable LineInfo of(@NotNull FileInfo fileInfo, int lineNumber) {
        return of(fileInfo.file, fileInfo.project, lineNumber);
    }
}
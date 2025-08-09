package com.github.frdevbyq.personideaplugin.showcomment.line;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class LineSelect {
    
    public static boolean notSelectLine(@NotNull Project project, int lineNumber) {
        try {
            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            if (editor == null) {
                return false;
            }
            
            int currentLine = editor.getCaretModel().getLogicalPosition().line;
            return currentLine != lineNumber;
        } catch (Exception e) {
            return false;
        }
    }
}
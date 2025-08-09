package com.github.frdevbyq.personideaplugin.showcomment;

import com.intellij.ide.actions.CopyReferenceAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.github.frdevbyq.personideaplugin.showcomment.settings.ShowBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

public class CopyReferenceSimple extends CopyReferenceAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setText(ShowBundle.message("copy.class.method.or.file.line"));
    }

    private static final Pattern QUALIFIED_PATTERN = Pattern.compile("[\\w.]+\\.");

    @Nullable
    @Override
    protected String getQualifiedName(@NotNull Editor editor, List elements) {
        try {
            return simpleName(editor, elements);
        } catch (Exception e) {
            return e.toString();
        }
    }

    @Nullable
    private String simpleName(@NotNull Editor editor, List elements) {
        // because 2nd param is List<PsiElement> in 2020.1 and List<? extends PsiElement> in new version
        //noinspection unchecked
        String qualifiedName = super.getQualifiedName(editor, elements);
        if (qualifiedName == null) {
            @NotNull Document document = editor.getDocument();
            @Nullable Project project = editor.getProject();
            if (project == null) {
                return null;
            }
            @Nullable PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (file != null) {
                VirtualFile virtualFile = file.getVirtualFile();
                if (virtualFile != null) {
                    return virtualFile.getName() + ":" + (editor.getCaretModel().getLogicalPosition().line + 1);
                }
            }
        }
        return QUALIFIED_PATTERN.matcher(qualifiedName).replaceAll("");
    }
}
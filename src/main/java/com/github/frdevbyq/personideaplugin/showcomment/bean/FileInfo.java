package com.github.frdevbyq.personideaplugin.showcomment.bean;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileInfo {
    @NotNull public final VirtualFile file;
    @NotNull public final Document document;
    @NotNull public final Project project;
    @NotNull public final InjectedLanguageManager inject;
    @NotNull public final SettingsInfo settingsInfo;

    protected FileInfo(@NotNull VirtualFile file, @NotNull Document document, @NotNull Project project,
                       @NotNull FuncEnum funcEnum) {
        this.project = project;
        this.file = file;
        this.document = document;
        this.inject = InjectedLanguageManager.getInstance(project);
        this.settingsInfo = SettingsInfo.of(project, funcEnum);
    }

    public static @Nullable FileInfo of(@NotNull VirtualFile file, @NotNull Project project) {
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null) {
            return null;
        }
        return new FileInfo(file, document, project, FuncEnum.LINE);
    }

    public static @Nullable FileInfo of(@NotNull AnActionEvent event) {
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) {
            return null;
        }
        return of(psiFile);
    }

    public static @Nullable FileInfo of(@NotNull PsiFile psiFile) {
        FileViewProvider viewProvider = psiFile.getViewProvider();
        Document document = viewProvider.getDocument();
        if (document == null) {
            return null;
        }
        VirtualFile file = viewProvider.getVirtualFile();
        Project project = psiFile.getProject();
        return new FileInfo(file, document, project, FuncEnum.LINE);
    }
}
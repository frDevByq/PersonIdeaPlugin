package com.github.frdevbyq.personideaplugin.showcomment.java;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorLinePainter;
import com.intellij.openapi.editor.LineExtensionInfo;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.github.frdevbyq.personideaplugin.showcomment.LineEnd;
import com.github.frdevbyq.personideaplugin.showcomment.bean.LineInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class JavaLangDoc extends EditorLinePainter {

    @Override
    public @Nullable Collection<LineExtensionInfo> getLineExtensions(@NotNull Project project,
                                                                     @NotNull VirtualFile file, int lineNumber) {
        try {
            if (!file.getName().endsWith(".java")) {
                return null;
            }
            
            Document document = FileDocumentManager.getInstance().getDocument(file);
            if (document == null) {
                return null;
            }
            
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (!(psiFile instanceof PsiJavaFile)) {
                return null;
            }
            
            LineInfo info = LineInfo.of(file, project, lineNumber);
            if (info == null) {
                return null;
            }
            
            String doc = extractJavaDoc(info);
            if (doc != null) {
                LineExtensionInfo lineExt = LineEnd.lineExtText(info, " // " + doc);
                return Collections.singleton(lineExt);
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private @Nullable String extractJavaDoc(@NotNull LineInfo info) {
        try {
            Document document = FileDocumentManager.getInstance().getDocument(info.file);
            if (document == null) {
                return null;
            }
            
            PsiFile psiFile = PsiDocumentManager.getInstance(info.project).getPsiFile(document);
            if (psiFile == null) {
                return null;
            }
            
            // Look for JavaDoc comments in the line
            PsiElement elementAt = psiFile.findElementAt(psiFile.getTextOffset() + info.text.length() / 2);
            if (elementAt != null) {
                PsiDocComment docComment = PsiTreeUtil.getParentOfType(elementAt, PsiDocComment.class);
                if (docComment != null) {
                    return docComment.getText().replaceAll("/\\*\\*|\\*/|\\*", "").trim();
                }
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
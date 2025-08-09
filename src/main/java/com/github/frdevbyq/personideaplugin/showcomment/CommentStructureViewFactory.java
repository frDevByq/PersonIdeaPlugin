package com.github.frdevbyq.personideaplugin.showcomment;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.github.frdevbyq.personideaplugin.showcomment.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Structure view factory for Java files with comment display support
 */
public class CommentStructureViewFactory implements PsiStructureViewFactory {

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile psiFile) {
        // Only handle Java files when settings are enabled
        if (!(psiFile instanceof PsiJavaFile)) {
            return null;
        }
        
        AppSettingsState settings = AppSettingsState.getInstance();
        if (!settings.showTreeComment) {
            return null;
        }
        return new TreeBasedStructureViewBuilder() {
            @NotNull
            @Override
            public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
                return new CommentStructureViewModel(psiFile, editor);
            }
        };
    }
}
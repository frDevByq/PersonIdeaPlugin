package com.github.frdevbyq.personideaplugin.showcomment;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Structure view model that supports comment display
 */
public class CommentStructureViewModel extends TextEditorBasedStructureViewModel {
    
    private final PsiFile psiFile;
    private final Editor editor;

    public CommentStructureViewModel(@NotNull PsiFile psiFile, @Nullable Editor editor) {
        super(editor, psiFile);
        this.psiFile = psiFile;
        this.editor = editor;
    }

    @NotNull
    @Override
    public StructureViewTreeElement getRoot() {
        return new CommentStructureViewElement(psiFile);
    }

    @Override
    protected @Nullable Editor getEditor() {
        return editor;
    }
    
    @NotNull
    @Override
    public Grouper[] getGroupers() {
        return Grouper.EMPTY_ARRAY;
    }
    
    @NotNull
    @Override
    public Sorter[] getSorters() {
        return Sorter.EMPTY_ARRAY;
    }
}
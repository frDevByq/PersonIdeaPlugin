package com.github.frdevbyq.personideaplugin.showcomment;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.github.frdevbyq.personideaplugin.showcomment.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Structure view element that supports comment display
 */
public class CommentStructureViewElement implements StructureViewTreeElement {
    
    private static final Logger LOG = LoggerFactory.getLogger(CommentStructureViewElement.class);
    private final PsiElement element;

    public CommentStructureViewElement(@NotNull PsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        try {
            AppSettingsState settings = AppSettingsState.getInstance();
            
            if (!settings.showTreeComment) {
                return getDefaultPresentation();
            }
            
            String comment = extractComment();
            if (comment == null || comment.trim().isEmpty()) {
                return getDefaultPresentation();
            }
            return new CommentItemPresentation(getElementName(), comment, settings.showTreeIdentifier, element.getIcon(0));
            
        } catch (Exception e) {
            LOG.warn("Error creating presentation for element: " + element, e);
            e.printStackTrace();
            return getDefaultPresentation();
        }
    }
    
    @NotNull
    private ItemPresentation getDefaultPresentation() {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return getElementName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return null;
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return element.getIcon(0);
            }
        };
    }
    
    
    @NotNull
    private String getElementName() {
        if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();
            if (name != null) {
                return name;
            }
        }
        
        if (element instanceof PsiFile) {
            return ((PsiFile) element).getName();
        }
        
        return element.getText();
    }
    
    @Nullable
    private String extractComment() {
        try {
            PsiDocComment docComment = null;
            
            if (element instanceof PsiClass) {
                docComment = ((PsiClass) element).getDocComment();
            } else if (element instanceof PsiMethod) {
                docComment = ((PsiMethod) element).getDocComment();
            } else if (element instanceof PsiField) {
                docComment = ((PsiField) element).getDocComment();
            }
            
            if (docComment != null) {
                return extractDocCommentText(docComment);
            }
            
            return null;
        } catch (Exception e) {
            LOG.warn("Error extracting comment from PSI element", e);
            return null;
        }
    }
    
    @Nullable
    private String extractDocCommentText(@NotNull PsiDocComment docComment) {
        PsiElement[] descriptionElements = docComment.getDescriptionElements();
        
        if (descriptionElements.length == 0) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        for (PsiElement el : descriptionElements) {
            if (el instanceof PsiWhiteSpace) {
                continue;
            }
            String text = el.getText().trim();
            if (!text.isEmpty() && !text.equals("*")) {
                sb.append(text).append(" ");
            }
        }
        
        return sb.toString().trim();
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        try {
            List<TreeElement> children = new ArrayList<>();
            
            if (element instanceof PsiJavaFile) {
                PsiJavaFile javaFile = (PsiJavaFile) element;
                for (PsiClass psiClass : javaFile.getClasses()) {
                    children.add(new CommentStructureViewElement(psiClass));
                }
            } else if (element instanceof PsiClass) {
                PsiClass psiClass = (PsiClass) element;
                
                // Add fields
                for (PsiField field : psiClass.getFields()) {
                    children.add(new CommentStructureViewElement(field));
                }
                
                // Add methods
                for (PsiMethod method : psiClass.getMethods()) {
                    children.add(new CommentStructureViewElement(method));
                }
                
                // Add inner classes
                for (PsiClass innerClass : psiClass.getInnerClasses()) {
                    children.add(new CommentStructureViewElement(innerClass));
                }
            }
            
            return children.toArray(new TreeElement[0]);
            
        } catch (Exception e) {
            LOG.warn("Error getting children for element: " + element, e);
            return TreeElement.EMPTY_ARRAY;
        }
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (element instanceof NavigatablePsiElement) {
            ((NavigatablePsiElement) element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return element instanceof NavigatablePsiElement && ((NavigatablePsiElement) element).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element instanceof NavigatablePsiElement && ((NavigatablePsiElement) element).canNavigateToSource();
    }
}
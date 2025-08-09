package com.github.frdevbyq.personideaplugin.showcomment;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.ui.SimpleTextAttributes;
import com.github.frdevbyq.personideaplugin.showcomment.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.Icon;

/**
 * 结构视图装饰器，为Java元素添加注释显示
 */
public class StructureDecorator {
    
    private static final Logger LOG = LoggerFactory.getLogger(StructureDecorator.class);
    
    public static ItemPresentation decoratePresentation(@NotNull StructureViewTreeElement element, 
                                                       @NotNull ItemPresentation original) {
        try {
            // Check if settings are enabled
            AppSettingsState settings = AppSettingsState.getInstance();
            if (!settings.showTreeComment) {
                return original;
            }
            
            Object value = element.getValue();
            if (!(value instanceof PsiElement)) {
                return original;
            }
            
            PsiElement psiElement = (PsiElement) value;
            String comment = extractComment(psiElement);
            
            if (comment == null || comment.trim().isEmpty()) {
                return original;
            }
            
            return createDecoratedPresentation(original, psiElement, comment, settings.showTreeIdentifier);
            
        } catch (Exception e) {
            LOG.warn("Error decorating structure element presentation", e);
            return original;
        }
    }
    
    @Nullable
    private static String extractComment(@NotNull PsiElement element) {
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
    private static String extractDocCommentText(@NotNull PsiDocComment docComment) {
        PsiElement[] descriptionElements = docComment.getDescriptionElements();
        
        if (descriptionElements.length == 0) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        for (PsiElement element : descriptionElements) {
            if (element instanceof PsiWhiteSpace) {
                continue;
            }
            String text = element.getText().trim();
            if (!text.isEmpty() && !text.equals("*")) {
                sb.append(text).append(" ");
            }
        }
        
        return sb.toString().trim();
    }
    
    @NotNull
    private static ItemPresentation createDecoratedPresentation(@NotNull ItemPresentation original,
                                                               @NotNull PsiElement element,
                                                               @NotNull String comment,
                                                               boolean showIdentifier) {
        return new ColoredItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                String elementName = getElementName(element);
                if (showIdentifier) {
                    return elementName + " " + comment;
                } else {
                    return comment + " " + elementName;
                }
            }
            
            @Nullable
            @Override
            public String getLocationString() {
                return original.getLocationString();
            }
            
            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return original.getIcon(unused);
            }
            
            @Nullable
            @Override
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }
        };
    }
    
    @NotNull
    private static String getElementName(@NotNull PsiElement element) {
        if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();
            if (name != null) {
                return name;
            }
        }
        
        return element.getText();
    }
}
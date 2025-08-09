package com.github.frdevbyq.personideaplugin.showcomment;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.navigation.ItemPresentation;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Custom ItemPresentation that supports colored text fragments for structure view
 */
public class CommentItemPresentation extends PresentationData implements ItemPresentation {
    
    private final String elementName;
    private final String comment;
    private final boolean showIdentifier;
    private final Icon icon;
    
    public CommentItemPresentation(@NotNull String elementName, 
                                  @NotNull String comment, 
                                  boolean showIdentifier,
                                  @Nullable Icon icon) {
        this.elementName = elementName;
        this.comment = comment;
        this.showIdentifier = showIdentifier;
        this.icon = icon;
        
        setupPresentation();
    }
    
    private void setupPresentation() {
        clearText();
        
        if (showIdentifier) {
            // Format: methodName comment (comment in gray)
            addText(elementName, SimpleTextAttributes.REGULAR_ATTRIBUTES);
            addText(" " + comment, SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES);
        } else {
            // Format: comment methodName (methodName in gray)
            addText(comment, SimpleTextAttributes.REGULAR_ATTRIBUTES);
            addText(" " + elementName, SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES);
        }
        
        if (icon != null) {
            setIcon(icon);
        }
    }
    
    @Nullable
    @Override
    public String getPresentableText() {
        if (showIdentifier) {
            return elementName + " " + comment;
        } else {
            return comment + " " + elementName;
        }
    }
    
    @Nullable
    @Override
    public String getLocationString() {
        return null;
    }
    
    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return icon;
    }
}
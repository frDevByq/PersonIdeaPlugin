package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JBCheckBox myShowLineEndComment = new JBCheckBox("Show line end comments");
    private final JBCheckBox myShowTreeComment = new JBCheckBox("Show tree comments");
    private final JBCheckBox myShowTreeIdentifier = new JBCheckBox("Show tree identifiers (class/method names)");
    private final JBCheckBox myLineEndCache = new JBCheckBox("Enable line end cache");
    private final JBTextField myLineEndPrefix = new JBTextField();

    public AppSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Line end comment prefix: ", myLineEndPrefix, 1, false)
                .addComponent(myShowLineEndComment, 1)
                .addComponent(myShowTreeComment, 1)
                .addComponent(myShowTreeIdentifier, 1)
                .addComponent(myLineEndCache, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myShowLineEndComment;
    }

    @NotNull
    public String getLineEndPrefix() {
        return myLineEndPrefix.getText();
    }

    public void setLineEndPrefix(@NotNull String newText) {
        myLineEndPrefix.setText(newText);
    }

    public boolean getShowLineEndComment() {
        return myShowLineEndComment.isSelected();
    }

    public void setShowLineEndComment(boolean newStatus) {
        myShowLineEndComment.setSelected(newStatus);
    }

    public boolean getShowTreeComment() {
        return myShowTreeComment.isSelected();
    }

    public void setShowTreeComment(boolean newStatus) {
        myShowTreeComment.setSelected(newStatus);
    }

    public boolean getShowTreeIdentifier() {
        return myShowTreeIdentifier.isSelected();
    }

    public void setShowTreeIdentifier(boolean newStatus) {
        myShowTreeIdentifier.setSelected(newStatus);
    }

    public boolean getLineEndCache() {
        return myLineEndCache.isSelected();
    }

    public void setLineEndCache(boolean newStatus) {
        myLineEndCache.setSelected(newStatus);
    }
}
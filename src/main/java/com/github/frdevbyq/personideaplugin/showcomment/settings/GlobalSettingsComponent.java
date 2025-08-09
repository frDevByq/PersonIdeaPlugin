package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GlobalSettingsComponent {

    private final JPanel myMainPanel;
    private final JBCheckBox myOnlySelectLine = new JBCheckBox("Only show comments for selected line");
    private final JBCheckBox myEnableTreeComment = new JBCheckBox("Enable tree comment");

    public GlobalSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(myOnlySelectLine, 1)
                .addComponent(myEnableTreeComment, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myOnlySelectLine;
    }

    public boolean getOnlySelectLine() {
        return myOnlySelectLine.isSelected();
    }

    public void setOnlySelectLine(boolean newStatus) {
        myOnlySelectLine.setSelected(newStatus);
    }

    public boolean getEnableTreeComment() {
        return myEnableTreeComment.isSelected();
    }

    public void setEnableTreeComment(boolean newStatus) {
        myEnableTreeComment.setSelected(newStatus);
    }
}
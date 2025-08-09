package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ProjectSettingsComponent {

    private final JPanel myMainPanel;
    private final JBCheckBox myEnableProjectFeatures = new JBCheckBox("Enable project-specific features");
    private final JBTextField myProjectSpecificPrefix = new JBTextField();

    public ProjectSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(myEnableProjectFeatures, 1)
                .addLabeledComponent("Project-specific prefix: ", myProjectSpecificPrefix, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myEnableProjectFeatures;
    }

    public boolean getEnableProjectFeatures() {
        return myEnableProjectFeatures.isSelected();
    }

    public void setEnableProjectFeatures(boolean newStatus) {
        myEnableProjectFeatures.setSelected(newStatus);
    }

    @NotNull
    public String getProjectSpecificPrefix() {
        return myProjectSpecificPrefix.getText();
    }

    public void setProjectSpecificPrefix(@NotNull String newText) {
        myProjectSpecificPrefix.setText(newText);
    }
}
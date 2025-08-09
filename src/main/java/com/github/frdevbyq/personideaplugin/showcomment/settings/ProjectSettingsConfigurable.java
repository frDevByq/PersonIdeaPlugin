package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProjectSettingsConfigurable implements Configurable {

    private ProjectSettingsComponent mySettingsComponent;
    private final Project myProject;

    public ProjectSettingsConfigurable(Project project) {
        myProject = project;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "PersonIdeaPlugin: Show Comment Project";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new ProjectSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        @NotNull ProjectSettingsState settings = myProject.getService(ProjectSettingsState.class);
        boolean modified = false;
        modified |= mySettingsComponent.getEnableProjectFeatures() != settings.enableProjectFeatures;
        modified |= !mySettingsComponent.getProjectSpecificPrefix().equals(settings.projectSpecificPrefix);
        return modified;
    }

    @Override
    public void apply() {
        @NotNull ProjectSettingsState settings = myProject.getService(ProjectSettingsState.class);
        settings.enableProjectFeatures = mySettingsComponent.getEnableProjectFeatures();
        settings.projectSpecificPrefix = mySettingsComponent.getProjectSpecificPrefix();
    }

    @Override
    public void reset() {
        @NotNull ProjectSettingsState settings = myProject.getService(ProjectSettingsState.class);
        mySettingsComponent.setEnableProjectFeatures(settings.enableProjectFeatures);
        mySettingsComponent.setProjectSpecificPrefix(settings.projectSpecificPrefix);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
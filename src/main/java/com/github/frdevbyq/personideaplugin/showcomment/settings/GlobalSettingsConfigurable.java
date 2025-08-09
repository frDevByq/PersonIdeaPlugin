package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GlobalSettingsConfigurable implements Configurable {

    private GlobalSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "PersonIdeaPlugin: Show Comment Global";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new GlobalSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        @NotNull GlobalSettingsState settings = GlobalSettingsState.getInstance();
        boolean modified = false;
        modified |= mySettingsComponent.getOnlySelectLine() != settings.onlySelectLine;
        modified |= mySettingsComponent.getEnableTreeComment() != settings.enableTreeComment;
        return modified;
    }

    @Override
    public void apply() {
        @NotNull GlobalSettingsState settings = GlobalSettingsState.getInstance();
        settings.onlySelectLine = mySettingsComponent.getOnlySelectLine();
        settings.enableTreeComment = mySettingsComponent.getEnableTreeComment();
    }

    @Override
    public void reset() {
        @NotNull GlobalSettingsState settings = GlobalSettingsState.getInstance();
        mySettingsComponent.setOnlySelectLine(settings.onlySelectLine);
        mySettingsComponent.setEnableTreeComment(settings.enableTreeComment);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
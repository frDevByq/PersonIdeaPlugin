package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "PersonIdeaPlugin: Show Comment App";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        @NotNull AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = false;
        modified |= !mySettingsComponent.getLineEndPrefix().equals(settings.lineEndPrefix);
        modified |= mySettingsComponent.getShowLineEndComment() != settings.showLineEndComment;
        modified |= mySettingsComponent.getShowTreeComment() != settings.showTreeComment;
        modified |= mySettingsComponent.getShowTreeIdentifier() != settings.showTreeIdentifier;
        modified |= mySettingsComponent.getLineEndCache() != settings.lineEndCache;
        return modified;
    }

    @Override
    public void apply() {
        @NotNull AppSettingsState settings = AppSettingsState.getInstance();
        settings.lineEndPrefix = mySettingsComponent.getLineEndPrefix();
        settings.showLineEndComment = mySettingsComponent.getShowLineEndComment();
        settings.showTreeComment = mySettingsComponent.getShowTreeComment();
        settings.showTreeIdentifier = mySettingsComponent.getShowTreeIdentifier();
        settings.lineEndCache = mySettingsComponent.getLineEndCache();
    }

    @Override
    public void reset() {
        @NotNull AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setLineEndPrefix(settings.lineEndPrefix);
        mySettingsComponent.setShowLineEndComment(settings.showLineEndComment);
        mySettingsComponent.setShowTreeComment(settings.showTreeComment);
        mySettingsComponent.setShowTreeIdentifier(settings.showTreeIdentifier);
        mySettingsComponent.setLineEndCache(settings.lineEndCache);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
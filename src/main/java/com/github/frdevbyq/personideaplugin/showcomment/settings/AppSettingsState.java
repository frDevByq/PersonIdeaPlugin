package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@Service
@State(
    name = "com.github.frdevbyq.personideaplugin.showcomment.settings.AppSettingsState",
    storages = @Storage("PersonIdeaPluginShowComment.xml")
)
public final class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    // Line end comment settings
    public boolean showLineEndComment = true;
    public boolean lineEndCache = true;
    public String lineEndPrefix = " // ";
    public TextAttributes lineEndTextAttr = new TextAttributes(JBColor.GRAY, null, null, null, Font.ITALIC);
    public TextAttributes lineEndJsonTextAttr = new TextAttributes(JBColor.GREEN, null, null, null, Font.ITALIC);

    // Tree comment settings  
    public boolean showTreeComment = true;
    public boolean showTreeIdentifier = true;

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }

    @Nullable
    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        this.showLineEndComment = state.showLineEndComment;
        this.lineEndCache = state.lineEndCache;
        this.lineEndPrefix = state.lineEndPrefix;
        this.lineEndTextAttr = state.lineEndTextAttr;
        this.lineEndJsonTextAttr = state.lineEndJsonTextAttr;
        this.showTreeComment = state.showTreeComment;
        this.showTreeIdentifier = state.showTreeIdentifier;
    }
}
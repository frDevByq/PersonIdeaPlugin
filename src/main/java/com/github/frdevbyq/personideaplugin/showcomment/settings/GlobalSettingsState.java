package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service
@State(
    name = "com.github.frdevbyq.personideaplugin.showcomment.settings.GlobalSettingsState",
    storages = @Storage("PersonIdeaPluginShowCommentGlobal.xml")
)
public final class GlobalSettingsState implements PersistentStateComponent<GlobalSettingsState> {

    public boolean onlySelectLine = false;
    public boolean enableTreeComment = true;

    public static GlobalSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(GlobalSettingsState.class);
    }

    @Nullable
    @Override
    public GlobalSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GlobalSettingsState state) {
        this.onlySelectLine = state.onlySelectLine;
        this.enableTreeComment = state.enableTreeComment;
    }
}
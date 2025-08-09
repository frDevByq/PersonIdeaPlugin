package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service(Service.Level.PROJECT)
@State(
    name = "com.github.frdevbyq.personideaplugin.showcomment.settings.ProjectSettingsState",
    storages = @Storage("PersonIdeaPluginShowCommentProject.xml")
)
public final class ProjectSettingsState implements PersistentStateComponent<ProjectSettingsState> {

    public boolean enableProjectFeatures = true;
    public String projectSpecificPrefix = "";
    public String externalDocPath = "";

    @Nullable
    @Override
    public ProjectSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectSettingsState state) {
        this.enableProjectFeatures = state.enableProjectFeatures;
        this.projectSpecificPrefix = state.projectSpecificPrefix;
        this.externalDocPath = state.externalDocPath;
    }
    
    @NotNull
    public static ProjectSettingsState getInstance(@NotNull Project project) {
        return project.getService(ProjectSettingsState.class);
    }
}
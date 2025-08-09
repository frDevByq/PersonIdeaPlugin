package com.github.frdevbyq.personideaplugin.showcomment.bean;

import com.intellij.openapi.project.Project;
import com.github.frdevbyq.personideaplugin.showcomment.settings.AppSettingsState;
import com.github.frdevbyq.personideaplugin.showcomment.settings.GlobalSettingsState;
import com.github.frdevbyq.personideaplugin.showcomment.settings.ProjectSettingsState;
import org.jetbrains.annotations.NotNull;

public class SettingsInfo {
    public final AppSettingsState appSettings;
    public final GlobalSettingsState globalSettings;
    public final ProjectSettingsState projectSettings;

    public SettingsInfo(AppSettingsState appSettings, 
                       GlobalSettingsState globalSettings, 
                       ProjectSettingsState projectSettings) {
        this.appSettings = appSettings;
        this.globalSettings = globalSettings;
        this.projectSettings = projectSettings;
    }

    public static @NotNull SettingsInfo of(@NotNull Project project, @NotNull FuncEnum funcEnum) {
        AppSettingsState appSettings = AppSettingsState.getInstance();
        GlobalSettingsState globalSettings = GlobalSettingsState.getInstance();
        ProjectSettingsState projectSettings = project.getService(ProjectSettingsState.class);
        
        return new SettingsInfo(appSettings, globalSettings, projectSettings);
    }
}
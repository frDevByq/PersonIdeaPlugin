package com.github.frdevbyq.personideaplugin.showcomment.cache;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class CacheUpdateProjectListener implements ProjectManagerListener {

    @Override
    public void projectClosing(@NotNull Project project) {
        // Clear cache for closing project to free memory
        TreeCacheUtils.clearProject(project);
    }
}
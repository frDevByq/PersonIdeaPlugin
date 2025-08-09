package com.github.frdevbyq.personideaplugin.showcomment.actions;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.github.frdevbyq.personideaplugin.showcomment.cache.TreeCacheUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Refresh all tree comments cache
 */
public class RefreshTreeCommentsAction extends AnAction {

    public RefreshTreeCommentsAction() {
        super("Refresh Tree Comments", "Refresh all tree comments cache", null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            // Clear entire project cache
            TreeCacheUtils.clearProject(project);
            
            // 刷新项目视图
            ProjectView projectView = ProjectView.getInstance(project);
            if (projectView != null) {
                projectView.refresh();
            }
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
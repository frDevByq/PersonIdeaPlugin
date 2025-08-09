package com.github.frdevbyq.personideaplugin.showcomment.actions;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.github.frdevbyq.personideaplugin.showcomment.cache.TreeCacheUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Refresh current file comments cache
 */
public class RefreshCurrentFileCommentsAction extends AnAction {

    public RefreshCurrentFileCommentsAction() {
        super("Refresh Current File Comments", "Refresh current file comments cache", null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        
        if (project != null && virtualFile != null) {
            // Clear cache for current file
            clearCacheForFile(project, virtualFile);
            
            // 刷新项目视图
            ProjectView projectView = ProjectView.getInstance(project);
            if (projectView != null) {
                projectView.refresh();
            }
        }
    }

    private void clearCacheForFile(@NotNull Project project, @NotNull VirtualFile file) {
        // 获取当前文件的PSI
        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) {
            return;
        }
        
        // Clear all cache entries related to this file
        var nodeCache = TreeCacheUtils.cache.get(project);
        if (nodeCache != null) {
            nodeCache.entrySet().removeIf(entry -> {
                var node = entry.getKey();
                return isNodeRelatedToFile(node, file);
            });
        }
    }
    
    private boolean isNodeRelatedToFile(@NotNull com.intellij.ide.projectView.ProjectViewNode<?> node, 
                                       @NotNull VirtualFile file) {
        try {
            // Check if node is a PsiFileNode for this file
            if (node instanceof com.intellij.ide.projectView.impl.nodes.PsiFileNode) {
                com.intellij.psi.PsiFile psiFile = ((com.intellij.ide.projectView.impl.nodes.PsiFileNode) node).getValue();
                return psiFile != null && file.equals(psiFile.getVirtualFile());
            }
            
            // Check if node's PSI element is in this file
            Object value = node.getValue();
            if (value instanceof com.intellij.psi.PsiElement) {
                com.intellij.psi.PsiFile containingFile = ((com.intellij.psi.PsiElement) value).getContainingFile();
                return containingFile != null && file.equals(containingFile.getVirtualFile());
            }
            
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        
        // Only show this action on Java files
        boolean enabled = project != null && virtualFile != null && 
                         virtualFile.getName().endsWith(".java");
        
        e.getPresentation().setEnabledAndVisible(enabled);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
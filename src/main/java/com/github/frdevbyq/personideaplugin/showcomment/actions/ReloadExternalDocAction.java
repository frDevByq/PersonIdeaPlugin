package com.github.frdevbyq.personideaplugin.showcomment.actions;

import com.github.frdevbyq.personideaplugin.showcomment.external.ExternalDocService;
import com.github.frdevbyq.personideaplugin.showcomment.settings.ShowBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ReloadExternalDocAction extends AnAction {
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setText(ShowBundle.message("reload.ext.doc"));
        Project project = e.getProject();
        e.getPresentation().setVisible(project != null);
    }
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            ExternalDocService service = ExternalDocService.getInstance(project);
            service.reloadExternalDocs();
        }
    }
}
package com.github.frdevbyq.personideaplugin.showcomment.external;

import com.github.frdevbyq.personideaplugin.showcomment.settings.ProjectSettingsState;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

@Service(Service.Level.PROJECT)
public final class ExternalDocService {
    
    private final Project project;
    private final ExternalDocLoader docLoader;
    private final ConcurrentHashMap<String, ExternalDocLoader> loaderCache;
    
    public ExternalDocService(@NotNull Project project) {
        this.project = project;
        this.docLoader = new ExternalDocLoader();
        this.loaderCache = new ConcurrentHashMap<>();
    }
    
    @NotNull
    public static ExternalDocService getInstance(@NotNull Project project) {
        return project.getService(ExternalDocService.class);
    }
    
    @Nullable
    public String getExternalDoc(@NotNull String className, @NotNull String methodName) {
        ProjectSettingsState settings = ProjectSettingsState.getInstance(project);
        if (settings == null || settings.externalDocPath == null || settings.externalDocPath.trim().isEmpty()) {
            return null;
        }
        
        String configPath = settings.externalDocPath.trim();
        loadExternalDocsIfNeeded(configPath);
        
        return docLoader.findMatchingDoc(className, methodName);
    }
    
    public void reloadExternalDocs() {
        ProjectSettingsState settings = ProjectSettingsState.getInstance(project);
        if (settings != null && settings.externalDocPath != null && !settings.externalDocPath.trim().isEmpty()) {
            docLoader.clearCache();
            docLoader.loadExternalDocs(project, settings.externalDocPath.trim());
        }
    }
    
    public void clearExternalDocs() {
        docLoader.clearCache();
        loaderCache.clear();
    }
    
    private void loadExternalDocsIfNeeded(@NotNull String configPath) {
        docLoader.loadExternalDocs(project, configPath);
    }
    
    public int getExternalDocCount() {
        return docLoader.getDocCount();
    }
    
    public boolean hasExternalDocs() {
        return docLoader.getDocCount() > 0;
    }
}
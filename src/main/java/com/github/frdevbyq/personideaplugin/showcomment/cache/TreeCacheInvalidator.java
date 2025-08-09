package com.github.frdevbyq.personideaplugin.showcomment.cache;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listen for file changes, clear related cache after debounce
 */
public class TreeCacheInvalidator implements FileDocumentManagerListener {
    
    private static final Logger LOG = LoggerFactory.getLogger(TreeCacheInvalidator.class);
    private static final int DEBOUNCE_DELAY_MS = 2000; // 2s debounce
    
    private final Map<VirtualFile, Alarm> pendingInvalidations = new ConcurrentHashMap<>();
    
    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        if (file != null && file.getName().endsWith(".java")) {
            LOG.info("Document saving for Java file: {}", file.getName());
            scheduleInvalidation(file, document);
        }
    }
    
    private void scheduleInvalidation(@NotNull VirtualFile file, @NotNull Document document) {
        // Cancel existing alarm for this file
        Alarm existingAlarm = pendingInvalidations.get(file);
        if (existingAlarm != null) {
            existingAlarm.cancelAllRequests();
        }
        
        // Create new alarm with debounce delay
        Alarm alarm = new Alarm();
        pendingInvalidations.put(file, alarm);
        
        alarm.addRequest(() -> {
            try {
                LOG.info("Executing cache invalidation for file: {}", file.getName());
                invalidateCacheForFile(file, document);
            } finally {
                pendingInvalidations.remove(file);
            }
        }, DEBOUNCE_DELAY_MS);
        
        LOG.info("Scheduled cache invalidation for file: {} (debounced for {}ms)", file.getName(), DEBOUNCE_DELAY_MS);
    }
    
    private void invalidateCacheForFile(@NotNull VirtualFile file, @NotNull Document document) {
        try {
            // Find all projects that might have this file
            for (Map.Entry<Project, Map<com.intellij.ide.projectView.ProjectViewNode<?>, TreeCache>> projectEntry : 
                 TreeCacheUtils.cache.entrySet()) {
                
                Project project = projectEntry.getKey();
                if (project.isDisposed()) {
                    continue;
                }
                
                // Get PsiFile for this document in this project
                PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
                if (psiFile != null && file.equals(psiFile.getVirtualFile())) {
                    LOG.info("Invalidating cache for file {} in project {}", file.getName(), project.getName());
                    invalidateProjectCache(project, file);
                }
            }
        } catch (Exception e) {
            LOG.warn("Error during cache invalidation for file: " + file.getName(), e);
        }
    }
    
    private void invalidateProjectCache(@NotNull Project project, @NotNull VirtualFile file) {
        Map<com.intellij.ide.projectView.ProjectViewNode<?>, TreeCache> nodeCache = 
            TreeCacheUtils.cache.get(project);
        
        if (nodeCache != null) {
            // Clear cache for nodes related to this file
            nodeCache.entrySet().removeIf(entry -> {
                com.intellij.ide.projectView.ProjectViewNode<?> node = entry.getKey();
                if (isNodeRelatedToFile(node, file)) {
                    LOG.info("Cleared cache for node: {} in file {}", node.getName(), file.getName());
                    return true;
                }
                return false;
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
        } catch (Exception e) {
            LOG.warn("Error checking if node is related to file: " + e.getMessage());
            return false;
        }
    }
}
package com.github.frdevbyq.personideaplugin.showcomment.cache;

import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TreeCacheUtils {

    private TreeCacheUtils() {}

    private static final Logger LOG = LoggerFactory.getLogger(TreeCacheUtils.class);

    public static final Map<Project, Map<ProjectViewNode<?>, TreeCache>> cache = new ConcurrentHashMap<>();

    @Nullable
    public static String treeDoc(@NotNull ProjectViewNode<?> node, @NotNull Project project) {
        try {
            LOG.info("TreeCacheUtils.treeDoc called for node: {} (class: {})", node.getName(), node.getClass().getSimpleName());
            @NotNull TreeCache treeCache = cache
                    .computeIfAbsent(project, a -> new ConcurrentHashMap<>())
                    .computeIfAbsent(node, a -> new TreeCache());
            
            // Simple direct extraction - cache will be invalidated by TreeCacheInvalidator
            if (treeCache.doc == null) {
                String doc = extractDocumentationDirectly(node, project);
                treeCache.doc = doc;
                LOG.info("Extracted doc for node {}: '{}'", node.getName(), doc);
            }
            
            return treeCache.doc;
        } catch (ProcessCanceledException e) {
            LOG.info("ProcessCanceledException in treeDoc for node: {}", node.getName());
            return null;
        } catch (Throwable e) {
            LOG.info("TreeCacheUtils catch Throwable but log to record.", e);
            return null;
        }
    }
    
    @Nullable
    private static String extractDocumentationDirectly(@NotNull ProjectViewNode<?> node, @NotNull Project project) {
        try {
            LOG.info("Extract doc for node: {} (type: {})", node.getName(), node.getClass().getSimpleName());
            
            // Handle PsiFileNode (Java files)
            if (node instanceof PsiFileNode) {
                PsiFile psiFile = ((PsiFileNode) node).getValue();
                LOG.info("PsiFileNode file: {}", psiFile != null ? psiFile.getName() : "null");
                if (psiFile != null && psiFile.getName().endsWith(".java")) {
                    return extractFileDocumentation(psiFile);
                }
            }
            
            // Handle any node by getting its PSI value
            Object value = node.getValue();
            LOG.info("Node value: {} (type: {})", value, value != null ? value.getClass().getSimpleName() : "null");
            
            if (value instanceof PsiClass) {
                PsiClass psiClass = (PsiClass) value;
                LOG.info("Processing PsiClass: {}", psiClass.getName());
                PsiDocComment docComment = psiClass.getDocComment();
                if (docComment != null) {
                    String doc = extractDocCommentText(docComment);
                    LOG.info("Found class doc: '{}'", doc);
                    return doc;
                } else {
                    LOG.info("No docComment found for class: {}", psiClass.getName());
                }
            } else if (value instanceof PsiMethod) {
                PsiMethod psiMethod = (PsiMethod) value;
                LOG.info("Processing PsiMethod: {}", psiMethod.getName());
                PsiDocComment docComment = psiMethod.getDocComment();
                if (docComment != null) {
                    String doc = extractDocCommentText(docComment);
                    LOG.info("Found method doc: '{}'", doc);
                    return doc;
                } else {
                    LOG.info("No docComment found for method: {}", psiMethod.getName());
                }
            } else if (value instanceof PsiField) {
                PsiField psiField = (PsiField) value;
                LOG.info("Processing PsiField: {}", psiField.getName());
                PsiDocComment docComment = psiField.getDocComment();
                if (docComment != null) {
                    String doc = extractDocCommentText(docComment);
                    LOG.info("Found field doc: '{}'", doc);
                    return doc;
                } else {
                    LOG.info("No docComment found for field: {}", psiField.getName());
                }
            }
            
            LOG.info("No documentation extracted for node: {}", node.getName());
            return null;
        } catch (Exception e) {
            LOG.warn("Error in extractDocumentationDirectly: {}", e.getMessage());
            return null;
        }
    }
    
    @Nullable
    private static String extractFileDocumentation(@Nullable PsiFile psiFile) {
        if (psiFile == null || !(psiFile instanceof PsiClassOwner)) {
            return null;
        }
        
        PsiClassOwner classOwner = (PsiClassOwner) psiFile;
        PsiClass[] classes = classOwner.getClasses();
        
        if (classes.length == 0) {
            return null;
        }
        
        // Get documentation from first class
        PsiClass firstClass = classes[0];
        PsiDocComment docComment = firstClass.getDocComment();
        
        if (docComment != null) {
            return extractDocCommentText(docComment);
        }
        
        return null;
    }
    
    @Nullable
    private static String extractDocCommentText(@NotNull PsiDocComment docComment) {
        PsiElement[] descriptionElements = docComment.getDescriptionElements();
        
        if (descriptionElements.length == 0) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        for (PsiElement element : descriptionElements) {
            if (element instanceof PsiWhiteSpace) {
                continue;
            }
            String text = element.getText().trim();
            if (!text.isEmpty() && !text.equals("*")) {
                sb.append(text).append(" ");
            }
        }
        
        return sb.toString().trim();
    }

    public static void clearCache() {
        cache.clear();
    }

    public static void clearProject(@NotNull Project project) {
        cache.remove(project);
    }
}
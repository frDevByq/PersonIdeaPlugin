package com.github.frdevbyq.personideaplugin.showcomment;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.ide.util.treeView.PresentableNodeDescriptor.ColoredFragment;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SimpleTextAttributes;
import com.github.frdevbyq.personideaplugin.showcomment.cache.TreeCacheUtils;
import com.github.frdevbyq.personideaplugin.showcomment.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class Tree implements ProjectViewNodeDecorator {

    private static final Logger LOG = LoggerFactory.getLogger(Tree.class);

    @Override
    public void decorate(@NotNull ProjectViewNode node, @NotNull PresentationData data) {
        try {
            decorateImpl(node, data);
        } catch (ProcessCanceledException ignore) {
            // ignore
        } catch (Throwable e) {
            LOG.info("Tree catch Throwable but log to record.", e);
        }
    }

    private void decorateImpl(@NotNull ProjectViewNode<?> node, @NotNull PresentationData data) {
        @NotNull AppSettingsState state = AppSettingsState.getInstance();
        LOG.info("Tree decorator called for node: {} (class: {}) showTreeComment: {}", 
            node.getName(), node.getClass().getSimpleName(), state.showTreeComment);
        
        if (!state.showTreeComment) {
            LOG.info("Tree comment disabled in settings");
            return;
        }
        @Nullable Project project = node.getProject();
        if (project == null) {
            LOG.info("Project is null for node: {}", node.getName());
            return;
        }

        // Check what's already in the presentation data
        LOG.info("Current presentation data for node {}: coloredText={}, presentableText='{}'", 
            node.getName(), data.getColoredText().size(), data.getPresentableText());

        @Nullable String doc = TreeCacheUtils.treeDoc(node, project);
        LOG.info("Got doc for node {}: '{}'", node.getName(), doc);
        
        if (doc != null && !doc.trim().isEmpty()) {
            // Check if we should show the identifier (class/method name)
            if (state.showTreeIdentifier) {
                // First, ensure the node name is displayed
                String nodeName = data.getPresentableText();
                if (nodeName == null || nodeName.trim().isEmpty()) {
                    nodeName = node.getName();
                }
                
                // Clear existing text and rebuild with node name + comment
                data.clearText();
                data.addText(new ColoredFragment(nodeName, SimpleTextAttributes.REGULAR_ATTRIBUTES));
                
                String comment = " " + doc.trim();
                LOG.info("Adding node name '{}' and comment '{}' to node {}", nodeName, comment, node.getName());
                // Add the comment as gray text after the node name
                data.addText(new ColoredFragment(comment, 
                    SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES));
            } else {
                // Only show comment without identifier
                data.clearText();
                data.addText(new ColoredFragment(doc.trim(), 
                    SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES));
                LOG.info("Adding only comment '{}' to node {}", doc.trim(), node.getName());
            }
            
            // Log final result
            LOG.info("Final presentation data for node {}: coloredText={}, presentableText='{}'", 
                node.getName(), data.getColoredText().size(), data.getPresentableText());
        } else {
            LOG.info("No doc to display for node: {}", node.getName());
        }
    }
}
package com.github.frdevbyq.personideaplugin.showcomment.java;

import com.intellij.json.psi.JsonProperty;
import com.intellij.json.psi.JsonStringLiteral;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JsonJumpJava extends PsiReferenceContributor {

    private static final Logger LOG = LoggerFactory.getLogger(JsonJumpJava.class);

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        try {
            register(registrar);
        } catch (Exception e) {
            LOG.info("JsonJumpJava catch exception but log to record.", e);
        }
    }

    private static void register(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(JsonStringLiteral.class),
                new PsiReferenceProvider() {
                    @Override
                    public @NotNull PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                                          @NotNull ProcessingContext context) {
                        try {
                            JsonProperty jsonProp = PsiTreeUtil.getParentOfType(
                                    element, JsonProperty.class, true);
                            if (jsonProp == null) {
                                return PsiReference.EMPTY_ARRAY;
                            }
                            VirtualFile virtualFile = element.getContainingFile().getVirtualFile();
                            if (virtualFile == null) {
                                return PsiReference.EMPTY_ARRAY;
                            }

                            // For now, return empty array as this is a simplified implementation
                            return PsiReference.EMPTY_ARRAY;
                        } catch (ProcessCanceledException ignored) {
                            return PsiReference.EMPTY_ARRAY;
                        } catch (Throwable e) {
                            LOG.error("JsonJumpJava.register catch Throwable but log to record.", e);
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}
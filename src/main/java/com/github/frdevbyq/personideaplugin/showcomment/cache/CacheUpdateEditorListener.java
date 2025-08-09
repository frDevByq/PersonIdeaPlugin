package com.github.frdevbyq.personideaplugin.showcomment.cache;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class CacheUpdateEditorListener implements FileEditorManagerListener {

    @Override
    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        // Clear cache when files are opened to ensure fresh data
        LineEndCacheUtils.clear();
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        // Clear cache when files are closed to free memory
        LineEndCacheUtils.clear();
    }

    @Override
    public void selectionChanged(@NotNull com.intellij.openapi.fileEditor.FileEditorManagerEvent event) {
        // Clear cache on selection change to update comments
        LineEndCacheUtils.clear();
    }
}
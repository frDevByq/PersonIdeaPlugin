package com.github.frdevbyq.personideaplugin.showcomment.cache;

import com.intellij.openapi.editor.LineExtensionInfo;
import com.github.frdevbyq.personideaplugin.showcomment.bean.LineInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LineEndCache {
    @NotNull
    public final Map<String, List<LineExtensionInfo>> map = new ConcurrentHashMap<>();
    public volatile boolean show = true;
    public volatile boolean selectChanged = false;
    @NotNull
    public volatile LineInfo info;

    public LineEndCache(@NotNull LineInfo info) {
        this.info = info;
    }

    public boolean needUpdate() {
        return show && selectChanged;
    }

    public void updated() {
        show = false;
        selectChanged = false;
    }

    public void clear() {
        map.clear();
    }
}
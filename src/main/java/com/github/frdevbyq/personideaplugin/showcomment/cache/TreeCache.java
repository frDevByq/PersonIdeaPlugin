package com.github.frdevbyq.personideaplugin.showcomment.cache;

import org.jetbrains.annotations.Nullable;

public class TreeCache {
    @Nullable
    public volatile String doc;
    public volatile boolean needUpdate = true;
    public volatile long lastModified = 0;

    public void clear() {
        doc = null;
        needUpdate = true;
        lastModified = 0;
    }

    public void setDoc(@Nullable String doc) {
        this.doc = doc;
        this.needUpdate = false;
    }
}
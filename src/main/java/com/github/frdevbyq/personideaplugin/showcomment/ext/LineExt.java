package com.github.frdevbyq.personideaplugin.showcomment.ext;

import com.github.frdevbyq.personideaplugin.showcomment.bean.LineInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LineExt {
    
    public static @Nullable String doc(@NotNull LineInfo info) {
        // External documentation system - basic implementation
        // This would integrate with TSV files and external comment systems
        // For now, return null to indicate no external documentation
        return null;
    }
}
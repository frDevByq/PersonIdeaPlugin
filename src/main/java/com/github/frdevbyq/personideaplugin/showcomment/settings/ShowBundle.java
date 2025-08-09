package com.github.frdevbyq.personideaplugin.showcomment.settings;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public final class ShowBundle extends DynamicBundle {

    private static final String BUNDLE = "messages.ShowCommentBundle";
    private static final ShowBundle INSTANCE = new ShowBundle();

    private ShowBundle() {
        super(BUNDLE);
    }

    @NotNull
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                      Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

    @NotNull
    public static Supplier<String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                                  Object @NotNull ... params) {
        return INSTANCE.getLazyMessage(key, params);
    }
}
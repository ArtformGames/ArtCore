package com.artformgames.core.function.settings.values;

import com.artformgames.core.function.settings.SettingsType;
import org.jetbrains.annotations.NotNull;

public class TextSettingsType extends SettingsType<String> {

    public TextSettingsType(int id, @NotNull String defaultValue) {
        super(id, String.class, defaultValue);
    }

    @Override
    public @NotNull String deserialize(@NotNull String primitive) {
        return primitive;
    }

}

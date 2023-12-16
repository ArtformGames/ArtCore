package com.artformgames.core.function.settings.values;

import com.artformgames.core.function.settings.SettingsType;
import org.jetbrains.annotations.NotNull;

public class BooleanSettingsType extends SettingsType<Boolean> {

    public BooleanSettingsType(int id, @NotNull Boolean defaultValue) {
        super(id, Boolean.class, defaultValue);
    }

    @Override
    public @NotNull Boolean deserialize(@NotNull String primitive) {
        try {
            return Boolean.valueOf(primitive);
        } catch (Exception ex) {
            return false;
        }
    }

}

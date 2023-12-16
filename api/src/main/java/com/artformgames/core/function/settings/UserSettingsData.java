package com.artformgames.core.function.settings;

import com.artformgames.core.user.handler.UserHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UserSettingsData extends UserHandler {

    <V> @NotNull V get(SettingsType<V> type);

    <V> void set(SettingsType<V> type, @Nullable V value);

}

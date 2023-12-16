package com.artformgames.core.function.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.util.Objects;

public abstract class SettingsType<V> {
    private final int id;

    private final @NotNull Class<V> valueClazz;
    private final @NotNull V defaultValue;

    public SettingsType(int id, @NotNull Class<V> valueClazz, @NotNull V defaultValue) {
        this.id = id;
        this.valueClazz = valueClazz;
        this.defaultValue = defaultValue;
    }

    public int getID() {
        return id;
    }

    @NotNull
    public Class<V> getValueClazz() {
        return this.valueClazz;
    }

    @NotNull
    public String serialize(@NotNull V complex) {
        return Objects.toString(complex);
    }

    @NotNull
    public abstract V deserialize(@NotNull String primitive) throws Exception;

    @NotNull
    public V getValue(@Nullable String data) throws Exception {
        if (data == null) throw new ParseException("数据值为空，无法分析", 0);
        else return deserialize(data);
    }

    public @NotNull V getDefaultValue() {
        return defaultValue;
    }


}

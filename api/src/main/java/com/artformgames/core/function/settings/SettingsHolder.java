package com.artformgames.core.function.settings;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingsHolder {

    private @Nullable String data;
    private @Nullable Object cachedValue;

    protected SettingsHolder(@Nullable String data, @Nullable Object cachedValue) {
        this.data = data;
        this.cachedValue = cachedValue;
    }

    public @Nullable String getData() {
        return data;
    }

    public <V> boolean setData(@NotNull SettingsType<V> type, V value) {
        if (value == null || value == type.getDefaultValue()) return false; // 和默认设置一样，应当被移除数据
        this.data = type.serialize(value); // 序列化数据，用于后续存储。
        updateCache(value); // 更新缓存
        return true;
    }

    private void updateCache(@Nullable Object cachedValue) {
        this.cachedValue = cachedValue;
    }

    public @Nullable Object getCachedValue() {
        return cachedValue;
    }

    public <V> @NotNull V getValue(@NotNull SettingsType<V> type) throws Exception {
        Object cachedValue = getCachedValue();
        if (type.getValueClazz().isInstance(cachedValue)) {
            return type.getValueClazz().cast(cachedValue);
        }

        V value = type.getValue(data); // 解析新数据
        updateCache(value);

        return value;
    }

    @Contract("null -> null; !null -> !null")
    public static @Nullable SettingsHolder of(@Nullable String data) {
        if (data == null) return null;
        else return new SettingsHolder(data, null);
    }

    public static <V> @Nullable SettingsHolder of(@NotNull SettingsType<V> type, @Nullable V value) {
        SettingsHolder holder = new SettingsHolder(null, null);
        if (holder.setData(type, value)) return holder;
        else return null;
    }

}

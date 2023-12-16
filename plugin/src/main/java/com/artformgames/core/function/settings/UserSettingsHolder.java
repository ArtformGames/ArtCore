package com.artformgames.core.function.settings;

import com.artformgames.core.user.User;
import com.artformgames.core.user.handler.AbstractUserHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class UserSettingsHolder extends AbstractUserHandler implements UserSettingsData {
    
    protected final Map<@NotNull Integer, @NotNull SettingsHolder> values;

    protected UserSettingsHolder(User user, Map<@NotNull Integer, @NotNull SettingsHolder> values) {
        super(user);
        this.values = values;
    }

    @Override
    public <V> @NotNull V get(SettingsType<V> type) {
        SettingsHolder holder = values.get(type.getID());
        if (holder == null) {
            return type.getDefaultValue();
        } else {
            try {
                return holder.getValue(type);
            } catch (Exception e) {
                values.remove(type.getID());
                return type.getDefaultValue();
            }
        }
    }

    @Override
    public <V> void set(SettingsType<V> type, @Nullable V value) {
        SettingsHolder holder = this.values.get(type.getID());
        if (holder == null) {
            SettingsHolder newHolder = SettingsHolder.of(type, value);
            if (newHolder != null) values.put(type.getID(), newHolder);
        } else if (!holder.setData(type, value)) {
            values.remove(type.getID());
        }
    }

}

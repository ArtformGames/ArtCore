package com.artformgames.core.user.handler;

import com.artformgames.core.ArtCore;
import com.artformgames.core.user.User;
import com.artformgames.core.user.UserKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UserHandler {

    User getUser();

    default UserKey getUserKey() {
        return getUser().getKey();
    }

    default UUID getUserUUID() {
        return getUser().getUserUUID();
    }

    default String getUsername() {
        return getUser().getUsername();
    }

    static <T extends UserHandler> T get(@NotNull Player player, Class<T> handlerClazz) {
        return get(ArtCore.getUserManager().get(player), handlerClazz);
    }

    static <T extends UserHandler> T get(@NotNull User user, Class<T> handlerClazz) {
        return user.getHandler(handlerClazz);
    }

}

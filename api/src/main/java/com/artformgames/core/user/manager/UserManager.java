package com.artformgames.core.user.manager;

import com.artformgames.core.user.User;
import com.artformgames.core.user.UserKey;
import com.artformgames.core.user.handler.UserHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ExecutorService;

public interface UserManager<USER extends User> extends UserKeyManager, UserHandlerManager {

    @NotNull ExecutorService getExecutor();

    @Unmodifiable
    @NotNull Map<@NotNull UUID, USER> list();

    @Nullable USER get(@NotNull UUID userUUID);

    @Nullable USER get(long uid);

    default @Nullable USER get(@NotNull UserKey key) {
        return get(key.uuid());
    }

    default @NotNull USER get(@NotNull Player player) {
        return Objects.requireNonNull(get(player.getUniqueId()));
    }

    @NotNull USER load(@NotNull UUID userUUID, @NotNull String username) throws Exception;

    @NotNull USER loadTemp(@NotNull UserKey key, @NotNull List<Class<? extends UserHandler>> handlerClasses) throws Exception;

    default @NotNull USER peek(@NotNull UserKey key, @NotNull List<Class<? extends UserHandler>> handlerClasses) throws Exception {
        return Optional.ofNullable(get(key)).orElse(loadTemp(key, handlerClasses));
    }

    void unload(@NotNull UUID userUUID);


}

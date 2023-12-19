package com.artformgames.core.user.manager;

import com.artformgames.core.user.User;
import com.artformgames.core.user.UserKey;
import com.artformgames.core.user.handler.AbstractUserHandler;
import com.artformgames.core.user.handler.SimpleHandlerLoader;
import com.artformgames.core.user.handler.UserHandler;
import com.artformgames.core.user.handler.UserHandlerLoader;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import panda.std.function.ThrowingBiConsumer;
import panda.std.function.ThrowingFunction;

import java.util.function.Function;

public interface UserHandlerManager {

    boolean containsHandler(@NotNull Class<? extends UserHandler> handlerClazz);

    <H extends AbstractUserHandler> void registerHandler(@NotNull UserHandlerLoader<H> loader);

    default <H extends AbstractUserHandler> void registerHandler(@NotNull Plugin plugin, @NotNull Class<H> handlerClass,
                                                                 @NotNull Function<User, @NotNull H> emptySupplier,
                                                                 @NotNull ThrowingFunction<User, @Nullable H, Exception> loader,
                                                                 @Nullable ThrowingBiConsumer<UserKey, H, Exception> saver) {
        registerHandler(SimpleHandlerLoader.of(plugin, handlerClass, emptySupplier, loader, saver));
    }

    default <H extends AbstractUserHandler> void registerHandler(@NotNull Plugin plugin, @NotNull Class<H> handlerClass,
                                                                 @NotNull Function<User, @NotNull H> emptySupplier,
                                                                 @NotNull ThrowingFunction<User, @Nullable H, Exception> loader) {
        registerHandler(SimpleHandlerLoader.of(plugin, handlerClass, emptySupplier, loader, null));
    }

    default <H extends AbstractUserHandler> void registerHandler(@NotNull Plugin plugin, @NotNull Class<H> handlerClass,
                                                                 @NotNull Function<User, H> loader) {
        registerHandler(SimpleHandlerLoader.of(plugin, handlerClass, loader));
    }

    void unregisterHandler(@NotNull Class<? extends UserHandler> handlerClazz);

}

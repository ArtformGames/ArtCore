package com.artformgames.core.user.manager;

import com.artformgames.core.user.User;
import com.artformgames.core.user.handler.AbstractUserHandler;
import com.artformgames.core.user.handler.TempHandlerLoader;
import com.artformgames.core.user.handler.UserHandler;
import com.artformgames.core.user.handler.UserHandlerLoader;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface UserHandlerManager {

    boolean containsHandler(@NotNull Class<? extends UserHandler> handlerClazz);

    <H extends AbstractUserHandler> void registerHandler(@NotNull UserHandlerLoader<H> loader);

    default <H extends AbstractUserHandler> void registerHandler(@NotNull Plugin plugin, @NotNull Class<H> handlerClass,
                                                                 @NotNull Function<User, H> loader) {
        registerHandler(TempHandlerLoader.of(plugin, handlerClass, loader));
    }

    void unregisterHandler(@NotNull Class<? extends UserHandler> handlerClazz);

}

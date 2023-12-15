package com.artformgames.core.user.manager;

import com.artformgames.core.user.handler.UserHandler;
import com.artformgames.core.user.handler.UserHandlerLoader;
import org.jetbrains.annotations.NotNull;

public interface UserHandlerManager {

    boolean containsHandler(@NotNull Class<? extends UserHandler> handlerClazz);

    <H extends UserHandler> void registerHandler(@NotNull String namespace, @NotNull UserHandlerLoader<H> loader);

    void unregisterHandler(@NotNull Class<? extends UserHandler> handlerClazz);

}

package com.artformgames.core.user.handler;

import com.artformgames.core.user.User;
import com.artformgames.core.user.UserKey;
import org.jetbrains.annotations.NotNull;

public abstract class UserHandlerLoader<H extends UserHandler> {

    protected final @NotNull Class<H> handlerClass;

    public UserHandlerLoader(@NotNull Class<H> handlerClass) {
        this.handlerClass = handlerClass;
    }

    public abstract @NotNull H load(User user) throws Exception;

    public abstract void unload(UserKey user, H handler);

    public abstract void save(UserKey user, H handler) throws Exception;

}

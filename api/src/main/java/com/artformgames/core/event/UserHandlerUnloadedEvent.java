package com.artformgames.core.event;

import com.artformgames.core.user.User;
import com.artformgames.core.user.handler.UserHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserHandlerUnloadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final User user;
    private final Class<? extends UserHandler> handlerClazz;

    public UserHandlerUnloadedEvent(User user, Class<? extends UserHandler> handlerClazz) {
        super(true);
        this.user = user;
        this.handlerClazz = handlerClazz;
    }


    public User getUser() {
        return this.user;
    }

    public Class<? extends UserHandler> getHandlerClazz() {
        return handlerClazz;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
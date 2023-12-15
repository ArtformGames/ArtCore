package com.artformgames.core.event;

import com.artformgames.core.user.User;
import com.artformgames.core.user.handler.UserHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserHandlerLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final User user;
    private final UserHandler handler;

    public UserHandlerLoadedEvent(User user, UserHandler handler) {
        super(true);
        this.user = user;
        this.handler = handler;
    }

    public User getUser() {
        return this.user;
    }

    public UserHandler getHandler() {
        return handler;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
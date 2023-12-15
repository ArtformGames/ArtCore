package com.artformgames.core.event;

import com.artformgames.core.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;

public class UserLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final User user;
    private final AsyncPlayerPreLoginEvent loginEvent;

    public UserLoadedEvent(User user, AsyncPlayerPreLoginEvent loginEvent) {
        super(true);
        this.user = user;
        this.loginEvent = loginEvent;
    }

    public AsyncPlayerPreLoginEvent getLoginEvent() {
        return loginEvent;
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
package com.artformgames.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserUnloadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID userUUID;

    public UserUnloadedEvent(UUID userUUID) {
        super(true);
        this.userUUID = userUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
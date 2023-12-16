package com.artformgames.core.user;

import com.artformgames.core.user.handler.AbstractUserHandler;
import com.artformgames.core.user.handler.UserHandler;
import com.artformgames.core.user.handler.UserHandlerLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitUser implements User {

    protected final @NotNull BukkitUserManager userManager;
    protected final @NotNull UserKey key;

    protected final @NotNull Map<UserHandlerLoader<?>, AbstractUserHandler> handlers = new ConcurrentHashMap<>();
    protected final @NotNull Map<String, Object> tags = new HashMap<>();
    protected boolean fullyLoaded = false;

    public BukkitUser(@NotNull BukkitUserManager userManager,
                      long uid, @NotNull UUID userUUID, @NotNull String username) {
        this(userManager, new UserKey(uid, userUUID, username));
    }

    public BukkitUser(@NotNull BukkitUserManager userManager, @NotNull UserKey key) {
        this.userManager = userManager;
        this.key = key;
    }

    @NotNull
    @Override
    public UserKey getKey() {
        return key;
    }

    @Override
    public boolean isFullyLoaded() {
        return fullyLoaded;
    }

    @Unmodifiable
    @Override
    public @NotNull Map<String, Object> getTags() {
        return Collections.unmodifiableMap(this.tags);
    }

    @Override
    public boolean containsTag(@NotNull String key) {
        return this.tags.containsKey(key);
    }

    @Override
    public @Nullable Object getTagValue(@NotNull String key, Object defaultValue) {
        return this.tags.getOrDefault(key, defaultValue);
    }

    @Override
    public boolean setTagValue(@NotNull String key, Object value) {
        return this.tags.put(key, value) != null;
    }

    @Override
    public boolean removeTag(@NotNull String key) {
        return this.tags.remove(key) != null;
    }

    @Override
    public boolean containsHandler(Class<? extends UserHandler> handlerClazz) {
        return this.handlers.keySet().stream().anyMatch(registry -> registry.isInstance(handlerClazz));
    }

    @Override
    public <T extends UserHandler> @Nullable T getNullableHandler(Class<T> handlerClazz) {
        return this.handlers.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(handlerClazz))
                .findFirst()
                .map(entry -> handlerClazz.cast(entry.getValue()))
                .orElse(null);
    }

    public @Nullable AbstractUserHandler getNullableHandler(UserHandlerLoader<?> registry) {
        return this.handlers.get(registry);
    }

    @Override
    public <T extends AbstractUserHandler> T loadHandler(@NotNull UserHandlerLoader<T> loader) throws Exception {
        if (this.handlers.containsKey(loader)) {
            throw new ConcurrentModificationException("Handler " + loader.getHandlerClass().getName() + " is already loaded.");
        }

        T handler = loader.load(this).join();
        this.handlers.put(loader, handler);
        return handler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractUserHandler> void unloadHandler(Class<T> handlerClazz) throws Exception {
        UserHandlerLoader<T> loader = this.handlers.keySet().stream()
                .filter(registry -> registry.isInstance(handlerClazz)).findFirst()
                .map(registry -> (UserHandlerLoader<T>) registry)
                .orElseThrow(() -> new NoSuchElementException("Handler " + handlerClazz.getName() + " is not loaded."));

        unloadHandler(loader);
    }

    @Override
    public <T extends AbstractUserHandler> void unloadHandler(UserHandlerLoader<T> loader) throws Exception {
        AbstractUserHandler handler = this.handlers.remove(loader);
        if (handler == null || !loader.isInstance(handler.getClass())) return;
        T castedHandler = loader.getHandlerClass().cast(handler);
        loader.saveData(this.key, castedHandler);
    }


}

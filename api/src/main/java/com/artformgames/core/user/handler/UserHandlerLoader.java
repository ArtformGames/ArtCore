package com.artformgames.core.user.handler;

import cc.carm.lib.easyplugin.utils.ColorParser;
import com.artformgames.core.ArtCore;
import com.artformgames.core.user.User;
import com.artformgames.core.user.UserKey;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class UserHandlerLoader<H extends AbstractUserHandler> {

    protected final @NotNull Plugin plugin;
    protected final @NotNull Class<H> handlerClass;
    protected final @NotNull Set<Class<? extends UserHandler>> implementations;

    protected UserHandlerLoader(@NotNull Plugin plugin, @NotNull Class<H> handlerClass) {
        this.plugin = plugin;
        this.handlerClass = handlerClass;
        this.implementations = getImplementations(handlerClass);
    }

    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    public boolean isDebugging() {
        return false;
    }

    public @NotNull Class<H> getHandlerClass() {
        return handlerClass;
    }

    public @NotNull CompletableFuture<H> load(@NotNull User user) {
        return CompletableFuture.supplyAsync(() -> {

            try {
                long s1 = System.currentTimeMillis();
                debug("Start to loading " + user.getUsername() + "'s data...");
                H data = loadData(user);
                if (data == null) {
                    debug("Non " + user.getUsername() + " data found, creating new data...");
                    return emptyData(user);
                } else {
                    debug("Successfully loaded " + user.getUsername() + "'s data, cost " + (System.currentTimeMillis() - s1) + " ms.");
                    return data;
                }
            } catch (Exception ex) {
                error("Failled to load " + user.getUsername() + "'s data, please check the configuration!");
                ex.printStackTrace();
                return errorData(user);
            }

        }, ArtCore.getUserManager().getExecutor());
    }

    public @NotNull CompletableFuture<Boolean> castSave(@NotNull AbstractUserHandler data) {
        if (!isInstance(data.getClass())) {
            error("Failed to save " + data.getUserKey().name() + "'s data, the handler is not instance of " + handlerClass.getName() + "!");
            return CompletableFuture.completedFuture(false);
        }
        return save(getHandlerClass().cast(data));
    }

    public @NotNull CompletableFuture<Boolean> save(@NotNull H data) {
        return CompletableFuture.supplyAsync(() -> {
            UserKey key = data.getUserKey();
            try {
                long s1 = System.currentTimeMillis();
                debug("Start saving " + key.name() + "'s data...");
                saveData(data.getUserKey(), data);
                debug("Successfully saved " + key.name() + "'s data, cost " + (System.currentTimeMillis() - s1) + " ms.");
                return true;
            } catch (Exception ex) {
                error("Failed to save " + key.name() + "'s data, please check the configuration!");
                ex.printStackTrace();
                return false;
            }

        }, ArtCore.getUserManager().getExecutor());
    }

    public abstract @NotNull H emptyData(@NotNull User user);

    public @NotNull H errorData(@NotNull User user) {
        return emptyData(user);
    }

    public abstract @Nullable H loadData(User user) throws Exception;

    public abstract void saveData(UserKey user, H handler) throws Exception;

    public void print(@Nullable String prefix, @Nullable String... messages) {
        Arrays.stream(messages)
                .map(message -> "[" + plugin.getName() + "] " + (prefix == null ? "" : prefix) + message)
                .map(ColorParser::parse)
                .forEach(message -> Bukkit.getConsoleSender().sendMessage(message));
    }

    public void log(@Nullable String... messages) {
        print(null, messages);
    }

    public void error(String... messages) {
        print("&c[ERROR] &r", messages);
    }

    public void debug(@Nullable String... messages) {
        if (isDebugging()) print("&8[DEBUG] &r", messages);
    }

    public boolean isInstance(@NotNull Class<? extends UserHandler> clazz) {
        if (handlerClass == clazz) return true;
        else return this.implementations.stream().anyMatch(other -> other == clazz);
    }

    public boolean hasImplemented(@NotNull Class<? extends UserHandler> clazz) {
        return clazz.isAssignableFrom(handlerClass);
    }

    private static @NotNull Set<Class<? extends UserHandler>> getImplementations(@NotNull Class<? extends AbstractUserHandler> handlerClazz) {
        return Arrays.stream(handlerClazz.getInterfaces())
                .flatMap(interfaceClazz -> getInterfaces(interfaceClazz).stream())
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private static @NotNull Set<Class<? extends UserHandler>> getInterfaces(@NotNull Class<?> interfaceClazz) {
        if (interfaceClazz == UserHandler.class) return new HashSet<>();
        if (!UserHandler.class.isAssignableFrom(interfaceClazz)) return new HashSet<>();
        Set<Class<? extends UserHandler>> classes = new HashSet<>();
        classes.add((Class<? extends UserHandler>) interfaceClazz);
        Arrays.stream(interfaceClazz.getInterfaces()).map(UserHandlerLoader::getInterfaces).forEach(classes::addAll);
        return classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserHandlerLoader<?> that = (UserHandlerLoader<?>) o;

        return handlerClass.equals(that.handlerClass);
    }

    @Override
    public int hashCode() {
        return handlerClass.hashCode();
    }

}

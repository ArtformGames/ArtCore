package com.artformgames.core.user.handler;

import com.artformgames.core.user.User;
import com.artformgames.core.user.UserKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import panda.std.function.ThrowingBiConsumer;
import panda.std.function.ThrowingFunction;

import java.util.function.Function;

public class SimpleHandlerLoader<H extends AbstractUserHandler> extends UserHandlerLoader<H> {

    public static <T extends AbstractUserHandler> SimpleHandlerLoader<T> of(@NotNull Plugin plugin, @NotNull Class<T> handlerClass,
                                                                            @NotNull Function<User, @NotNull T> loader) {
        return of(plugin, handlerClass, loader, loader::apply, null);
    }

    public static <T extends AbstractUserHandler> SimpleHandlerLoader<T> of(@NotNull Plugin plugin, @NotNull Class<T> handlerClass,
                                                                            @NotNull Function<User, @NotNull T> emptySupplier,
                                                                            @NotNull ThrowingFunction<User, @Nullable T, Exception> loader) {
        return of(plugin, handlerClass, emptySupplier, loader, null);
    }

    public static <T extends AbstractUserHandler> SimpleHandlerLoader<T> of(@NotNull Plugin plugin, @NotNull Class<T> handlerClass,
                                                                            @NotNull Function<User, @NotNull T> emptySupplier,
                                                                            @NotNull ThrowingFunction<User, @Nullable T, Exception> loader,
                                                                            @Nullable ThrowingBiConsumer<UserKey, T, Exception> saver) {
        return new SimpleHandlerLoader<>(plugin, handlerClass, emptySupplier, loader, saver);
    }

    protected final @NotNull Function<User, @NotNull H> emptySupplier;
    protected final @NotNull ThrowingFunction<User, H, Exception> loader;
    protected final @Nullable ThrowingBiConsumer<UserKey, H, Exception> saver;

    protected SimpleHandlerLoader(@NotNull Plugin plugin, @NotNull Class<H> handlerClass,
                                  @NotNull Function<User, @NotNull H> emptySupplier,
                                  @NotNull ThrowingFunction<User, @Nullable H, Exception> loader,
                                  @Nullable ThrowingBiConsumer<UserKey, H, Exception> saver) {
        super(plugin, handlerClass);
        this.emptySupplier = emptySupplier;
        this.loader = loader;
        this.saver = saver;
    }

    @Override
    public @NotNull H emptyData(@NotNull User user) {
        return emptySupplier.apply(user);
    }

    @Override
    public @Nullable H loadData(User user) throws Exception {
        return loader.apply(user);
    }

    @Override
    public void saveData(UserKey user, H handler) throws Exception {
        if (saver != null) saver.accept(user, handler);
    }

    

}

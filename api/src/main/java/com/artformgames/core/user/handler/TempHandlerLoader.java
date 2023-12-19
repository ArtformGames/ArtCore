package com.artformgames.core.user.handler;

import com.artformgames.core.user.User;
import com.artformgames.core.user.UserKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TempHandlerLoader<H extends AbstractUserHandler> extends UserHandlerLoader<H> {

    public static <T extends AbstractUserHandler> TempHandlerLoader<T> of(@NotNull Plugin plugin, @NotNull Class<T> handlerClass,
                                                                   @NotNull Function<User, T> loader) {
        return new TempHandlerLoader<>(plugin, handlerClass, loader);
    }

    protected final @NotNull Function<User, H> loader;

    protected TempHandlerLoader(@NotNull Plugin plugin, @NotNull Class<H> handlerClass, @NotNull Function<User, H> loader) {
        super(plugin, handlerClass);
        this.loader = loader;
    }

    @Override
    public @NotNull H emptyData(@NotNull User user) {
        return loader.apply(user);
    }

    @Override
    public @Nullable H loadData(User user) {
        return loader.apply(user);
    }

    @Override
    public void saveData(UserKey user, H handler) {
    }

}

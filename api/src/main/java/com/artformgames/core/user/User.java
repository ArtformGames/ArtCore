package com.artformgames.core.user;

import com.artformgames.core.user.handler.AbstractUserHandler;
import com.artformgames.core.user.handler.UserHandler;
import com.artformgames.core.user.handler.UserHandlerLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public interface User extends Comparable<User> {

    @NotNull UserKey getKey();

    default long getID() {
        return getKey().id();
    }

    default @NotNull UUID getUserUUID() {
        return getKey().uuid();
    }

    default @NotNull String getUsername() {
        return getKey().name();
    }

    boolean isFullyLoaded();

    <T extends UserHandler> @Nullable T getNullableHandler(Class<T> handlerClazz);

    default <T extends UserHandler> @NotNull T getHandler(Class<T> handlerClazz) throws NullPointerException {
        return Objects.requireNonNull(
                getNullableHandler(handlerClazz),
                "User '" + getUsername() + "' missing '" + handlerClazz.getSimpleName() + "' ."
        );
    }

    default <T extends UserHandler> @NotNull Optional<@Nullable T> getOptionalHandler(Class<T> handlerClazz) {
        return Optional.ofNullable(getNullableHandler(handlerClazz));
    }

    boolean containsHandler(Class<? extends UserHandler> handlerClazz);

    <T extends AbstractUserHandler> T loadHandler(@NotNull UserHandlerLoader<T> loader) throws Exception;

    <T extends AbstractUserHandler> void unloadHandler(Class<T> handlerClazz) throws Exception;

    <T extends AbstractUserHandler> void unloadHandler(UserHandlerLoader<T> loader) throws Exception;

    @Unmodifiable
    @NotNull Map<String, Object> getTags();

    boolean containsTag(@NotNull String key);

    boolean removeTag(@NotNull String key);

    boolean setTagValue(@NotNull String key, Object value);


    @Contract("_, !null -> !null")
    Object getTagValue(@NotNull String key, @Nullable Object defaultValue);

    default @Nullable Object getTagValue(@NotNull String key) {
        return getTagValue(key, null);
    }

    default <V> @Nullable V getTagValue(@NotNull String key, @NotNull Class<V> valueClazz, @Nullable V defaultValue) {
        return getTagOptional(key, valueClazz).orElse(defaultValue);
    }

    default Optional<@Nullable Object> getTagOptional(@NotNull String key) {
        return Optional.ofNullable(getTagValue(key));
    }

    default <V> Optional<@Nullable V> getTagOptional(@NotNull String key, @NotNull Class<V> valueClazz) {
        return getTagOptional(key).map(valueClazz::cast);
    }

    @Override
    default int compareTo(@NotNull User that) {
        return Long.compare(this.getID(), that.getID());
    }

}

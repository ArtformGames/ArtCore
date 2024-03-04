package com.artformgames.core.user.manager;

import com.artformgames.core.user.UserKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface UserKeyManager {

    UserKey upsertKey(@NotNull UUID userUUID, @NotNull String username) throws Exception;

    @Nullable UserKey getKey(UserKey.KeyType type, Object param);


    default @Nullable UserKey getKey(long id) {
        return getKey(UserKey.KeyType.ID, id);
    }

    default @Nullable UserKey getKey(@NotNull UUID uuid) {
        return getKey(UserKey.KeyType.UUID, uuid);
    }

    default @Nullable UserKey getKey(@NotNull String username) {
        return getKey(UserKey.KeyType.NAME, username);
    }

    default @Nullable Long getID(@NotNull String username) {
        return Optional.ofNullable(getKey(username)).map(UserKey::id).orElse(null);
    }

    default @Nullable Long getID(@NotNull UUID userUUID) {
        return Optional.ofNullable(getKey(userUUID)).map(UserKey::id).orElse(null);
    }

    default @Nullable String getUsername(long id) {
        return Optional.ofNullable(getKey(id)).map(UserKey::name).orElse(null);
    }

    default @Nullable String getUsername(@NotNull UUID userUUID) {
        return Optional.ofNullable(getKey(userUUID)).map(UserKey::name).orElse(null);
    }

    default @Nullable UUID getUUID(long id) {
        return Optional.ofNullable(getKey(id)).map(UserKey::uuid).orElse(null);
    }

    default @Nullable UUID getUUID(@NotNull String username) {
        return Optional.ofNullable(getKey(username)).map(UserKey::uuid).orElse(null);
    }

}

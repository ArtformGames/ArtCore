package com.artformgames.core.user;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public record UserKey(
        long id, @NotNull UUID uuid,
        @NotNull String name
) {

    public String getValue(KeyType keyType) {
        return switch (keyType) {
            case ID -> String.valueOf(id);
            case UUID -> uuid.toString();
            case NAME -> name;
        };
    }

    public boolean isInstance(KeyType type, Object param) {
        return switch (type) {
            case ID -> param instanceof Long uid && id == uid;
            case UUID -> param instanceof UUID userUUID && uuid.equals(userUUID);
            case NAME -> param instanceof String username && name.equals(username);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserKey userKey = (UserKey) o;
        return id == userKey.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum KeyType {
        ID("id"), UUID("uuid"), NAME("name");

        final String columnName;

        KeyType(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

}

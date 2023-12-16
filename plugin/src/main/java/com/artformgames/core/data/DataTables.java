package com.artformgames.core.data;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.ForeignKeyRule;
import cc.carm.lib.easysql.api.enums.IndexType;
import com.artformgames.core.user.UserKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public enum DataTables implements SQLTable {

    USERS("users", (table) -> {
        table.addAutoIncrementColumn(UserKey.KeyType.ID.getColumnName());
        table.addColumn(UserKey.KeyType.UUID.getColumnName(), "CHAR(36) NOT NULL");
        table.addColumn(UserKey.KeyType.NAME.getColumnName(), "VARCHAR(20)");

        table.setIndex(IndexType.INDEX, "idx_user_name", UserKey.KeyType.NAME.getColumnName());
        table.setIndex(IndexType.UNIQUE_KEY, "idx_user_uuid", UserKey.KeyType.UUID.getColumnName());
    }),

    USER_SETTINGS("user_settings", (builder) -> {
        builder.addColumn("uid", "INT UNSIGNED NOT NULL");
        builder.addColumn("type", "INT(11) UNSIGNED NOT NULL");
        builder.addColumn("value", "TEXT");
        builder.setIndex(IndexType.PRIMARY_KEY, "settings", "uid", "type");
        builder.addForeignKey(
                "uid", "fk_u_settings",
                USERS.getTableName(), "id",
                ForeignKeyRule.CASCADE, ForeignKeyRule.CASCADE
        );
    });

    private final @Nullable String tableName;
    private final @Nullable Consumer<TableCreateBuilder> builder;
    private SQLManager manager;


    DataTables(@Nullable String tableName,
               @Nullable Consumer<TableCreateBuilder> builder) {
        this.tableName = tableName;
        this.builder = builder;
    }

    public boolean create(@NotNull SQLManager sqlManager) throws SQLException {
        if (this.manager == null) this.manager = sqlManager;

        TableCreateBuilder tableBuilder = sqlManager.createTable(getTableName());
        if (builder != null) builder.accept(tableBuilder);
        return tableBuilder.build().executeFunction(l -> l > 0, false);
    }

    @Override
    public @Nullable SQLManager getSQLManager() {
        return this.manager;
    }

    public @NotNull String getTableName() {
        return Optional.ofNullable(this.tableName).orElse(name().toLowerCase(Locale.ROOT));
    }

    public static void initializeTables(@NotNull SQLManager sqlManager) {
        for (DataTables value : values()) {
            try {
                value.create(sqlManager);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

package com.artformgames.core.user;

import cc.carm.lib.easysql.api.SQLQuery;
import com.artformgames.core.data.DataTables;
import com.artformgames.core.user.handler.AbstractUserHandler;
import com.artformgames.core.user.handler.UserHandler;
import com.artformgames.core.user.handler.UserHandlerLoader;
import com.artformgames.core.user.manager.UserManager;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import panda.std.function.ThrowingConsumer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BukkitUserManager implements UserManager<BukkitUser> {

    private final @NotNull Logger logger;
    protected final ExecutorService executorPool;

    protected final @NotNull Set<UserHandlerLoader<?>> handlers;
    protected final @NotNull ConcurrentHashMap<UUID, BukkitUser> users;

    protected final @NotNull Cache<Object, UserKey> keyCache = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES).build();

    public BukkitUserManager(@NotNull Logger logger) {
        this.logger = logger;
        this.executorPool = Executors.newFixedThreadPool(5, r -> {
            Thread thread = new Thread(r, "USER-MANAGER");
            thread.setDaemon(true);
            return thread;
        });
        this.handlers = new HashSet<>();
        this.users = new ConcurrentHashMap<>();
    }

    public void loadAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getUsersMap().containsKey(player.getUniqueId())) continue;
            try {
                load(player.getUniqueId(), player.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void unloadAll() {
        list().keySet().forEach(this::unload);
        getUsersMap().clear();
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    public @NotNull ExecutorService getExecutor() {
        return this.executorPool;
    }

    @Override
    public boolean containsHandler(@NotNull Class<? extends UserHandler> handlerClazz) {
        return this.handlers.stream().anyMatch(registry -> registry.isInstance(handlerClazz));
    }

    @Override
    public <H extends AbstractUserHandler> void registerHandler(@NotNull UserHandlerLoader<H> loader) {
        if (containsHandler(loader.getHandlerClass())) return;

        getLogger().info("Registering [" + loader.getHandlerClass().getSimpleName() + "] by #" + loader.getPlugin().getName() + "...");

        this.handlers.add(loader);

        if (getUsersMap().isEmpty()) return;
        modifyAllUsers(user -> user.loadHandler(loader));
    }

    @Override
    public void unregisterHandler(@NotNull Class<? extends UserHandler> handlerClazz) {
        UserHandlerLoader<?> loader = this.handlers.stream().filter(registry -> registry.isInstance(handlerClazz)).findFirst().orElse(null);
        if (loader == null) return;

        this.handlers.remove(loader);

        getLogger().info("Unregistering [" + loader.getHandlerClass().getSimpleName() + "] by #" + loader.getPlugin().getName() + "...");
        modifyAllUsers(user -> user.unloadHandler(loader));
    }

    public @Nullable UserKey getKeyFromDatabase(UserKey.KeyType type, Object param) {
        return DataTables.USERS.createQuery()
                .addCondition(type.getColumnName().toLowerCase(), param)
                .setLimit(1).build().execute(query -> {
                    ResultSet resultSet = query.getResultSet();
                    if (!resultSet.next()) return null;
                    return new UserKey(
                            resultSet.getInt(UserKey.KeyType.ID.getColumnName()),
                            UUID.fromString(resultSet.getString(UserKey.KeyType.UUID.getColumnName())),
                            resultSet.getString(UserKey.KeyType.NAME.getColumnName())
                    );
                }, null, null);
    }

    @Override
    public @Nullable UserKey getKey(UserKey.KeyType type, Object v) {
        return keyCache.get(v, param -> {
            UserKey fromLoaded = list().values().stream()
                    .filter(user -> user.getKey().isInstance(type, param))
                    .findFirst().map(BukkitUser::getKey).orElse(null);
            if (fromLoaded != null) return fromLoaded;

            return getKeyFromDatabase(type, param);
        });
    }


    public Map<UUID, BukkitUser> getUsersMap() {
        return users;
    }

    public Long upsertID(@NotNull UUID uuid, @NotNull String username) throws Exception {
        long id = -1;
        String cachedName = null;

        try (SQLQuery query = DataTables.USERS.createQuery()
                .addCondition(UserKey.KeyType.UUID.getColumnName(), uuid)
                .setLimit(1).build().execute()) {
            ResultSet resultSet = query.getResultSet();
            if (resultSet != null && resultSet.next()) {
                id = resultSet.getInt(UserKey.KeyType.ID.getColumnName());
                cachedName = resultSet.getString(UserKey.KeyType.NAME.getColumnName());
            }
        } catch (SQLException ignore) {
        }

        if (id > 0) {
            if (cachedName == null || !cachedName.equals(username)) {
                DataTables.USERS.createUpdate()
                        .addColumnValue(UserKey.KeyType.NAME.getColumnName(), username)
                        .addCondition("id", id)
                        .build().execute((e, a) -> {
                            getLogger().severe("更新用户 " + username + " 的用户名失败！");
                            e.printStackTrace();
                        });
            }
            return id;
        } else {
            try {
                return DataTables.USERS.createInsert()
                        .setColumnNames(UserKey.KeyType.UUID.getColumnName(), UserKey.KeyType.NAME.getColumnName())
                        .setParams(uuid, username).returnGeneratedKey(Long.class).execute();
            } catch (SQLException e) {
                getLogger().severe("创建新用户 " + username + " 失败！");
                return null;
            }
        }
    }

    public BukkitUser createUser(@NotNull UUID userUUID, @NotNull String username) throws Exception {
        Long id = upsertID(userUUID, username);
        if (id == null || id <= 0) throw new Exception("无法获取用户 " + username + " 的UID！");
        return new BukkitUser(this, new UserKey(id, userUUID, username));
    }

    @NotNull
    @Unmodifiable
    public Map<UUID, BukkitUser> list() {
        return Collections.unmodifiableMap(getUsersMap());
    }

    @Override
    public @Nullable BukkitUser get(@NotNull UUID userUUID) {
        return getUsersMap().get(userUUID);
    }

    @Override
    public @Nullable BukkitUser get(long uid) {
        return getUsersMap().values().stream().filter(u -> u.getKey().id() == uid).findFirst().orElse(null);
    }

    @Override
    public @NotNull BukkitUser load(@NotNull UUID userUUID, @NotNull String username) throws Exception {
        BukkitUser user = createUser(userUUID, username);

        for (UserHandlerLoader<?> loader : this.handlers) {
            user.loadHandler(loader);
        }

        user.fullyLoaded = true;
        this.users.put(userUUID, user);
        return user;
    }

    @Override
    public @NotNull BukkitUser loadTemp(@NotNull UserKey key, @NotNull List<Class<? extends UserHandler>> handlerClasses) throws Exception {
        BukkitUser user = new BukkitUser(this, key);

        Set<UserHandlerLoader<?>> required = new HashSet<>();
        for (Class<? extends UserHandler> handlerClazz : handlerClasses) {
            UserHandlerLoader<?> handlerRegistry = this.handlers.stream().filter(r -> r.isInstance(handlerClazz)).findFirst().orElse(null);
            if (handlerRegistry == null) {
                throw new UnsupportedOperationException("Unsupported handler class: " + handlerClazz.getName() + "! ");
            }
            required.add(handlerRegistry);
        }

        for (UserHandlerLoader<?> registry : required) {
            user.loadHandler(registry);
        }

        user.fullyLoaded = true;
        return user;
    }

    @Override
    public void unload(@NotNull UUID userUUID) {
        BukkitUser user = getUsersMap().remove(userUUID);
        if (user == null) return;

        user.handlers.forEach((registry, handler) -> {
            registry.castSave(handler).join();
            user.handlers.remove(registry);
        });

        user.handlers.clear();
    }

    public void remove(@NotNull UUID userUUID) {
        BukkitUser user = getUsersMap().remove(userUUID);
        if (user == null) return;
        user.handlers.clear();
    }

    protected void modifyAllUsers(@NotNull ThrowingConsumer<BukkitUser, Exception> actions) {
        getUsersMap().values().forEach(value -> modifyUser(value, actions));
    }

    protected void modifyUser(@NotNull BukkitUser user, @NotNull ThrowingConsumer<BukkitUser, Exception> actions) {
        try {
            actions.accept(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void unloadPluginHandler(@NotNull Plugin plugin) {
        getLogger().info("Unregistering all handlers from #" + plugin.getName() + " ...");
        Set<UserHandlerLoader<?>> requireRemoved = new HashSet<>();
        this.handlers.stream().filter(handler -> handler.getPlugin().getName().equals(plugin.getName())).forEach(requireRemoved::add);
        requireRemoved.forEach(loader -> unregisterHandler(loader.getHandlerClass()));
    }


}

package com.artformgames.core.function.settings;

import cc.carm.lib.easysql.api.SQLQuery;
import com.artformgames.core.data.DataTables;
import com.artformgames.core.user.User;
import com.artformgames.core.user.UserKey;
import com.artformgames.core.user.handler.UserHandlerLoader;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.*;

public class UserSettingsLoader extends UserHandlerLoader<UserSettingsHolder> {

    public UserSettingsLoader(@NotNull Plugin plugin) {
        super(plugin, UserSettingsHolder.class);
    }

    @Override
    public @NotNull UserSettingsHolder emptyData(@NotNull User user) {
        return new UserSettingsHolder(user, new HashMap<>());
    }

    @Override
    public @NotNull UserSettingsHolder loadData(User user) throws Exception {
        Map<@NotNull Integer, @NotNull SettingsHolder> values = new HashMap<>();
        try (SQLQuery query = DataTables.USER_SETTINGS.createQuery()
                .addCondition("uid", user.getID())
                .build().execute()) {
            ResultSet resultSet = query.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt("type");
                String value = resultSet.getString("value");
                if (value != null) values.put(id, SettingsHolder.of(value));
            }
        }
        return new UserSettingsHolder(user, values);
    }

    @Override
    public void saveData(UserKey user, UserSettingsHolder handler) throws Exception {
        long uid = user.id();

        // 先删除所有旧的设置
        DataTables.USER_SETTINGS.createDelete().addCondition("uid", uid).build().execute(null);

        // 准备要更新到表中的配置数据
        List<Object[]> params = new ArrayList<>();
        handler.values.forEach((type, holder) -> Optional.ofNullable(holder.getData()).ifPresent(data -> params.add(new Object[]{uid, type, data})));

        // 再插入新的设置
        if (!params.isEmpty()) {
            DataTables.USER_SETTINGS.createReplaceBatch().setColumnNames("uid", "type", "value").setAllParams(params).execute(null);
        }
    }


}

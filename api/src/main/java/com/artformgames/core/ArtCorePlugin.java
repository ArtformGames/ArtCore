package com.artformgames.core;

import cc.carm.lib.easysql.api.SQLManager;
import com.artformgames.core.user.manager.UserManager;
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import org.jetbrains.annotations.NotNull;

interface ArtCorePlugin {

    @NotNull SQLManager getSQLManager();

    @NotNull UserManager<?> getUserManager();

    @NotNull BungeeChannelApi getBungeeAPI();


}

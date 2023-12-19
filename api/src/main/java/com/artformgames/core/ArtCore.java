package com.artformgames.core;

import cc.carm.lib.easysql.api.SQLManager;
import com.artformgames.core.user.manager.UserManager;
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ArtCore {

    private static ArtCorePlugin instance;

    private ArtCore() {
        throw new IllegalStateException("API class");
    }

    private static ArtCorePlugin plugin() {
        if (instance != null) return instance;

        Plugin plugin = Bukkit.getPluginManager().getPlugin("ArtCore");
        if (!(plugin instanceof ArtCorePlugin core)) {
            throw new IllegalStateException("ArtCore is not installed");
        }
        instance = core;
        return core;
    }

    public static SQLManager getSQLManager() {
        return plugin().getSQLManager();
    }

    public static UserManager<?> getUserManager() {
        return plugin().getUserManager();
    }

    public static BungeeChannelApi getBungeeAPI() {
        return plugin().getBungeeAPI();
    }


}

package com.artformgames.core;

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

    public static UserManager<?> getUserManager() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public static BungeeChannelApi getBungeeAPI() {
        throw new UnsupportedOperationException("Not implemented");
    }


}

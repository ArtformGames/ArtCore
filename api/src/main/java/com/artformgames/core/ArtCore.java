package com.artformgames.core;

import cc.carm.lib.easysql.api.SQLManager;
import com.artformgames.core.command.handlers.CommandUsageHandler;
import com.artformgames.core.command.handlers.PermMissedHandler;
import com.artformgames.core.user.User;
import com.artformgames.core.user.handler.UserHandler;
import com.artformgames.core.user.manager.UserManager;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.LiteBukkitSettings;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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

    public static LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> createCommand() {
        return LiteCommandsBukkit.builder().missingPermission(new PermMissedHandler()).invalidUsage(new CommandUsageHandler());
    }

    public static SQLManager getSQLManager() {
        return plugin().getSQLManager();
    }

    public static UserManager<?> getUserManager() {
        return plugin().getUserManager();
    }

    public static User getUser(@NotNull Player player) {
        return getUserManager().get(player);
    }

    public static <T extends UserHandler> @NotNull T getHandler(@NotNull Player player,
                                                                @NotNull Class<T> handler) {
        return getUser(player).getHandler(handler);
    }

    public static BungeeChannelApi getBungeeAPI() {
        return plugin().getBungeeAPI();
    }


}

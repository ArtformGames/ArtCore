package com.artformgames.core.listener;

import com.artformgames.core.Main;
import com.artformgames.core.event.UserLoadedEvent;
import com.artformgames.core.event.UserUnloadedEvent;
import com.artformgames.core.user.BukkitUser;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        try {
            BukkitUser user = Main.getInstance().getUserManager().load(event.getUniqueId(), event.getName());
            Bukkit.getPluginManager().callEvent(new UserLoadedEvent(user, event));
        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Failed to load user data. Please try again later.");
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLoginMonitor(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            Main.getInstance().getUserManager().remove(event.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        BukkitUser user = Main.getInstance().getUserManager().get(e.getPlayer().getUniqueId());
        if (user == null) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("Failed to load user data. Please try again later.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Main.getInstance().getScheduler().runAsync(() -> {
            Main.getInstance().getUserManager().unload(uuid);
            Bukkit.getPluginManager().callEvent(new UserUnloadedEvent(uuid));
        });

    }


}

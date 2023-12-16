package com.artformgames.core.listener;

import com.artformgames.core.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PluginListener implements Listener {

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        Main.getInstance().getUserManager().unloadPluginHandler(event.getPlugin());
    }

}

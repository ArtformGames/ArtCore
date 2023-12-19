package com.artformgames.core;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import com.artformgames.core.conf.PluginConfig;
import com.artformgames.core.conf.PluginMessages;
import com.artformgames.core.data.DataManager;
import com.artformgames.core.function.settings.UserSettingsLoader;
import com.artformgames.core.listener.PluginListener;
import com.artformgames.core.listener.UserListener;
import com.artformgames.core.user.BukkitUserManager;
import com.artformgames.core.utils.GHUpdateChecker;
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class Main extends EasyPlugin implements ArtCorePlugin {

    private static Main instance;

    public Main() {
        Main.instance = this;
    }

    protected MineConfiguration configuration;
    protected DataManager dataManager;
    protected BukkitUserManager usersManager;
    protected BungeeChannelApi bungeeChannel;

    @Override
    protected void load() {

        log("Loading plugin configurations...");
        configuration = new MineConfiguration(this);
        configuration.initializeConfig(PluginConfig.class);
        configuration.initializeMessage(PluginMessages.class);


        log("Loading database...");
        this.dataManager = new DataManager(getLogger());
        if (!dataManager.initialize()) {
            dataManager.shutdown();
            setEnabled(false);
            return;
        }

        log("Initializing users manager...");
        this.usersManager = new BukkitUserManager(getLogger());
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            this.usersManager.loadAll();
        }


        log("Initializing channels...");
        this.bungeeChannel = new BungeeChannelApi(this);

    }

    @Override
    protected boolean initialize() {

        log("Registering listeners...");
        GUI.initialize(this);
        registerListener(new UserListener());
        registerListener(new PluginListener());

        log("Registering commands...");


        log("Enabling user settings data...");
        this.usersManager.registerHandler(new UserSettingsLoader(this));


        if (PluginConfig.METRICS.getNotNull()) {
            log("Initializing bStats...");
            new Metrics(this, 20501);
        }

        if (PluginConfig.CHECK_UPDATE.getNotNull()) {
            log("Start to check the plugin versions...");
            getScheduler().runAsync(GHUpdateChecker.runner(this));
        } else {
            log("Version checker is disabled, skipped.");
        }

        return true;
    }

    @Override
    protected void shutdown() {

        log("Close channels...");
        this.bungeeChannel.unregister();

        log("Shutting down UserManager...");
        try {
            this.usersManager.unloadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log("Shutting down DataManager...");
        this.dataManager.shutdown();

    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public static void info(String... messages) {
        getInstance().log(messages);
    }

    public static void severe(String... messages) {
        getInstance().error(messages);
    }

    public static void debugging(String... messages) {
        getInstance().debug(messages);
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public @NotNull SQLManager getSQLManager() {
        return this.dataManager.getSQLManager();
    }

    @Override
    public @NotNull BukkitUserManager getUserManager() {
        return this.usersManager;
    }

    @Override
    public @NotNull BungeeChannelApi getBungeeAPI() {
        return this.bungeeChannel;
    }

}

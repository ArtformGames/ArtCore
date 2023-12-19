package com.artformgames.core.data;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import com.artformgames.core.Main;

import java.util.logging.Logger;


public class DataManager {


    private final Logger logger;
    private SQLManager sqlManager;

    public DataManager(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean initialize() {
        try {
            getLogger().info("	Connecting to database...");
            this.sqlManager = EasySQL.createManager(
                    DBConfiguration.DRIVER_NAME.getNotNull(), DBConfiguration.buildJDBC(),
                    DBConfiguration.USERNAME.getNotNull(), DBConfiguration.PASSWORD.getNotNull()
            );
            this.sqlManager.setDebugMode(() -> Main.getInstance().isDebugging());
        } catch (Exception exception) {
            getLogger().severe("Error connecting to database, please check the configuration.");
            exception.printStackTrace();
            return false;
        }

        getLogger().info("	Initializing tables...");
        DataTables.initializeTables(this.sqlManager);

        return true;
    }

    public void shutdown() {
        EasySQL.shutdownManager(getSQLManager());
        this.sqlManager = null;
    }

    public SQLManager getSQLManager() {
        return sqlManager;
    }


}

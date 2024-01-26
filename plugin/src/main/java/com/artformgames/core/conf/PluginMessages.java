package com.artformgames.core.conf;

import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.core.annotation.ConfigPath;

public interface PluginMessages extends Configuration {

    @ConfigPath(root = true)
    Class<?> GENERAL = GeneralMessages.class;

}

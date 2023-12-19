package com.artformgames.core.conf;

import cc.carm.lib.configuration.core.annotation.ConfigPath;

public class PluginMessages extends MessagesRoot {

    @ConfigPath(root = true)
    public static final Class<?> GENERAL = GeneralMessages.class;

}

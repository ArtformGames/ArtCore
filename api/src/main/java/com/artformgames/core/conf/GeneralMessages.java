package com.artformgames.core.conf;

import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import net.md_5.bungee.api.chat.BaseComponent;

public class GeneralMessages extends MessagesRoot {

    public static final ConfiguredMessageList<BaseComponent[]> UNKNOWN_COMMAND = list()
            .defaults(
                    "&c&lUnknown command! &fPlease check your inputs.",
                    "&8# &fMaybe you want to use &e%(command) &f?"
            ).params("command").build();

    public static final ConfiguredMessageList<BaseComponent[]> UNKNOWN_COMMANDS = list()
            .defaults(
                    "&c&lUnknown command! &fPossible commands:"
            ).build();


    public static final ConfiguredMessageList<BaseComponent[]> NO_PERMISSIONS = list()
            .defaults("&c&lSorry! &fYou don't have permission to do this.")
            .build();

    public static final ConfiguredMessageList<BaseComponent[]> MISSING_PERMISSION = list()
            .defaults("&7Missing permissions: ")
            .build();

}

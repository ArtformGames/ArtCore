package com.artformgames.core.conf;

import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import net.md_5.bungee.api.chat.BaseComponent;

public interface GeneralMessages extends Messages {

    ConfiguredMessageList<BaseComponent[]> UNKNOWN_COMMAND = Messages.list()
            .defaults(
                    "&c&lUnknown command! &fPlease check your inputs.",
                    "&8# &fMaybe you want to use &e%(command) &f?"
            ).params("command").build();

    ConfiguredMessageList<BaseComponent[]> UNKNOWN_COMMANDS = Messages.list()
            .defaults(
                    "&c&lUnknown command! &fPossible commands:"
            ).build();


    ConfiguredMessageList<BaseComponent[]> NO_PERMISSIONS = Messages.list()
            .defaults("&c&lSorry! &fYou don't have permission to do this.")
            .build();

    ConfiguredMessageList<BaseComponent[]> MISSING_PERMISSION = Messages.list()
            .defaults("&7Missing permissions: ")
            .build();

}

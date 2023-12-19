package com.artformgames.core.command.handlers;

import com.artformgames.core.conf.GeneralMessages;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

public class CommandUsageHandler implements InvalidUsageHandler<CommandSender> {


    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        Schematic schematic = result.getSchematic();

        if (schematic.isOnlyFirst()) {
            GeneralMessages.UNKNOWN_COMMAND.send(sender, schematic.first());
            return;
        }

        GeneralMessages.UNKNOWN_COMMANDS.send(sender);
        schematic.all().forEach(scheme -> sender.sendMessage("§8 # §7" + scheme));
    }

}
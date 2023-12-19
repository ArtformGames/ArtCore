package com.artformgames.core.command.handlers;

import com.artformgames.core.conf.GeneralMessages;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class PermMissedHandler implements MissingPermissionsHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation,
                       MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> resultHandlerChain) {
        CommandSender sender = invocation.sender();

        GeneralMessages.NO_PERMISSIONS.send(sender);

        if (sender.hasPermission("group.admin")) {
            int i = 1;
            for (String permission : missingPermissions.getPermissions()) {
                if (!sender.hasPermission(permission)) {
                    GeneralMessages.MISSING_PERMISSION.send(sender);
                    sender.sendMessage("§8 # §7" + i + ". " + permission);
                    i++;
                }
            }
        }

    }


}

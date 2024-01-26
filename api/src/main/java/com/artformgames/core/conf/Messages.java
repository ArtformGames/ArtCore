package com.artformgames.core.conf;

import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageListBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.title.TitleConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredTitle;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface Messages extends Configuration {

    static @NotNull CraftMessageListBuilder<BaseComponent[]> list() {
        return ConfiguredMessageList.create(getParser())
                .whenSend((sender, message) -> message.forEach(m -> sender.spigot().sendMessage(m)));
    }

    static @NotNull CraftMessageValueBuilder<BaseComponent[]> value() {
        return ConfiguredMessage.create(getParser())
                .whenSend((sender, message) -> sender.spigot().sendMessage(message));
    }

    static @NotNull TitleConfigBuilder title() {
        return ConfiguredTitle.create();
    }

    private static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return (sender, message) -> {
            if (sender == null) return MineDown.parse(message);
            if (sender instanceof Player player) {
                return MineDown.parse(PlaceholderAPI.setPlaceholders(player, message));
            } else {
                return MineDown.parse(message);
            }
        };
    }


}

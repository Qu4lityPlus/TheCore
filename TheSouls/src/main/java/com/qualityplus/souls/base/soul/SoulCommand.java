package com.qualityplus.souls.base.soul;

import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Data @EqualsAndHashCode(callSuper = true)
public final class SoulCommand extends OkaeriConfig {
    private final CommandExecutor executor;
    private final String command;

    public void execute(Player player) {
        String toExecute = command.replaceAll("%player%", player.getName());
        if (executor.equals(CommandExecutor.PLAYER)) {
            player.performCommand(toExecute);
        } else
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), toExecute);
    }

    public enum CommandExecutor{
        PLAYER,
        CONSOLE
    }
}

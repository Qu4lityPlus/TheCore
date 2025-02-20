package com.qualityplus.dragon.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.exception.check.NoSpawnException;
import com.qualityplus.dragon.api.exception.check.NoStructureException;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Component
public final class StartGameCommand extends AssistantCommand {
    private @Inject Logger logger;
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Player player = (Player) sender;

            if (!gameCanStart(player)) return false;

            box.game().start();
        } else
            sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));
        return true;
    }

    private boolean gameCanStart(Player player) {
        try {
            box.game().canStart();

            return box.game().canStart();
        } catch (NoSpawnException e) {
            logger.warning("Game can't start Spawn is not set!");
            player.sendMessage(StringUtils.color(box.files().messages().gameMessages.gameCantStartSpawn
                    .replace("%prefix%", box.files().config().prefix)));
            return false;
        } catch (NoStructureException e) {
            logger.warning("Game can't start Schematic Structure is not set!");
            player.sendMessage(StringUtils.color(box.files().messages().gameMessages.gameCantStartStructure
                    .replace("%prefix%", box.files().config().prefix)));
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().startGameCommand));
    }
}

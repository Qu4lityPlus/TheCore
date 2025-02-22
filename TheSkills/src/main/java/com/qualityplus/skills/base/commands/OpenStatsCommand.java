package com.qualityplus.skills.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.gui.main.MainGUI;
import com.qualityplus.skills.gui.stats.StatsAndPerksGUI;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@Component
public final class OpenStatsCommand extends AssistantCommand {
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String syntaxMsg = StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax));
        String mustBeAPlayer = StringUtils.color(box.files().messages().pluginMessages.mustBeAPlayer.replace("%usage%", syntax));
        String invalidPlayer = StringUtils.color(box.files().messages().pluginMessages.invalidPlayer.replace("%usage%", syntax));

        Player player = (Player) sender;

        return openInventory(args, sender, new StatsAndPerksGUI(box, player, 1, StatsAndPerksGUI.GUIType.STAT), syntaxMsg, mustBeAPlayer, invalidPlayer);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().statsCommand));
    }
}

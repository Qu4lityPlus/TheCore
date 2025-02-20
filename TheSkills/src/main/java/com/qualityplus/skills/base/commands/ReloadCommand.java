package com.qualityplus.skills.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.api.provider.MinionsProvider;
import com.qualityplus.skills.base.skill.registry.Skills;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@Component
public final class ReloadCommand extends AssistantCommand {
    private @Inject MinionsProvider minionsProvider;
    private @Inject TheSkills theSkills;
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.successfullyReloaded));

            box.files().reloadFiles();

            box.skillFiles().reloadFiles();

            Skills.reloadSkills(box, minionsProvider);

            TheAssistantPlugin.getAPI().getCommandProvider().reloadCommands();
        } else
            sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().reloadCommand));
    }
}

package com.qualityplus.skills.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.list.ListBuilder;
import com.qualityplus.assistant.util.list.ListUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.api.effect.CommonObject;
import com.qualityplus.skills.base.skill.registry.Skills;
import com.qualityplus.skills.base.stat.registry.Stats;
import com.qualityplus.skills.persistance.data.UserData;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class ResetCommand extends AssistantCommand {
    private static final String SUCCESSFULLY_RESET_MESSAGE = "&aYou successfully have reset stats for &2%player%&a!";
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 2) {

            final Player player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidPlayer));
                return false;
            }

            final Optional<UserData> data = box.service().getData(player.getUniqueId());

            data.ifPresent(UserData::resetData);

            final List<IPlaceholder> placeholders = new Placeholder("player", player.getName()).alone();

            final String message = Optional.ofNullable(box.files().messages().skillsMessages.successfullyResetStatsAndSkills)
                            .orElse(SUCCESSFULLY_RESET_MESSAGE);

            sender.sendMessage(StringUtils.processMulti(message, placeholders));

        } else
            sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 2 ? null : args.length == 3 ?
                new ListBuilder<String>()
                        .with(Stats.values().stream().map(CommonObject::getId).collect(Collectors.toList()))
                        .with(Skills.values().stream().map(CommonObject::getId).collect(Collectors.toList()))
                        .get()
                : args.length == 4 ? NumberUtil.stringSecuence(0, 10) : Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject final Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().addCommand));
    }
}

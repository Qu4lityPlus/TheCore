package com.qualityplus.minions.base.commands.giveitems;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.util.MinionEggUtil;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class GiveMinionCommand extends AssistantCommand {
    private static final String INVALID_LEVEL_FALLBACK_MSG = "&cInvalid level!";
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length != 3 && args.length != 4) {
            final String message = this.box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax);

            sender.sendMessage(StringUtils.color(message));
        } else {
            final Player toGive = Bukkit.getPlayer(args[1]);
            final Minion minion = Minions.getByID(args[2]);

            if (toGive == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidPlayer));
                return false;
            }

            if (minion == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().minionMessages.invalidMinion));
                return false;
            }

            int level = 1;
            if (args.length == 4) {
                final int tempLevel = NumberUtil.intOrZero(args[3]);

                if (tempLevel < 1) {
                    final Optional<String> msg = Optional.ofNullable(this.box.files().messages().pluginMessages.invalidLevel);
                    final String message = msg.orElse(INVALID_LEVEL_FALLBACK_MSG);
                    sender.sendMessage(StringUtils.color(message));
                    return false;
                }

                level = tempLevel;
            }

            final Optional<ItemStack> minionEgg = MinionEggUtil.createNewEgg(toGive, box.files().config().minionEggItem, minion, level);

            if (minionEgg.isEmpty()) {
                sender.sendMessage(StringUtils.color(box.files().messages().minionMessages.invalidEgg));
                return false;
            }

            toGive.getInventory().addItem(minionEgg.get());
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return switch (args.length) {
            case 2 -> null;
            case 3 -> Minions.values()
                    .stream()
                    .map(Minion::getId)
                    .collect(Collectors.toList());
            case 4 -> NumberUtil.stringSecuence(1, 10);
            default -> Collections.emptyList();
        };
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().giveMinionCommand));
    }

}
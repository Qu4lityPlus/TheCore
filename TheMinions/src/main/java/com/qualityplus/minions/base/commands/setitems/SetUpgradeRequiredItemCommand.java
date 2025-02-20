package com.qualityplus.minions.base.commands.setitems;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.base.config.upgrades.NormalUpgrades;
import com.qualityplus.minions.base.gui.changeitem.ChangeItem;
import com.qualityplus.minions.base.gui.changeitem.ChangeItemGUI;
import com.qualityplus.minions.base.gui.changeitem.ChangeItemRequest;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class SetUpgradeRequiredItemCommand extends AssistantCommand {
    private @Inject NormalUpgrades upgrades;
    private @Inject Box box;


    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length == 3) {

            Player player = (Player) sender;

            Minion minion = Minions.getByID(args[1]);

            MinionUpgrade upgrade = upgrades.getById(args[2]);

            if (upgrade == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().minionMessages.invalidUpgrade));
                return false;
            }

            if (minion == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().minionMessages.invalidMinion));
                return false;
            }

            ItemStack inHand = player.getItemInHand();

            if (BukkitItemUtil.isNull(inHand)) {
                sender.sendMessage(StringUtils.color(Optional.ofNullable(box.files().messages().minionMessages.invalidMinion)
                        .orElse("&cInvalid Item!")));
                return false;
            }

            player.openInventory(new ChangeItemGUI(box, ChangeItemRequest.builder()
                    .minion(minion)
                    .minionUpgrade(upgrade)
                    .changeItem(ChangeItem.UPGRADE_ITEM_REQUIRED_ITEM)
                    .build(), inHand.clone()).getInventory());

        } else {
            String message = box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax);

            sender.sendMessage(StringUtils.color(message));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 2 ? getMinions() : args.length == 3 ? getUpgrades() : Collections.emptyList();
    }

    private List<String> getUpgrades() {
        return new ArrayList<>(box.files().upgrades().normalUpgrades.keySet());
    }

    private List<String> getMinions() {
        return Minions.values()
                .stream()
                .map(Minion::getId)
                .collect(Collectors.toList());
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI()
                .getCommandProvider()
                .registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().setUpgradeRequiredItemCommand));
    }

}
package com.qualityplus.bank.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.bank.api.box.Box;
import com.qualityplus.bank.api.transaction.TransactionType;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI;
import com.qualityplus.bank.persistence.data.BankTransaction;
import com.qualityplus.bank.persistence.data.TransactionCaller;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

@Component
public final class DepositCommand extends AssistantCommand {
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 3) {
            final Player player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidPlayer));
                return false;
            }

            final Integer amount = NumberUtil.intOrNull(args[2]);

            if (amount == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidAmount));
                return false;
            }

            final BankTransaction transaction = new BankTransaction(amount, TransactionType.DEPOSIT, BankInterfaceGUI.GUIType.GENERAL, TransactionCaller.SERVER);

            this.box.service().handleTransaction(player, transaction, false, true, false);

            final List<IPlaceholder> placeholders = PlaceholderBuilder
                    .init("player", player.getName())
                    .with("amount", String.valueOf(amount))
                    .get();

            sender.sendMessage(StringUtils.processMulti("&aYou successfully deposited %amount% to %player%'s bank", placeholders));
        } else
            sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().depositBankCommand));
    }
}
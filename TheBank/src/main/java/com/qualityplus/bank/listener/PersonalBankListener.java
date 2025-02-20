package com.qualityplus.bank.listener;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.assistant.util.time.RemainingTime;
import com.qualityplus.assistant.util.time.TimeUtils;
import com.qualityplus.bank.api.box.Box;
import com.qualityplus.bank.base.config.Messages;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


@Component
public final class PersonalBankListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onInteract(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        ItemStack inHand = player.getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        if (isPersonalBank(inHand)) e.setCancelled(true);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;

        Player player = e.getPlayer();

        ItemStack inHand = player.getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        if (!isPersonalBank(inHand)) return;

        BankData data = box.service().getData(player.getUniqueId()).orElse(new BankData());

        RemainingTime time = TimeUtils.getRemainingTime(new Markable(box.files().config().personalBankDelay.getEffectiveTime(), data.getLastInterestTime()).remainingTime());

        if (time.isZero())
            player.openInventory(new BankInterfaceGUI(box, player, BankInterfaceGUI.GUIType.PERSONAL).getInventory());
        else
            player.sendMessage(StringUtils.color(getLastUsed(time)));

    }

    private String getLastUsed(RemainingTime time) {
        Messages.BankMessages messages = box.files().messages().bankMessages;

        return getParsedTime(time, messages.personalBankRemainingTime);
    }

    private String getParsedTime(RemainingTime remainingTime, String message) {
        Messages.BankMessages messages = box.files().messages().bankMessages;

        String days = messages.days;
        String hours = messages.hours;
        String minutes = messages.minutes;
        String seconds = messages.seconds;
        String noTime = messages.noTime;

        return TimeUtils.getParsedTime(remainingTime, message, days, hours, minutes, seconds, noTime, messages.showTimeIfZero);
    }

    private boolean isPersonalBank(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        return nbtItem.hasKey("personalBank");
    }
}

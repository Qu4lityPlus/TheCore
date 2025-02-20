package com.qualityplus.runes.base.gui.runetable.handler;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.session.RuneSession;
import com.qualityplus.runes.base.gui.ClickHandler;
import com.qualityplus.runes.base.gui.runetable.RuneTableGUI;
import com.qualityplus.runes.base.session.RuneSessionImpl;
import com.qualityplus.runes.util.RunesUtils;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@AllArgsConstructor
public final class ShiftClickHandler implements ClickHandler {
    private final Box box;
    private final RuneTableGUI gui;
    private final RuneSession session;

    @Override
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == gui.getConfig().getCombinedFilledItem().getSlot()) {
            if (BukkitItemUtil.isNull(session.getResult())) {
                event.setCancelled(true);
                return;
            }

            gui.setGiveItem(false);

            player.openInventory(new RuneTableGUI(box, new RuneSessionImpl(player.getUniqueId(), null, null, null, null)).getInventory());

            player.getInventory().addItem(session.getResult());

        } else {
            if (!BukkitItemUtil.isNull(session.getResult())) {
                player.sendMessage(StringUtils.color(box.files().messages().runeMessages.thereIsAnItemToPickup));
                event.setCancelled(true);
                return;
            }

            if (session.isFusing()) {
                event.setCancelled(true);
                return;
            }

            final ItemStack copy = Optional.ofNullable(event.getCursor()).map(ItemStack::clone).orElse(null);
            final ItemStack current = Optional.ofNullable(event.getCurrentItem()).map(ItemStack::clone).orElse(null);

            if (event.getSlot() == gui.getConfig().getToSacrificeSlot()) {
                if (BukkitItemUtil.isNull(copy) && !BukkitItemUtil.isNull(current)) {
                    gui.setGiveItem(false);
                    player.openInventory(new RuneTableGUI(box, new RuneSessionImpl(player.getUniqueId(), null, session.getItemToUpgrade(), null, null)).getInventory());
                    player.getInventory().addItem(current);
                }
            } else {
                if (BukkitItemUtil.isNull(copy) && !BukkitItemUtil.isNull(current)) {
                    gui.setGiveItem(false);
                    player.openInventory(new RuneTableGUI(box, new RuneSessionImpl(player.getUniqueId(), null, null, session.getItemToSacrifice(), RunesUtils.getRuneInstance(session.getItemToSacrifice()))).getInventory());
                    player.getInventory().addItem(current);
                }
            }
        }
    }

    @Override
    public void handleOutSide(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        final ItemStack current = Optional.ofNullable(event.getCurrentItem())
                .map(ItemStack::clone)
                .orElse(null);

        ShiftTarget target = getTarget();

        if (!BukkitItemUtil.isNull(session.getResult())) {
            player.sendMessage(StringUtils.color(box.files().messages().runeMessages.thereIsAnItemToPickup));
            event.setCancelled(true);
            return;
        }

        if (BukkitItemUtil.isNull(session.getItemToSacrifice()) && !BukkitItemUtil.isNull(current) && target.equals(ShiftTarget.SACRIFICE)) {

            gui.setGiveItem(false);

            event.setCurrentItem(null);

            player.openInventory(new RuneTableGUI(box, new RuneSessionImpl(player.getUniqueId(), null, session.getItemToUpgrade(), current, RunesUtils.getRuneInstance(current))).getInventory());
        }

        if (BukkitItemUtil.isNull(session.getItemToUpgrade()) && !BukkitItemUtil.isNull(current) && target.equals(ShiftTarget.UPGRADE)) {

            gui.setGiveItem(false);

            event.setCurrentItem(null);

            player.openInventory(new RuneTableGUI(box, new RuneSessionImpl(player.getUniqueId(), null, current, session.getItemToSacrifice(), RunesUtils.getRuneInstance(session.getItemToSacrifice()))).getInventory());
        }
    }

    private ShiftTarget getTarget() {
        boolean toUpgradeIsNull = BukkitItemUtil.isNull(session.getItemToUpgrade());

        return toUpgradeIsNull ? ShiftTarget.UPGRADE : ShiftTarget.SACRIFICE;

    }

    enum ShiftTarget{
        UPGRADE,
        SACRIFICE
    }
}

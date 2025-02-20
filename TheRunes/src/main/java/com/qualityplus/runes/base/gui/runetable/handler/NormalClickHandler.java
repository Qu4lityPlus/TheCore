package com.qualityplus.runes.base.gui.runetable.handler;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.session.RuneSession;
import com.qualityplus.runes.base.gui.ClickHandler;
import com.qualityplus.runes.base.gui.runetable.RuneTableGUI;
import com.qualityplus.runes.base.gui.runetable.effect.EffectHandler;
import com.qualityplus.runes.base.session.RuneSessionImpl;
import com.qualityplus.runes.util.RuneFinderUtil;
import com.qualityplus.runes.util.RunesUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@AllArgsConstructor
public final class NormalClickHandler implements ClickHandler {
    private final Box box;
    private final RuneTableGUI gui;
    private final RuneSession session;

    private final EffectHandler<RuneTableGUI> effectHandler;

    @Override
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == gui.getConfig().getClickToCombineRunesItem().getSlot() || event.getSlot() == gui.getConfig().getClickToCombineItemAndRuneItem().getSlot()) {
            event.setCancelled(true);

            if (!session.bothItemsAreSet()) return;

            if (!RuneFinderUtil.hasRequiredLevel(session, player)) {
                player.sendMessage(StringUtils.color(box.files().messages().runeMessages.youDontHaveEnoughRunecraftingLevel));
                return;
            }

            gui.setGiveItem(false);

            session.setFusing(true);

            effectHandler.handle(player, gui, event.getClickedInventory());


        } else if (event.getSlot() == gui.getConfig().getCombinedFilledItem().getSlot()) {
            if (BukkitItemUtil.isNull(session.getResult())) {
                event.setCancelled(true);
                return;
            }

            gui.setGiveItem(false);

            player.openInventory(new RuneTableGUI(box, new RuneSessionImpl(player.getUniqueId(), null, null, null, null)).getInventory());

            player.setItemOnCursor(session.getResult());

        } else {
            event.setCancelled(true);

            if (session.isFusing())
                return;


            if (!BukkitItemUtil.isNull(session.getResult())) {
                player.sendMessage(StringUtils.color(box.files().messages().runeMessages.thereIsAnItemToPickup));
                return;
            }

            final ItemStack copy = Optional.ofNullable(event.getCursor()).map(ItemStack::clone).orElse(null);
            final ItemStack current = Optional.ofNullable(event.getCurrentItem()).map(ItemStack::clone).orElse(null);

            if (BukkitItemUtil.isNull(copy)) {

                RuneSession newSession = event.getSlot() == gui.getConfig().getToSacrificeSlot() ?
                        new RuneSessionImpl(player.getUniqueId(), null, session.getItemToUpgrade(), null, null) :
                        new RuneSessionImpl(player.getUniqueId(), null, null, session.getItemToSacrifice(), RunesUtils.getRuneInstance(session.getItemToSacrifice()));

                //PickUp
                gui.setGiveItem(false);
                event.setCurrentItem(null);
                player.openInventory(new RuneTableGUI(box, newSession).getInventory());
                player.setItemOnCursor(current);
            } else {

                if (!BukkitItemUtil.isNull(current)) {
                    event.setCancelled(true);
                    return;
                }

                RuneSession newSession = event.getSlot() == gui.getConfig().getToSacrificeSlot() ?
                        new RuneSessionImpl(player.getUniqueId(), null, session.getItemToUpgrade(), copy, RunesUtils.getRuneInstance(copy)) :
                        new RuneSessionImpl(player.getUniqueId(), null, copy, session.getItemToSacrifice(), RunesUtils.getRuneInstance(session.getItemToSacrifice()));
                //Put
                gui.setGiveItem(false);

                player.setItemOnCursor(null);

                player.openInventory(new RuneTableGUI(box, newSession).getInventory());
            }
        }
    }

    @Override
    public void handleOutSide(InventoryClickEvent event) {

    }
}

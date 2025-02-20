package com.qualityplus.runes.base.gui.derune;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.session.RemoveSession;
import com.qualityplus.runes.api.session.RemoveSession.RemoveSessionResult;
import com.qualityplus.runes.base.gui.ClickHandler;
import com.qualityplus.runes.base.gui.RuneGUI;
import com.qualityplus.runes.base.gui.derune.handler.RMainClickHandler;
import com.qualityplus.runes.base.gui.derune.handler.RNormalClickHandler;
import com.qualityplus.runes.base.gui.derune.handler.RShiftClickHandler;
import com.qualityplus.runes.base.gui.runetable.RuneTableGUI;
import com.qualityplus.runes.base.session.RuneSessionImpl;
import com.qualityplus.runes.util.RuneFinderUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class RemoveRuneGUI extends RuneGUI {
    private @Getter final RemoveRuneGUIConfig config;

    private @Setter boolean giveItem = true;
    private final ClickHandler handler;
    private final RemoveSession session;

    public RemoveRuneGUI(Box box, RemoveSession session) {
        super(box.files().inventories().removeRuneGUIConfig, box);

        this.handler = new RMainClickHandler(
                new RNormalClickHandler(box, this, session),
                new RShiftClickHandler(box, this, session));
        this.config = box.files().inventories().removeRuneGUIConfig;
        this.session = session;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        RemoveSession.RemoveSessionResult answer = RuneFinderUtil.getAnswer(session);


        if (session.isSuccess()) {
            inventory.setItem(config.getToUpgradeSlot(), session.getItemToRemove());
            inventory.setItem(config.getSuccessItem().getSlot(), ItemStackUtils.makeItem(config.getSuccessItem()));
        } else {
            if (answer.equals(RemoveSessionResult.ITEM_SET)) {
                config.getToUpgradeFilledSlots().forEach(slot -> inventory.setItem(slot, ItemStackUtils.makeItem(config.getToUpgradeFilledItem())));
                inventory.setItem(config.getClickToRemoveRune().getSlot(), ItemStackUtils.makeItem(config.getClickToRemoveRune()));
            }

            if (!BukkitItemUtil.isNull(session.getItemToRemove()))
                inventory.setItem(config.getToUpgradeSlot(), ItemStackUtils.makeItem(config.getToRemoveItem(), PlaceholderBuilder
                        .create(new Placeholder("to_remove_rune_item_displayname", BukkitItemUtil.getName(session.getItemToRemove())),
                                new Placeholder("to_remove_rune_item_lore", BukkitItemUtil.getItemLore(session.getItemToRemove())))
                        .get(), session.getItemToRemove()));

        }

        if (answer.isError())
            setItem(config.getCombinedErrorItem());

        setItem(config.getCloseGUI());
        setItem(config.getGoBack());


        return inventory;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!giveItem) return;
        Optional.ofNullable(session.getItemToRemove()).ifPresent(itemStack -> player.getInventory().addItem(itemStack));

    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        int slot = e.getSlot();

        if (getTarget(e).equals(ClickTarget.INSIDE)) {

            if (isItem(slot, config.getCloseGUI())) {
                e.setCancelled(true);
                player.closeInventory();
            } else if (isItem(slot, config.getGoBack())) {
                player.openInventory(new RuneTableGUI(box, new RuneSessionImpl(player.getUniqueId(), null, null, null, null)).getInventory());
            } else if (slot == config.getToUpgradeSlot() || slot == config.getClickToRemoveRune().getSlot()) {
                handler.handle(e);
            } else {
                e.setCancelled(true);
            }
        } else {
            handler.handleOutSide(e);
        }


    }
}

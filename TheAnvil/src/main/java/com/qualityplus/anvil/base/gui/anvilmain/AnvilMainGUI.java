package com.qualityplus.anvil.base.gui.anvilmain;

import com.qualityplus.anvil.api.box.Box;
import com.qualityplus.anvil.api.session.AnvilSession;
import com.qualityplus.anvil.api.session.AnvilSession.SessionResult;
import com.qualityplus.anvil.base.config.Messages;
import com.qualityplus.anvil.base.gui.AnvilGUI;
import com.qualityplus.anvil.util.AnvilFinderUtil;
import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class AnvilMainGUI extends AnvilGUI {
    private @Getter final AnvilMainGUIConfig config;
    private @Setter boolean giveItem = true;
    private final AnvilSession session;

    public AnvilMainGUI(final Box box, final AnvilSession session) {
        super(box.files().inventories().enchantMainGUI, box);

        this.config = box.files().inventories().enchantMainGUI;
        this.session = session;
    }


    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        final int slot = event.getSlot();

        final ItemStack cursorNew = Optional.of(player.getItemOnCursor())
                .filter(BukkitItemUtil::isNotNull)
                .map(ItemStack::clone)
                .orElse(null);

        if (slot == config.getToSacrificeSlot()) {
            this.session.setItemToSacrifice(cursorNew);
        } else if (slot == config.getToUpgradeSlot()) {
            this.session.setItemToUpgrade(cursorNew);
        }

        updateInventory();

        if (BukkitItemUtil.isNotNull(this.session.getResult()) && slot != config.getCombinedFilledItem().getSlot()) {
            player.sendMessage(StringUtils.color(box.files().messages().anvilMessages.thereIsAnItemToPickup));
            event.setCancelled(true);
            return;
        }

        // Handle when
        if (slot == config.getCombinedFilledItem().getSlot()) {
            final ItemStack result = session.getResult();
            final ItemStack cursor = player.getItemOnCursor();

            event.setCancelled(true);

            if (BukkitItemUtil.isNull(result)) {
            } else if (BukkitItemUtil.isNotNull(cursor)) {
            } else {
                final ItemStack newItem = this.session.getResult().clone();
                this.session.setResult(null);
                Bukkit.getScheduler().runTask(box.plugin(), this::updateInventory);
                Bukkit.getScheduler().runTask(box.plugin(), () -> player.setItemOnCursor(newItem));
                return;
            }
        }

        // Handle when anvil to combine is clicked
        if (slot == config.getCombineFilledItem().getSlot()) {
            event.setCancelled(true);

            final AnvilSession.SessionResult sessionResult = session.getSessionResult();

            if (!sessionResult.equals(AnvilSession.SessionResult.BOTH_ITEMS_SET)) {
                return;
            }

            final Messages.AnvilMessages msgs = box.files().messages().anvilMessages;

            if (AnvilFinderUtil.dontHavePermission(session, player)) {
                player.sendMessage(StringUtils.color(msgs.youDontHaveEnoughPermissions));
                return;
            }

            if (AnvilFinderUtil.getMoneyCost(session) > TheAssistantPlugin.getAPI().getAddons().getEconomy().getMoney(player)) {
                player.sendMessage(StringUtils.color(msgs.youDontHaveEnoughMoney));
                return;
            }

            if (AnvilFinderUtil.getLevelsCost(session) > player.getLevel()) {
                player.sendMessage(StringUtils.color(msgs.youDontHaveEnoughLevels));
                return;
            }

            final ItemStack finalItem = AnvilFinderUtil.getFinalItem(this.session);
            this.session.setItemToUpgrade(null);
            this.session.setItemToSacrifice(null);
            this.inventory.setItem(this.config.getToSacrificeSlot(), null);
            this.inventory.setItem(this.config.getToUpgradeSlot(), null);

            this.session.setResult(finalItem);
            this.updateInventory();
            return;
        }

        if (slot == config.getCloseGUI().getSlot()) {
            event.setCancelled(true);
            player.closeInventory();
            return;
        }

        final Map<Integer, Item> items = config.getBackground().getItems();
        if (items != null && items.containsKey(slot) && !getTarget(event).equals(ClickTarget.PLAYER)) {
            event.setCancelled(true);
            return;
        }

        if (event.isShiftClick() && getTarget(event).equals(ClickTarget.PLAYER)) {
            Bukkit.getScheduler().runTask(box.plugin(), () -> {
                this.session.setItemToSacrifice(inventory.getItem(config.getToSacrificeSlot()));
                this.session.setItemToUpgrade(inventory.getItem(config.getToUpgradeSlot()));
                updateInventory();
            });
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        updateInventory();

        return this.inventory;
    }

    private void updateInventory() {
        InventoryUtils.fillInventory(this.inventory, this.config.getBackground());

        final SessionResult answer = AnvilFinderUtil.getAnswer(this.session);

        //Izquierda
        if (answer.equals(SessionResult.BOTH_ITEMS_SET) || answer.equals(SessionResult.ONLY_ITEM_TO_UPGRADE)) {
            this.config.getToUpgradeFilledSlots().forEach(slot -> this.inventory.setItem(slot, ItemStackUtils.makeItem(this.config.getToUpgradeFilledItem())));
        }

        //Derecha
        if (answer.equals(SessionResult.BOTH_ITEMS_SET) || answer.equals(SessionResult.ONLY_ITEM_TO_SACRIFICE)) {
            this.config.getToSacrificeFilledSlots().forEach(slot -> this.inventory.setItem(slot, ItemStackUtils.makeItem(this.config.getToSacrificeFilledItem())));
        }

        //Items de abajo
        if (answer.equals(SessionResult.BOTH_ITEMS_SET)) {
            final ItemStack newItem = AnvilFinderUtil.getFinalItem(this.session);
            //Final Item
            final ItemStack newItemInInv = ItemStackUtils.makeItem(this.config.getCombinedFilledItem(), getPlaceholders(newItem), newItem, false, false);

            this.inventory.setItem(this.config.getCombinedFilledItem().getSlot(), newItemInInv);

            this.config.getReadyToCombineSlots().forEach(slot -> this.inventory.setItem(slot, ItemStackUtils.makeItem(config.getReadyToCombineItem())));
            //Anvil
            setItem(this.config.getCombineFilledItem(), Arrays.asList(
                    new Placeholder("anvil_enchant_exp_cost", getParsed(this.box.files().messages().anvilPlaceholders.experienceCost)),
                    new Placeholder("anvil_enchant_money_cost", getParsed(this.box.files().messages().anvilPlaceholders.moneyCost))
            ));
        }

        if (!BukkitItemUtil.isNull(this.session.getResult())) {
            this.inventory.setItem(this.config.getCombinedFilledItem().getSlot(), this.session.getResult());
        }

        if (answer.isError()) {
            setItem(this.config.getCombinedErrorItem(), Collections.singletonList(new Placeholder("anvil_error", getErrorPlaceholder(answer))));
        }

        setItem(this.config.getCloseGUI());
    }


    private String getParsed(String msg) {
        List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                        new Placeholder("enchanting_enchant_exp_cost_amount", AnvilFinderUtil.getLevelsCost(session)),
                        new Placeholder("enchanting_enchant_money_cost_amount", AnvilFinderUtil.getMoneyCost(session))
        ).get();

        return StringUtils.processMulti(msg, placeholders);
    }

    private List<IPlaceholder> getPlaceholders(ItemStack itemStack) {
        return Arrays.asList(
                new Placeholder("anvil_result_item_displayname", BukkitItemUtil.getName(itemStack)),
                new Placeholder("anvil_result_item_lore", BukkitItemUtil.getItemLore(itemStack))
        );
    }

    private List<String> getErrorPlaceholder(SessionResult answer) {
        return answer.equals(SessionResult.ERROR_CANNOT_COMBINE_THOSE_ITEMS) ? box.files().messages().anvilPlaceholders.youCannotCombineThoseItems : box.files().messages().anvilPlaceholders.youCannotAddThatEnchantment;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!giveItem) return;

        Optional.ofNullable(session.getItemToSacrifice()).ifPresent(itemStack -> player.getInventory().addItem(itemStack));
        Optional.ofNullable(session.getItemToUpgrade()).ifPresent(itemStack -> player.getInventory().addItem(itemStack));
        Optional.ofNullable(session.getResult()).ifPresent(itemStack -> player.getInventory().addItem(itemStack));

    }
}

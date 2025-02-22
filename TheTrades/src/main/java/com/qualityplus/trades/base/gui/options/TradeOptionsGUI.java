package com.qualityplus.trades.base.gui.options;

import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.trades.api.box.Box;
import com.qualityplus.trades.api.session.TradeSession;
import com.qualityplus.trades.base.gui.TradeGUI;
import com.qualityplus.trades.base.gui.trades.TradesGUI;
import com.qualityplus.trades.base.trades.PluginTrade;
import com.qualityplus.trades.util.TradePlaceholderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TradeOptionsGUI extends TradeGUI {
    private final Map<Integer, Integer> slotsAndAmountMap = new HashMap<>();
    private final TradeOptionsGUIConfig config;
    private final PluginTrade trade;
    private final UUID uuid;

    public TradeOptionsGUI(Box box, PluginTrade trade, UUID uuid) {
        super(box.files().inventories().tradeOptionsGUIConfig, box);

        this.config = box.files().inventories().tradeOptionsGUIConfig;
        this.trade = trade;
        this.uuid = uuid;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        for (Map.Entry<Integer, Item> entry : config.getItemsAndAmountToTrade().entrySet()) {

            inventory.setItem(entry.getValue().getSlot(), ItemStackUtils.makeItem(
                    entry.getValue(),
                    TradePlaceholderUtils.getRecipePlaceholders(box, trade, entry.getKey()).get(),
                    trade.getResult(),
                    false
            ));

            slotsAndAmountMap.put(entry.getValue().getSlot(), entry.getKey());
        }

        setItem(config.getCloseGUI());

        setItem(config.getGoBack());

        return inventory;
    }



    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new TradesGUI(box, uuid, 1).getInventory());
        } else if (slotsAndAmountMap.containsKey(slot)) {
            Integer amount = slotsAndAmountMap.getOrDefault(slot, null);

            if (amount == null) return;

            if (!trade.hasPermission(player)) {
                player.sendMessage(StringUtils.color(box.files().messages().tradeMessages.noPermission));
                return;
            }

            TradeSession session = box.tradesService().newSession(trade, amount);

            box.tradesService().checkout(player, session);
        }
    }
}

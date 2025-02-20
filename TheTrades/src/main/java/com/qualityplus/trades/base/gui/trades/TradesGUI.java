package com.qualityplus.trades.base.gui.trades;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.trades.api.box.Box;
import com.qualityplus.trades.api.recipes.Trades;
import com.qualityplus.trades.api.session.TradeSession;
import com.qualityplus.trades.base.gui.TradeGUI;
import com.qualityplus.trades.base.gui.options.TradeOptionsGUI;
import com.qualityplus.trades.base.trades.PluginTrade;
import com.qualityplus.trades.util.TradePlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class TradesGUI extends TradeGUI {
    private final Map<Integer, PluginTrade> tradeMap = new HashMap<>();
    private final TradesGUIConfig config;
    private final UUID uuid;

    public TradesGUI(Box box, UUID uuid, int page) {
        super(box.files().inventories().tradesGUIConfig, box);

        this.hasNext = Trades.values(trade -> trade.getPage() > page).stream().findAny().isPresent();
        this.config = box.files().inventories().tradesGUIConfig;
        this.page = page;
        this.uuid = uuid;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        setItem(config.getCloseGUI());

        Player player = Bukkit.getPlayer(uuid);

        if (page > 1)
            setItem(config.getPreviousPage(), Collections.singletonList(new Placeholder("previous_page", page - 1)));

        if (hasNext)
            setItem(config.getNextPage(), Collections.singletonList(new Placeholder("next_page", page + 1)));

        for (PluginTrade trade : Trades.values(trade -> trade.getPage() == page)) {
            boolean locked = !trade.hasPermission(player);

            List<IPlaceholder> placeholders = TradePlaceholderUtils.getRecipePlaceholders(box, trade, 0).get();

            Item item = locked ? config.getLockedItem() : config.getUnlockedItem();

            boolean showItem = locked && config.isShowIconInLockedItem() || !locked && config.isShowIconInUnlockedItem();

            if (showItem)
                inventory.setItem(trade.getSlot(), ItemStackUtils.makeItem(item, placeholders, trade.getResult()));
            else
                inventory.setItem(trade.getSlot(), ItemStackUtils.makeItem(item, placeholders));

            tradeMap.put(trade.getSlot(), trade);
        }

        setItem(config.getGoBack());

        Optional.ofNullable(config.getCustomGoBackItem()).ifPresent(this::setItem);

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        int slot = e.getSlot();

        e.setCancelled(true);

        if (!getTarget(e).equals(ClickTarget.INSIDE)) return;

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (tradeMap.containsKey(slot)) {
            PluginTrade trade = tradeMap.getOrDefault(slot, null);

            if (trade == null) return;

            if (e.isLeftClick()) {
                if (!trade.hasPermission(player)) {
                    player.sendMessage(StringUtils.color(box.files().messages().tradeMessages.noPermission));
                    return;
                }

                TradeSession session = box.tradesService().newSession(trade, 0);

                box.tradesService().checkout(player, session);
            } else
                player.openInventory(new TradeOptionsGUI(box, trade, uuid).getInventory());

        } else if (isItem(slot, config.getNextPage()) && hasNext) {
            player.openInventory(new TradesGUI(box, uuid, page + 1).getInventory());
        } else if (isItem(slot, config.getPreviousPage()) && page > 1) {
            player.openInventory(new TradesGUI(box, uuid, page - 1).getInventory());
        } else if (isItem(slot, config.getGoBack())) {
            Optional.ofNullable(config.getGoBack().getCommand()).ifPresent(player::performCommand);
        } else if (isItem(slot, config.getCustomGoBackItem())) {
            handleItemCommandClick(player, config.getCustomGoBackItem());
        }
    }
}

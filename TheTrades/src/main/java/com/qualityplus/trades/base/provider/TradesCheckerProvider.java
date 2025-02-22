package com.qualityplus.trades.base.provider;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.trades.api.provider.CheckerProvider;
import com.qualityplus.trades.api.session.TradeSession;
import com.qualityplus.trades.base.config.Messages;
import com.qualityplus.trades.base.trades.PluginTrade;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Component
public final class TradesCheckerProvider implements CheckerProvider<TradeSession> {
    private @Inject Messages messages;

    @Override
    public void checkout(Player player, TradeSession session) {

        PluginTrade trade = session.getTrade();

        if (!trade.hasPermission(player)) {
            player.sendMessage(StringUtils.color(messages.tradeMessages.noPermission));
            return;
        }

        double playerMoney = TheAssistantPlugin.getAPI().getAddons().getEconomy().getMoney(player);

        double price = trade.getPrice() * session.getAmount();

        if (playerMoney < price) {
            player.sendMessage(StringUtils.color(messages.tradeMessages.noMoney));
            return;
        }

        int resultAmount = trade.getResult().getAmount();

        Map<XMaterial, Integer> tradeCost = new HashMap<>();

        for (Map.Entry<XMaterial, Integer> itemCost : trade.getItemCost().entrySet()) {
            int toMultiply = itemCost.getValue() / resultAmount;

            int amount = session.getAmount() == 0 ? itemCost.getValue() : session.getAmount() * Math.max(toMultiply, 1);

            int playerAmount = InventoryUtils.getItemQuantity(player.getInventory().getContents(), itemCost.getKey().parseItem());

            if (amount > playerAmount) {
                player.sendMessage(StringUtils.color(messages.tradeMessages.noItems));
                return;
            }

            tradeCost.put(itemCost.getKey(), amount);
        }

        tradeCost.forEach((mat, amount) -> InventoryUtils.removeItems(player.getInventory(), mat.parseItem(), amount));

        int toGive = session.getAmount() == 0 ? trade.getResult().getAmount() : session.getAmount();

        InventoryUtils.addItems(player.getInventory(), trade.getResult(), toGive);

        int priceMultiplier = session.getAmount() == 0 ? 1 : session.getAmount();

        TheAssistantPlugin.getAPI().getAddons().getEconomy().withdrawMoney(player, trade.getPrice() * priceMultiplier);

        player.sendMessage(StringUtils.color(messages.tradeMessages.youBought
                .replace("%trade_result_item_name%", BukkitItemUtil.getMaterialName(trade.getResult()))
                .replace("%trade_amount%", String.valueOf(toGive))
        ));

    }
}

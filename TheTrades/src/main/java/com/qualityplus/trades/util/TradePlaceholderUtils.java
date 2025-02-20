package com.qualityplus.trades.util;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.trades.api.box.Box;
import com.qualityplus.trades.base.config.Messages;
import com.qualityplus.trades.base.trades.PluginTrade;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class TradePlaceholderUtils {
    public PlaceholderBuilder getRecipePlaceholders(Box box, PluginTrade trade, int multiplier) {
        return PlaceholderBuilder.create(
                new Placeholder("trade_id", trade.getDisplayName()),
                new Placeholder("trade_description", trade.getDescription()),
                new Placeholder("trade_price", trade.getPrice()),
                new Placeholder("trade_slot", trade.getSlot()),
                new Placeholder("trade_page", trade.getPage()),
                getResultFormat(box, trade, multiplier),
                getFormattedCost(box, trade, multiplier)
        );
    }

    private IPlaceholder getResultFormat(Box box, PluginTrade trade, int multiplier) {
        Messages.TradeMessages msg = box.files().messages().tradeMessages;

        int amount = multiplier <= 1 ? trade.getResult().getAmount() : multiplier;

        String placeholder = amount > 1 ? msg.itemResultAmountFormat : msg.itemResultAmountOneFormat;

        return new Placeholder("trade_result_item_amount", placeholder.replaceAll("%amount%", String.valueOf(amount)));
    }

    private IPlaceholder getFormattedCost(Box box, PluginTrade trade, int multiplier) {

        List<String> finalList = new ArrayList<>();

        for (String line : box.files().messages().tradeMessages.costFormat) {

            if (line.contains("%trade_cost_list%")) {
                if (trade.getPrice() > 0) {
                    int toMultiply = multiplier == 0 ? 1 : multiplier;

                    finalList.add(box.files().messages().tradeMessages.moneyCostFormat.replace("%trade_money_price%", MathUtil.round(trade.getPrice() * toMultiply)));
                }


                int resultAmount = trade.getResult().getAmount();

                for (Map.Entry<XMaterial, Integer> itemCost : trade.getItemCost().entrySet()) {
                    String name = BukkitItemUtil.getMaterialName(itemCost.getKey().parseItem());

                    int toMultiply = itemCost.getValue() / resultAmount;

                    int amount = multiplier == 0 ? itemCost.getValue() : multiplier * Math.max(toMultiply, 1);

                    List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                            new Placeholder("trade_cost_item_name", name),
                            new Placeholder("trade_cost_item_amount", amount)
                    ).get();

                    finalList.add(StringUtils.processMulti(box.files().messages().tradeMessages.itemCostFormat, placeholders));
                }

            } else {
                finalList.add(line);
            }
        }

        return new Placeholder("trade_cost", finalList);
    }
}

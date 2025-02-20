package com.qualityplus.minions.base.minions.minion.upgrade;

import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.util.MinionUpgradeUtil;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class MinionAutoShipping extends OkaeriConfig {
    private String id;
    private Item item;
    private String displayName;
    private List<String> description;
    private double percentageOfPriceToSell;

    public ItemStack getItemStack(long items, double coins) {
        List<String> lore = StringUtils.processMulti(description, new Placeholder("minion_upgrade_percentage_price", percentageOfPriceToSell).alone());

        ItemStack itemStack = ItemStackUtils.makeItem(item,
                PlaceholderBuilder.create(
                        new Placeholder("minion_upgrade_display_name", displayName),
                        new Placeholder("minion_upgrade_lore", lore)
                ).get());

        return MinionUpgradeUtil.addAutoShipTags(itemStack, id, items, coins);
    }
}

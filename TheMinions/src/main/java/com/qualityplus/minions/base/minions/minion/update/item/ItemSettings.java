package com.qualityplus.minions.base.minions.minion.update.item;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ItemSettings extends OkaeriConfig {
    private List<ItemStack> itemsToGive;
   // private Map<ItemStack, Integer> requiredItemsToCreate;
    private Map<Integer, ItemStack> requiredItemsToCreate;

    public List<ItemStack> getItemsCloneToGive() {
        return itemsToGive.stream()
                .filter(BukkitItemUtil::isNotNull)
                .map(ItemStack::clone)
                .collect(Collectors.toList());
    }

    public ItemStack getRequiredItemsToCreateSingle() {
        for (Map.Entry<Integer, ItemStack> entry : requiredItemsToCreate.entrySet()) {
            if (BukkitItemUtil.isNull(entry.getValue())) continue;

            ItemStack itemStack = entry.getValue().clone();

            itemStack.setAmount(entry.getKey());

            return itemStack;
        }

        return null;
    }
}

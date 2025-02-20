package com.qualityplus.crafting.util;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.crafting.base.category.Category;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@UtilityClass
public class CraftingItemStackUtil {
    public static ItemStack makeCategoryItem(Item item, List<IPlaceholder> placeholders, Category category) {
        try {
            Item item1 = ItemBuilder.of(category.getIcon(), 1, 1, item.getDisplayName(), item.getLore()).headData(category.getIconTexture()).build();

            ItemStack firstProcess = ItemStackUtils.makeItem(
                    category.getIcon(),
                    1,
                    StringUtils.processMulti(item.getDisplayName(), placeholders),
                    StringUtils.processMulti(item.getLore(), placeholders));

            return ItemStackUtils.makeItem(item1, placeholders, firstProcess);
        } catch (Exception e) {
            e.printStackTrace();
            return XMaterial.STONE.parseItem();
        }
    }
}

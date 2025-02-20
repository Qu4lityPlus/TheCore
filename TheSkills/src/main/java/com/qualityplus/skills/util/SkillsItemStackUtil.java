package com.qualityplus.skills.util;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

@UtilityClass
public final class SkillsItemStackUtil {
    public static ItemStack makeItem(Item item, List<IPlaceholder> placeholders, GUIOptions skillGUIOptions) {
        try {
            Item item1 = ItemBuilder.of(skillGUIOptions.getItem(), 1, 1, "", Collections.emptyList()).headData(skillGUIOptions.getTexture()).build();

            ItemStack firstProcess = ItemStackUtils.makeItem(
                    skillGUIOptions.getItem(),
                    1,
                    StringUtils.processMulti(item.getDisplayName(), placeholders),
                    StringUtils.processMulti(item.getLore(), placeholders));

            BukkitItemUtil.addCustomModelData(firstProcess, skillGUIOptions.getCustomModelData());


            return ItemStackUtils.getFinalItem(item1, firstProcess, placeholders);
        } catch (Exception e) {
            e.printStackTrace();
            return XMaterial.STONE.parseItem();
        }
    }


}

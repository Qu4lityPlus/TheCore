package com.qualityplus.runes.util;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.recipes.Runes;
import com.qualityplus.runes.api.session.RuneInstance;
import com.qualityplus.runes.base.rune.Rune;
import com.qualityplus.runes.base.session.ItemRuneInstanceImpl;
import com.qualityplus.runes.base.session.RuneInstanceImpl;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public final class RunesUtils {
    private static final String RUNE_LEVEL_KEY = "THE_RUNES_RUNE_LEVEL";
    private static final String RUNE_ID_KEY = "THE_RUNES_RUNE_ID";

    private static final String ITEM_STACK_RUNE_ID_KEY = "THE_RUNES_ITEM_STACK_RUNE_ID";
    private static final String ITEM_STACK_RUNE_LEVEL_KEY = "THE_RUNES_ITEM_STACK_RUNE_LEVEL";

    public boolean hasNBTData(ItemStack itemStack, String key) {
        return !BukkitItemUtil.isNull(itemStack) && new NBTItem(itemStack).hasKey(key);
    }

    public boolean isRune(ItemStack itemStack) {
        return hasNBTData(itemStack, RUNE_ID_KEY);
    }

    public boolean isRunedItem(ItemStack itemStack) {
        return hasNBTData(itemStack, ITEM_STACK_RUNE_ID_KEY);
    }

    public @Nullable RuneInstance getRuneInstance(ItemStack itemStack) {
        if (BukkitItemUtil.isNull(itemStack)) return null;

        NBTItem nbtItem = new NBTItem(itemStack);

        String id = nbtItem.getString(RUNE_ID_KEY);
        int level = nbtItem.getInteger(RUNE_LEVEL_KEY);

        return new RuneInstanceImpl(Runes.getByID(id), level);
    }

    public RuneInstance getRuneItemInstance(ItemStack itemStack) {
        if (BukkitItemUtil.isNull(itemStack)) return new ItemRuneInstanceImpl(null, 0);

        NBTItem nbtItem = new NBTItem(itemStack);

        String id = nbtItem.hasKey(ITEM_STACK_RUNE_ID_KEY) ? nbtItem.getString(ITEM_STACK_RUNE_ID_KEY) : "";
        int level = nbtItem.hasKey(ITEM_STACK_RUNE_LEVEL_KEY) ? nbtItem.getInteger(ITEM_STACK_RUNE_LEVEL_KEY) : 0;

        return new ItemRuneInstanceImpl(Runes.getByID(id), level);
    }

    public ItemStack makeRune(Box box, Rune rune, int level) {
        Item item = ItemBuilder.of(new Item(box.files().config().runeItem))
                .material(rune.getRuneItem().getMaterial())
                .headData(rune.getRuneItem().getTexture())
                .build();

        NBTItem nbtItem = new NBTItem(ItemStackUtils.makeItem(item, RunesPlaceholderUtils.placeholderWithRequired(rune, level).get()));

        nbtItem.setString(RUNE_ID_KEY, rune.getId());

        nbtItem.setInteger(RUNE_LEVEL_KEY, level);

        return nbtItem.getItem();
    }

    public ItemStack removeRuneFromItem(ItemStack itemStack) {
        NBTItem item = new NBTItem(itemStack);

        String id = item.getString(ITEM_STACK_RUNE_ID_KEY);

        int level = item.getInteger(ITEM_STACK_RUNE_LEVEL_KEY);

        Rune rune = Runes.getByID(id);

        List<String> lore = new ArrayList<>(BukkitItemUtil.getItemLore(itemStack));

        String toRemove = Optional.ofNullable(rune)
                .map(rune1 -> StringUtils.color(rune1.getToAddLore().replace("%rune_level%", NumberUtil.toRoman(level))))
                .orElse(null);

        if (toRemove != null)
            lore.remove(toRemove);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        NBTItem item2 = new NBTItem(itemStack);
        item2.removeKey(ITEM_STACK_RUNE_ID_KEY);
        item2.removeKey(ITEM_STACK_RUNE_LEVEL_KEY);

        return item2.getItem();
    }

    public ItemStack addNBTData(ItemStack itemStack, String id, int level) {

        NBTItem item = new NBTItem(itemStack);

        item.setString(ITEM_STACK_RUNE_ID_KEY, id);
        item.setInteger(ITEM_STACK_RUNE_LEVEL_KEY, level);

        return item.getItem();
    }
}

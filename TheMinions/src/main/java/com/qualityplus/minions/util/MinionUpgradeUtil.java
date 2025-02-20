package com.qualityplus.minions.util;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.minions.persistance.data.upgrade.AutomatedShippingEntity;
import com.qualityplus.minions.persistance.data.upgrade.FuelEntity;
import com.qualityplus.minions.persistance.data.upgrade.SkinEntity;
import com.qualityplus.minions.persistance.data.upgrade.UpgradeEntity;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class MinionUpgradeUtil {
    private static final String SKIN_ID = "THE_MINIONS_SKIN_ID";

    private static final String UPGRADE_ID = "THE_MINIONS_UPGRADE_ID";
    private static final String AUTO_SHIP_ID = "THE_MINIONS_AUTO_SHIP_ID";
    private static final String AUTO_SHIP_HELD = "THE_MINIONS_AUTO_SHIP_HELD";
    private static final String AUTO_SHIP_ITEMS = "THE_MINIONS_AUTO_SHIP_ITEMS";
    private static final String FUEL_ID = "THE_MINIONS_FUEL_ID";
    private static final String FUEL_DELAY = "THE_MINIONS_FUEL_DELAY";
    private static final String FUEL_LAST_MARKED = "THE_MINIONS_FUEL_MARKED";

    public ItemStack addUpdgradeTags(ItemStack itemStack, String id) {
        NBTItem nbtItem = new NBTItem(itemStack);

        nbtItem.setString(UPGRADE_ID, id);

        return nbtItem.getItem();
    }

    public boolean isUpgrade(ItemStack itemStack) {
        if (BukkitItemUtil.isNull(itemStack)) return false;

        return new NBTItem(itemStack).hasTag(UPGRADE_ID);
    }

    public UpgradeEntity getUpgradeFromItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        String id = nbtItem.getString(UPGRADE_ID);


        return UpgradeEntity.builder()
                .id(id)
                .build();
    }
    //---------------------------
    public ItemStack addSkinTags(ItemStack itemStack, String id) {
        NBTItem nbtItem = new NBTItem(itemStack);

        nbtItem.setString(SKIN_ID, id);

        return nbtItem.getItem();
    }

    public boolean isSkin(ItemStack itemStack) {
        if (BukkitItemUtil.isNull(itemStack)) return false;

        return new NBTItem(itemStack).hasTag(SKIN_ID);
    }

    public SkinEntity getSkinFromItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        String id = nbtItem.getString(SKIN_ID);

        return SkinEntity.builder()
                .id(id)
                .build();
    }

    //---------------------------
    public ItemStack addAutoShipTags(ItemStack itemStack, String id, long items, double coins) {
        NBTItem nbtItem = new NBTItem(itemStack);

        nbtItem.setString(AUTO_SHIP_ID, id);
        nbtItem.setLong(AUTO_SHIP_HELD, items);
        nbtItem.setDouble(AUTO_SHIP_ITEMS, coins);

        return nbtItem.getItem();
    }

    public boolean isAutoShip(ItemStack itemStack) {
        if (BukkitItemUtil.isNull(itemStack)) return false;

        return new NBTItem(itemStack).hasTag(AUTO_SHIP_ID);
    }

    public AutomatedShippingEntity getAutoShipFromItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        String id = nbtItem.getString(AUTO_SHIP_ID);
        long items = nbtItem.getLong(AUTO_SHIP_ITEMS);
        double coins = nbtItem.getDouble(AUTO_SHIP_HELD);

        return AutomatedShippingEntity.builder()
                .heldCoins(coins)
                .id(id)
                .soldItems(items)
                .build();
    }

    //---------------------------
    public ItemStack addFuelTags(ItemStack itemStack, String id, long delay, long marked) {
        NBTItem nbtItem = new NBTItem(itemStack);

        nbtItem.setString(FUEL_ID, id);
        nbtItem.setLong(FUEL_DELAY, delay);
        nbtItem.setLong(FUEL_LAST_MARKED, marked);

        return nbtItem.getItem();
    }

    public boolean isFuel(ItemStack itemStack) {
        if (BukkitItemUtil.isNull(itemStack)) return false;

        return new NBTItem(itemStack).hasTag(FUEL_ID);
    }

    public FuelEntity getFuelFromItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        String id = nbtItem.getString(FUEL_ID);
        long marked = nbtItem.getLong(FUEL_LAST_MARKED);
        long delay = nbtItem.getLong(FUEL_DELAY);

        return FuelEntity.builder()
                .markable(new Markable(delay, marked))
                .id(id)
                .build();
    }

    public FuelEntity getFuelFromItem(ItemStack itemStack, long delay, long marked) {
        NBTItem nbtItem = new NBTItem(itemStack);

        String id = nbtItem.getString(FUEL_ID);

        return FuelEntity.builder()
                .markable(new Markable(delay, marked))
                .id(id)
                .build();
    }
}

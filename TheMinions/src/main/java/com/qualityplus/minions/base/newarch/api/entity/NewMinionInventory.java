package com.qualityplus.minions.base.newarch.api.entity;

import org.bukkit.inventory.ItemStack;

public interface NewMinionInventory {
    public boolean hasEmptySlots();

    public boolean canAdd(final ItemStack itemStack);

    public void add(final ItemStack itemStack);

    public void remove(final ItemStack itemStack);

    public int getMaxSlots();
}

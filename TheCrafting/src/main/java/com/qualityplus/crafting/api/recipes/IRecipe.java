package com.qualityplus.crafting.api.recipes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IRecipe {
    public ItemStack getResult();

    public default int getCraftAmountTimes(final Player player) {
        return 1;
    }

    public default boolean canCraft(final Player player) {
        return true;
    }
}

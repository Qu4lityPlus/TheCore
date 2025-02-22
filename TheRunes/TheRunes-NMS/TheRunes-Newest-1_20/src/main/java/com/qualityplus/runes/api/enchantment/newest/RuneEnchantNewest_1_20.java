package com.qualityplus.runes.api.enchantment.newest;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public final class RuneEnchantNewest_1_20 extends Enchantment {
    private final NamespacedKey key;

    public RuneEnchantNewest_1_20() {
        super();
        this.key = NamespacedKey.minecraft("glow");
    }

    @NotNull
    @Override
    public String getName() {
        return "Glow";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @NotNull
    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return false;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return this.key.getKey();
    }
}

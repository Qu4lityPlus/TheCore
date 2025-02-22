package com.qualityplus.enchanting.api.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ICoreEnchantment {
    String getIdentifier();

    int getStartLevel();

    int getMaxLevel();

    String getName();

    String getDescription();
    String getDescriptionPerLevel(int level);

    boolean conflictsWith(Enchantment other);

    boolean canEnchantItem(ItemStack item);

    int getRequiredBookShelf();

    boolean isEnabled();

    double getRequiredLevelToEnchant(int level);

    double getRequiredMoneyToEnchant(int level);

    double getRequiredLevelToRemove(int level);

    double getRequiredMoneyToRemove(int level);

    boolean canEnchant(Player player, int level);

    Enchantment getEnchantment();

    ProviderType getProviderType();

    default boolean isVanilla() {
        return getProviderType().equals(ProviderType.VANILLA_ENCHANT);
    }

    default boolean isEco() {
        return getProviderType().equals(ProviderType.ECO_ENCHANT);
    }

    default boolean isAE() {
        return getProviderType().equals(ProviderType.ADVANCED_ENCHANT);
    }
}

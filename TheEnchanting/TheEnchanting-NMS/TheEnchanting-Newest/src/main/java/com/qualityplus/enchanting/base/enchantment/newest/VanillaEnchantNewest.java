package com.qualityplus.enchanting.base.enchantment.newest;

import com.qualityplus.enchanting.api.enchantment.ProviderType;
import com.qualityplus.enchanting.api.enchantment.VanillaEnchantment;
import com.qualityplus.enchanting.api.enchantment.newest.CoreEnchantNewest;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class VanillaEnchantNewest extends CoreEnchantNewest implements VanillaEnchantment {
    private final Map<Integer, String> descriptionPerLevel;
    @Getter
    private final Enchantment enchantment;

    @Builder
    public VanillaEnchantNewest(@NotNull String identifier, Map<Integer, String> requiredPermissionsToEnchant, Map<Integer, Double> requiredXpLevelToEnchant, int maxLevel, boolean enabled, int requiredBookShelf,
                                Map<Integer, Double> requiredMoneyToEnchant, String displayName, String description, Enchantment enchantment, ProviderType providerType, Map<Integer, String> descriptionPerLevel) {
        super(identifier, requiredPermissionsToEnchant, requiredXpLevelToEnchant, maxLevel, enabled, requiredBookShelf, requiredMoneyToEnchant, displayName, description, providerType);

        this.descriptionPerLevel = descriptionPerLevel;
        this.enchantment = enchantment;
    }

    @Override
    public String getDescriptionPerLevel(int level) {
        return Optional.ofNullable(descriptionPerLevel)
                .filter(map -> map.containsKey(level))
                .map(map -> map.get(level))
                .orElse("");
    }

    @Override
    public @NotNull String getName() {
        return this.displayName;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return Optional.ofNullable(this.enchantment)
                .map(Enchantment::getItemTarget)
                .orElse(EnchantmentTarget.ALL);
    }

    @Override
    public boolean isTreasure() {
        return Optional.ofNullable(this.enchantment)
                .map(Enchantment::isTreasure)
                .orElse(false);
    }

    @Override
    public boolean isCursed() {
        return Optional.ofNullable(this.enchantment)
                .map(Enchantment::isCursed)
                .orElse(false);
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return Optional.ofNullable(this.enchantment)
                .map(enchantment1 -> enchantment1.conflictsWith(other))
                .orElse(false);
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return Optional.ofNullable(this.enchantment)
                .map(enchantment1 -> enchantment1.canEnchantItem(item))
                .orElse(false);
    }
}

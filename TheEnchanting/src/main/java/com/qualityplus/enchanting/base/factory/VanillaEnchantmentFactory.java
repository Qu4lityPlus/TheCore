package com.qualityplus.enchanting.base.factory;

import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.enchantment.ProviderType;
import com.qualityplus.enchanting.base.enchantment.legacy.VanillaEnchantLegacy;
import com.qualityplus.enchanting.base.enchantment.newest.VanillaEnchantNewest;
import com.qualityplus.enchanting.base.enchantment.newest.VanillaEnchantNewest1_20;
import lombok.Builder;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class VanillaEnchantmentFactory {
    private static final AtomicInteger ENCHANTMENT_COUNTER = new AtomicInteger(100);
    private static final List<String> LEGACY_VERSIONS = List.of(
            "v1_7",
            "v1_8",
            "v1_9",
            "v1_10",
            "v1_11",
            "v1_12"
    );
    private static final String V1_20_1 = "1.20.1";
    private static final String V1_20 = "1.20";
    private static final String V1_21 = "1.21";
    private final Map<Integer, String> requiredPermissionsToEnchant;
    private final Map<Integer, Double> requiredXpLevelToEnchant;
    private final Map<Integer, Double> requiredMoneyToEnchant;
    private final Map<Integer, String> descriptionPerLevel;
    private final Enchantment enchantment;
    private final int requiredBookShelf;
    private final String displayName;
    private final String description;
    private final String identifier;
    private final boolean enabled;
    private final int maxLevel;

    @Builder
    public VanillaEnchantmentFactory(@NotNull String identifier, Map<Integer, String> requiredPermissionsToEnchant, Map<Integer, Double> requiredXpLevelToEnchant, int maxLevel, boolean enabled, int requiredBookShelf,
                                     Map<Integer, Double> requiredMoneyToEnchant, String displayName, String description, Enchantment enchantment, Map<Integer, String> descriptionPerLevel) {
        this.requiredPermissionsToEnchant = requiredPermissionsToEnchant;
        this.requiredXpLevelToEnchant = requiredXpLevelToEnchant;
        this.requiredMoneyToEnchant = requiredMoneyToEnchant;
        this.descriptionPerLevel = descriptionPerLevel;
        this.requiredBookShelf = requiredBookShelf;
        this.enchantment = enchantment;
        this.displayName = displayName;
        this.description = description;
        this.identifier = identifier;
        this.maxLevel = maxLevel;
        this.enabled = enabled;
    }

    public ICoreEnchantment build(ProviderType providerType) {
        MinecraftVersion minecraftVersion = MinecraftVersion.DEFAULT;
        final String[] versionSplit = Bukkit.getServer().getVersion().split("-");

        if (versionSplit.length > 1) {
            final String version = versionSplit[0];

            if (version.contains(V1_20) || version.contains(V1_21)) {
                if (!version.startsWith(V1_20_1)) {
                    minecraftVersion = MinecraftVersion.V1_20_2_HIGHER;
                } else {
                    minecraftVersion = MinecraftVersion.NEW;
                }
            }
        }

        if (minecraftVersion.equals(MinecraftVersion.DEFAULT)) {
            final String nmsVersion = Bukkit.getServer().getClass()
                    .getPackage()
                    .getName()
                    .split("\\.")[3];
            final String versionSimplified = nmsVersion.split("R")[0];
            if (LEGACY_VERSIONS.contains(versionSimplified)) {
                minecraftVersion = MinecraftVersion.LEGACY;
            } else {
                minecraftVersion = MinecraftVersion.NEW;
            }
        }

        if (minecraftVersion.equals(MinecraftVersion.V1_20_2_HIGHER)) {
            return VanillaEnchantNewest1_20.builder()
                    .requiredPermissionsToEnchant(requiredPermissionsToEnchant)
                    .requiredXpLevelToEnchant(requiredXpLevelToEnchant)
                    .requiredMoneyToEnchant(requiredMoneyToEnchant)
                    .descriptionPerLevel(descriptionPerLevel)
                    .requiredBookShelf(requiredBookShelf)
                    .providerType(providerType)
                    .enchantment(enchantment)
                    .displayName(displayName)
                    .description(description)
                    .identifier(identifier)
                    .maxLevel(maxLevel)
                    .enabled(enabled)
                    .build();
        } else if (minecraftVersion.equals(MinecraftVersion.NEW)) {
            return VanillaEnchantNewest.builder()
                    .requiredPermissionsToEnchant(requiredPermissionsToEnchant)
                    .requiredXpLevelToEnchant(requiredXpLevelToEnchant)
                    .requiredMoneyToEnchant(requiredMoneyToEnchant)
                    .descriptionPerLevel(descriptionPerLevel)
                    .requiredBookShelf(requiredBookShelf)
                    .providerType(providerType)
                    .enchantment(enchantment)
                    .displayName(displayName)
                    .description(description)
                    .identifier(identifier)
                    .maxLevel(maxLevel)
                    .enabled(enabled)
                    .build();
        } else {
            return VanillaEnchantLegacy.builder()
                    .requiredPermissionsToEnchant(requiredPermissionsToEnchant)
                    .requiredXpLevelToEnchant(requiredXpLevelToEnchant)
                    .requiredMoneyToEnchant(requiredMoneyToEnchant)
                    .descriptionPerLevel(descriptionPerLevel)
                    .id(ENCHANTMENT_COUNTER.getAndIncrement())
                    .requiredBookShelf(requiredBookShelf)
                    .providerType(providerType)
                    .displayName(displayName)
                    .description(description)
                    .enchantment(enchantment)
                    .identifier(identifier)
                    .maxLevel(maxLevel)
                    .enabled(enabled)
                    .build();
        }
    }

    private enum MinecraftVersion {
        NEW,
        V1_20_2_HIGHER,
        LEGACY,
        DEFAULT
    }
}

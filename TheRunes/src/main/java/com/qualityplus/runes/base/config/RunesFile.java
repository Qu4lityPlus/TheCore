package com.qualityplus.runes.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.particle.ParticleColor;
import com.qualityplus.runes.api.config.RuneTableConfig.RuneItem;
import com.qualityplus.runes.base.rune.Rune;
import com.qualityplus.runes.base.rune.RuneApply;
import com.qualityplus.runes.base.rune.RuneEffect;
import com.qualityplus.runes.base.rune.RuneLevel;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Particle;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration(path = "runes.yml")
@Header("================================")
@Header("       Runes      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class RunesFile extends OkaeriConfig {

    public List<Rune> runes = Arrays.asList(
            Rune.builder()
                    .id("gem")
                    .displayName("&2Gem Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Boots",
                            "",
                            "&7Create a gem effect behind your",
                            "&7arrows!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDNhMWFkNGZjYzQyZmI2M2M2ODEzMjhlNDJkNjNjODNjYTE5M2IzMzNhZjJhNDI2NzI4YTI1YThjYzYwMDY5MiJ9fX0="))
                    .toAddLore("&2Gem Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.VILLAGER_HAPPY)
                                    .particleQuantity(1)
                                    .toApplyIn(RuneApply.BOOTS)
                                    .build()
                    )
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("ice")
                    .displayName("&bSnow Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Bows",
                            "",
                            "&7Create an epic snow effect",
                            "&7in your arrows!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjNTdjNzVhZGYzOWVjNmYwZTA5MTYwNDlkZDk2NzFlOThhOGExZTYwMDEwNGU4NGU2NDVjOTg4OTUwYmQ3In19fQ=="))
                    .toAddLore("&bSnow Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.REDSTONE)
                                    .particleColor(ParticleColor.WHITE)
                                    .particleQuantity(10)
                                    .toApplyIn(RuneApply.BOW)
                                    .build())
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("golden")
                    .displayName("&eGolden Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Bows",
                            "",
                            "&7Flaunt your wealth with",
                            "&7golden arrows!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVmNDg2MWFhNWIyMmVlMjhhOTBlNzVkYWI0NWQyMjFlZmQxNGMwYjFlY2M4ZWU5OThmYjY3ZTQzYmI4ZjNkZSJ9fX0="))
                    .toAddLore("&eGolden Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.REDSTONE)
                                    .particleColor(ParticleColor.YELLOW)
                                    .particleQuantity(10)
                                    .toApplyIn(RuneApply.BOW)
                                    .fakeDropItems(Arrays.asList(XMaterial.GOLDEN_APPLE, XMaterial.GOLD_INGOT, XMaterial.GOLD_BLOCK, XMaterial.GOLD_NUGGET))
                                    .build())
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("smokey")
                    .displayName("&7Smokey Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Weapons",
                            "",
                            "&7Create a burnt trail behind",
                            "&7your arrows!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkOGE4ZDUyN2Y2NWE0ZjQzNGY4OTRmN2VlNDJlYjg0MzAxNWJkYTc5MjdjNjNjNmVhOGE3NTRhZmU5YmIxYiJ9fX0="))
                    .toAddLore("&7Smokey Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.SMOKE_LARGE)
                                    .particleQuantity(1)
                                    .toApplyIn(RuneApply.SWORD)
                                    .build()
                    )
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("magic")
                    .displayName("&dMagic Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Bows",
                            "",
                            "&7Your arrows have a",
                            "&7magical shine!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQ0ODBlMzllYTYzZTM0N2QyNjhkZTgzMDkwZDA5OTg0YmYzNDM5NDExODg0ODM0OGJmNGViNTc0OTBjZTlkMiJ9fX0="))
                    .toAddLore("&dMagic Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.SPELL_WITCH)
                                    .particleQuantity(1)
                                    .toApplyIn(RuneApply.BOW)
                                    .build()
                    )
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("lava")
                    .displayName("&4Lava Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Bows",
                            "",
                            "&7Shoot Magma Arrows!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjEzZDkwM2Y2MDEwMzRhYzM0MDBkMjYyNWZlZjEwNGU5YjA5NDA3NDZjNTU0MTkzZjZkOWU4NWE4NGE5NjZhMSJ9fX0="))
                    .toAddLore("&4Lava Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.DRIP_LAVA)
                                    .particleQuantity(1)
                                    .toApplyIn(RuneApply.BOW)
                                    .build()
                    )
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("blood")
                    .displayName("&cBlood Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Weapons",
                            "",
                            "&7Create a blood effect when you!",
                            "&7when you kill mobs!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY2NjBiMDE2ZDA1NjQ1ZmZmZDFmNDhiNzkyZDFhYmU1ZDhmMzBkYzk2NTY3NTY5YWUxZDk4MmQyNTBiNjkzYyJ9fX0="))
                    .toAddLore("&cBlood Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.REDSTONE)
                                    .particleColor(ParticleColor.RED)
                                    .particleQuantity(1)
                                    .toApplyIn(RuneApply.SWORD)
                                    .build()
                    )
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("heart")
                    .displayName("&cHearts Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Weapons",
                            "",
                            "&7Shower you enemies in love!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmMxYzE3OWFkNTE5NTVmMTUyMmM0OGVhOTkzMWYwOWMxNjI3NDFiNDVlMjJlOWQzZmViNjgyYzdlNWVkODI3NCJ9fX0="))
                    .toAddLore("&cHearts Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.HEART)
                                    .particleQuantity(1)
                                    .toApplyIn(RuneApply.SWORD)
                                    .build()
                    )
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("music")
                    .displayName("&bMusic Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Weapons",
                            "",
                            "&7Put monsters out of there misery",
                            "&7with sweet tunes!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0ODFjMzFkYzY4M2JkY2I3ZDM3NWE3YzVkYjdhYzdhZGY5ZTlmZThiNmMwNGE2NDkzMTYxM2UyOWZlNDcwZSJ9fX0="))
                    .toAddLore("&bMusic Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .particle(Particle.NOTE)
                                    .particleQuantity(1)
                                    .toApplyIn(RuneApply.SWORD)
                                    .build()
                    )
                    .levelDataMap(defaultMap())
                    .build(),
            Rune.builder()
                    .id("lighting")
                    .displayName("&9Lightning Rune")
                    .description(Arrays.asList(
                            "&8Requires level %rune_required_level%",
                            "&8Weapons",
                            "",
                            "&7Chance to strike lightning on",
                            "&7the enemies you defeat!"
                    ))
                    .runeItem(new RuneItem(XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg1YmNmN2Y4MmQzNGRiODlhOTVhZGRmOGU1MzI1M2UyZDk1NTRjNmZkMmYyZTM5ZTI0MzYyZDI0M2EwY2NmNyJ9fX0="))
                    .toAddLore("&9Lightning Rune %rune_level%")
                    .effect(
                            RuneEffect.builder()
                                    .strikeLightning(true)
                                    .toApplyIn(RuneApply.SWORD)
                                    .build()
                    )
                    .levelDataMap(defaultMap())
                    .build()
    );

    private Map<Integer, RuneLevel> defaultMap() {
        return FastMap.builder(Integer.class, RuneLevel.class)
                .put(1, new RuneLevel(1, 80, 1))
                .put(2, new RuneLevel(2, 70, 2))
                .put(3, new RuneLevel(3, 60, 3))
                .put(4, new RuneLevel(4, 50, 4))
                .build();
    }
}

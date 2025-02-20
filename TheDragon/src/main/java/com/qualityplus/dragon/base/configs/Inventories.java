package com.qualityplus.dragon.base.configs;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.inventory.CommonGUI;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.inventory.background.Background;
import com.qualityplus.assistant.inventory.background.DefaultBackgrounds;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.CustomKey;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.dragon.gui.altars.DragonAltarsGUIConfig;
import com.qualityplus.dragon.gui.confirm.ConfirmGUIConfig;
import com.qualityplus.dragon.gui.crystals.DragonCrystalsGUIConfig;
import com.qualityplus.dragon.gui.dragons.DragonsGUIConfig;
import com.qualityplus.dragon.gui.equipment.GuardianEquipmentGUIConfig;
import com.qualityplus.dragon.gui.guardian.GuardianGUIConfig;
import com.qualityplus.dragon.gui.guardians.DragonGuardiansGUIConfig;
import com.qualityplus.dragon.gui.guardianspawns.GuardianSpawnsGUIConfig;
import com.qualityplus.dragon.gui.mainmenu.MainMenuGUIConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration(path = "inventories.yml")
@Header("================================")
@Header("       Inventories      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Inventories extends OkaeriConfig implements DefaultBackgrounds {

    @CustomKey("guardiansGUIConfig")
    public DragonGuardiansGUIConfig guardiansGUIConfig = new DragonGuardiansGUIConfig(
            new CommonGUI(
                    "Guardians GUI",
                    27,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  22, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "&5&nGuardian:&d %thedragon_guardian_id%", Arrays.asList("",
                     "&5► &dLeft-Click to edit", "&5► &dRight-Click to remove"))
                    .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M4OWRjNTA0YTVjNmZjZTMxNzM0N2ZmZDQ4NGVjY2EyMjAyMmU2MjRjMDM0ZWI5YzgyNDYzZTg0OGE1YTg5NyJ9fX0")
                    .build(),
            ItemBuilder.of(XMaterial.ARROW,  21, 1, "&5Previous Page", Collections.singletonList("&7Click to go to previous page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  23, 1, "&5Next Page", Collections.singletonList("&7Click to go to next page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  18, 1, "&5Go Back", Collections.singletonList("&7Click to go to main menu")).build()
    );

    @CustomKey("altarsGUIConfig")
    public DragonAltarsGUIConfig dragonAltarsGUIConfig = new DragonAltarsGUIConfig(
            new CommonGUI(
                    "Altars GUI",
                    27,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  22, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "&5&nAltar:&d %thedragon_altar_id%", Arrays.asList("",
                            "&5► &dLocation: &5%thedragon_altar_location%",
                            "",
                            "&5► &dLeft-Click to teleport",
                            "&5► &dRight-Click to remove"))
                    .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQ2ODRlM2U3ODkwY2FmN2QxMzc2MmVhMTllYjE0YzU5NDBiODhmZDdmMDc3ZDgxZTZlZmZiNGY2ZGYxNmMyNiJ9fX0=").build(),
            ItemBuilder.of(XMaterial.ARROW,  21, 1, "&5Previous Page", Collections.singletonList("&7Click to go to previous page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  23, 1, "&5Next Page", Collections.singletonList("&7Click to go to next page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  18, 1, "&5Go Back", Collections.singletonList("&7Click to go to main menu")).build()

    );

    @CustomKey("crystalsGUIConfig")
    public DragonCrystalsGUIConfig dragonCrystalsGUIConfig = new DragonCrystalsGUIConfig(
            new CommonGUI(
                    "Crystals GUI",
                    27,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  22, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "&5&nCrystal:&d %thedragon_crystal_id%", Arrays.asList("",
                            "&5► &dLocation: &5%thedragon_crystal_location%",
                            "",
                            "&5► &dLeft-Click to teleport",
                            "&5► &dRight-Click to remove"))
                    .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc0ODEzOWVlNmNmZjc0NGM3ZTg5NTkzZjZiOTBkNDYwNDRkMDdlZTVlNjM4MjhmYmU5NTMxZmI2NmRmOWI4ZiJ9fX0=").build(),
            ItemBuilder.of(XMaterial.ARROW,  21, 1, "&5Previous Page", Collections.singletonList("&7Click to go to previous page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  23, 1, "&5Next Page", Collections.singletonList("&7Click to go to next page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  18, 1, "&5Go Back", Collections.singletonList("&7Click to go to main menu")).build()

    );

    @CustomKey("guardianSpawnsGUIConfig")
    public GuardianSpawnsGUIConfig guardianSpawnsGUIConfig = new GuardianSpawnsGUIConfig(
            new CommonGUI(
                    "Guardian Spawns GUI",
                    27,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  22, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "&5&nGuardian Spawn:&d %thedragon_guardian_spawn_id%", Arrays.asList("",
                            "&5► &dLocation: &5%thedragon_guardian_spawn_location%",
                            "",
                            "&5► &dLeft-Click to teleport",
                            "&5► &dRight-Click to remove"))
                    .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQwOWUwM2JlZTYxNDdlYzllYTdjMjFiNjc1MmQ0OWJlMDRhZTFlNzdjYmVjNTc0YWY4N2FjMzlhNDE5NzUyYyJ9fX0=").build(),
            ItemBuilder.of(XMaterial.ARROW,  21, 1, "&5Previous Page", Collections.singletonList("&7Click to go to previous page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  23, 1, "&5Next Page", Collections.singletonList("&7Click to go to next page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  18, 1, "&5Go Back", Collections.singletonList("&7Click to go to main menu")).build()

    );


    @CustomKey("dragonsGUIConfig")
    public DragonsGUIConfig dragonsGUIConfig = new DragonsGUIConfig(
            new CommonGUI(
                    "Dragons GUI",
                    27,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  22, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "&5&nDragon:&d %thedragon_dragon_id%", Arrays.asList("",
                            "&5► &dDisplayName: &5%thedragon_dragon_displayname%",
                            "&5► &dMax Health: &5%thedragon_dragon_maxhealth%",
                            "&5► &dChance: &5%thedragon_dragon_chance%%",
                            "&5► &dXp: &5%thedragon_dragon_xp%"))
                    .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjY4YzFjMDc5YTdmZmIzNmY0OGRkNzE1MDM1NWUzZTBiN2Y2OGRkNjA1ZTZmODg0NzMxM2MzNjBjZjYxZTBjIn19fQ==").build(),
            ItemBuilder.of(XMaterial.ARROW,  21, 1, "&5Previous Page", Collections.singletonList("&7Click to go to previous page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  23, 1, "&5Next Page", Collections.singletonList("&7Click to go to next page")).build(),
            ItemBuilder.of(XMaterial.ARROW,  18, 1, "&5Go Back", Collections.singletonList("&7Click to go to main menu")).build()

    );

    @CustomKey("confirmGUIConfig")
    public ConfirmGUIConfig confirmGUIConfig = new ConfirmGUIConfig(
            new CommonGUI(
                    "Confirm GUI",
                    27,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ),
            ItemBuilder.of(XMaterial.LIME_WOOL,  11, 1, "&aConfirm", Collections.singletonList("&7Click to confirm")).build(),
            ItemBuilder.of(XMaterial.RED_WOOL, 15, 1, "&cCancel", Collections.singletonList("&7Click to cancel")).build()
    );

    @CustomKey("guardianEquipmentGUIConfig")
    public GuardianEquipmentGUIConfig guardianEquipmentGUIConfig = new GuardianEquipmentGUIConfig(
            new CommonGUI(
                    "Select Equipment:",
                    45,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            )
    );

    @CustomKey("mainMenuGUIConfig")
    public MainMenuGUIConfig mainMenuGUIConfig = new MainMenuGUIConfig(
            new CommonGUI(
                    "Game Settings",
                    45,
                    getMainMenuFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  40, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 11, 1, "&5&nDragon Game Spawn", Arrays.asList("", "&5► &dLocation: &5%thedragon_spawn_location%", "", "&5» &dClick to teleport")).headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZiYTk5ODdmNzM4ZTZkNzVkM2IwMmMzMGQxNDgwYTM2MDU5M2RkYjQ2NGJkMWM4MWFiYjlkNzFkOWU2NTZjMCJ9fX0=").build(),
            ItemBuilder.of(XMaterial.PLAYER_HEAD,  12, 1, "&5&nGame Crystals", Arrays.asList("", "&5► &dAmount: &5%thedragon_crystals_amount%", "", "&5» &dClick to view")).headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc0ODEzOWVlNmNmZjc0NGM3ZTg5NTkzZjZiOTBkNDYwNDRkMDdlZTVlNjM4MjhmYmU5NTMxZmI2NmRmOWI4ZiJ9fX0=").build(),
            ItemBuilder.of(XMaterial.PLAYER_HEAD,  13, 1, "&5&nGame Altars", Arrays.asList("", "&5► &dAmount: &5%thedragon_altars_amount%", "", "&5» &dClick to view")).headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQ2ODRlM2U3ODkwY2FmN2QxMzc2MmVhMTllYjE0YzU5NDBiODhmZDdmMDc3ZDgxZTZlZmZiNGY2ZGYxNmMyNiJ9fX0").build(),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 14, 1, "&5&nCurrent Game Schematic", Arrays.asList("", "&5► &dGame Schematic: &5%thedragon_schematic_id%", "", "&5» &dAdd it in &5/TheDragon/schematics", "  &dand then change it in &5config.yml")).headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ4Yjg5YjFhYTk4OGRhODc4MDA0ODhjMzcyNjBhYTg5OGQyYzA3YWM4NmMwOTljNDQ5NGZjMGY0ZjBkY2NhMiJ9fX0=").build(),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 15, 1, "&5&nGame Dragons", Arrays.asList("", "&5► &dAmount: &5%thedragon_dragons_amount%", "", "&5» &dClick to view all dragons!")).headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjY4YzFjMDc5YTdmZmIzNmY0OGRkNzE1MDM1NWUzZTBiN2Y2OGRkNjA1ZTZmODg0NzMxM2MzNjBjZjYxZTBjIn19fQ==").build(),
            ItemBuilder.of(XMaterial.PLAYER_HEAD, 22, 1, "&5&nGame Guardians", Arrays.asList("", "&5► &dAmount: &5%thedragon_guardians_amount%", "", "&5» &dClick to view all guardians!")).headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M4OWRjNTA0YTVjNmZjZTMxNzM0N2ZmZDQ4NGVjY2EyMjAyMmU2MjRjMDM0ZWI5YzgyNDYzZTg0OGE1YTg5NyJ9fX0=").build(),
            getBackUpGuardianSpawn(),
            getBackUpWiki()

    );

    public Item getBackUpGuardianSpawn() {
        return ItemBuilder.of(XMaterial.PLAYER_HEAD, 23, 1, "&5&nGame Spawn Guardians", Arrays.asList("", "&5► &dAmount: &5%thedragon_guardian_spawns_amount%", "", "&5» &dClick to view all guardian spawns!")).headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQwOWUwM2JlZTYxNDdlYzllYTdjMjFiNjc1MmQ0OWJlMDRhZTFlNzdjYmVjNTc0YWY4N2FjMzlhNDE5NzUyYyJ9fX0=").build();
    }

    public Item getBackUpWiki() {
        return ItemBuilder.of(XMaterial.PLAYER_HEAD, 21, 1, "&5&nWiki Tutorial", Arrays.asList("", "&5► &dClick to view wiki tutorial!")).headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjZGVlNmQwNmRmMjM0YjhlNjAzMzI4Yjk2YzU3ZjNhMzEyZTc5YWFiZmMzYmU3MmE4YjQyMTg3OGVkNjhjZiJ9fX0=").build();
    }

    @CustomKey("guardianGUIConfig")
    public GuardianGUIConfig guardianGUIConfig = new GuardianGUIConfig(
            new CommonGUI(
                    "Guardian GUI",
                    45,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  40, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ),
            ItemBuilder.of(XMaterial.ARROW, 39, 1, "&5Go Back", Arrays.asList("&7Click to go back")).build(),
            ItemBuilder.of(XMaterial.DIAMOND_HELMET, 10,1, "&5&nEdit Helmet", Arrays.asList("", "&5 » &dClick to change")).build(),
            ItemBuilder.of(XMaterial.DIAMOND_CHESTPLATE, 11, 1, "&5&nEdit Chestplate", Arrays.asList("", "&5 » &dClick to change")).build(),
            ItemBuilder.of(XMaterial.DIAMOND_LEGGINGS, 12, 1, "&5&nEdit Leggings", Arrays.asList("", "&5 » &dClick to change")).build(),
            ItemBuilder.of(XMaterial.DIAMOND_BOOTS, 13, 1, "&5&nEdit Boots", Arrays.asList("", "&5 » &dClick to change")).build(),
            ItemBuilder.of(XMaterial.ZOMBIE_HEAD, 14, 1, "&5&nMob Type", Arrays.asList("", "&5► &dType: &5%type%", "", "&5 » &dClick to change")).build(),
            ItemBuilder.of(XMaterial.NAME_TAG, 15, 1, "&5&nDisplayName", Arrays.asList("", "&5► &dDisplayname: &5%displayname%", "", "&5 » &dClick to change")).build(),
            ItemBuilder.of(XMaterial.APPLE, 16, 1, "&5&nHealth", Arrays.asList("", "&5► &dHealth: &5%health%", "&5 » &dClick to change")).build(),
            ItemBuilder.of(XMaterial.DIAMOND_BOOTS, 22, 1, "&5&nEdit Weapon", Arrays.asList("", "&5 » &dClick to change")).build()
            );

    private Background getMainMenuFiller() {
        return Background.builder()
                .filler(ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .useFiller(true)
                .items(ImmutableMap.<Integer, Item>builder()
                        .put(0, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(1, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(9, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(7, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(8, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(17, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(27, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(36, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(37, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(35, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(43, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .put(44, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                        .build())

                .build();
    }
}

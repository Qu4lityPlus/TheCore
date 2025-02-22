package com.qualityplus.minions.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.faster.FastStack;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.base.minions.minion.skin.MinionSkin;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import org.bukkit.Color;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Configuration(path = "skins.yml")
@Header("================================")
@Header("       Skins      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Skins extends OkaeriConfig {
    public Map<String, MinionSkin> minionSkins = FastMap.builder(String.class, MinionSkin.class)
            .put("grinch_skin", getGrinchSkin())

            .put("wheat_minion_1", getWheatMinion("Wheat Skin I", 1))
            .put("wheat_minion_2", getWheatMinion("Wheat Skin II", 2))
            .put("wheat_minion_3", getWheatMinion("Wheat Skin II", 3))
            .put("wheat_minion_4", getWheatMinion("Wheat Skin IV", 4))
            .put("wheat_minion_5", getWheatMinion("Wheat Skin V", 5))
            .put("wheat_minion_6", getWheatMinion("Wheat Skin VI", 6))
            .put("wheat_minion_7", getWheatMinion("Wheat Skin VII", 7))
            .put("wheat_minion_8", getWheatMinion("Wheat Skin VIII", 8))
            .put("wheat_minion_9", getWheatMinion("Wheat Skin IX", 9))
            .put("wheat_minion_10", getWheatMinion("Wheat Skin X", 10))
            .put("wheat_minion_11", getWheatMinion("Wheat Skin XI", 11))


            .put("diamond_minion_1", getDiamondSkin("Diamond Skin I", 1))
            .put("diamond_minion_2", getDiamondSkin("Diamond Skin II", 2))
            .put("diamond_minion_3", getDiamondSkin("Diamond Skin II", 3))
            .put("diamond_minion_4", getDiamondSkin("Diamond Skin IV", 4))
            .put("diamond_minion_5", getDiamondSkin("Diamond Skin V", 5))
            .put("diamond_minion_6", getDiamondSkin("Diamond Skin VI", 6))
            .put("diamond_minion_7", getDiamondSkin("Diamond Skin VII", 7))
            .put("diamond_minion_8", getDiamondSkin("Diamond Skin VIII", 8))
            .put("diamond_minion_9", getDiamondSkin("Diamond Skin IX", 9))
            .put("diamond_minion_10", getDiamondSkin("Diamond Skin X", 10))
            .put("diamond_minion_11", getDiamondSkin("Diamond Skin XI", 11))

            .put("snow_minion_1", getSnowMinion(1))
            .put("snow_minion_2", getSnowMinion(2))
            .put("snow_minion_3", getSnowMinion(3))
            .put("snow_minion_4", getSnowMinion(4))
            .put("snow_minion_5", getSnowMinion(5))
            .put("snow_minion_6", getSnowMinion(6))
            .put("snow_minion_7", getSnowMinion(7))
            .put("snow_minion_8", getSnowMinion(8))
            .put("snow_minion_9", getSnowMinion(9))


            .put("cow_minion_1", getCowMinion(1))
            .put("cow_minion_2", getCowMinion(2))
            .put("cow_minion_3", getCowMinion(3))
            .put("cow_minion_4", getCowMinion(4))
            .put("cow_minion_5", getCowMinion(5))
            .put("cow_minion_6", getCowMinion(6))
            .put("cow_minion_7", getCowMinion( 7))
            .put("cow_minion_8", getCowMinion(8))
            .put("cow_minion_9", getCowMinion(9))

            .build();


    public static Optional<MinionSkin> getSkin(String skin) {
        Skins skins = TheMinions.getApi().getConfigFiles().skins();
        return Optional.ofNullable(skins.minionSkins.getOrDefault(skin, null));
    }

    private static MinionSkin getGrinchSkin() {
        return MinionSkin.builder()
                .id("grinch_skin")
                .displayName("Grinch Skin")
                .helmet(ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "", Collections.emptyList())
                        .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTljODlhODg5YzRiYjQyMWVhOWM1NmNiZWFlN2FmMWU2ZDg1ZjRmYWZmMzRhY2JhNzJlMWE4ZDVjNDExNmE4In19fQ==")
                        .buildStack())
                .chestplate(FastStack.fastWithColor(XMaterial.LEATHER_CHESTPLATE, Color.BLUE))
                .leggings(FastStack.fastWithColor(XMaterial.LEATHER_LEGGINGS, Color.BLACK))
                .boots(FastStack.fastWithColor(XMaterial.LEATHER_BOOTS, Color.BLACK))
                .inHand(ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "", Collections.emptyList())
                        .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI0YjVjNzQ4MzA2MDlmOWNlN2JmYjhjYTI2ZmFjMTcxZWRiYzZiMjdjNjM3ZWExMDFmYzFhZTFhMmZhYzQ0MSJ9fX0=")
                        .buildStack())
                .build();
    }

    private static MinionSkin getWheatMinion(String displayName, int level) {
        Map<Integer, String> dataWithLevel = FastMap.builder(Integer.class, String.class)
                .put(1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNkZWYyNTI0ZTA2MjU0NzBlYmRmYTUyZDJlMGUzM2ZiOWJjYjI1NjgyZGUwOWZiNjljZWY5NmM5OWZjYTEyZiJ9fX0=")
                .put(2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI5OWUwODFiMTg4NzdhNzQ2MmIwYjRlMDk2ODZhOTQ0ZDk2NTliNzRmNmM5NTFlM2ZiMDYxMWMxZjJhOGFkYiJ9fX0=")
                .put(3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNlNmZmYTY1M2VkZjQxODYxYjhkOWQyNDg4NDM0ZDg3NTY5NDM2ZjdmYTljNmQ2OGFhYTUwOTE2MDRiYjI0In19fQ==")
                .put(4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjg1YzIyZmFmNWFkM2Y3MTg5ZmUxMTdjZTkzOTM3MjExYjE3OTAxMjhmMmJmZmQ5ODVjYmJjZDhhN2U3ZjU3MyJ9fX0=")
                .put(5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc2Y2ZlMThjODkyZTE1ZTBjOWRhOWZlNjI5MzA1ZmQ1OTU5NWMxMTRhMmZmMmMwMTY1NzMwMWMwYmU0N2U2YSJ9fX0=")
                .put(6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFhNWUzNzM4NTgyNjI0NmVkY2E5NzM4Y2U2NzVlNGM2MzJlYjk0MDY3YTAzNjQ3YjBkODBjYjhlZGNmOWVmYyJ9fX0=")
                .put(7, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJkNTgzMDFhNGMyYzFkMTJkOGFlM2VhMGE4ZjhkZDNjZjA0ODM3NGJjZTJhYTAwNjQ1NTY5MDVhMDA0MjhiYyJ9fX0=")
                .put(8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQxYjBjYjdlNDI5OWRiOWY4MTJjOTNjYzE2NjNlOTk4NDU3N2VhNzg1ZDUwNGIxM2JiYzljNzRlMmEyYzc1YiJ9fX0=")
                .put(9, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmY0ODc4ZTExYjdiMDNhNDEwNDI2ZDE1ODEyNjdkMDk5ZDgyNzBhZDBjYjBkY2MxNGZjNTExNmJkMmM3N2IwNiJ9fX0=")
                .put(10, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGM4NWYzMzA4MmMzYjIxMGFmMTBiY2JiMTYwMjI1NTNjMzAyZTQwNzE1ODNkYTRhZmY4ZjUzNzVmYmQxMWU3MiJ9fX0=")
                .put(11, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJjYjQ2ODM2Y2NjY2JhYjQyNGRhZDEzNTU3ZjIwOTYyM2Y5ZDZlYzVjNjY1ZjdiZTc2OWJlYmVjNGMxNTdmYSJ9fX0=")
                .build();

        return MinionSkin.builder()
                .id("wheat_skin_"+level)
                .displayName(displayName)
                .helmet(ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "", Collections.emptyList())
                        .headData(dataWithLevel.get(level))
                        .buildStack())
                .chestplate(FastStack.fastWithColor(XMaterial.LEATHER_CHESTPLATE, Color.YELLOW))
                .leggings(FastStack.fastWithColor(XMaterial.LEATHER_LEGGINGS, Color.YELLOW))
                .boots(FastStack.fastWithColor(XMaterial.LEATHER_BOOTS, Color.YELLOW))
                .inHand(FastStack.fast(XMaterial.DIAMOND_HOE, 1))
                .build();
    }

    private static MinionSkin getDiamondSkin(String displayName, int level) {
        Map<Integer, String> dataWithLevel = FastMap.builder(Integer.class, String.class)
                .put(1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRiMDA4MDY4ODIzODQ0ZDEyOWVmZDgzOWM4MzA2NTBkOThlZWE4MTA0Y2YyYjMyNmE5YmU3ZGZiMTdhYjk5ZCJ9fX0=")
                .put(2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJkMjA0ZjNiNGJiNGRjMzdhMDk0OWY1ODc3ODg0ODlhMWNkMTQ4ODIxYTEwN2FiMDU3NTgyY2VjMWJjZDEwMyJ9fX0=")
                .put(3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWI4YjQzYjMzZWI5OGIzZjhlOGZjYmZiMTE5MjliNzM2YTU2ZDcwMDE2M2RmNGUwZGNkYTIxYzM5N2Q4MWM3MSJ9fX0=")
                .put(4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ2YWU5OWEzYTlmM2VlNjI2M2U0ZDA5NzZkN2ZhNGNiNmYwNTU3YzBiNjE0NWMwY2JlODEzOGUzNjQ5NTRkZSJ9fX0=")
                .put(5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTA3YjkwNGQxNzJjNzIyNGYyOWZmYTQ0NmQ3OTc3MGMxNGM0MGUwYjExNDg4YTAxNGQzYWI2NzgwNTc3ZmRjOCJ9fX0=")
                .put(6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmNTZkOTQ5Mzk0ZTI1ZTU2ODZiOWYxODAwYmI1ZGY3YzUzMGY5ZTE2Nzg1ZWRiN2I0MDA3NmE1ZjkzYjlhMyJ9fX0=")
                .put(7, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ1ZmQwYzFkZGExZDRiMDBhYjM2MzQ4ZmNkY2NlZjViYTE5Y2I5MjAwOWMwM2M2ZTQ3MjkxZDZhYTk4NmQxNSJ9fX0=")
                .put(8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg4NmEzM2U0ZWQ5NzRhNGNlYjIzMTc4MGIyMzE2YjYzMGU0MzQ4NzQyNDlmZTUwZjM5MTk3YzQ1ZWU0YzFiOCJ9fX0=")
                .put(9, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRhOTBmNTdjZmZlMDZlZTEyNGFlYTM4YjMyZDY2ZjcwMTZhZTlkYjY0YTUwNTA4Y2E1Yzc0YWVjZjhmZGU2ZSJ9fX0=")
                .put(10, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRiODVkMjk5OTNkMWIzNWVlNDA0YmRjNWQxNmM1YTA0YTc2ODg1ZmQ5NWZkMzM3ZjIwMTQzYzM5YjU3NWE3OSJ9fX0=")
                .put(11, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBjM2ZlNjg3NDAwNDdjYTk3NjIzZWU5NzVkYjE2OTAyYWI1NDFjY2M2MTFmNGQ2NzA1NmQ1OTNiZjA4OWFiYiJ9fX0=")

                .build();

        return MinionSkin.builder()
                .id("diamond_skin_"+level)
                .displayName(displayName)
                .helmet(ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "", Collections.emptyList())
                        .headData(dataWithLevel.get(level))
                        .buildStack())
                .chestplate(FastStack.fastWithColor(XMaterial.LEATHER_CHESTPLATE, Color.BLUE))
                .leggings(FastStack.fastWithColor(XMaterial.LEATHER_LEGGINGS, Color.BLUE))
                .boots(FastStack.fastWithColor(XMaterial.LEATHER_BOOTS, Color.BLUE))
                .inHand(FastStack.fast(XMaterial.DIAMOND_PICKAXE, 1))
                .build();
    }

    private static MinionSkin getSnowMinion(int level) {
        Map<Integer, String> dataWithLevel = FastMap.builder(Integer.class, String.class)
                .put(1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkMTgwNjg0YzM1MjFjOWZjODk0NzhiYTQ0MDVhZTljZTQ5N2RhODEyNGZhMGRhNWEwMTI2NDMxYzRiNzhjMyJ9fX0=")
                .put(2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk5MjFiYWI1NGFmMTQwNDgxYzAxNmE1OWE4MTliMzY5NjY3YTRlNGZiMmYyNDQ5Y2VlYmY3Yzg5N2VkNTg4ZSJ9fX0=")
                .put(3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGUxMzg2MmQxZDBjNTJkMjcyZWNlMTA5ZTkyM2FmNjJhZWRlYmIxM2I1NmM0NzA4NWY0MTc1MmE1ZDRkNTllMiJ9fX0=")
                .put(4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ0ODVkOTBhMTI5ZmY2NzJkOTI4N2FmN2JmNDdmOGVjZTk0YWJlYjQ5NmJkYTM4MzY2MzMwODkzYWE2OTQ2NCJ9fX0=")
                .put(5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRhOWQzYmZhNDMxMjA2YWIzM2U2MmY4ODE1ZTQwOTJkYWU2ZThmYzlmMDRiOWEwMDVhMjA1MDYxZWE4OTVhOCJ9fX0=")
                .put(6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M1M2U5ZWY0YWJhM2E0MWZlOGUwM2M0M2U2YTMxMGVlYzAyMmQxMDg5ZmQ5YTkyZjNhZjhlZDhlZWQ0ZWMwMyJ9fX0=")
                .put(7, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTFmZDJiMzBmMmVmOTM3ODU0MDRjZjRjYTQyZTZmMjg3NTVlMjkzNWNkM2NhZTkxMDEyMWJmYTQzMjczNDVjMSJ9fX0=")
                .put(8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY1MzIyMWIxYjJlNDBhOTdhN2ExMGZiNDc3MTBlNjFiZGQ4NGUxNTA1MmRkODE3ZGEyZjg5NzgzMjQ4Mzc1ZSJ9fX0=")
                .put(9, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2FhMzcwYmVlYmU3N2NlZDViYTRkMTA2NTkxZDUyMzY0MGY1N2U3YzQ2YTRjZWNlYzYwYTRmZTBlYmFjNGE0YyJ9fX0=")
                .build();


        return MinionSkin.builder()
                .displayName("Snow Minion " + NumberUtil.toRoman(level))
                .helmet(ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "", Collections.emptyList())
                        .headData(dataWithLevel.get(level))
                        .buildStack())
                .chestplate(FastStack.fastWithColor(XMaterial.LEATHER_CHESTPLATE, Color.WHITE))
                .leggings(FastStack.fastWithColor(XMaterial.LEATHER_LEGGINGS, Color.WHITE))
                .boots(FastStack.fastWithColor(XMaterial.LEATHER_BOOTS, Color.WHITE))
                .inHand(FastStack.fast(XMaterial.DIAMOND_SHOVEL, 1))
                .build();
    }

    private static MinionSkin getCowMinion(int level) {
        Map<Integer, String> dataWithLevel = FastMap.builder(Integer.class, String.class)
                .put(1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRmZjBmNThjMmYyY2RjNzQyMzQwYTdhNzFlYjEzYmU5MjhiZmY2M2FmZjgzMzAwMmE0N2I3NDZiOTZiNDQ4OCJ9fX0=")
                .put(2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFjMDEwODIzNjgyZWRkZmQyNzdiODQ4MjVkODk3MjNjNjc1NDFkZjcyMTZhYTI3M2I5N2Y1YjZjMjRhNjg1MCJ9fX0=")
                .put(3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdlYzlhNDU1NWQ0NDcyOGVhNTQ0NzFmYzU4NjcyMjZkMTZhZDFkNTljMzk1ZTc3ZjU5MjY5ZmUyMzI0MmM1YSJ9fX0=")
                .put(4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk5ZjkzMjY0YWU4MzQyYzkzOWQyYjFlY2IxOGNlYjUzMjI2ZWI0ODM3OGMxYjAwYzg2N2E5MjNlOThmNTJkYyJ9fX0=")
                .put(5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU3MDhkYzIzYzQzOTlhNjE3OWVlMmY0OWYzMWQ2YzRmOTg0YzRkMmE1ZTUyMzc5YzgxNTJkOTlhYzczMDJkNCJ9fX0=")
                .put(6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA5NTRmYjk2YzNmNmM5ZDgzM2IyZTQwYjgzNTI2NTU0YWNlNmU0ZDJhMTIxYjAxYzY0N2Q5OGNlOWUyZDZmMiJ9fX0=")
                .put(7, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2VjNGEyOTA0MDIxMjE4ZGFkODI5NTQzNTAxYzI1OGE1MGRhNThiMWRiMjZkOWE5YWIzOWJjM2YyNzU5Mjg5OCJ9fX0=")
                .put(8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2YxODk5NjQ0ZDA5ZDY0OTE1YzgxN2FjZTQzYjE3Y2Y5YzRhOWQxNDAxMjQzOGJmOTdmNmIxN2IwZmZiY2RlMiJ9fX0=")
                .put(9, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmOWIxMDk1NDI4YWRlNDE1MjY0ZDk5MmJjNGUyOTNlNmMxOTc0YTcyYTA0YTlmYTMyMDA3NDg0MDdiODRkMCJ9fX0=")
                .build();


        return MinionSkin.builder()
                .displayName("Cow Minion " + NumberUtil.toRoman(level))
                .helmet(ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "", Collections.emptyList())
                        .headData(dataWithLevel.get(level))
                        .buildStack())
                .chestplate(FastStack.fastWithColor(XMaterial.LEATHER_CHESTPLATE, Color.MAROON))
                .leggings(FastStack.fastWithColor(XMaterial.LEATHER_LEGGINGS, Color.WHITE))
                .boots(FastStack.fastWithColor(XMaterial.LEATHER_BOOTS, Color.MAROON))
                .inHand(FastStack.fast(XMaterial.DIAMOND_SWORD, 1))
                .build();
    }
}

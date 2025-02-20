package com.qualityplus.crafting.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.crafting.base.category.Category;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration(path = "categories.yml")
@Header("================================")
@Header("       Categories      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Categories extends OkaeriConfig {
    public List<Category> categoryList = Arrays.asList(
            new Category("farming", "Farming", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFmMzI4Yzg3YjA2ODUwOWFjYTk4MzRlZmFjZTE5NzcwNWZlNWQ0ZjA4NzE3MzFiN2IyMWNkOTliOWZkZGMifX19=", 20),
            new Category("mining", "Mining", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTgzNjhkZmFmOTQ4NGUxMjNkMzBhNTc2ODI2ZTk3NjNkZDg2Mzg0MThjZGNhYmFkZjYxMzZjZTQ1NzQ5YWExMSJ9fX0=", 21),
            new Category("combat", "Combat", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ1MjhiMzIyOTY2MGYzZGZhYjQyNDE0ZjU5ZWU4ZmQwMWU4MDA4MWRkM2RmMzA4Njk1MzZiYTliNDE0ZTA4OSJ9fX0=", 22),
            new Category("fishing", "Fishing", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJhZDNjYzZkMzYzMWNhYTg4N2E5MWViYzVlNmE2NWNmMjU3ODAzYzdjN2FjZDU3ZDE5YTBhYzIyZmFlODQwMyJ9fX0=", 23),
            new Category("foraging", "Foraging", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0OTc3ODZkN2JhMTkyMzY5OTMyOTY2Njg2YWE5ZmI3MTQ3ZmE1ODg0ZjlhNjIwOWM0YTczZTJkNjBmMzlmNSJ9fX0=", 24),
            new Category("enchanting", "Enchanting", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2RjOTg1YTdhNjhjNTc0ZjY4M2MwYjg1OTUyMWZlYjNmYzNkMmZmYTA1ZmEwOWRiMGJhZTQ0YjhhYzI5YjM4NSJ9fX0=", 29),
            new Category("alchemy", "Alchemy", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ5MmNhOTQwNzkxMzZkMjUyNTcwM2QzNzVjMjU1N2VhYzIwMWVlN2RkMzljZTExYzY0YTljMzgxNDdlY2M0ZCJ9fX0=", 30),
            new Category("carpentry", "Carpentry", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzNjA0NTIwOGY5YjVkZGNmOGM0NDMzZTQyNGIxY2ExN2I5NGY2Yjk2MjAyZmIxZTUyNzBlZThkNTM4ODFiMSJ9fX0=", 31),
            new Category("runecrafting", "Runecrafting", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTAyNjc3MDUzZGM1NDI0NWRhYzRiMzk5ZDE0YWFlMjFlZTcxYTAxMGJkOWMzMzZjOGVjZWUxYTBkYmU4ZjU4YiJ9fX0=", 32),
            new Category("discoverer", "Discoverer", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5MTk2YjMzMGM2Yjg5NjJmMjNhZDU2MjdmYjZlY2NlNDcyZWFmNWM5ZDQ0Zjc5MWY2NzA5YzdkMGY0ZGVjZSJ9fX0=", 33)
    );

    public Category getCategory(String id) {
        return categoryList.stream().filter(category -> category.getId().equals(id)).findAny().orElse(null);
    }
}

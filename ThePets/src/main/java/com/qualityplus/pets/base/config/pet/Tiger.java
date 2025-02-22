package com.qualityplus.pets.base.config.pet;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.common.rewards.commands.CommandRewards;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.egg.PetEgg;
import com.qualityplus.pets.base.pet.egg.PetModelEngine;
import com.qualityplus.pets.base.pet.entity.PetEntityOptions;
import com.qualityplus.pets.base.pet.level.PetLevel;
import com.qualityplus.pets.base.pet.potion.PetPotion;
import com.qualityplus.pets.base.rewards.StatReward;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration(path = "pets/tiger_pet.yml")
@Header("================================")
@Header("       Tiger Pet      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Tiger extends OkaeriConfig {
    private final PetEntityOptions petEntityOptions = new PetEntityOptions("&8[Lvl %pet_level_number%] &7%player%'s %pet_egg_displayname%", XMaterial.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZkZjM3OGJlZTY5ODBhNzg0NTkzYTFhZTk4OTI4MGE2ZTk2MjFhOTE5MTcyZGE0OWQzMTgxNWYzYzQ5Mjg2ZSJ9fX0=", "DRIP_LAVA");
    private final PetEgg petEgg = PetEgg.builder()
            .petId("tiger")
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZkZjM3OGJlZTY5ODBhNzg0NTkzYTFhZTk4OTI4MGE2ZTk2MjFhOTE5MTcyZGE0OWQzMTgxNWYzYzQ5Mjg2ZSJ9fX0=")
            .displayName("&6Tiger")
            .eggDisplayName("&7[Lvl %pet_level_number%] %pet_egg_displayname%")
            .petModelEngine(PetModelEngine.builder().useModelEngine(false).build())
            .material(XMaterial.PLAYER_HEAD)
            .build();
    private final CommandRewards commandRewards = new CommandRewards(new HashMap<>());

    private final Map<Integer, PetLevel> petLevels = getPetLevelsMap();
    private final PetModelEngine petModelEngine = new PetModelEngine();
    private final String category = "foraging";
    private final Boolean enabled = true;
    private final String id = "tiger";
    private final int maxLevel = 50;

    public Pet getPet() {
        return Pet.builder()
                .id(id)
                .category(category)
                .maxLevel(maxLevel)
                .petEgg(petEgg)
                .enabled(enabled)
                .petEntityOptions(petEntityOptions)
                .petLevels(petLevels)
                .build();
    }

    private Map<Integer, PetLevel> getPetLevelsMap() {
        return FastMap.builder(Integer.class, PetLevel.class)
                .put(1, PetLevel.builder()
                        .requiredXp(15)
                        .petInfoInGUI(Arrays.asList("&8%pet_category_displayname% Pet", "", "&8+&a5 &9☠ Critic Damage",
                                "&8+&a5 &9☣ Critic Chance",
                                "",
                                "&6Fire Resistance Force",
                                "&fAdds &c10% &fFire resistance",
                                "&fprovided from a &cmagic potion&f!"))
                        .petPotions(Collections.singletonList(PetPotion.builder().potion("FIRE_RESISTANCE").level(2).build()))
                        .statRewards(Arrays.asList(new StatReward("critic_chance", 5), new StatReward("critic_damage", 5)))
                        .petInfoInMessage(Arrays.asList("&8+&a5 &9☠ Critic Damage",
                                "&8+&a5 &9☣ Critic Chance",
                                "",
                                "&6Fire Resistance Force",
                                "&fAdds &c+10% &fFire resistance",
                                "&fprovided from a &cmagic potion&f!"))
                        .commandRewards(Collections.emptyList())
                        .build())
                .put(5, PetLevel.builder()
                        .requiredXp(25)
                        .petInfoInGUI(Arrays.asList("&8%pet_category_displayname% Pet", "", "&8+&a10 &9☠ Critic Damage",
                                "&8+&a10 &9☣ Critic Chance",
                                "",
                                "&6Fire Resistance Force",
                                "&fAdds &c+25% &fFire resistance",
                                "&fprovided from a &cmagic potion&f!"))
                        .petPotions(Collections.singletonList(PetPotion.builder().potion("FIRE_RESISTANCE").level(3).build()))
                        .statRewards(Arrays.asList(new StatReward("critic_chance", 10), new StatReward("critic_damage", 10)))
                        .petInfoInMessage(Arrays.asList("&8+&a10 &9☠ Critic Damage",
                                "&8+&a10 &9☣ Critic Chance",
                                "",
                                "&6Fire Resistance Force",
                                "&fAdds &c+25% &fFire resistance",
                                "&fprovided from a &cmagic potion&f!"))
                        .commandRewards(Collections.emptyList())
                        .build())
                .build();

    }

}

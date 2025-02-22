package com.qualityplus.pets.base.config.pet;

import com.qualityplus.assistant.api.common.rewards.commands.CommandRewards;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.CustomKey;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.egg.PetEgg;
import com.qualityplus.pets.base.pet.egg.PetModelEngine;
import com.qualityplus.pets.base.pet.entity.PetEntityOptions;
import com.qualityplus.pets.base.pet.level.PetLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class PetConfig extends OkaeriConfig {
    @CustomKey("pet-entity-options")
    private PetEntityOptions petEntityOptions;
    @CustomKey("pet-egg")
    private PetEgg petEgg;
    @CustomKey("command-rewards")
    private CommandRewards commandRewards;
    @CustomKey("pet-levels")
    private Map<Integer, PetLevel> petLevels;
    @CustomKey("pet-model-engine")
    private PetModelEngine petModelEngine;
    @CustomKey("category")
    private String category;
    @CustomKey("enabled")
    private Boolean enabled;
    @CustomKey("id")
    private String id;
    @CustomKey("max-level")
    private int maxLevel;

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
}

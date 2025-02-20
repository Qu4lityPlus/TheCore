package com.qualityplus.pets.base.pet;

import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.pets.base.pet.egg.PetEgg;
import com.qualityplus.pets.base.pet.entity.PetEntityOptions;
import com.qualityplus.pets.base.pet.level.PetLevel;
import com.qualityplus.pets.base.pet.potion.PetPotion;
import com.qualityplus.pets.base.rewards.StatReward;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class Pet extends OkaeriConfig {
    private String id;
    private int maxLevel;
    private PetEgg petEgg;
    private boolean enabled;
    private String category;
    private Map<Integer, PetLevel> petLevels;
    private PetEntityOptions petEntityOptions;

    public double getLevelRequirement(int level) {
        return getPetLevel(level)
                .map(PetLevel::getRequiredXp)
                .orElse(1D);
    }

    public List<String> getPetCachedMessage(int level) {
        return getPetLevel(level)
                .map(PetLevel::getPetInfoInMessage)
                .orElse(Collections.emptyList());
    }

    public List<String> getPetCachedGUI(int level) {
        return getPetLevel(level)
                .map(PetLevel::getPetInfoInMessage)
                .orElse(Collections.emptyList());
    }

    public List<CommandReward> getCommandRewards(int level) {
        return getPetLevel(level)
                .map(PetLevel::getCommandRewards)
                .orElse(Collections.emptyList());
    }

    public List<StatReward> getStatRewards(int level) {
        return getPetLevel(level)
                .map(PetLevel::getStatRewards)
                .orElse(Collections.emptyList());
    }

    public List<PetPotion> getPetPotions(int level) {
        return getPetLevel(level)
                .map(PetLevel::getPetPotions)
                .orElse(Collections.emptyList());
    }

    public Optional<PetLevel> getPetLevel(int level) {
        Map<Integer, PetLevel> toWorkWith = petLevels;

        if (toWorkWith.containsKey(level)) {
            return Optional.of(toWorkWith.get(level));
        } else {
            int highestLevel = 1;

            for (Integer startLevel : toWorkWith.keySet()) {
                if (startLevel > level)
                    break;

                if (startLevel > highestLevel)
                    highestLevel = startLevel;
            }

            return Optional.ofNullable(toWorkWith.getOrDefault(highestLevel, null));
        }
    }
}

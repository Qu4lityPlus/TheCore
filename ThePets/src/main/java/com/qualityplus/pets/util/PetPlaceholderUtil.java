package com.qualityplus.pets.util;

import com.qualityplus.assistant.util.actionbar.ActionBarUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.pets.ThePets;
import com.qualityplus.pets.api.category.Categories;
import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.api.pet.category.PetCategory;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.egg.PetEgg;
import com.qualityplus.pets.persistance.data.PetData;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class PetPlaceholderUtil {
    /*public List<IPlaceholder> getPlaceholders(PetEntity petEntity) {
        Optional<Pet> pet = Optional.ofNullable(Pets.getByID(petEntity.getEgg().getPet().getId()));
        Optional<PetEntity> entity = PetEntityTracker.getByID(petEntity.getPetUniqueId());

        double maxXp = entity.map(PetEntity::getMaxXp).orElse(1D);
        double xp = entity.map(PetEntity::getXp).orElse(1D);
        double percentage = ActionBarUtils.getPercentageFromTotal(xp, maxXp);


        return PlaceholderBuilder.create(
                new Placeholder("pet_egg_egg_displayname", pet.map(p -> p.getPetEgg().getEggDisplayName()).orElse("")),
                new Placeholder("pet_egg_displayname", pet.map(p -> p.getPetEgg().getDisplayName()).orElse("")),

                new Placeholder("pet_egg_description", pet.map(p -> p.getPetEgg().getLore()).orElse(Collections.emptyList())),
                new Placeholder("pet_level_number", entity.map(PetEntity::getLevel).orElse(1)),
                new Placeholder("pet_level_progress", percentage),
                new Placeholder("pet_action_bar", ActionBarUtils.getReplacedBar(percentage)),
                new Placeholder("pet_xp", xp),
                new Placeholder("pet_max_xp", maxXp)
        ).get();
    }*/

    public PlaceholderBuilder getPetPlaceholders(Pet pet) {

        if (pet == null) return PlaceholderBuilder.empty();


        return PlaceholderBuilder.create(
                new Placeholder("pet_category_displayname", Categories.getByID(pet.getCategory()).map(PetCategory::getDisplayName).orElse("")),
                new Placeholder("pet_egg_egg_displayname", pet.getPetEgg().getEggDisplayName()),
                new Placeholder("pet_egg_displayname", pet.getPetEgg().getDisplayName())
        );
    }

    public PlaceholderBuilder getPetPlaceholders(String id) {
        return getPetPlaceholders(Pets.getByID(id));
    }

    /**
     * TODO
     *
     * Optimize this placeholderbuilder so it doesn't call getData in both
     */
    public PlaceholderBuilder getPetPlaceholders(UUID petUuid) {
        Optional<PetData> petData = ThePets.getApi().getPetsService().getData(petUuid);

        int level = petData.map(PetData::getLevel).orElse(1);

        return getPetPlaceholders(petUuid, level);
    }

    public PlaceholderBuilder getPetPlaceholders(PetData data) {
        Optional<PetData> petData = Optional.ofNullable(data);

        Optional<Pet> pet = petData.map(PetData::getPetId)
                .filter(Objects::nonNull)
                .map(Pets::getByID);

        int level = petData.map(PetData::getLevel).orElse(1);
        double xp = petData.map(PetData::getXp).orElse(0D);
        double maxXp = pet.map(pet1 -> pet1.getLevelRequirement(level + 1)).orElse(xp);
        double percentage = ActionBarUtils.getPercentageFromTotal(xp, maxXp);

        return PlaceholderBuilder.create(
                new Placeholder("pet_level_number", level),
                new Placeholder("pet_level_roman", NumberUtil.toRoman(level)),

                new Placeholder("pet_level_progress", percentage),
                new Placeholder("pet_action_bar", ActionBarUtils.getReplacedBar(percentage)),
                new Placeholder("pet_description_chat", pet.map(p -> p.getPetCachedMessage(level)).orElse(Collections.emptyList())),
                new Placeholder("pet_description_gui", pet.map(p -> p.getPetCachedGUI(level)).orElse(Collections.emptyList())),
                new Placeholder("pet_xp", xp),
                new Placeholder("pet_max_xp", maxXp)
        );
    }

    public PlaceholderBuilder getPetPlaceholders(UUID petUuid, int level) {
        Optional<PetData> petData = ThePets.getApi().getPetsService().getData(petUuid);

        Optional<Pet> pet = petData.map(PetData::getPetId)
                .filter(Objects::nonNull)
                .map(Pets::getByID);

        double xp = petData.map(PetData::getXp).orElse(0D);
        double maxXp = pet.map(pet1 -> pet1.getLevelRequirement(level + 1)).orElse(xp);
        double percentage = ActionBarUtils.getPercentageFromTotal(xp, maxXp);

        return PlaceholderBuilder.create(
                new Placeholder("pet_egg_displayname", pet.map(Pet::getPetEgg).map(PetEgg::getDisplayName).orElse("")),
                new Placeholder("pet_level_number", level),
                new Placeholder("pet_level_roman", NumberUtil.toRoman(level)),
                new Placeholder("pet_level_progress", percentage),
                new Placeholder("pet_action_bar", ActionBarUtils.getReplacedBar(percentage)),
                new Placeholder("pet_description_chat", pet.map(p -> p.getPetCachedMessage(level)).orElse(Collections.emptyList())),
                new Placeholder("pet_description_gui", pet.map(p -> p.getPetCachedGUI(level)).orElse(Collections.emptyList())),
                new Placeholder("pet_xp", xp),
                new Placeholder("pet_max_xp", maxXp)
        );
    }

    private List<String> getCachedLore(Pet pet, int level) {
        return pet.getPetCachedMessage(level);
    }
}

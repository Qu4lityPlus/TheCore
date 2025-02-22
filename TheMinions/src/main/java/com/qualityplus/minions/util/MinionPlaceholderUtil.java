package com.qualityplus.minions.util;

import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.time.HumanTime;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.persistance.data.MinionData;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class MinionPlaceholderUtil {
    public PlaceholderBuilder getMinionPlaceholders(Minion minion) {

        if (minion == null) return PlaceholderBuilder.empty();


        return PlaceholderBuilder.create(
                new Placeholder("minion_description", minion.getDescription()),
                new Placeholder("minion_egg_egg_displayname", minion.getMinionEgg().getEggDisplayName()),
                new Placeholder("minion_egg_displayname", minion.getMinionEgg().getDisplayName())
        );
    }

    public PlaceholderBuilder getMinionPlaceholders(final String id) {
        return getMinionPlaceholders(Minions.getByID(id));
    }

    public PlaceholderBuilder getMinionPlaceholders(final UUID petUuid) {
        final Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(petUuid);

        final int level = minionData.map(MinionData::getLevel).orElse(1);

        return getMinionPlaceholders(petUuid, level);
    }

    public PlaceholderBuilder getMinionPlaceholders(final MinionData data) {
        Optional<MinionData> petData = Optional.ofNullable(data);

        int level = petData.map(MinionData::getLevel).orElse(1);

        return MinionPlaceholderUtil.getMinionPlaceholders(petData
                .map(MinionData::getUuid)
                .orElse(null), level);
    }

    public PlaceholderBuilder getMinionPlaceholders(UUID petUuid, int level) {
        Optional<MinionData> petData = TheMinions.getApi().getMinionsService().getData(petUuid);

        Optional<Minion> pet = petData.map(MinionData::getMinionId)
                .filter(Objects::nonNull)
                .map(Minions::getByID);

        int resourcesGenerated = petData.map(MinionData::getResourcesGenerated).orElse(1);

        return PlaceholderBuilder.create(
                new Placeholder("minion_level_number", level),
                new Placeholder("minion_level_roman", NumberUtil.toRoman(level)),
                new Placeholder("minion_resources_generated", resourcesGenerated),
                new Placeholder("minion_max_storage", pet.map(p -> p.getMaxStorageForInv(level)).orElse(1)),
                new Placeholder("minion_time_between_actions", getSeconds(petData, pet, level) )
        );
    }

    @SuppressWarnings("all")
    private double getSeconds(Optional<MinionData> petData, Optional<Minion> pet, int level) {
        double mainTime = pet.map(p -> p.getTimer(level))
                .map(HumanTime::getSeconds)
                .map(Double::valueOf)
                .orElse(1D);
        double fuelReduction = petData.map(data -> data.getFuelReductionSeconds(mainTime)).orElse(0D);
        double upgradeReduction = petData.map(data -> data.getUpgradesReductionSeconds(mainTime)).orElse(0D);

        return mainTime - fuelReduction - upgradeReduction;
    }
}

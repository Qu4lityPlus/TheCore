package com.qualityplus.minions.base.minions.entity.getter;

import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.persistance.data.MinionData;

import java.util.Optional;

public interface LevelGetter extends MinionUUIDGetter {

    default int getLevel() {
        final Optional<MinionData> petData = TheMinions.getApi().getMinionsService().getData(getMinionUniqueId());

        return petData.map(MinionData::getLevel).orElse(1);
    }
}

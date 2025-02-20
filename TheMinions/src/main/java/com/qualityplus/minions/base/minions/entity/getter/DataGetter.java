package com.qualityplus.minions.base.minions.entity.getter;

import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.persistance.data.MinionData;

import java.util.Optional;

public interface DataGetter extends MinionUUIDGetter {
    public default Optional<MinionData> getData() {
        return TheMinions.getApi().getMinionsService().getData(getMinionUniqueId());
    }
}

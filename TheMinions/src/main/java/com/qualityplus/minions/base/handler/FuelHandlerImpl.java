package com.qualityplus.minions.base.handler;

import com.qualityplus.minions.api.handler.FuelHandler;
import com.qualityplus.minions.base.minions.entity.getter.DataGetter;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.FuelEntity;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public final class FuelHandlerImpl implements FuelHandler, DataGetter {
    private final UUID minionUniqueId;

    /* Remove Fuel if expired */
    @Override
    public void removeFuel() {
        final Optional<MinionData> data = getData();

        final FuelEntity fuel = data.map(MinionData::getFuel).orElse(null);

        if (fuel == null) {
            return;
        }

        if (fuel.getMarkable().isMarked()) {
            return;
        }

        data.ifPresent(MinionData::removeFuel);
    }

    @Override
    public UUID getMinionUniqueId() {
        return minionUniqueId;
    }
}

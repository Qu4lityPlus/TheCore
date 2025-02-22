package com.qualityplus.minions.base.handler;

import com.qualityplus.minions.api.handler.StorageHandler;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.MinionStorageState;
import com.qualityplus.minions.base.minions.entity.getter.MinionItemsGetter;
import com.qualityplus.minions.base.minions.minion.Minion;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class StorageHandlerImpl implements StorageHandler, MinionItemsGetter {
    private final MinionEntity minionEntity;
    private final Minion minion;

    @Override
    public MinionStorageState getMinionStorageState() {
        return getMinionState(minionEntity.getState().getFakeInventory(), minionEntity.getMinionUniqueId(), minion);
    }
}

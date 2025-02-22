package com.qualityplus.minions.base.newarch.base.handler;

import com.qualityplus.minions.api.TheMinionsAPI;
import com.qualityplus.minions.base.newarch.api.entity.NewMinionEntity;
import com.qualityplus.minions.base.newarch.api.handler.NewStorageHandler;

public final class NewStorageHandlerImpl implements NewStorageHandler {
    private TheMinionsAPI api;

    @Override
    public boolean hasFullStorage(NewMinionEntity entity) {
        return false;
    }
}

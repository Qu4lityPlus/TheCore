package com.qualityplus.minions.base.newarch.base.task;

import com.qualityplus.minions.base.minions.entity.status.MinionStatus;
import com.qualityplus.minions.base.newarch.api.entity.NewMinionEntity;
import com.qualityplus.minions.base.newarch.api.handler.NewStorageHandler;
import com.qualityplus.minions.base.newarch.api.handler.NewLayoutHandler;
import com.qualityplus.minions.base.newarch.api.service.NewMinionEntityService;

public final class MinionStateTask implements Runnable {
    private NewMinionEntityService minionEntityService;
    private NewStorageHandler storageHandler;
    private NewLayoutHandler layoutHandler;

    @Override
    public void run() {
        for (final NewMinionEntity entity : this.minionEntityService.getMinionEntities()) {
            if (!this.layoutHandler.hasValidLayout(entity)) {
                entity.updateStatus(MinionStatus.INVALID_LAYOUT);
            } else if (this.storageHandler.hasFullStorage(entity)) {
                entity.updateStatus(MinionStatus.STORAGE_FULL);
            } else {
                entity.updateStatus(MinionStatus.IDEAL_LAYOUT);
            }
        }
    }
}

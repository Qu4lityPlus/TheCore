package com.qualityplus.minions.api.service;

import com.qualityplus.minions.base.minions.entity.mob.MinionMobEntity;
import com.qualityplus.minions.base.minions.minion.Minion;

public interface MinionMobSpawnService {
    public void spawnMinionMob(
            final Minion minion,
            final MinionMobEntity entity
    );
}

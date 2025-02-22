package com.qualityplus.minions.api.service;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.state.MinionState;

public interface MinionDisplayNameService {
    public void updateDisplayName(final MinionEntity minionEntity);
}

package com.qualityplus.minions.base.minions.entity.scheduler;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.Map;
import java.util.UUID;

@Component
public final class MinionTickServiceImpl implements MinionTickService {
    @Override
    public void tick(Map.Entry<UUID, MinionEntity> entry) {
        entry.getValue().tick();
    }
}
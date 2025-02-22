package com.qualityplus.minions.base.newarch.base.service;

import com.qualityplus.minions.base.newarch.api.entity.NewMinionEntity;
import com.qualityplus.minions.base.newarch.api.service.NewMinionEntityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class NewMinionEntityServiceImpl implements NewMinionEntityService {
    private final Map<UUID, NewMinionEntity> entities = new HashMap<>();

    @Override
    public List<NewMinionEntity> getMinionEntities() {
        return List.copyOf(this.entities.values());
    }

    @Override
    public void addMinionEntity(final NewMinionEntity newMinionEntity) {
        this.entities.put(newMinionEntity.getId(), newMinionEntity);
    }

    @Override
    public void removeMinionEntity(final NewMinionEntity newMinionEntity) {
        this.entities.remove(newMinionEntity.getId());
    }
}

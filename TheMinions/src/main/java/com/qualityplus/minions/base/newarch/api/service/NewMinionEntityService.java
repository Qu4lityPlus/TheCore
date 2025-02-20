package com.qualityplus.minions.base.newarch.api.service;

import com.qualityplus.minions.base.newarch.api.entity.NewMinionEntity;

import java.util.List;

public interface NewMinionEntityService {
    public List<NewMinionEntity> getMinionEntities();

    public void addMinionEntity(final NewMinionEntity newMinionEntity);

    public void removeMinionEntity(final NewMinionEntity newMinionEntity);
}

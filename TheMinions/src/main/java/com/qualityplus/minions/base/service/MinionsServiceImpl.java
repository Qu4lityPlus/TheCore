package com.qualityplus.minions.base.service;

import com.qualityplus.minions.api.service.MinionsService;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.*;

@Component
public final class MinionsServiceImpl implements MinionsService {
    private final Map<UUID, MinionData> dataMap = new HashMap<>();

    @Override
    public Optional<MinionData> getData(final UUID uuid) {
        return Optional.ofNullable(dataMap.getOrDefault(uuid, null));
    }

    @Override
    public void addData(MinionData data) {
        dataMap.put(data.getUuid(), data);
    }

    @Override
    public void removeData(MinionData data) {
        dataMap.remove(data.getUuid());
    }

    @Override
    public List<UUID> getAllKeys() {
        return new ArrayList<>(dataMap.keySet());
    }
}

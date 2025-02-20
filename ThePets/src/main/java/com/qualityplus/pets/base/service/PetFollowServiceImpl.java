package com.qualityplus.pets.base.service;

import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.api.service.PetFollowService;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.Map;
import java.util.UUID;

@Component
public final class PetFollowServiceImpl implements PetFollowService {
    @Override
    public void follow(Map.Entry<UUID, PetEntity> entry) {
        entry.getValue().followOwner();
    }
}

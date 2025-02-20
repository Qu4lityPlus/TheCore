package com.qualityplus.pets.base.service;

import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.api.service.PetParticleService;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.Map;
import java.util.UUID;

@Component
public final class PetParticleServiceImpl implements PetParticleService {
    @Override
    public void spellParticle(Map.Entry<UUID, PetEntity> entry) {
        entry.getValue().spellParticle();
    }
}

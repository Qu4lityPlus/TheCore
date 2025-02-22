package com.qualityplus.minions.persistance;

import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.DocumentPersistence;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.repository.DocumentRepository;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.repository.annotation.DocumentCollection;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.repository.annotation.DocumentPath;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.DependsOn;

import java.util.Optional;
import java.util.UUID;

@DependsOn(type = DocumentPersistence.class, name = "persistence")
@DocumentCollection(path = "minions", keyLength = 36)
public interface MinionsRepository extends DocumentRepository<UUID, MinionData> {
    @DocumentPath("uuid")
    Optional<MinionData> findByUuid(UUID uuid);


    default MinionData get(MinionData petData) {
        MinionData data = this.findOrCreateByPath(petData.getUuid());

        data.setMinionId(petData.getMinionId());
        data.setLevel(petData.getLevel());
        data.setUuid(petData.getUuid());
        data.setOwner(petData.getOwner());

        return data;
    }
}

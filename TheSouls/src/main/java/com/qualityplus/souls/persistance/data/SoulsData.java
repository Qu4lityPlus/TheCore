package com.qualityplus.souls.persistance.data;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data @EqualsAndHashCode(callSuper = true)
public final class SoulsData extends Document {
    private UUID uuid;
    private String name;
    private List<UUID> soulsCollected;
    private List<UUID> tiaSoulsCollected;

    public void removeAmount(int amount) {
        if (amount <= 0) return;

        tiaSoulsCollected = tiaSoulsCollected.subList(0, tiaSoulsCollected.size() - amount);
    }
}

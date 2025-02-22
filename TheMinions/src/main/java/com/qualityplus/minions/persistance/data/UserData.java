package com.qualityplus.minions.persistance.data;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class UserData extends Document {
    private Set<UUID> minionsPlaced;
    private String name;
    private UUID uuid;

    public int getMinionsToPlace() {
        checkMinionsList();

        return Optional.ofNullable(minionsPlaced)
                .map(Set::size)
                .orElse(0);
    }

    public void addMinion(UUID minionUuid) {
        checkMinionsList();

        minionsPlaced.add(minionUuid);
    }

    public void removeMinion(UUID minionUuid) {
        checkMinionsList();

        minionsPlaced.remove(minionUuid);
    }

    private void checkMinionsList() {
        if (minionsPlaced != null) return;
        minionsPlaced = new HashSet<>();
    }
}

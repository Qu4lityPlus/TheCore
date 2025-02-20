package com.qualityplus.pets.persistance.data;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class PetData extends Document {
    private UUID uuid;
    private int level;
    private double xp;
    private String petId;

    public void addXp(double xp) {
        this.xp += xp;
    }

    public void addLevel(int i) {
        this.level += i;
    }
}

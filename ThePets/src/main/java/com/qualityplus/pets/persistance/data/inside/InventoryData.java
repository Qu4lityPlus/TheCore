package com.qualityplus.pets.persistance.data.inside;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
public final class InventoryData extends Document{
    private List<InventoryPet> pets = new ArrayList<>();

    public void addInventoryPet(UUID uuid, String petId) {
        pets.add(new InventoryPet(uuid, petId));
    }

    public void removePet(UUID uuid) {
        pets.stream()
                .filter(pet -> pet.getUuid().equals(uuid))
                .findFirst()
                .ifPresent(pets::remove);
    }
}

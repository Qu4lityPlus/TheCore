package com.qualityplus.pets.persistance.data;

import com.qualityplus.pets.ThePets;
import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.persistance.data.inside.InventoryData;
import com.qualityplus.pets.persistance.data.inside.SpawnedData;
import com.qualityplus.pets.persistance.data.inside.UserSettings;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public final class UserData extends Document {
    private InventoryData inventoryData = new InventoryData();
    private UserSettings userSettings = new UserSettings();
    private SpawnedData spawnedData = new SpawnedData();
    private String name;
    private UUID uuid;

    public void switchConvertToItemMode() {
        boolean convertToItemMode = userSettings.isConvertPetToItemMode();

        userSettings.setConvertPetToItemMode(!convertToItemMode);
    }

    public void switchPetsHiddenMode() {
        boolean petsAreHidden = userSettings.isPetsAreHidden();

        userSettings.setPetsAreHidden(!petsAreHidden);
    }


    public Optional<PetData> getSpawnedPetData() {
        return Optional.ofNullable(spawnedData.getSpawnedPetUUID())
                .flatMap(ThePets.getApi().getPetsService()::getData);
    }

    public Optional<Pet> getSpawnedPet() {
        return Optional.ofNullable(spawnedData.getSpawnedPetId())
                .map(Pets::getByID);
    }
}

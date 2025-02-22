package com.qualityplus.pets.base.pet.factory;

import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.egg.PetEgg;
import com.qualityplus.pets.base.pet.entity.ArmorStandPet;
import com.qualityplus.pets.base.pet.entity.ModelEnginePet;

import java.util.UUID;

public final class PetEntityFactory {
    public static PetEntity create(UUID petUniqueId, UUID owner, Pet pet) {
        PetEgg petEgg = pet.getPetEgg();

        if (petEgg.getPetModelEngine().isUseModelEngine())
            return ModelEnginePet.create(petUniqueId, owner, pet);
        else
            return ArmorStandPet.create(petUniqueId, owner, pet, true);
    }
}

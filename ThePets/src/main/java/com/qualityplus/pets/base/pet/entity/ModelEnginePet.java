package com.qualityplus.pets.base.pet.entity;

import com.qualityplus.pets.ThePets;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.base.pet.Pet;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.AnimationHandler;
import com.ticxo.modelengine.api.animation.property.IAnimationProperty;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Optional;
import java.util.UUID;

public final class ModelEnginePet extends MinecraftPet {
    private ArmorStand armorStand;

    private ModelEnginePet(UUID petUniqueId, UUID owner, Pet pet) {
        super(petUniqueId, owner, pet);
    }

    public static ModelEnginePet create(UUID petUniqueId, UUID owner, Pet pet) {
        return new ModelEnginePet(petUniqueId, owner, pet);
    }

    @Override
    public void spawn() {
        super.spawn();

        Optional.ofNullable(getNextLocation())
                .ifPresent(this::createArmorStand);
    }

    @Override
    public void deSpawn(PetEntity.DeSpawnReason deSpawnReason) {
        super.deSpawn(deSpawnReason);

        Optional.ofNullable(armorStand)
                .filter(ModelEnginePet::entityIsValid)
                .ifPresent(Entity::remove);
    }

    @Override
    public void update() {
        super.update();

        Optional.ofNullable(armorStand).ifPresent(stand -> stand.setCustomName(getDisplayName()));
    }

    @Override
    public void prepareToLevelUp() {
        super.removePotions();
        super.removeStats();
    }

    @Override
    public Location getSpawn() {
        return getNextLocation();
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }


    @Override
    public void followOwner() {
        Optional.ofNullable(getNextLocation())
                .filter(location -> entityIsValid(armorStand))
                .ifPresent(this::teleportAndRotate);
    }

    private void teleportAndRotate(Location location) {
        armorStand.teleport(location);
        armorStand.setRotation((float) (20 * count / (2 * Math.PI)), 0f);
    }


    private static boolean entityIsValid(ArmorStand armorStand) {
        return armorStand != null && !armorStand.isDead();
    }

    private void createArmorStand(Location location) {

        armorStand = location.getWorld().spawn(location, ArmorStand.class);

        armorStand.setSmall(true);
        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setCollidable(false);
        armorStand.setCustomNameVisible(true);

        String modelId = pet.getPetEgg().getPetModelEngine().getModelId();
        String animationId = pet.getPetEgg().getPetModelEngine().getModelEngineAnimation();

        ActiveModel model = ModelEngineAPI.createActiveModel(modelId);

        if (animationId != null) {
            AnimationHandler animationHandler = model.getAnimationHandler();
            IAnimationProperty animationProperty = animationHandler.getAnimation(animationId);

            if (animationProperty != null) {
                animationHandler.playAnimation(animationProperty, true);
            } else {
                Bukkit.getConsoleSender().sendMessage("Animation $meAnimation not found in model" + modelId + ", defaulting to walk!");

                IAnimationProperty animationPropertyWalk = animationHandler.getAnimation("walk");
                if (animationPropertyWalk != null) {
                    animationHandler.playAnimation(animationPropertyWalk, true);
                } else {
                    Bukkit.getConsoleSender().sendMessage("Walk animation not found in " + modelId);
                }
            }
        }

        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(armorStand);
        modeledEntity.addModel(model, true);

        update();
    }

}

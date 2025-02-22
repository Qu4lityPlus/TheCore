package com.qualityplus.pets.api.pet.entity;

import org.bukkit.Location;

import java.util.UUID;

public interface PetEntity {
    UUID getOwner();
    void followOwner();
    void spellParticle();
    UUID getPetUniqueId();
    void spawn();
    void deSpawn(final DeSpawnReason reason);
    void update();
    void prepareToLevelUp();

    Location getSpawn();

    void load();
    void unload();


    public static enum DeSpawnReason{
        SERVER_TURNED_OFF,
        PLAYER_DE_SPAWN_PET
    }
}

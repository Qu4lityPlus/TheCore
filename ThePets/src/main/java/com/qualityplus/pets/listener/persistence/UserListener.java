package com.qualityplus.pets.listener.persistence;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.factory.PetEntityFactory;
import com.qualityplus.pets.base.pet.tracker.PetEntityTracker;
import com.qualityplus.pets.persistance.UserPetRepository;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.pets.persistance.data.UserData;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.lib.eu.okaeri.tasker.core.Tasker;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@Component
public final class UserListener implements Listener {
    private @Inject UserPetRepository repository;
    private @Inject Plugin plugin;
    private @Inject Tasker tasker;
    private @Inject Box box;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        tasker.newChain()
                .async(() -> repository.get(event.getPlayer()))
                .acceptSync(box.service()::addData)
                .acceptSync(this::loadPetIfWasSpawned)
                .execute();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        savePlayerData(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerKickEvent event) {
        savePlayerData(event.getPlayer());
    }

    private void savePlayerData(Player player) {

        Optional<UserData> data = box.service().getData(player.getUniqueId());

        data.ifPresent(this::deSpawnPetItWasSpawned);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> data.ifPresent(Document::save));

    }

    private void loadPetIfWasSpawned(UserData data) {
        Optional<PetData> petData = data.getSpawnedPetData();

        if (!petData.isPresent()) return;

        Pet pet = Pets.getByID(petData.get().getPetId());

        if (pet == null) return;

        PetEntity petEntity = PetEntityFactory.create(petData.get().getUuid(), data.getUuid(), pet);

        petEntity.spawn();
    }

    private void deSpawnPetItWasSpawned(UserData data) {
        Optional<PetData> petData = data.getSpawnedPetData();

        if (!petData.isPresent()) return;

        Optional<PetEntity> petEntity = PetEntityTracker.getByID(petData.get().getUuid());

        if (!petEntity.isPresent()) return;

        petEntity.get().deSpawn(PetEntity.DeSpawnReason.SERVER_TURNED_OFF);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> petData.ifPresent(petData1 -> petData1.save()));


    }
}
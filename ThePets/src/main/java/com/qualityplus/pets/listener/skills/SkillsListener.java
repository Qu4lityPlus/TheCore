package com.qualityplus.pets.listener.skills;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.pets.persistance.data.UserData;
import com.qualityplus.skills.base.event.SkillsXPGainEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public final class SkillsListener implements Listener {
    private @Inject Box box;

    @EventHandler(ignoreCancelled = true)
    public void onGainXp(SkillsXPGainEvent event) {
        Player player = event.getPlayer();

        Optional<UserData> data = box.service().getData(player.getUniqueId());

        if (!data.isPresent()) return;

        Optional<PetData> spawnedPet = data.get().getSpawnedPetData();

        if (!spawnedPet.isPresent()) return;

        box.petService().addXp(spawnedPet.get(), Math.max(box.files().config().petXpToGiveSkillAmount, 1));
    }
}

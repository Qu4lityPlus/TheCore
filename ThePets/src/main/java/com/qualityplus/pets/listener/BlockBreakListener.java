package com.qualityplus.pets.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.pets.persistance.data.UserData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public final class BlockBreakListener implements Listener {
    private @Inject Box box;

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Optional<UserData> data = box.service().getData(player.getUniqueId());

        if (!data.isPresent()) return;

        Optional<PetData> spawnedPet = data.get().getSpawnedPetData();

        if (!spawnedPet.isPresent()) return;

        box.petService().addXp(spawnedPet.get(), Math.max(box.files().config().petXpToGiveBlockBreakAmount, 1));
    }
}

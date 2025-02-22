package com.qualityplus.pets.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.base.pet.entity.tracker.PetArmorStandTracker;
import com.qualityplus.pets.base.pet.tracker.PetEntityTracker;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@Component
public final class PetChunkListener implements Listener {
    private @Inject Plugin plugin;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(final ChunkLoadEvent e) {
        final Chunk chunk = e.getChunk();
        for (final PetEntity entity : PetEntityTracker.values()) {
            final Location location = entity.getSpawn();

            if (!isIn(chunk.getChunkSnapshot(), location)) {
                continue;
            }

            entity.load();
        }

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(final ChunkUnloadEvent e) {
        for (final Entity entity : e.getChunk().getEntities()) {
            if (!(entity instanceof ArmorStand)) {
                continue;
            }

            final Optional<PetEntity> minionEntity = PetArmorStandTracker.getByID(entity.getUniqueId());

            if (minionEntity.isEmpty()) {
                continue;
            }

            minionEntity.get().unload();
        }
    }


    public boolean isIn(ChunkSnapshot chunk, Location l) {
        Chunk loc = l.getChunk();

        return loc.getWorld().getName().equals(chunk.getWorldName()) && loc.getX() == chunk.getX()
                && loc.getZ() == chunk.getZ();
    }
}

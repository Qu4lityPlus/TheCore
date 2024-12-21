package com.qualityplus.minions.listener.chunk;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.tracker.MinionArmorStandTracker;
import com.qualityplus.minions.base.minions.entity.tracker.MinionEntityTracker;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
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
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public final class ChunkListenerNewest implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();

        for (MinionEntity entity : MinionEntityTracker.values()) {
            if (entity.getState().isLoaded()) continue;

            Location location = entity.getState().getSpawn();

            if (!isIn(chunk.getChunkSnapshot(), location)) return;

            if (entity.getState().isLoaded()) continue;

            entity.load();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(EntitiesUnloadEvent e) {

        for (Entity entity : e.getEntities()) {

            if (!(entity instanceof ArmorStand)) continue;

            Optional<MinionEntity> minionEntity = MinionArmorStandTracker.getByID(entity.getUniqueId());

            if (!minionEntity.isPresent()) continue;

            if (!minionEntity.get().getState().isLoaded()) continue;

            minionEntity.get().unload();
        }
    }


    public boolean isIn(ChunkSnapshot chunk, Location l) {
        Chunk loc = l.getChunk();

        return loc.getWorld().getName().equals(chunk.getWorldName()) && loc.getX() == chunk.getX()
                && loc.getZ() == chunk.getZ();
    }
}
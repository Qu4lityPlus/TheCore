package com.qualityplus.minions.listener.chunk;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.tracker.MinionArmorStandTracker;
import com.qualityplus.minions.base.minions.entity.tracker.MinionEntityTracker;
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

import java.util.*;

public final class ChunkListenerLegacy implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();

        for (MinionEntity entity : MinionEntityTracker.values()) {
            Location location = entity.getState().getSpawn();

            if (!isIn(chunk.getChunkSnapshot(), location)) return;

            entity.spawnMinionEntity();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(ChunkUnloadEvent e) {

        for (Entity entity : e.getChunk().getEntities()) {
            if (!(entity instanceof ArmorStand)) return;

            Optional<MinionEntity> minionEntity = MinionArmorStandTracker.getByID(entity.getUniqueId());

            if (!minionEntity.isPresent()) continue;


            minionEntity.get().unloadMinionEntity();

        }
    }


    public boolean isIn(ChunkSnapshot chunk, Location l) {
        Chunk loc = l.getChunk();

        return loc.getWorld().getName().equals(chunk.getWorldName()) && loc.getX() == chunk.getX()
                && loc.getZ() == chunk.getZ();
    }
}
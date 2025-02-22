package com.qualityplus.minions.base.service;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.minions.api.service.MinionMobSpawnService;
import com.qualityplus.minions.base.minions.entity.mob.MinionMobEntity;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.mob.MinionMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

/**
 * Service dedicated to spawn mobs from minions mob killers
 */
@Component
public final class MinionMobSpawnServiceImpl implements MinionMobSpawnService {
    @Override
    public void spawnMinionMob(final Minion minion, final MinionMobEntity entity) {
        final Location location = entity.getLocation();
        if (!isValidLocation(location)) {
            return;
        }

        final MinionMob minionMob = minion.getMinionLayout().getMinionMob();
        if (minionMob.isFromMythicMobs()) {
            spawnMobFromMythicMobs(minionMob, location);
            return;
        }

        spawnVanillaMob(minionMob, location);
    }

    @SuppressWarnings("all")
    private void spawnVanillaMob(final MinionMob minionMob, final Location location) {
        final EntityType entityType = minionMob.getEntityType();

        if (entityType == null) {
            return;
        }

        final World world = location.getWorld();

        world.spawnEntity(location, entityType);
    }

    private void spawnMobFromMythicMobs(final MinionMob minionMob, final Location location) {
        TheAssistantPlugin.getAPI().getAddons().getMythicMobs().spawn(minionMob.getId(), location, minionMob.getMythicMobsLevel());
    }

    private boolean isValidLocation(final Location location) {
        if (location == null) {
            return false;
        }

        return location.getWorld() != null;
    }
}

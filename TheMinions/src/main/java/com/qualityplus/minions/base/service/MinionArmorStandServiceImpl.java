package com.qualityplus.minions.base.service;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.service.MinionArmorStandService;
import com.qualityplus.minions.base.minions.entity.tracker.MinionArmorStandTracker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.concurrent.CompletableFuture;

@Component
public final class MinionArmorStandServiceImpl implements MinionArmorStandService {
    @Override
    public CompletableFuture<ArmorStand> spawnArmorStand(final MinionEntity minionEntity, final Location location) {
        final CompletableFuture<ArmorStand> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTask(TheMinions.getInstance(), () -> {
            final ArmorStand entity = ArmorStandUtil.createDefault(location);

            MinionArmorStandTracker.registerNewEntity(entity, minionEntity);

            minionEntity.setEntity(entity);

            future.complete(entity);
        });

        return future;
    }
}

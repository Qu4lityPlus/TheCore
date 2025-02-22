package com.qualityplus.minions.api.service;

import com.qualityplus.minions.api.minion.MinionEntity;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.concurrent.CompletableFuture;

public interface MinionArmorStandService {
    public CompletableFuture<ArmorStand> spawnArmorStand(final MinionEntity minionEntity, final Location location);
}

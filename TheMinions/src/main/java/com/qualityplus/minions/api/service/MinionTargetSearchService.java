package com.qualityplus.minions.api.service;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.mob.MinionMobEntity;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.concurrent.CompletableFuture;

public interface MinionTargetSearchService {
    public CompletableFuture<Block> getTargetBlock(final MinionEntity minionEntity);
    public CompletableFuture<MinionMobEntity> getTargetEntity(final MinionEntity minionEntity);
}

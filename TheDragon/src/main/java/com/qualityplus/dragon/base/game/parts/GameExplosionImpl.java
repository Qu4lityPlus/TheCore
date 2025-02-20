package com.qualityplus.dragon.base.game.parts;

import com.qualityplus.assistant.api.addons.paster.session.PasterSession;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.scheduler.PlatformScheduler;
import com.qualityplus.dragon.api.game.part.GameExplosion;
import com.qualityplus.dragon.base.configs.Config;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.lib.eu.okaeri.tasker.core.Tasker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@Component
public final class GameExplosionImpl implements GameExplosion {
    private final List<FallingBlock> fallingBlocks = new ArrayList<>();
    private @Inject("scheduler") PlatformScheduler scheduler;
    private CompletableFuture<Void> future;
    private @Inject Config config;
    private @Inject Tasker tasker;

    @Override
    public CompletableFuture<Void> makeBlockExplosion(final PasterSession session) {
        this.future = new CompletableFuture<>();

        spawnFallingBlocks(session).thenRun(this::clearBlocks);

        return future;
    }

    private CompletableFuture<Void> spawnFallingBlocks(PasterSession session) {
        CompletableFuture<Void> future1 = new CompletableFuture<>();

        tasker.newChain()
                        .sync(() -> spawnBlocks(session))
                        .acceptSync((Consumer<Void>) value -> future1.complete(null))
                        .execute();

        return future1;
    }

    private Void spawnBlocks(final PasterSession session) {
        Optional.ofNullable(session)
                .map(PasterSession::getAllBlocks)
                .orElse(Collections.emptyList())
                .forEach(this::spawnIndividualBlock);

        return null;
    }

    private void spawnIndividualBlock(final Block block) {
        Material material = block.getType();

        byte data = block.getData();

        block.setType(Material.AIR);

        if (!ThreadLocalRandom.current().nextBoolean()) return;

        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), material, data);

        fallingBlock.setVelocity(new Vector(randomDouble(), 0.95D, randomDouble()));

        fallingBlock.setDropItem(false);

        fallingBlocks.add(fallingBlock);

    }

    private void clearBlocks() {
        scheduler.runLater(this::clearBlocksTask, 60, false);
    }

    private void clearBlocksTask() {
        fallingBlocks.forEach(this::clearIndividualFallingBlock);
        fallingBlocks.clear();
        future.complete(null);
    }

    private void clearIndividualFallingBlock(FallingBlock fallingBlock) {
        if (fallingBlock.isOnGround()) fallingBlock.getLocation().getBlock().setType(Material.AIR);

        if (!fallingBlock.isDead()) fallingBlock.remove();
    }


    private double randomDouble() {
        return -0.5D + ThreadLocalRandom.current().nextDouble();
    }
}

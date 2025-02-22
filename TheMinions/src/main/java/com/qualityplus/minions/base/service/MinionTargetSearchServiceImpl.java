package com.qualityplus.minions.base.service;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.service.MinionTargetSearchService;
import com.qualityplus.minions.base.minions.entity.mob.MinionMobEntity;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.layout.LayoutType;
import com.qualityplus.minions.base.minions.minion.mob.MinionMob;
import com.qualityplus.minions.util.MinionAnimationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public final class MinionTargetSearchServiceImpl implements MinionTargetSearchService {

    @Override
    public CompletableFuture<Block> getTargetBlock(final MinionEntity minionEntity) {
        if (minionEntity.getEntity() == null) {
            return CompletableFuture.completedFuture(null);
        }

        final ArmorStand armorStand = (ArmorStand) minionEntity.getEntity();
        if (!ArmorStandUtil.entityIsValid(armorStand)) {
            return CompletableFuture.completedFuture(null);
        }

        armorStand.setHeadPose(new EulerAngle(-24.5, 0, 0));

        final LayoutType layout = TheMinions.getApi().getMinionLayoutService().getMinionLayout(minionEntity);
        final List<Vector> vectors = layout.equals(LayoutType.THREE_X_THREE) ? MinionAnimationUtil.getThree() : MinionAnimationUtil.getSecond();

        final Vector randomVector = RandomUtil.getRandom(vectors);
        final Location location = armorStand.getLocation().subtract(new Vector(0, 1, 0));

        if (randomVector == null) {
            return CompletableFuture.completedFuture(null);
        }

        final Location newLocation = location.clone().add(randomVector);
        ArmorStandUtil.rotate(armorStand, newLocation);

        return CompletableFuture.completedFuture(newLocation.getBlock());
    }


    @Override
    public CompletableFuture<MinionMobEntity> getTargetEntity(final MinionEntity minionEntity) {
        if (minionEntity.getEntity() == null) {
            return CompletableFuture.completedFuture(null);
        }

        final ArmorStand armorStand = (ArmorStand) minionEntity.getEntity();
        if (!ArmorStandUtil.entityIsValid(armorStand)) {
            return CompletableFuture.completedFuture(null);
        }

        final Location location = armorStand.getLocation();
        final CompletableFuture<MinionMobEntity> future = new CompletableFuture<>();

        getNearbyEntity(minionEntity, location).thenAccept(entity -> {
            int random = RandomUtil.randomUpTo(100);

            if (entity == null || random > 50) {
                double x = RandomUtil.randomBetween(1, 2);
                double z = RandomUtil.randomBetween(1, 2);

                Location newLocation = location.clone()
                        .add(x, 0, z);

                ArmorStandUtil.rotate(armorStand, newLocation);

                future.complete(MinionMobEntity.builder().location(newLocation).build());

                return;
            }

            final Location block = entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().clone();
            ArmorStandUtil.rotate(armorStand, block);

            future.complete(MinionMobEntity.builder().entity(entity).build());
        });


        return future;
    }


    private CompletableFuture<Entity> getNearbyEntity(final MinionEntity minionEntity, final Location location) {
        if (location == null || location.getWorld() == null) {
            return CompletableFuture.completedFuture(null);
        }

        final CompletableFuture<Entity> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTask(TheMinions.getInstance(), () -> {
            final MinionMob mob = minionEntity.getMinion().getMinionLayout().getMinionMob();
            final World world = location.getWorld();

            final Entity foundEntity = world.getNearbyEntities(location, 2, 2, 2)
                    .stream()
                    .filter(e -> e.getType().equals(mob.getEntityType()))
                    .findFirst()
                    .orElse(null);

            future.complete(foundEntity);
        });

        return future;
    }

}

package com.qualityplus.minions.base.minions.animations;

import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.minion.animation.MinionAnimation;
import com.qualityplus.minions.api.minion.animation.MinionAnimationContext;
import com.qualityplus.minions.util.MinionAnimationUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class PlaceAnimation implements MinionAnimation {
    private BukkitTask runnable;
    private EulerAngle[] toIterate = MinionAnimationUtil.rightHandFastPickaxeMovementNew;
    private boolean iterateId = true;
    private int passedTimes = 0;
    private int counter = 0;

    @Override
    public CompletableFuture<Void> executeAnimation(final MinionAnimationContext context) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        this.runnable = Bukkit.getScheduler().runTaskTimer(TheMinions.getInstance(), () -> {
            final ArmorStand armorStand = (ArmorStand) context.getMinionEntity().getEntity();
            armorStand.setHeadPose(new EulerAngle(-24.5, 0, 0));

            if (passedTimes >= 11) {
                if (ArmorStandUtil.entityIsValid(armorStand)) {
                    armorStand.setRightArmPose(new EulerAngle(0, 0, 0));
                    armorStand.setLeftArmPose(new EulerAngle(0, 0, 0));
                }
                future.complete(null);
                this.runnable.cancel();
                return;
            }

            if (counter == toIterate.length - 1) {
                counter = 0;
                iterateId = !iterateId;
                toIterate = iterateId ? MinionAnimationUtil.rightHandFastPickaxeMovementNew : MinionAnimationUtil.rightHandFastPickaxeMovementNew2;
            }

            if (ArmorStandUtil.entityIsValid(armorStand)) {
                armorStand.setRightArmPose(toIterate[this.counter]);
                armorStand.setLeftArmPose(toIterate[this.counter]);
            }

            this.passedTimes++;
            this.counter++;
        }, 0L, 1L);

        return future;
    }

    @Override
    public void cancel() {
        this.runnable.cancel();
    }
}

package com.qualityplus.minions.base.minions.animations;

import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.minion.animation.MinionAnimation;
import com.qualityplus.minions.api.minion.animation.MinionAnimationContext;
import com.qualityplus.minions.util.MinionAnimationUtil;
import com.qualityplus.minions.base.minions.entity.animation.FinishAnimation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import java.util.concurrent.CompletableFuture;

public final class StartAnimation implements MinionAnimation {
    private BukkitTask runnable;
    private int counter2 = MinionAnimationUtil.rightArmUp.length;
    private int counter = 0;

    @Override
    public CompletableFuture<Void> executeAnimation(final MinionAnimationContext context) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        this.runnable = Bukkit.getScheduler().runTaskTimer(TheMinions.getInstance(), () -> {
            final ArmorStand armorStand = (ArmorStand) context.getMinionEntity().getEntity();
            if (this.counter >= MinionAnimationUtil.rightArmUp.length) {
                if (this.counter2 <= 0) {
                    future.complete(null);
                    Bukkit.getScheduler().runTaskLater(TheMinions.getInstance(), () -> {
                        context.getMinionEntity().getState().setCanExecuteAnimation(true);
                    }, 20L);
                    this.runnable.cancel();
                    return;
                }

                this.counter2--;
                if (ArmorStandUtil.entityIsValid(armorStand)) {
                    armorStand.setRightArmPose(MinionAnimationUtil.rightArmUp[this.counter2]);
                    armorStand.setLeftArmPose(MinionAnimationUtil.leftArmUp[this.counter2]);
                    armorStand.setHeadPose(MinionAnimationUtil.moveHeadBack[this.counter2]);
                }
            } else {
                if (ArmorStandUtil.entityIsValid(armorStand)) {
                    armorStand.setRightArmPose(MinionAnimationUtil.rightArmUp[this.counter]);
                    armorStand.setLeftArmPose(MinionAnimationUtil.leftArmUp[this.counter]);
                    armorStand.setHeadPose(MinionAnimationUtil.moveHeadBack[this.counter]);
                }
                this.counter++;
            }
        }, 0L, 1L);

        return future;
    }

    @Override
    public void cancel() {
        this.runnable.cancel();
    }
}

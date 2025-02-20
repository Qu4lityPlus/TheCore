package com.qualityplus.minions.base.minions.animations;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.assistant.util.player.PlayerUtils;
import com.qualityplus.assistant.util.time.HumanTime;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.animation.MinionAnimation;
import com.qualityplus.minions.api.minion.animation.MinionAnimationContext;
import com.qualityplus.minions.base.minions.entity.type.BlockBreakMinion;
import com.qualityplus.minions.util.MinionAnimationUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class BreakAnimation implements MinionAnimation {
    private BukkitTask runnable;
    private int counter = 0;
    private int passedTimes = 0;
    private double damageCounter = 0;
    private boolean iterateId = true;
    private EulerAngle[] toIterate = MinionAnimationUtil.rightHandFastPickaxeMovementNew;
    private final double maxTimes;
    private final double blockToPerform;

    public BreakAnimation() {
        final HumanTime timer = new HumanTime(2, HumanTime.TimeType.SECONDS);
        final long millis = Duration.ofSeconds(timer.getSeconds()).toMillis();
        final int toDivide = (int) ((int) millis / (20 * timer.getSeconds()));

        this.maxTimes = (int) (millis / toDivide);
        this.blockToPerform = 9D / maxTimes;
    }

    @Override
    public CompletableFuture<Void> executeAnimation(final MinionAnimationContext context) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        final Block block = context.getTargetBlock();

        this.runnable = Bukkit.getScheduler().runTaskTimer(TheMinions.getInstance(), () -> {
            final ArmorStand armorStand = context.getMinionEntity().getEntity();

            if (this.passedTimes >= this.maxTimes) {
                if (ArmorStandUtil.entityIsValid(armorStand)) {
                    armorStand.setRightArmPose(new EulerAngle(0, 0, 0));
                }

                TheAssistantPlugin.getAPI().getNms().damageBlock(PlayerUtils.all(), block, -1);
                future.complete(null);

                this.runnable.cancel();
                return;
            }

            if (this.counter == this.toIterate.length - 1) {
                this.counter = 0;
                this.iterateId = !this.iterateId;
                this.toIterate = this.iterateId ? MinionAnimationUtil.rightHandFastPickaxeMovementNew : MinionAnimationUtil.rightHandFastPickaxeMovementNew2;
            }

            TheAssistantPlugin.getAPI().getNms().damageBlock(PlayerUtils.all(), block, (int) Math.ceil(this.damageCounter));

            if (ArmorStandUtil.entityIsValid(armorStand)) {
                armorStand.setRightArmPose(this.toIterate[this.counter]);
            }

            this.damageCounter+=this.blockToPerform;
            this.passedTimes++;
            this.counter++;
        }, 0L, 1L);

        return future;
    }

    @Override
    public void cancel() {
        runnable.cancel();
    }
}

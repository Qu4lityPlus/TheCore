package com.qualityplus.minions.base.minions.entity.type;

import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.animation.MinionAnimationContext;
import com.qualityplus.minions.base.minions.animations.PlaceAnimation;
import com.qualityplus.minions.base.minions.entity.ArmorStandMinion;
import com.qualityplus.minions.base.minions.entity.animation.MinionAnimationContextImpl;
import com.qualityplus.minions.base.minions.entity.mob.MinionMobEntity;
import com.qualityplus.minions.base.minions.minion.Minion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public final class MobKillerMinion extends ArmorStandMinion<MinionMobEntity> {
    private MobKillerMinion(UUID minionUniqueId, UUID owner, Minion minion, boolean loaded) {
        super(minionUniqueId, owner, minion, loaded);
    }

    public static MobKillerMinion create(UUID minionUniqueId, UUID owner, Minion minion, boolean loaded) {
        return new MobKillerMinion(minionUniqueId, owner, minion, loaded);
    }


    @Override
    protected synchronized void rotateToTarget() {
        if (!state.isCanExecuteAnimation()) {
            return;
        }

        this.state.setCanExecuteAnimation(false);

        if (state.isArmorStandLoaded()) {
            TheMinions.getApi()
                    .getMinionTargetSearchService()
                    .getTargetEntity(this)
                    .thenAccept(this::checkTargetAfterRotate);
        } else {
            checkTargetAfterRotate(null);
        }
    }

    @Override
    public void executeWhenTargetIsNull(final MinionMobEntity target) {
        TheMinions.getApi().getMinionMobSpawnService().spawnMinionMob(this.minion, target);

        resetPositionAndBreakingState();
    }

    @Override
    public void executeWhenTargetIsNotNull(final MinionMobEntity entity) {
        final LivingEntity living = (LivingEntity) entity.getEntity();
        living.setHealth(0);

        addItemsToMinionInventory();

        resetPositionAndBreakingState();
    }

    @Override
    protected void checkTargetAfterRotate(final MinionMobEntity target) {
        if (!this.state.isArmorStandLoaded()) {
            resetPositionAndBreakingState();
            addItemsToMinionInventory();
            return;
        }

        final Entity targetEntity = target.getEntity();
        final MinionAnimationContext context = MinionAnimationContextImpl.builder()
                .targetEntity(targetEntity)
                .minionEntity(this)
                .build();

        if (targetEntity == null || targetEntity.isDead()) {
            this.currentAnimation = new PlaceAnimation();
            this.currentAnimation.executeAnimation(context).thenRun(() -> executeWhenTargetIsNull(target));
        } else {
            this.currentAnimation = new PlaceAnimation();
            this.currentAnimation.executeAnimation(context).thenRun(() -> executeWhenTargetIsNotNull(target));
        }
    }
}

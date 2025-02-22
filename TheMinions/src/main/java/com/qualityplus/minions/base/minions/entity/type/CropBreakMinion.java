package com.qualityplus.minions.base.minions.entity.type;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XBlock;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.CropUtil;
import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.assistant.util.block.BlockUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.animation.MinionAnimationContext;
import com.qualityplus.minions.base.minions.animations.BreakAnimation;
import com.qualityplus.minions.base.minions.animations.PlaceAnimation;
import com.qualityplus.minions.base.minions.entity.ArmorStandMinion;
import com.qualityplus.minions.base.minions.entity.animation.MinionAnimationContextImpl;
import com.qualityplus.minions.base.minions.minion.Minion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.UUID;

public final class CropBreakMinion extends BlockBreakMinion {
    private CropBreakMinion(final UUID minionUniqueId, final UUID owner, final Minion minion, final boolean loaded) {
        super(minionUniqueId, owner, minion, loaded);
    }

    public static CropBreakMinion create(final UUID minionUniqueId, final UUID owner, final Minion minion, final boolean loaded) {
        return new CropBreakMinion(minionUniqueId, owner, minion, loaded);
    }

    @Override
    protected void checkTargetAfterRotate(Block target) {
        if (!this.state.isArmorStandLoaded()) {
            resetPositionAndBreakingState();
            addItemsToMinionInventory();
            return;
        }

        if (!ArmorStandUtil.entityIsValid(entity)) {
            return;
        }

        if (!cropHasMaxLevel(target)) {
            final MinionAnimationContext context = MinionAnimationContextImpl.builder()
                    .targetBlock(target)
                    .minionEntity(this)
                    .build();

            this.currentAnimation = new PlaceAnimation();
            this.currentAnimation.executeAnimation(context).thenRun(() -> executeWhenTargetIsNull(target));
        } else {
            final MinionAnimationContext context = MinionAnimationContextImpl.builder()
                    .targetBlock(getCropBlock(target))
                    .minionEntity(this)
                    .build();

            this.currentAnimation = new BreakAnimation();
            this.currentAnimation.executeAnimation(context).thenRun(() -> executeWhenTargetIsNotNull(target));
        }
    }

    @Override
    public void executeWhenTargetIsNull(final Block target) {
        final XMaterial material = this.minion.getMinionLayout().getToReplaceBlock();
        final XMaterial crop = this.minion.getMinionLayout().getToReplaceCrop();

        final Block toPlace = getCropBlock(target);

        if (BlockUtils.isNull(toPlace)) {
            Bukkit.getScheduler().runTask(TheMinions.getInstance(), () -> {
                BlockUtils.setBlock(target, material);

                Optional.ofNullable(CropUtil.cropFromXMaterial(crop)).ifPresent(toPlace::setType);

                XBlock.setAge(toPlace, 0);
            });
        } else {
            Bukkit.getScheduler().runTask(TheMinions.getInstance(), () -> {
                int age = TheAssistantPlugin.getAPI().getNms().getAge(toPlace);

                XBlock.setAge(toPlace, age + 1);
            });
        }

        resetPositionAndBreakingState();
    }

    @Override
    public void executeWhenTargetIsNotNull(final Block block) {
        final Block toPlace = getCropBlock(block);

        if (toPlace == null || toPlace.getType().equals(Material.AIR)) {
            resetPositionAndBreakingState();
            return;
        }

        BlockUtils.setBlock(toPlace, XMaterial.AIR);

        addItemsToMinionInventory();
        resetPositionAndBreakingState();
    }


    private boolean cropHasMaxLevel(final Block block) {
        final Block toPlace = getCropBlock(block);

        if (BlockUtils.isNull(toPlace)) {
            return false;
        }

        final int age = TheAssistantPlugin.getAPI().getNms().getAge(toPlace);
        final int maxAge = TheAssistantPlugin.getAPI().getNms().getMaxAge(toPlace);

        return age >= maxAge;
    }

    private Block getCropBlock(final Block below) {
        return below.getLocation().clone().add(new Vector(0,1,0)).getBlock();
    }
}

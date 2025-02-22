package com.qualityplus.minions.base.minions.entity.type;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.block.BlockUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.animation.MinionAnimationContext;
import com.qualityplus.minions.base.minions.animations.BreakAnimation;
import com.qualityplus.minions.base.minions.animations.PlaceAnimation;
import com.qualityplus.minions.base.minions.entity.animation.MinionAnimationContextImpl;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.entity.ArmorStandMinion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public class BlockBreakMinion extends ArmorStandMinion<Block> {
    protected BlockBreakMinion(UUID minionUniqueId, UUID owner, Minion minion, boolean loaded) {
        super(minionUniqueId, owner, minion, loaded);
    }

    public static BlockBreakMinion create(UUID minionUniqueId, UUID owner, Minion minion, boolean loaded) {
        return new BlockBreakMinion(minionUniqueId, owner, minion, loaded);
    }

    @Override
    protected synchronized void rotateToTarget() {
        if (!this.state.isCanExecuteAnimation()) {
            return;
        }

        this.state.setCanExecuteAnimation(false);

        if (this.state.isArmorStandLoaded()) {
            TheMinions.getApi()
                    .getMinionTargetSearchService()
                    .getTargetBlock(this)
                    .thenAccept(this::checkTargetAfterRotate);
        } else {
            checkTargetAfterRotate(null);
        }
    }

    @Override
    protected void checkTargetAfterRotate(Block target) {
        if (!this.state.isArmorStandLoaded()) {
            resetPositionAndBreakingState();
            addItemsToMinionInventory();
            return;
        }

        executeAnimation(target);
    }

    @Override
    public void executeWhenTargetIsNull(Block target) {
        final XMaterial material = this.minion.getMinionLayout().getToReplaceBlock();

        BlockUtils.setBlock(target, material);

        resetPositionAndBreakingState();
    }

    @Override
    public void executeWhenTargetIsNotNull(final Block block) {
        if (BlockUtils.isNull(block)) {
            resetPositionAndBreakingState();
            return;
        }

        BlockUtils.setBlock(block, XMaterial.AIR);

        addItemsToMinionInventory();

        resetPositionAndBreakingState();
    }


    private void executeAnimation(final Block block) {
        final Material material = this.minion.getMinionLayout().getToReplaceBlock().parseMaterial();

        final MinionAnimationContext context = MinionAnimationContextImpl.builder()
                .minionEntity(this)
                .targetBlock(block)
                .build();

        if (BlockUtils.isNull(block) || !block.getType().equals(material)) {
            this.currentAnimation = new PlaceAnimation();
            this.currentAnimation.executeAnimation(context).thenRun(() -> executeWhenTargetIsNull(block));
        } else {
            this.currentAnimation = new BreakAnimation();
            this.currentAnimation.executeAnimation(context).thenRun(() -> executeWhenTargetIsNotNull(block));
        }
    }
}

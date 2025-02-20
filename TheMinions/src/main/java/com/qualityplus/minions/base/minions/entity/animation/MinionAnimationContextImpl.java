package com.qualityplus.minions.base.minions.entity.animation;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.minion.animation.MinionAnimationContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

@Getter
@Builder
@AllArgsConstructor
public final class MinionAnimationContextImpl implements MinionAnimationContext {
    private final MinionEntity minionEntity;
    private final Block targetBlock;
    private final Entity targetEntity;
}

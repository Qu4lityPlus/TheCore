package com.qualityplus.minions.api.minion.animation;

import com.qualityplus.minions.api.minion.MinionEntity;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public interface MinionAnimationContext {
    public MinionEntity getMinionEntity();
    public Block getTargetBlock();
    public Entity getTargetEntity();
}

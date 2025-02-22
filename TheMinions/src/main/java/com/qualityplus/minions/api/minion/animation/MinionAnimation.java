package com.qualityplus.minions.api.minion.animation;

import com.qualityplus.minions.api.minion.MinionEntity;
import org.bukkit.Location;

import java.util.concurrent.CompletableFuture;

public interface MinionAnimation {
    public CompletableFuture<Void> executeAnimation(final MinionAnimationContext context);

    public void cancel();
}

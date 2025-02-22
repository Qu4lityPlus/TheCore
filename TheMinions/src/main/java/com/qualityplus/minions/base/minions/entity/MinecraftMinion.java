package com.qualityplus.minions.base.minions.entity;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.getter.DataGetter;
import com.qualityplus.minions.base.minions.entity.state.MinionState;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.entity.handler.MinionHandlers;
import com.qualityplus.minions.base.minions.entity.handler.MinionHandlersImpl;
import lombok.Getter;
import java.util.UUID;

public abstract class MinecraftMinion implements MinionEntity, DataGetter {
    @Getter
    protected final  MinionState state;
    protected final MinionHandlers handlers;
    @Getter
    protected final Minion minion;

    protected MinecraftMinion(
            final UUID minionUniqueId,
            final UUID owner,
            final Minion minion,
            final boolean loaded
    ) {
        this.state = new MinionState(minionUniqueId, owner, loaded);

        this.minion = minion;
        this.handlers = new MinionHandlersImpl(this, minion);
    }

    @Override
    public UUID getMinionUniqueId() {
        return state.getUuid();
    }
}

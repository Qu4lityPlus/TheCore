package com.qualityplus.minions.base.newarch.api.entity;

import com.qualityplus.minions.base.minions.entity.status.MinionStatus;
import com.qualityplus.minions.base.minions.minion.Minion;
import org.bukkit.entity.ArmorStand;

import java.util.UUID;

public interface NewMinionEntity {
    public void spawn();

    public void remove();

    public void tick();

    public MinionStatus getStatus();

    public void updateStatus(final MinionStatus status);

    public UUID getId();

    public UUID getOwner();

    public Minion getConfig();
    public ArmorStand getEntity();

    public NewMinionInventory getInventory();
}

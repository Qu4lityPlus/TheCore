package com.qualityplus.minions.base.minions.entity.state;

import com.qualityplus.assistant.api.gui.FakeInventory;
import com.qualityplus.minions.base.minions.entity.MinionStorageState;
import com.qualityplus.minions.base.minions.entity.status.MinionStatus;
import lombok.*;
import org.bukkit.Location;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public final class MinionState {
    private MinionStorageState storageState;
    private FakeInventory fakeInventory;
    private MinionStatus oldStatus;
    private MinionStatus status;
    private long lastActionTime;
    private boolean canExecuteAnimation;
    private boolean isSelling;
    private final UUID owner;
    private final UUID uuid;
    private boolean armorStandLoaded;
    private Location spawn;

    public MinionState(final UUID uuid, final UUID owner, final boolean loaded) {
        this.lastActionTime = System.currentTimeMillis();
        this.status = MinionStatus.INVALID_LAYOUT;
        this.canExecuteAnimation = true;
        this.isSelling = false;
        this.armorStandLoaded = loaded;
        this.owner = owner;
        this.uuid = uuid;
    }
}

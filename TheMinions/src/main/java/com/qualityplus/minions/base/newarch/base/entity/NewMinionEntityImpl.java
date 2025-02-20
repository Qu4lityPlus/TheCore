package com.qualityplus.minions.base.newarch.base.entity;

import com.qualityplus.assistant.hologram.TheHologram;
import com.qualityplus.assistant.util.random.RandomSelector;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.base.minions.entity.message.RandomMessage;
import com.qualityplus.minions.base.minions.entity.status.MinionStatus;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.newarch.api.entity.NewMinionEntity;
import com.qualityplus.minions.base.newarch.api.entity.NewMinionInventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class NewMinionEntityImpl implements NewMinionEntity {
    private final UUID id;
    private final UUID owner;
    private ArmorStand entity;
    private MinionStatus status;
    private final Minion config;
    private TheHologram hologram;
    private Location initialSpawn;
    private final NewMinionInventory inventory;

    public NewMinionEntityImpl(final UUID id, final UUID owner, final Minion config, final NewMinionInventory inventory) {
        this.id = id;
        this.owner = owner;
        this.config = config;
        this.inventory = inventory;
        this.status = MinionStatus.SPAWNING;
    }

    @Override
    public void spawn() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void tick() {
        switch (this.status) {
            case UNDEFINED -> {
                return;
            }
            case IDEAL_LAYOUT -> {

            }
            case STORAGE_FULL -> {

            }
            case INVALID_LAYOUT -> {

            }
        }
    }

    @Override
    public void updateStatus(final MinionStatus newStatus) {
        if (this.status == MinionStatus.SPAWNING) {
            return;
        }

        if (this.status == newStatus) {
            return;
        }

        this.status = newStatus;

        updateDisplayName();
    }

    private void updateDisplayName() {
        final List<RandomMessage> randomMessages = TheMinions.getApi().getConfigFiles().config().messages.getOrDefault(this.status, null);

        final RandomMessage randomSelector = new RandomSelector<>(randomMessages).getRandomOrUniqueItem();

        final List<String> msg = Optional.ofNullable(randomSelector)
                .map(RandomMessage::getMessage)
                .orElse(Collections.singletonList("&cInvalid Error!"));

        Bukkit.getScheduler().runTask(TheMinions.getInstance(), () -> {
            if (this.hologram == null) {
                this.hologram = TheHologram.create(msg, this.initialSpawn.clone());
            } else {
                this.hologram.rename(msg);
            }
        });

    }
}

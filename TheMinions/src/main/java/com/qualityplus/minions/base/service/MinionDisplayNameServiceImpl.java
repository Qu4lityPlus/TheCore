package com.qualityplus.minions.base.service;

import com.qualityplus.assistant.hologram.TheHologram;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.util.random.RandomSelector;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.service.MinionDisplayNameService;
import com.qualityplus.minions.base.minions.entity.message.RandomMessage;
import com.qualityplus.minions.base.minions.entity.state.MinionState;
import com.qualityplus.minions.base.minions.entity.status.MinionStatus;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public final class MinionDisplayNameServiceImpl implements MinionDisplayNameService {
    @Override
    public void updateDisplayName(final MinionEntity minionEntity) {
        final MinionState minionState = minionEntity.getState();
        final MinionStatus status = minionState.getStatus();
        final MinionStatus oldStatus = minionState.getOldStatus();

        if (status == null || (oldStatus != null && oldStatus.equals(status))) {
            return;
        }

        final List<RandomMessage> randomMessages = TheMinions.getApi().getConfigFiles().config().messages.getOrDefault(status, null);
        final RandomMessage randomSelector = new RandomSelector<>(randomMessages).getRandomOrUniqueItem();
        final List<String> msg = Optional.ofNullable(randomSelector)
                .map(RandomMessage::getMessage)
                .orElse(Collections.singletonList("&cInvalid Error!"));

        //TODO check this with a future or boolean
        Bukkit.getScheduler().runTask(TheMinions.getInstance(), () -> {
            final TheHologram hologram = minionEntity.getHologram();

            if (hologram == null) {
                minionEntity.setHologram(TheHologram.create(msg, minionEntity.getState().getSpawn().clone()));
            } else {
                hologram.rename(msg);
            }
        });

        minionState.setOldStatus(status);
    }
}

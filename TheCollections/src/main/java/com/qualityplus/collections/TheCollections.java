package com.qualityplus.collections;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.okaeri.OkaeriSilentPlugin;
import com.qualityplus.collections.api.TheCollectionsAPI;
import com.qualityplus.collections.api.box.Box;
import com.qualityplus.collections.persistance.data.UserData;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Scan;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.ExecutionPhase;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.Planned;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Scan(deep = true)
public final class TheCollections extends OkaeriSilentPlugin {
    private static @Inject
    @Getter TheCollectionsAPI api;

    @Planned(ExecutionPhase.PRE_SHUTDOWN)
    private void whenStop(Box box) {
        Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getUniqueId)
                .forEach(uuid -> box.service().getData(uuid).ifPresent(UserData::save));
    }
}

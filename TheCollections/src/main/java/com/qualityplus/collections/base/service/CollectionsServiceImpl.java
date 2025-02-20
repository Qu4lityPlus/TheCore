package com.qualityplus.collections.base.service;

import com.qualityplus.collections.api.event.CollectionEvent;
import com.qualityplus.collections.api.service.CollectionsService;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.event.CollectionsLevelUPEvent;
import com.qualityplus.collections.base.event.CollectionsUnlockEvent;
import com.qualityplus.collections.base.event.CollectionsXPGainEvent;
import com.qualityplus.collections.persistance.data.UserData;
import com.qualityplus.collections.persistance.data.user.UserCollections;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public final class CollectionsServiceImpl implements CollectionsService {
    private final Map<UUID, UserData> dataMap = new HashMap<>();

    @Override
    public Optional<UserData> getData(UUID uuid) {
        return Optional.ofNullable(dataMap.getOrDefault(uuid, null));
    }

    @Override
    public void addData(UserData data) {
        dataMap.put(data.getUuid(), data);
    }

    @Override
    public void removeData(UserData data) {
        dataMap.remove(data.getUuid());
    }

    @Override
    public void addXp(Player player, Collection collection, double toAdd) {
        CollectionsXPGainEvent collectionsXPGainEvent = new CollectionsXPGainEvent(player, collection, toAdd);

        Bukkit.getPluginManager().callEvent(collectionsXPGainEvent);

        if (collectionsXPGainEvent.isCancelled()) return;

        toAdd = collectionsXPGainEvent.getXp();

        UserCollections collections = getData(player.getUniqueId())
                .map(UserData::getCollections)
                .orElse(new UserCollections());


        if (collections.getXp(collection.getId()) == 0) {
            CollectionsUnlockEvent unlockEvent = new CollectionsUnlockEvent(player, collection);

            Bukkit.getPluginManager().callEvent(unlockEvent);

            if (unlockEvent.isCancelled()) return;
        }

        collections.addXp(collection.getId(), toAdd);

        int level = collections.getLevel(collection.getId());

        if (collections.getXp(collection.getId()) >= collection.getLevelRequirement(level + 1) && level + 1 <= collection.getMaxLevel()) {
            //skills.setXp(collection.getId(), 0.0);
            collections.addLevel(collection.getId(),  1);
            CollectionEvent levelUpEvent = new CollectionsLevelUPEvent(player, collection, level + 1);
            Bukkit.getPluginManager().callEvent(levelUpEvent);
        }
    }
}

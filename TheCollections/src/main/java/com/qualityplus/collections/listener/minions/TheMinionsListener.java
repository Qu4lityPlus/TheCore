package com.qualityplus.collections.listener.minions;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.collections.api.service.CollectionsService;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.registry.CollectionsRegistry;
import com.qualityplus.minions.base.event.PlayerPickUpMinionItemsEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public final class TheMinionsListener implements Listener {
    private @Inject CollectionsService collectionsService;

    @EventHandler
    public void onPickUp(PlayerPickUpMinionItemsEvent e) {
        Player player = e.getPlayer();

        for (ItemStack pickUpItem : e.getItems()) {
            Optional<Collection> collection = CollectionsRegistry.getByItem(pickUpItem);

            if (!collection.isPresent()) return;

            collectionsService.addXp(player, collection.get(), pickUpItem.getAmount());
        }
    }
}

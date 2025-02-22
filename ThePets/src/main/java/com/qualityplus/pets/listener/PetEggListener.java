package com.qualityplus.pets.listener;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.pets.persistance.data.UserData;
import com.qualityplus.pets.util.PetEggUtil;
import com.qualityplus.pets.util.PetPlaceholderUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@Component
public final class PetEggListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onJoin(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack inHand = player.getItemInHand();

        Optional<PetData> data = PetEggUtil.dataFromEgg(inHand);

        if (!data.isPresent())
            return;

        event.setCancelled(true);

        player.setItemInHand(BukkitItemUtil.getItemWithout(inHand, 1));

        box.service()
                .getData(player.getUniqueId())
                .map(UserData::getInventoryData)
                .ifPresent(inventoryData -> inventoryData.addInventoryPet(data.get().getUuid(), data.get().getPetId()));

        PlaceholderBuilder placeholderList = PetPlaceholderUtil.getPetPlaceholders(data.get().getPetId());

        String message = StringUtils.processMulti(box.files().messages().petMessages.successfullyAddedToYourPetMenu, placeholderList.get());

        player.sendMessage(message);
    }
}
package com.qualityplus.pets.listener;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.sound.SoundUtils;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.base.config.Config;
import com.qualityplus.pets.base.event.PetLevelUPEvent;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.tracker.PetEntityTracker;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.pets.util.PetPlaceholderUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;

@Component
public final class PetsInternalListener implements Listener {
    private @Inject Box box;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onXpGain(PetLevelUPEvent event) {
        PetData petData = event.getData();

        Pet pet = Pets.getByID(petData.getPetId());

        Optional<PetEntity> entity = PetEntityTracker.getByID(petData.getUuid());

        if (!entity.isPresent()) return;

        if (pet == null) return;

        Player player = Bukkit.getPlayer(entity.get().getOwner());

        if (player == null || !player.isOnline()) return;


        Bukkit.getScheduler().runTask(box.plugin(), () -> {
            pet.getCommandRewards(event.getNewLevel())
               .forEach(reward -> reward.execute(player));


            PlaceholderBuilder builder = PetPlaceholderUtil.getPetPlaceholders(entity.get().getPetUniqueId())/*data
                    .map(dat -> PetPlaceholderUtil.getPetPlaceholders(dat, pet))
                    .orElse(PlaceholderBuilder.create());*/;

            List<IPlaceholder> placeholders = builder
                    .with(new Placeholder("pet_info_message", StringUtils.processMulti(pet.getPetCachedMessage(event.getNewLevel() + 1), builder.get())))
                    .get();

            Config.LevelUpSettings settings = box.files().config().levelUpSettings;

            SoundUtils.playSound(player, settings.sound);

            if (settings.message.isEnabled())
                StringUtils.processMulti(settings.message.getMessages(), placeholders).forEach(msg -> player.sendMessage(StringUtils.color(msg)));

            entity.get().update();
        });
    }
}

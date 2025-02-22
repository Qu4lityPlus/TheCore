package com.qualityplus.collections.listener;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.sound.SoundUtils;
import com.qualityplus.collections.api.box.Box;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.config.Config;
import com.qualityplus.collections.base.event.CollectionsLevelUPEvent;
import com.qualityplus.collections.base.event.CollectionsUnlockEvent;
import com.qualityplus.collections.base.event.CollectionsXPGainEvent;
import com.qualityplus.collections.persistance.data.UserData;
import com.qualityplus.collections.util.CollectionsPlaceholderUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class CollectionsInternalListener implements Listener {
    private @Inject Box box;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onXpGain(CollectionsXPGainEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTask(box.plugin(), () -> SoundUtils.playSound(player, box.files().config().gainXPSettings.sound));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onUnlock(CollectionsUnlockEvent event) {
        Player player = event.getPlayer();

        List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                        new Placeholder("collection_displayname", event.getCollection().getDisplayName()),
                        new Placeholder("collection_id", event.getCollection().getId()))
                .get();

        player.spigot().sendMessage(StringUtils.getMessage(box.files().messages().collectionsMessages.unlockedMessage, placeholders));

        Bukkit.getScheduler().runTask(box.plugin(), () -> SoundUtils.playSound(player, box.files().config().unlockSettings.sound));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onXpGain(CollectionsLevelUPEvent event) {
        Player player = event.getPlayer();

        UUID uuid = player.getUniqueId();

        Optional<UserData> data = box.service().getData(uuid);

        Collection skill = event.getCollection();

        Bukkit.getScheduler().runTask(box.plugin(), () -> {
            event.getCollection()
                    .getRewards(event.getNewLevel())
                    .forEach(reward -> reward.execute(player));

            PlaceholderBuilder builder = data
                    .map(d -> CollectionsPlaceholderUtil.getCollectionsPlaceholdersBuilder(d, skill))
                    .orElse(PlaceholderBuilder.create());

            List<IPlaceholder> placeholders = builder
                    .with(new Placeholder("collection_info_message", StringUtils.processMulti(skill.getCollectionsCacheMessage(event.getNewLevel() + 1, Collection.MessageType.LEVEL_UP), builder.get())))
                    .get();

            Config.LevelUpSettings settings = box.files().config().levelUpSettings;

            SoundUtils.playSound(player, settings.sound);

            if (settings.message.isEnabled())
                StringUtils.processMulti(settings.message.getMessages(), placeholders).forEach(msg -> player.sendMessage(StringUtils.color(msg)));


        });
    }
}

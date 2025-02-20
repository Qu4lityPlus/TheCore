package com.qualityplus.dragon.base.game.parts;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XSound;
import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.config.ConfigTitle;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.sound.SoundUtils;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.api.game.part.GameCountdown;
import com.qualityplus.dragon.api.game.structure.type.DragonSpawn;
import com.qualityplus.dragon.base.configs.Config;
import com.qualityplus.dragon.base.configs.Messages;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public final class GameCountdownImpl implements GameCountdown {
    private @Inject Config configuration;
    private @Inject Messages messages;
    private Integer taskID;
    private Integer time = 0;


    @Override
    public CompletableFuture<Void> start(final TheDragonEntity theDragonEntity) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        final Optional<DragonSpawn> spawn = TheDragon.getApi().getStructureService().getSpawn();

        if (!spawn.isPresent()) {
            return future;
        }

        this.time = configuration.eventSettings.generalSettings.dragonSpawnCountdownSeconds;

        final Messages.GameTitlesMessages titles = this.messages.gameTitlesMessages;

        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(TheDragon.getApi().getPlugin(), () -> {

            if (this.time <= 0) {
                getPlayersAround(spawn.get().getLocation()).forEach(player -> sendTitle(player, theDragonEntity, titles.dragonSpawned));
                future.complete(null);
                Bukkit.getScheduler().cancelTask(taskID);
                return;
            }

            getPlayersAround(spawn.get().getLocation()).forEach(player -> sendTitle(player, theDragonEntity, titles.dragonSpawning));

            this.time--;

        }, 20, 20);

        return future;
    }

    private void sendTitle(Player player, TheDragonEntity theDragonEntity, ConfigTitle configTitle) {
        final String dragonName = theDragonEntity == null ? "" : theDragonEntity.getDisplayName();

        final List<IPlaceholder> placeholders = Arrays.asList(
                new Placeholder("thedragon_dragon_displayname", dragonName),
                new Placeholder("thedragon_dragon_spawning_time", time));

        final String title = StringUtils.processMulti(configTitle.getTitle(), placeholders);
        final String subTitle = StringUtils.processMulti(configTitle.getSubtitle(), placeholders);

        TheAssistantPlugin.getAPI().getNms().sendTitle(player, title, subTitle, 5, 5, 5);

        SoundUtils.playSound(player, XSound.ENTITY_ENDER_DRAGON_GROWL);
    }

    private Collection<Player> getPlayersAround(Location location) {
        return location.getWorld().getNearbyEntities(location, 100, 100, 100).stream()
                .filter(en -> en instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());
    }

}

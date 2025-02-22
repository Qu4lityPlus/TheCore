package com.qualityplus.dragon.base.service;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.TheDragonAPI;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.api.game.structure.GameStructure;
import com.qualityplus.dragon.api.game.structure.type.DragonSpawn;
import com.qualityplus.dragon.api.service.BossBarService;
import com.qualityplus.dragon.api.service.GamePlayerCheckService;
import com.qualityplus.dragon.base.configs.Messages;
import com.qualityplus.dragon.base.game.player.EventPlayer;
import com.qualityplus.dragon.base.game.player.PlayerStatus;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link BossBarService}
 */
@Component
public final class GamePlayerCheckServiceImpl implements GamePlayerCheckService {
    private static final int DRAGON_RADIUS_AROUND = 100;
    private @Inject DragonGame dragonGame;
    private @Inject Messages messages;
    private @Inject Plugin plugin;
    private Integer task;

    @Override
    public void startChecking() {
        this.addTargetPlayers();

        this.task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::checkPlayerStatus, 20, 20);
    }

    @Override
    public void stopChecking() {
        Optional.ofNullable(this.task).ifPresent(Bukkit.getScheduler()::cancelTask);

        TheAssistantPlugin.getAPI().getNms().sendBossBar(null, null);
    }

    private void checkPlayerStatus() {
        if (!this.dragonGame.isActive()) {
            return;
        }

        updatePlayerStatus();
    }

    private void updatePlayerStatus() {
        this.dragonGame.getPlayers().forEach(this::updateStatus);
    }

    private void updateStatus(final Player player) {
        final World playerWorld = player.getLocation().getWorld();

        final World dragonWorld = TheDragon.getApi().getStructureService()
                .getSpawn()
                .map(GameStructure::getWorld)
                .orElse(null);

        if (dragonWorld == null || playerWorld == null) {
            return;
        }

        final Optional<EventPlayer> eventPlayer = TheDragon.getApi()
                .getUserService()
                .getByUUID(player.getUniqueId());

        if (!eventPlayer.isPresent()) {
            return;
        }

        if (eventPlayer.get().getStatus().equals(PlayerStatus.ACTIVE)) {
            if (playerWorld.equals(dragonWorld)) {
                return;
            }

            eventPlayer.ifPresent(EventPlayer::switchStatus);

        } else {
            if (!playerWorld.equals(dragonWorld)) {
                return;
            }

            eventPlayer.ifPresent(EventPlayer::switchStatus);
        }

    }

    private void addTargetPlayers() {
        final TheDragonAPI api = TheDragon.getApi();

        List<EventPlayer> players = this.getSpawn()
                .map(this::getNearbyPlayers)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::getDefaultEventPlayer)
                .collect(Collectors.toList());


        players.forEach(api.getUserService()::addPlayer);
    }

    private EventPlayer getDefaultEventPlayer(final Player player) {
        return new EventPlayer(player.getUniqueId(), 0, PlayerStatus.ACTIVE);
    }

    private Collection<Player> getNearbyPlayers(final Location location) {
        return location.getWorld()
                .getNearbyEntities(location, DRAGON_RADIUS_AROUND, DRAGON_RADIUS_AROUND, DRAGON_RADIUS_AROUND)
                .stream()
                .filter(en -> en instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());
    }

    private Optional<Location> getSpawn() {
        final TheDragonAPI api = TheDragon.getApi();

        final Optional<DragonSpawn> spawn = api.getStructureService().getSpawn();

        return spawn.map(GameStructure::getLocation);

    }
}

